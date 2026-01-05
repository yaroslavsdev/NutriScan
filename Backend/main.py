from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
import models
from database import engine, get_db


models.Base.metadata.create_all(bind=engine)

app = FastAPI(title="NutriScan API")

@app.get("/products/{barcode}")
def read_product(barcode: str, db: Session = Depends(get_db)):
    product = db.query(models.Product).filter(models.Product.barcode == barcode).first()
    if product is None:
        raise HTTPException(status_code=404, detail="Product not found")
    return product

@app.post("/products/")
def create_product(barcode: str, name: str, ingredients: str, calories: float, db: Session = Depends(get_db)):
    db_product = models.Product(barcode=barcode, name=name, ingredients=ingredients, calories=calories)
    db.add(db_product)
    db.commit()
    db.refresh(db_product)
    return db_product