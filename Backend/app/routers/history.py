from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from app import models, dependencies
from app.database import get_db
from app.schemas import *

router = APIRouter(prefix="/history", tags=["History"])


# Получение истории сканирований пользователя
@router.get("/scans", response_model=list[ScanHistoryItem])
def get_scan_history(
    limit: int = 100,
    db: Session = Depends(get_db),
    current_user: models.User = Depends(dependencies.get_current_user),
):
    rows = (
        db.query(models.ScanHistory, models.Product)
        .outerjoin(models.Product, models.ScanHistory.product_id == models.Product.id)
        .filter(models.ScanHistory.user_id == current_user.id)
        .order_by(models.ScanHistory.scan_time.desc())
        .limit(limit)
        .all()
    )

    result = []
    for scan, product in rows:
        result.append(ScanHistoryItem(
            id=scan.id,
            status=scan.status,
            barcode=product.barcode if product else scan.scanned_barcode,
            name=product.name if product else None,
            brand=product.brand if product else None,
            ingredients=product.ingredients if product else None,
            calories=product.calories if product else None,
            proteins=product.proteins if product else None,
            fats=product.fats if product else None,
            carbs=product.carbs if product else None,
            scan_time=scan.scan_time,
        ))

    return result