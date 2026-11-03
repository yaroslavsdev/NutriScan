from app import models


def add_allergen(client, name, trigger):
    db = client.db_factory()
    allergen = models.Allergen(name=name)
    db.add(allergen)
    db.commit()
    db.add(models.AllergenTrigger(allergen_id=allergen.id, trigger_word=trigger))
    db.commit()
    db.close()


def create_product_body(barcode="12345670"):
    return {
        "barcode": barcode,
        "name": "Печенье",
        "brand": "Вкусные продукты",
        "ingredients": "мука, сахар",
        "nutrition": {"calories": 400, "proteins": 6, "fats": 15, "carbs": 60},
    }


def custom_body():
    return {
        "name": "Пирог из яблок",
        "ingredients": "мука, яблоки, яйца, сахар, сливочное масло",
        "nutrition": {"calories": 250, "proteins": 4, "fats": 8, "carbs": 40},
    }


def test_get_product_without_token(client, product):
    response = client.get(f"/products/{product.barcode}")
    assert response.status_code == 401


def test_get_product(client, auth_headers, product):
    response = client.get(f"/products/{product.barcode}", headers=auth_headers)

    assert response.status_code == 200
    data = response.json()
    assert data["name"] == "Молоко"
    assert data["barcode"] == product.barcode
    assert data["calories"] == 67.0


def test_get_unknown_product(client, auth_headers):
    response = client.get("/products/0000000000000", headers=auth_headers)
    assert response.status_code == 404


def test_no_allergens_in_product(client, auth_headers, product):
    response = client.get(f"/products/{product.barcode}", headers=auth_headers)
    assert response.json()["matched_allergens"] == []


def test_user_allergen_is_found(client, auth_headers, product):
    add_allergen(client, "lactose", "молоко")
    client.post("/auth/allergens", headers=auth_headers, json={"allergens": ["lactose"]})
    response = client.get(f"/products/{product.barcode}", headers=auth_headers)

    assert response.json()["matched_allergens"] == ["lactose"]


def test_allergen_of_other_user_is_ignored(client, auth_headers, product):
    add_allergen(client, "lactose", "молоко")
    other = client.post("/auth/register", json={"username": "other", "email": "other@mail.ru", "password": "pass1234"}).json()

    other_headers = {"Authorization": f"Bearer {other['access_token']}"}
    client.post("/auth/allergens", headers=other_headers, json={"allergens": ["lactose"]})
    response = client.get(f"/products/{product.barcode}", headers=auth_headers)

    assert response.json()["matched_allergens"] == []


def test_create_product(client, auth_headers):
    response = client.post("/products", headers=auth_headers, json=create_product_body())

    assert response.status_code == 200
    assert response.json()["name"] == "Печенье"


def test_create_product_with_same_barcode(client, auth_headers):
    first = client.post("/products", headers=auth_headers, json=create_product_body())
    second = client.post("/products", headers=auth_headers, json=create_product_body())

    assert first.status_code == 200
    assert second.status_code == 400


def test_created_product_can_be_found(client, auth_headers):
    client.post("/products", headers=auth_headers, json=create_product_body("4600000000099"))
    response = client.get("/products/4600000000099", headers=auth_headers)

    assert response.status_code == 200


def test_create_custom_product(client, auth_headers):
    response = client.post("/products/custom", headers=auth_headers, json=custom_body())

    assert response.status_code == 200
    assert response.json()["barcode"].startswith("custom-")


def test_custom_products_have_different_barcodes(client, auth_headers):
    first = client.post("/products/custom", headers=auth_headers, json=custom_body())
    second = client.post("/products/custom", headers=auth_headers, json=custom_body())

    assert first.json()["barcode"] != second.json()["barcode"]


def test_custom_product_can_be_found(client, auth_headers):
    created = client.post("/products/custom", headers=auth_headers, json=custom_body())
    barcode = created.json()["barcode"]

    response = client.get(f"/products/{barcode}", headers=auth_headers)

    assert response.status_code == 200
    assert response.json()["name"] == "Пирог из яблок"


def test_custom_product_without_token(client):
    response = client.post("/products/custom", json=custom_body())
    assert response.status_code == 401