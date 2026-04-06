from pydantic import BaseModel

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

    class Config:
        from_attributes = True

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

class AllergensUpdate(BaseModel):
    allergens: list[str]