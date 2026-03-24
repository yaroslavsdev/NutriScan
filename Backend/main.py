import json

from fastapi import FastAPI, Depends, HTTPException, UploadFile, File
from fastapi.security import OAuth2PasswordBearer
from jose import jwt, JWTError
from sqlalchemy.orm import Session
from pydantic import BaseModel

import auth_utils
import models
from database import engine, get_db

# Создание таблиц в Postgres, если их еще нет
models.Base.metadata.create_all(bind=engine)

app = FastAPI(title="NutriScan API")

class Nutrition(BaseModel):
    calories: float
    proteins: float
    fats: float
    carbs: float

class ProductCreate(BaseModel):
    barcode: str
    name: str
    brand: str | None = None
    ingredients: str

    nutrition: Nutrition

class ProductResponse(BaseModel):
    id: int
    barcode: str
    name: str
    brand: str | None
    ingredients: str

    calories: float
    proteins: float
    fats: float
    carbs: float

    class Config:
        from_attributes = True

class UserCreate(BaseModel):
    username: str
    email: str
    password: str

class UserLogin(BaseModel):
    email: str
    password: str

class Token(BaseModel):
    access_token: str
    token_type: str


oauth2_scheme = OAuth2PasswordBearer(tokenUrl="auth/login")

class AllergensUpdate(BaseModel):
    allergens: list[str]

def get_current_user(db: Session = Depends(get_db), token: str = Depends(oauth2_scheme)):
    try:
        payload = jwt.decode(token, auth_utils.SECRET_KEY, algorithms=[auth_utils.ALGORITHM])
        email: str = payload.get("sub")
        if email is None:
            raise HTTPException(status_code=401, detail="Невалидный токен")
    except JWTError:
        raise HTTPException(status_code=401, detail="Ошибка авторизации")

    user = db.query(models.User).filter(models.User.email == email).first()
    if user is None:
        raise HTTPException(status_code=401, detail="Пользователь не найден")
    return user

@app.post("/auth/register", response_model=Token)
def register(user: UserCreate, db: Session = Depends(get_db)):
    db_user = db.query(models.User).filter(models.User.email == user.email).first()
    if db_user:
        raise HTTPException(status_code=400, detail="Email уже зарегистрирован")

    new_user = models.User(
        username=user.username,
        email=user.email,
        password_hash=auth_utils.hash_password(user.password)
    )
    db.add(new_user)
    db.commit()
    db.refresh(new_user)

    token = auth_utils.create_access_token(data={"sub": new_user.email})
    return {"access_token": token, "token_type": "bearer"}


@app.post("/auth/login", response_model=Token)
def login(user: UserLogin, db: Session = Depends(get_db)):
    db_user = db.query(models.User).filter(models.User.email == user.email).first()
    if not db_user or not auth_utils.verify_password(user.password, db_user.password_hash):
        raise HTTPException(status_code=401, detail="Неверный email или пароль")

    token = auth_utils.create_access_token(data={"sub": db_user.email})
    return {"access_token": token, "token_type": "bearer"}


@app.post("/auth/allergens")
def save_user_allergens(
        data: AllergensUpdate,
        db: Session = Depends(get_db),
        current_user: models.User = Depends(get_current_user)
):
    current_user.user_allergens = data.allergens

    db.commit()
    db.refresh(current_user)

    return {"status": "success", "saved_allergens": current_user.user_allergens}


@app.get("/auth/allergens")
def get_user_allergens(current_user: models.User = Depends(get_current_user)):
    return {"allergens": current_user.user_allergens or []}

@app.get("/auth/me")
def get_me(current_user: models.User = Depends(get_current_user)):
    return {
        "username": current_user.username,
        "email": current_user.email,
        "allergens": current_user.user_allergens or []
    }

# Добавить товар в базу (POST)
@app.post("/products")
def create_product(product: ProductCreate, db: Session = Depends(get_db)):
    db_product = models.Product(
        barcode=product.barcode,
        name=product.name,
        brand=product.brand,
        ingredients=product.ingredients,

        calories=product.nutrition.calories,
        proteins=product.nutrition.proteins,
        fats=product.nutrition.fats,
        carbs=product.nutrition.carbs,
    )

    db.add(db_product)
    try:
        db.commit()
        db.refresh(db_product)
        return db_product
    except:
        db.rollback()
        raise HTTPException(status_code=400, detail="Товар с таким штрих-кодом уже есть")

@app.post("/products/import")
def import_products(
    file: UploadFile = File(...),
    db: Session = Depends(get_db)
):
    data = json.load(file.file)

    added = 0

    for item in data:
        exists = db.query(models.Product)\
            .filter(models.Product.barcode == item["barcode"])\
            .first()

        if exists:
            continue

        product = models.Product(
            barcode=item["barcode"],
            name=item["name"],
            brand=item.get("brand"),
            ingredients=item["ingredients"],

            calories=item["nutrition"]["calories"],
            proteins=item["nutrition"]["proteins"],
            fats=item["nutrition"]["fats"],
            carbs=item["nutrition"]["carbs"]
        )

        db.add(product)
        added += 1

    db.commit()
    return {"added": added}


# Поиск товара в базе (GET)
@app.get("/products/{barcode}")
def get_product(barcode: str, db: Session = Depends(get_db)):
    product = db.query(models.Product).filter(models.Product.barcode == barcode).first()
    if not product:
        raise HTTPException(status_code=404, detail="Товар не найден")
    return product


# Получение всего списка товаров (GET)
@app.get("/products", response_model=list[ProductResponse])
def get_all_products(db: Session = Depends(get_db)):
    products = db.query(models.Product).all()
    return products


# Удаление товара по штрих-коду (DELETE)
@app.delete("/products/{barcode}")
def delete_product_by_barcode(barcode: str, db: Session = Depends(get_db)):
    product = db.query(models.Product).filter(models.Product.barcode == barcode).first()
    if not product:
        raise HTTPException(status_code=404, detail=f"Товар со штрих-кодом {barcode} не найден")
    db.delete(product)
    db.commit()
    return {"detail": f"Товар с штрих-кодом {barcode} успешно удален"}