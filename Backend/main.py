from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from pydantic import BaseModel
import models
from database import engine, get_db

# Таблицы в Postgres, если их еще нет
models.Base.metadata.create_all(bind=engine)

app = FastAPI(title="NutriScan Simple API")


# Схема для добавления товара (то, что присылает Android)
class ProductCreate(BaseModel):
    barcode: str
    name: str
    ingredients: str
    calories: float


# --- ТОВАРЫ ---

# 1. Добавить товар в базу (POST)
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


# 2. Найти товар по штрих-коду (GET)
# Запись в историю
@app.get("/products/{barcode}")
def get_product(barcode: str, db: Session = Depends(get_db)):
    product = db.query(models.Product).filter(models.Product.barcode == barcode).first()
    if not product:
        raise HTTPException(status_code=404, detail="Товар не найден")

    # // УБРАТЬ ПЕРЕД ПРОДОМ //
    # new_scan = models.ScanHistory(user_id=1, product_barcode=barcode)
    # db.add(new_scan)
    # db.commit()

    return product


# 3. Получить всю историю сканирований (GET)
@app.get("/history")
def get_history(db: Session = Depends(get_db)):
    # Возвращает все записи из таблицы истории, отсортированные по времени (сначала новые)
    history = db.query(models.ScanHistory).order_by(models.ScanHistory.scan_time.desc()).all()
    return history