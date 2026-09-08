from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from app import models, schemas, auth_utils, database, dependencies

router = APIRouter(prefix="/auth", tags=["Auth"])


# Регистрация пользователя
@router.post("/register", response_model=schemas.Token)
def register(user: schemas.UserCreate, db: Session = Depends(database.get_db)):
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


# Авторизация пользователя
@router.post("/login", response_model=schemas.Token)
def login(user: schemas.UserLogin, db: Session = Depends(database.get_db)):
    db_user = db.query(models.User).filter(models.User.email == user.email).first()
    if not db_user or not auth_utils.verify_password(user.password, db_user.password_hash):
        raise HTTPException(status_code=401, detail="Неверный email или пароль")

    token = auth_utils.create_access_token(data={"sub": db_user.email})
    return {"access_token": token, "token_type": "bearer"}


# Получение аллергенов пользователя
def get_user_allergen_names(db: Session, user_id: int) -> list[str]:
    rows = (
        db.query(models.Allergen.name)
        .join(models.UserAllergen, models.UserAllergen.allergen_id == models.Allergen.id)
        .filter(models.UserAllergen.user_id == user_id)
        .all()
    )
    return [row.name for row in rows]


# Получение информации о пользователе
@router.get("/me")
def get_me(
        db: Session = Depends(database.get_db),
        current_user: models.User = Depends(dependencies.get_current_user)
):
    return {
        "username": current_user.username,
        "email": current_user.email,
        "allergens": get_user_allergen_names(db, current_user.id),
        "dailyCalorieGoal": current_user.daily_calorie_goal
    }


# Отправить список аллергенов
@router.post("/allergens")
def save_user_allergens(
        data: schemas.AllergensUpdate,
        db: Session = Depends(database.get_db),
        current_user: models.User = Depends(dependencies.get_current_user)
):
    db.query(models.UserAllergen).filter(
        models.UserAllergen.user_id == current_user.id
    ).delete()

    allergens = db.query(models.Allergen).filter(
        models.Allergen.name.in_(data.allergens)
    ).all()

    for allergen in allergens:
        db.add(models.UserAllergen(user_id=current_user.id, allergen_id=allergen.id))

    db.commit()

    saved_names = [allergen.name for allergen in allergens]
    return {"status" : "success", "saved_allergens" : saved_names}


# Получить список аллергенов
@router.get("/allergens")
def get_user_allergens(
        db: Session = Depends(database.get_db),
        current_user: models.User = Depends(dependencies.get_current_user)
):
    return {"allergens": get_user_allergen_names(db, current_user.id)}


# Обновить лимит калорий
@router.post("/calories")
def save_user_calorie_goal(
    data: schemas.NutritionUpdate,
    db: Session = Depends(database.get_db),
    current_user: models.User = Depends(dependencies.get_current_user)
):
    current_user.daily_calorie_goal = data.daily_calorie_goal

    db.commit()
    db.refresh(current_user)

    return {
        "status": "success",
        "daily_calorie_goal": current_user.daily_calorie_goal
    }