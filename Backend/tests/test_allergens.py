from app.routers.products import get_matched_allergens

def test_no_allergens_returns_empty(db_session, user_with_gluten):
    result = get_matched_allergens(db_session, "вода, сахар", user_with_gluten.id)
    assert result == []