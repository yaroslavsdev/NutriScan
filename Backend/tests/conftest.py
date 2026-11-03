import os
os.environ.setdefault("SECRET_KEY", "test-secret-key")

from fastapi.testclient import TestClient
from sqlalchemy.pool import StaticPool
from app.main import app
from app.database import get_db

import pytest
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from app import models
from app.database import Base

@pytest.fixture
def db_session():
    engine = create_engine("sqlite:///:memory:")
    Base.metadata.create_all(engine)
    session = sessionmaker(bind=engine)()
    yield session
    session.close()


@pytest.fixture
def client():
    engine = create_engine(
        "sqlite://",
        connect_args={"check_same_thread": False},
        poolclass=StaticPool
    )
    Base.metadata.create_all(engine)
    test_session = sessionmaker(bind=engine)

    def override_get_db():
        db = test_session()
        try:
            yield db
        finally:
            db.close()

    app.dependency_overrides[get_db] = override_get_db

    test_client = TestClient(app)
    test_client.db_factory = test_session

    yield test_client

    app.dependency_overrides.clear()


@pytest.fixture
def auth_headers(client):
    response = client.post("/auth/register", json={
        "username": "simple_user",
        "email": "simple_user@mail.ru",
        "password": "test1234",
    })
    token = response.json()["access_token"]
    return {"Authorization": f"Bearer {token}"}


@pytest.fixture
def product(client):
    db = client.db_factory()
    item = models.Product(
        barcode="4600000000001",
        name="Молоко",
        brand="Бренд",
        ingredients="Молоко",
        calories=67.0,
        proteins=3.0,
        fats=4.0,
        carbs=5.5,
    )
    db.add(item)
    db.commit()
    db.refresh(item)
    db.close()
    return item


@pytest.fixture
def user_with_gluten(db_session):
    user = models.User(username="test", email="test@mail.ru", password_hash="X")
    allergen = models.Allergen(name="gluten")
    db_session.add_all([user, allergen])
    db_session.commit()

    db_session.add(models.AllergenTrigger(allergen_id=allergen.id, trigger_word="глютен"))
    db_session.add(models.UserAllergen(user_id=user.id, allergen_id=allergen.id))
    db_session.commit()
    return user


@pytest.fixture
def other_user(db_session):
    user = models.User(username="other", email="other@mail.ru", password_hash="XYZ")
    db_session.add(user)
    db_session.commit()
    return user


@pytest.fixture
def user_without_gluten(db_session):
    user = models.User(username="user2", email="user2@yandex.ru", password_hash="XY")
    db_session.add_all([user])
    db_session.commit()
    return user


@pytest.fixture
def user_with_2_allergens(db_session):
    user = models.User(username="user3", email="user3@mail.ru", password_hash="XYZ")
    allergen1 = models.Allergen(name="gluten")
    allergen2 = models.Allergen(name="eggs")
    db_session.add_all([user, allergen1, allergen2])
    db_session.commit()

    db_session.add(models.AllergenTrigger(allergen_id=allergen1.id, trigger_word="глютен"))
    db_session.add(models.UserAllergen(user_id=user.id, allergen_id=allergen1.id))

    db_session.add(models.AllergenTrigger(allergen_id=allergen2.id, trigger_word="яйца"))
    db_session.add(models.UserAllergen(user_id=user.id, allergen_id=allergen2.id))

    db_session.commit()
    return user