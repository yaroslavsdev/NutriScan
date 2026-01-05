import os
from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

# Настройки из переменных окружения (которые в Docker)
# Формат: postgresql://user:password@hostname:port/dbname
DATABASE_URL = os.getenv("DATABASE_URL", "postgresql://user:password@localhost:5432/nutriscan")

engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()