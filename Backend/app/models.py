from sqlalchemy import Column, Integer, String, Float, ForeignKey, DateTime, func, Index, Date
from app.database import Base

# Таблица Пользователей
class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True)
    username = Column(String, nullable=False)
    email = Column(String, unique=True, index=True, nullable=False)
    password_hash = Column(String, nullable=False)
    daily_calorie_goal = Column(Integer, nullable=False, default=2000)


# Таблица refresh-токенов
class RefreshToken(Base):
    __tablename__ = "refresh_tokens"

    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    token_hash = Column(String, unique=True, nullable=False)
    expires_at = Column(DateTime(timezone=True), nullable=False)


# Таблица Продуктов
class Product(Base):
    __tablename__ = "products"

    id = Column(Integer, primary_key=True)
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

    id = Column(Integer, primary_key=True)
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

    id = Column(Integer, primary_key=True)
    name = Column(String, unique=True, nullable=False)


# Таблица триггеров
class AllergenTrigger(Base):
    __tablename__ = "allergen_triggers"

    allergen_id = Column(Integer, ForeignKey("allergens.id"), primary_key=True)
    trigger_word = Column(String, primary_key=True)


# Таблица аллергенов у пользователей
class UserAllergen(Base):
    __tablename__ = "user_allergens"

    user_id = Column(Integer, ForeignKey("users.id"), primary_key=True)
    allergen_id = Column(Integer, ForeignKey("allergens.id"), primary_key=True)


# Таблица записей в дневнике питания
class FoodDiaryEntry(Base):
    __tablename__ = "food_diary_entries"

    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    product_id = Column(Integer, ForeignKey("products.id"), nullable=True)

    meal_type = Column(String, nullable=False)
    weight_grams = Column(Float, nullable=False)

    calories = Column(Float, nullable=False)
    proteins = Column(Float, nullable=False)
    fats = Column(Float, nullable=False)
    carbs = Column(Float, nullable=False)

    product_name = Column(String, nullable=False)

    entry_date = Column(Date, nullable=False)
    created_at = Column(DateTime(timezone=True), server_default=func.now(), nullable=False)

    __table_args__ = (
        Index("index_food_diary_user_date", "user_id", "entry_date"),
    )