from sqlalchemy import Column, Integer, String, Float, ForeignKey, DateTime, func
from database import Base


# 1. Таблица Юзеров
class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    username = Column(String, unique=True, index=True)
    email = Column(String, unique=True, index=True)
    password_hash = Column(String)  # Храним только хэш пароля!

    # Персональные аллергены (можно хранить строкой "арахис, молоко")
    user_allergens = Column(String, default="")


# 2. Таблица Продуктов
class Product(Base):
    __tablename__ = "products"

    id = Column(Integer, primary_key=True, index=True)

    barcode = Column(String, unique=True, index=True, nullable=False)
    name = Column(String, nullable=False)
    brand = Column(String, nullable=True)

    ingredients = Column(String)

    calories = Column(Float)
    proteins = Column(Float)
    fats = Column(Float)
    carbs = Column(Float)


# 3. Таблица истории сканирования
class ScanHistory(Base):
    __tablename__ = "scan_history"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"))
    product_barcode = Column(String)
    scan_time = Column(DateTime(timezone=True), server_default=func.now())