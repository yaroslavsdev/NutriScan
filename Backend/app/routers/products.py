import json
from fastapi import APIRouter, Depends, HTTPException, UploadFile, File
from sqlalchemy.orm import Session

from app import models, dependencies
from app.database import get_db
from app.schemas import *
from sqlalchemy import desc

router = APIRouter(prefix="/products", tags=["Products"])


# Добавление товара в базу
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


# Импорт нескольких товаров
@router.post("/import")
def import_products(
    file: UploadFile = File(...),
    db: Session = Depends(get_db)
):
    data = json.load(file.file)

    unique = {item["barcode"]: item for item in data}

    existing = set(
        row.barcode for row in
        db.query(models.Product.barcode)
          .filter(models.Product.barcode.in_(unique.keys()))
          .all()
    )

    to_insert = [
        models.Product(
            barcode=item["barcode"],
            name=item["name"],
            brand=item.get("brand"),
            ingredients=item.get("ingredients", ""),
            calories=item["nutrition"]["calories"],
            proteins=item["nutrition"]["proteins"],
            fats=item["nutrition"]["fats"],
            carbs=item["nutrition"]["carbs"],
        )
        for item in unique.values()
        if item["barcode"] not in existing
    ]

    BATCH_SIZE = 500
    for i in range(0, len(to_insert), BATCH_SIZE):
        db.bulk_save_objects(to_insert[i:i + BATCH_SIZE])
        db.flush()

    db.commit()
    return {"added": len(to_insert), "skipped": len(data) - len(to_insert)}


# Получение одного товара
@router.get("/{barcode}")
def get_product_by_barcode(
    barcode: str,
    db: Session = Depends(get_db),
    current_user: models.User = Depends(dependencies.get_current_user),
):
    product = db.query(models.Product).filter(models.Product.barcode == barcode).first()

    if product:
        db.add(models.ScanHistory(
            user_id=current_user.id,
            product_id=product.id,
            status="success",
        ))
        db.flush()

        old_ids = (
            db.query(models.ScanHistory.id)
            .filter(models.ScanHistory.user_id == current_user.id)
            .order_by(models.ScanHistory.scan_time.desc())
            .offset(100)
            .all()
        )
        old_ids = [row.id for row in old_ids]

        if old_ids:
            db.query(models.ScanHistory).filter(
                models.ScanHistory.id.in_(old_ids)
            ).delete(synchronize_session=False)

        db.commit()

    if not product:
        raise HTTPException(status_code=404, detail="Товар не найден")

    return product


# Получение истории сканирований пользователя
@router.get("/history/scans", response_model=list[ScanHistoryItem])
def get_scan_history(
    limit: int = 100,
    db: Session = Depends(get_db),
    current_user: models.User = Depends(dependencies.get_current_user),
):
    rows = (
        db.query(models.ScanHistory, models.Product)
        .join(models.Product, models.ScanHistory.product_id == models.Product.id)
        .filter(
            models.ScanHistory.user_id == current_user.id,
            models.ScanHistory.status == "success",
        )
        .order_by(desc(models.ScanHistory.scan_time))
        .limit(limit)
        .all()
    )

    return [
        ScanHistoryItem(
            id=scan.id,
            barcode=product.barcode,
            name=product.name,
            brand=product.brand,
            ingredients=product.ingredients,
            calories=product.calories,
            proteins=product.proteins,
            fats=product.fats,
            carbs=product.carbs,
            scan_time=scan.scan_time,
        )
        for scan, product in rows
    ]


# Получение списка товаров
@router.get("", response_model=list[ProductResponse])
def get_all_products(db: Session = Depends(get_db)):
    products = db.query(models.Product).all()
    return products


# Удаление товара по штрих-коду
@router.delete("/{barcode}")
def delete_product_by_barcode(barcode: str, db: Session = Depends(get_db)):
    product = db.query(models.Product).filter(models.Product.barcode == barcode).first()
    if not product:
        raise HTTPException(status_code=404, detail=f"Товар со штрих-кодом {barcode} не найден")
    db.delete(product)
    db.commit()
    return {"detail": f"Товар с штрих-кодом {barcode} успешно удален"}