import json
from fastapi import APIRouter, Depends, HTTPException, UploadFile, File
from sqlalchemy.orm import Session
from app import models
from app.database import get_db
from app.schemas import *

router = APIRouter(prefix="/products", tags=["Products"])

# Добавить товар в базу (POST)
@router.post("")
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

@router.post("/import")
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
@router.get("/{barcode}")
def get_product(barcode: str, db: Session = Depends(get_db)):
    product = db.query(models.Product).filter(models.Product.barcode == barcode).first()
    if not product:
        raise HTTPException(status_code=404, detail="Товар не найден")
    return product


# Получение всего списка товаров (GET)
@router.get("", response_model=list[ProductResponse])
def get_all_products(db: Session = Depends(get_db)):
    products = db.query(models.Product).all()
    return products


# Удаление товара по штрих-коду (DELETE)
@router.delete("/{barcode}")
def delete_product_by_barcode(barcode: str, db: Session = Depends(get_db)):
    product = db.query(models.Product).filter(models.Product.barcode == barcode).first()
    if not product:
        raise HTTPException(status_code=404, detail=f"Товар со штрих-кодом {barcode} не найден")
    db.delete(product)
    db.commit()
    return {"detail": f"Товар с штрих-кодом {barcode} успешно удален"}