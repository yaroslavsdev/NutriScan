from sqlalchemy import Column, Integer, String, Float, ForeignKey, DateTime, func, ARRAY, Index
from app.database import Base


# Таблица Пользователей
class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    username = Column(String, nullable=False)
    email = Column(String, unique=True, index=True, nullable=False)
    password_hash = Column(String, nullable=False)
    daily_calorie_goal = Column(Integer, nullable=False, default=2000)


# Таблица Продуктов
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


# Таблица истории сканирования
class ScanHistory(Base):
    __tablename__ = "scan_history"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    product_id = Column(Integer, ForeignKey("products.id"), nullable=True)
    scanned_barcode = Column(String, nullable=True)
    status = Column(String, nullable=False, default="success")
    scan_time = Column(DateTime(timezone=True), server_default=func.now(), nullable=False)

    __table_args__ = (
        Index("index_scan_history_user_time", "user_id", "scan_time"),
    )


# Таблица аллергенов
class Allergen(Base):
    __tablename__ = "allergens"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, unique=True, nullable=False)


# Таблица триггеров
class AllergenTrigger(Base):
    __tablename__ = "allergen_triggers"

    id = Column(Integer, primary_key=True, index=True)
    allergen_id = Column(Integer, ForeignKey("allergens.id"), nullable=False)
    trigger_word = Column(String, nullable=False)

    __table_args__ = (
        Index("index_allergen_triggers_allergen", "allergen_id"),
    )


# Таблица аллергенов у пользователей
class UserAllergen(Base):
    __tablename__ = "user_allergens"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    allergen_id = Column(Integer, ForeignKey("allergens.id"), nullable=False)

    __table_args__ = (
        Index("index_user_allergens_user", "user_id"),
    )