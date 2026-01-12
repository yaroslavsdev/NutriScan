from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from pydantic import BaseModel
import models
from database import engine, get_db

# Таблицы в Postgres, если их еще нет
models.Base.metadata.create_all(bind=engine)

app = FastAPI(title="NutriScan Simple API")

# Схема для добавления товара
class ProductCreate(BaseModel):
    barcode: str
    name: str
    ingredients: str
    calories: float

class ProductResponse(ProductCreate):
    id: int

    class Config:
        from_attributes = True

# --- ТОВАРЫ ---

# Добавить товар в базу (POST)
@app.post("/products")
def create_product(product: ProductCreate, db: Session = Depends(get_db)):
    db_product = models.Product(
        barcode=product.barcode,
        name=product.name,
        ingredients=product.ingredients,
        calories=product.calories
    )
    db.add(db_product)
    try:
        db.commit()
        db.refresh(db_product)
        return db_product
    except:
        db.rollback()
        raise HTTPException(status_code=400, detail="Товар с таким штрих-кодом уже есть")


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


# Удаление товара по штрихкоду (DELETE)
@app.delete("/products/barcode/{barcode}")
def delete_product_by_barcode(barcode: str, db: Session = Depends(get_db)):
    product = db.query(models.Product).filter(models.Product.barcode == barcode).first()
    if not product:
        raise HTTPException(status_code=404, detail=f"Товар со штрих-кодом {barcode} не найден")
    db.delete(product)
    db.commit()
    return {"detail": f"Товар с штрих-кодом {barcode} успешно удален"}