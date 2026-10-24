from datetime import datetime, timezone, timedelta

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

    return issue_tokens(db, new_user)


# Авторизация пользователя
@router.post("/login", response_model=schemas.Token)
def login(user: schemas.UserLogin, db: Session = Depends(database.get_db)):
    db_user = db.query(models.User).filter(models.User.email == user.email).first()
    if not db_user or not auth_utils.verify_password(user.password, db_user.password_hash):
        raise HTTPException(status_code=401, detail="Неверный email или пароль")

    return issue_tokens(db, db_user)


def issue_tokens(db: Session, user: models.User) -> dict:
    now = datetime.now(timezone.utc)

    db.query(models.RefreshToken).filter(
        models.RefreshToken.user_id == user.id,
        models.RefreshToken.expires_at < now
    ).delete()

    refresh_token = auth_utils.create_refresh_token()
    refresh_hash = auth_utils.hash_token(refresh_token)
    expires_at = now + timedelta(days=auth_utils.REFRESH_TOKEN_DAYS)

    db.add(models.RefreshToken(
        user_id=user.id,
        token_hash=refresh_hash,
        expires_at=expires_at
    ))
    db.commit()

    access_token = auth_utils.create_access_token(user.id)

    return {
        "access_token": access_token,
        "refresh_token": refresh_token,
        "token_type": "bearer"
    }


@router.post("/refresh", response_model=schemas.Token)
def refresh(data: schemas.RefreshRequest, db: Session = Depends(database.get_db)):
    token_hash = auth_utils.hash_token(data.refresh_token)

    saved = db.query(models.RefreshToken).filter(
        models.RefreshToken.token_hash == token_hash
    ).first()

    if not saved or saved.expires_at < datetime.now(timezone.utc):
        raise HTTPException(status_code=401, detail="Недействительный refresh-токен")

    user = db.query(models.User).filter(models.User.id == saved.user_id).first()
    if not user:
        raise HTTPException(status_code=401, detail="Пользователь не найден")

    db.delete(saved)
    db.commit()

    return issue_tokens(db, user)


# Выход из профиля
@router.post("/logout")
def logout(data: schemas.RefreshRequest, db: Session = Depends(database.get_db)):
    token_hash = auth_utils.hash_token(data.refresh_token)
    db.query(models.RefreshToken).filter(
        models.RefreshToken.token_hash == token_hash
    ).delete()
    db.commit()

    return {"status": "success"}


# Изменение имени пользователя
@router.post("/username")
def update_username(
    data: schemas.UsernameUpdate,
    db: Session = Depends(database.get_db),
    current_user: models.User = Depends(dependencies.get_current_user)
):
    current_user.username = data.username
    db.commit()

    return {"status": "success", "username": current_user.username}


# Изменение пароля
@router.post("/password")
def update_password(
    data: schemas.PasswordUpdate,
    db: Session = Depends(database.get_db),
    current_user: models.User = Depends(dependencies.get_current_user)
):
    if not auth_utils.verify_password(data.current_password, current_user.password_hash):
        raise HTTPException(status_code=400, detail="Неверный текущий пароль")

    current_user.password_hash = auth_utils.hash_password(data.new_password)
    db.query(models.RefreshToken).filter(models.RefreshToken.user_id == current_user.id).delete()
    db.commit()

    return {"status": "success"}


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
@router.get("/user")
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