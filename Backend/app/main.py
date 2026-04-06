import uvicorn
from fastapi import FastAPI
from app.routers import auth, products
from app.database import engine
from app import models

# Создаем таблицы
models.Base.metadata.create_all(bind=engine)

app = FastAPI(
    title="NutriScan API",
    openapi_tags=[
        {"name": "Auth", "description": "Авторизация и профиль"},
        {"name": "Products", "description": "Работа с базой продуктов"}
    ]
)

# Подключаем роутеры
app.include_router(auth.router)
app.include_router(products.router)

if __name__ == "__main__":
    uvicorn.run("app.main:app", host="0.0.0.0", port=8000, reload=True)