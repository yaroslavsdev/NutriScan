import json
import uuid

from fastapi import APIRouter, Depends, HTTPException, UploadFile, File
from sqlalchemy.orm import Session

from app import models, dependencies
from app.allergen_data import NEGATION_WORDS
from app.database import get_db
from app.schemas import *

router = APIRouter(prefix="/products", tags=["Products"])


# Добавление товара в базу
@router.post("", response_model=ProductResponse)
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

# Добавление товара без штрих-кода
@router.post("/custom", response_model=ProductResponse)
def create_custom_product(
    data: CustomProductCreate,
    db: Session = Depends(get_db),
    current_user: models.User = Depends(dependencies.get_current_user)
):
    internal_code = "custom-" + uuid.uuid4().hex[:12]

    product = models.Product(
        barcode=internal_code,
        name=data.name,
        brand=data.brand,
        ingredients=data.ingredients,
        calories=data.nutrition.calories,
        proteins=data.nutrition.proteins,
        fats=data.nutrition.fats,
        carbs=data.nutrition.carbs,
    )

    db.add(product)
    db.commit()
    db.refresh(product)

    return product


# Получение аллергенов для конкретного пользователя по составу продукта
def get_matched_allergens(db: Session, ingredients: str, user_id: int) -> list[str]:
    ingredients_lower = ingredients.lower()

    rows = (db.query(models.Allergen.name, models.AllergenTrigger.trigger_word)
        .join(models.AllergenTrigger, models.AllergenTrigger.allergen_id == models.Allergen.id)
        .join(models.UserAllergen, models.UserAllergen.allergen_id == models.Allergen.id)
        .filter(models.UserAllergen.user_id == user_id)
        .all()
    )

    matched = set()
    for name, trigger_word in rows:
        trigger_lower = trigger_word.lower()
        index = ingredients_lower.find(trigger_lower)

        if index == -1:
            continue

        words_before = ingredients_lower[max(0, index - 30):index]
        is_negated = any(negative in words_before for negative in NEGATION_WORDS)

        if not is_negated:
            matched.add(name)

    return list(matched)


# Получение одного товара
@router.get("/{barcode}", response_model=ProductResponse)
def get_product_by_barcode(
    barcode: str,
    db: Session = Depends(get_db),
    current_user: models.User = Depends(dependencies.get_current_user)
):
    product = db.query(models.Product).filter(models.Product.barcode == barcode).first()

    db.add(models.ScanHistory(
        user_id=current_user.id,
        product_id=product.id if product else None,
        scanned_barcode=barcode if not product else None,
        status="success" if product else "not_found",
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

    matched_allergens = get_matched_allergens(db, product.ingredients, current_user.id)

    return ProductResponse(
        id=product.id,
        barcode=product.barcode,
        name=product.name,
        brand=product.brand,
        ingredients=product.ingredients,
        calories=product.calories,
        proteins=product.proteins,
        fats=product.fats,
        carbs=product.carbs,
        matched_allergens=matched_allergens,
    )