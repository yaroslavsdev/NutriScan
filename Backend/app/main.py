from fastapi import FastAPI
from app.routers import auth, products, diary
from app.database import engine, SessionLocal
from app import models
from app.update_allergen_triggers import update_allergen_triggers

models.Base.metadata.create_all(bind=engine)

db = SessionLocal()
try:
    update_allergen_triggers(db)
finally:
    db.close()

app = FastAPI(
    title="NutriScan API",
    openapi_tags=[
        {"name": "Auth", "description": "Авторизация и профиль"},
        {"name": "Products", "description": "Работа с базой продуктов"},
        {"name": "Diary", "description": "Дневник питания"}
    ]
)

app.include_router(auth.router)
app.include_router(products.router)
app.include_router(diary.router)