import pytest

from app.routers.products import get_matched_allergens

def test_no_allergens_returns_empty(db_session, user_with_gluten):
    result = get_matched_allergens(db_session, "Вода, сахар", user_with_gluten.id)
    assert result == []

def test_gluten_found(db_session, user_with_gluten):
    result = get_matched_allergens(db_session, "Мука, глютен", user_with_gluten.id)
    assert result == ["gluten"]

def test_user_without_allergens_returns_empty(db_session, user_without_gluten):
    result = get_matched_allergens(db_session, "Вода, яйца, мука, глютен", user_without_gluten.id)
    assert result == []

def test_letters_size_does_not_matter(db_session, user_with_gluten):
    result = get_matched_allergens(db_session, "МУКа, глЮТЕн", user_with_gluten.id)
    assert result == ["gluten"]

def test_empty_ingredients(db_session, user_with_gluten):
    result = get_matched_allergens(db_session, "", user_with_gluten.id)
    assert result == []

def test_allergens_of_other_user_are_ignored(db_session, user_with_gluten, other_user):
    result = get_matched_allergens(db_session, "мука, глютен", other_user.id)
    assert result == []

@pytest.mark.parametrize(
    ("ingredients", "target"),
    [("Безглютеновая мука", []),
    ("Не содержит подсолнечное масло. Может содержать следы глютена.", ['gluten'])]
)
def test_trigger_inside_word(db_session, user_with_gluten, ingredients: str, target: bool):
    result = get_matched_allergens(db_session, ingredients, user_with_gluten.id)
    assert result == target