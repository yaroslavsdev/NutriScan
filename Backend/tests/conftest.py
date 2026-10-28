import os
os.environ.setdefault("SECRET_KEY", "test-secret-key")

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
    user = models.User(username="test", email="test@mail.ru", password_hash="X")
    db_session.add_all([user])
    db_session.commit()
    return user