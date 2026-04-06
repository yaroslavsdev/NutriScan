from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from app import models, schemas, auth_utils, database, dependencies

router = APIRouter(prefix="/auth", tags=["Auth"])


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

@router.post("/login", response_model=schemas.Token)
def login(user: schemas.UserLogin, db: Session = Depends(database.get_db)):
    db_user = db.query(models.User).filter(models.User.email == user.email).first()
    if not db_user or not auth_utils.verify_password(user.password, db_user.password_hash):
        raise HTTPException(status_code=401, detail="Неверный email или пароль")

    token = auth_utils.create_access_token(data={"sub": db_user.email})
    return {"access_token": token, "token_type": "bearer"}

@router.get("/me")
def get_me(current_user: models.User = Depends(dependencies.get_current_user)):
    return {
        "username": current_user.username,
        "email": current_user.email,
        "allergens": current_user.user_allergens or []
    }

@router.post("/allergens")
def save_user_allergens(
        data: schemas.AllergensUpdate,
        db: Session = Depends(database.get_db),
        current_user: models.User = Depends(dependencies.get_current_user)
):
    current_user.user_allergens = data.allergens

    db.commit()
    db.refresh(current_user)

    return {"status": "success", "saved_allergens": current_user.user_allergens}


@router.get("/allergens")
def get_user_allergens(current_user: models.User = Depends(dependencies.get_current_user)):
    return {"allergens": current_user.user_allergens or []}