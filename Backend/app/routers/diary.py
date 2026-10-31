from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from datetime import date as date_type

from app import schemas, models, dependencies
from app.database import get_db

router = APIRouter(prefix="/diary", tags=["Diary"])

# Добавление записи
@router.post("", response_model=schemas.DiaryEntryItem)
def add_diary_entry(
        data: schemas.DiaryEntryCreate,
        db: Session = Depends(get_db),
        current_user: models.User = Depends(dependencies.get_current_user)
):
    if data.meal_type not in schemas.MEAL_TYPES:
        raise HTTPException(status_code=400, detail="Некорректная категория приёма пищи")

    product = db.query(models.Product).filter(
        models.Product.barcode == data.barcode
    ).first()

    if not product:
        raise HTTPException(status_code=404, detail="Товар не найден")

    factor = data.weight_grams / 100

    entry = models.FoodDiaryEntry(
        user_id=current_user.id,
        product_id=product.id,
        meal_type=data.meal_type,
        weight_grams=data.weight_grams,
        calories=product.calories * factor,
        proteins=product.proteins * factor,
        fats=product.fats * factor,
        carbs=product.carbs * factor,
        product_name=product.name,
        entry_date=data.entry_date
    )

    db.add(entry)
    db.commit()
    db.refresh(entry)

    return entry


# Получение дневника питания по дате
@router.get("", response_model=schemas.DiaryDayResponse)
def get_diary_day(
        date: date_type,
        db: Session = Depends(get_db),
        current_user: models.User = Depends(dependencies.get_current_user),
):
    rows = (
        db.query(models.FoodDiaryEntry, models.Product.barcode)
        .outerjoin(models.Product, models.FoodDiaryEntry.product_id == models.Product.id)
        .filter(
            models.FoodDiaryEntry.user_id == current_user.id,
            models.FoodDiaryEntry.entry_date == date,
        )
        .order_by(models.FoodDiaryEntry.id)
        .all()
    )

    grouped = {"breakfast": [], "lunch": [], "dinner": [], "snack": []}
    entries = []

    for entry, barcode in rows:
        entries.append(entry)

        item = schemas.DiaryEntryItem(
            id=entry.id,
            barcode=barcode,
            product_name=entry.product_name,
            weight_grams=entry.weight_grams,
            calories=entry.calories,
            proteins=entry.proteins,
            fats=entry.fats,
            carbs=entry.carbs,
            created_at=entry.created_at,
        )
        grouped[entry.meal_type].append(item)

    total_calories = sum(e.calories for e in entries)
    total_proteins = sum(e.proteins for e in entries)
    total_fats = sum(e.fats for e in entries)
    total_carbs = sum(e.carbs for e in entries)

    return schemas.DiaryDayResponse(
        date=date,
        total_calories=total_calories,
        total_proteins=total_proteins,
        total_fats=total_fats,
        total_carbs=total_carbs,
        breakfast=grouped["breakfast"],
        lunch=grouped["lunch"],
        dinner=grouped["dinner"],
        snack=grouped["snack"],
    )