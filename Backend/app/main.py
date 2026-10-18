from fastapi import FastAPI
from app.routers import auth, diary, history, products
from app.database import SessionLocal
from app.update_allergen_triggers import update_allergen_triggers

db = SessionLocal()
try:
    update_allergen_triggers(db)
finally:
    db.close()

app = FastAPI(
    title="NutriScan API",
    openapi_tags=[
        {"name": "Auth", "description": "Авторизация и профиль"},
        {"name": "Diary", "description": "Дневник питания"},
        {"name": "History", "description": "История сканирований"},
        {"name": "Products", "description": "Работа с базой продуктов"}
    ]
)

app.include_router(auth.router)
app.include_router(diary.router)
app.include_router(history.router)
app.include_router(products.router)