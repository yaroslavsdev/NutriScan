from pydantic import BaseModel, Field
from datetime import datetime, date as date_type

MEAL_TYPES = ["breakfast", "lunch", "dinner", "snack"]

class Nutrition(BaseModel):
    calories: float
    proteins: float
    fats: float
    carbs: float

class ProductCreate(BaseModel):
    barcode: str
    name: str
    brand: str | None = None
    ingredients: str
    nutrition: Nutrition

class ProductResponse(BaseModel):
    id: int
    barcode: str
    name: str
    brand: str | None
    ingredients: str
    calories: float
    proteins: float
    fats: float
    carbs: float
    matched_allergens: list[str] = []

    class Config:
        from_attributes = True

class ScanHistoryItem(BaseModel):
    id: int
    status: str
    barcode: str
    name: str | None = None
    brand: str | None = None
    ingredients: str | None = None
    calories: float | None = None
    proteins: float | None = None
    fats: float | None = None
    carbs: float | None = None
    scan_time: datetime

    class Config:
        from_attributes = True

# Профиль
class UserCreate(BaseModel):
    username: str
    email: str
    password: str

class UserLogin(BaseModel):
    email: str
    password: str

class Token(BaseModel):
    access_token: str
    token_type: str

class UsernameUpdate(BaseModel):
    username: str = Field(min_length=1, max_length=50)

class PasswordUpdate(BaseModel):
    current_password: str
    new_password: str = Field(min_length=6, max_length=100)

class AllergensUpdate(BaseModel):
    allergens: list[str]

class NutritionUpdate(BaseModel):
    daily_calorie_goal: int = Field(ge=500, le=10000)

class DiaryEntryCreate(BaseModel):
    barcode: str
    meal_type: str
    weight_grams: float = Field(gt = 0, le = 5000)
    entry_date: date_type

class DiaryEntryItem(BaseModel):
    id: int
    product_name: str
    weight_grams: float
    calories: float
    proteins: float
    fats: float
    carbs: float
    created_at: datetime

    class Config:
        from_attributes = True

class DiaryDayResponse(BaseModel):
    date: date_type
    total_calories: float
    total_proteins: float
    total_fats: float
    total_carbs: float
    breakfast: list[DiaryEntryItem] = []
    lunch: list[DiaryEntryItem] = []
    dinner: list[DiaryEntryItem] = []
    snack: list[DiaryEntryItem] = []