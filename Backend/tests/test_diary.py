DATE = "2026-11-01"


def add_entry(client, headers, barcode, meal_type="breakfast", weight=100, date=DATE):
    return client.post("/diary", headers=headers, json={
        "barcode": barcode,
        "meal_type": meal_type,
        "weight_grams": weight,
        "entry_date": date,
    })


def register_other_user(client):
    response = client.post("/auth/register", json={
        "username": "other",
        "email": "other@mail.ru",
        "password": "test1234",
    })
    token = response.json()["access_token"]
    return {"Authorization": f"Bearer {token}"}


def test_add_entry_calculates_correctly(client, auth_headers, product):
    response = add_entry(client, auth_headers, product.barcode, weight=150)
    assert response.status_code == 200
    data = response.json()
    assert data["calories"] == 100.5
    assert data["proteins"] == 4.5
    assert data["product_name"] == "Молоко"


def test_add_entry_wrong_meal_type(client, auth_headers, product):
    response = add_entry(client, auth_headers, product.barcode, meal_type="midnight")
    assert response.status_code == 400


def test_add_entry_zero_weight(client, auth_headers, product):
    response = add_entry(client, auth_headers, product.barcode, weight=0)
    assert response.status_code == 422


def test_add_entry_too_big_weight(client, auth_headers, product):
    response = add_entry(client, auth_headers, product.barcode, weight=5001)
    assert response.status_code == 422


def test_add_entry_unknown_barcode(client, auth_headers):
    response = add_entry(client, auth_headers, "0000000000000")
    assert response.status_code == 404


def test_add_entry_without_token(client, product):
    response = add_entry(client, {}, product.barcode)
    assert response.status_code == 401


def test_get_day_grouped(client, auth_headers, product):
    add_entry(client, auth_headers, product.barcode, meal_type="breakfast", weight=100)
    add_entry(client, auth_headers, product.barcode, meal_type="lunch", weight=200)
    response = client.get(f"/diary?date={DATE}", headers=auth_headers)

    assert response.status_code == 200
    data = response.json()
    assert len(data["breakfast"]) == 1
    assert len(data["lunch"]) == 1
    assert data["dinner"] == []
    assert data["snack"] == []
    assert data["total_calories"] == 201


def test_get_empty_day(client, auth_headers):
    response = client.get(f"/diary?date={DATE}", headers=auth_headers)

    assert response.status_code == 200
    data = response.json()
    assert data["total_calories"] == 0
    assert data["breakfast"] == []


def test_entry_of_other_date_is_ignored(client, auth_headers, product):
    add_entry(client, auth_headers, product.barcode, date="2026-10-31")
    response = client.get(f"/diary?date={DATE}", headers=auth_headers)
    assert response.json()["breakfast"] == []


def test_entry_of_other_user_is_ignored(client, auth_headers, product):
    other_headers = register_other_user(client)
    add_entry(client, other_headers, product.barcode)
    response = client.get(f"/diary?date={DATE}", headers=auth_headers)
    assert response.json()["breakfast"] == []


def test_entry_has_barcode(client, auth_headers, product):
    add_entry(client, auth_headers, product.barcode)
    response = client.get(f"/diary?date={DATE}", headers=auth_headers)
    entry = response.json()["breakfast"][0]
    assert entry["barcode"] == product.barcode


def test_get_day_without_token(client):
    response = client.get(f"/diary?date={DATE}")
    assert response.status_code == 401