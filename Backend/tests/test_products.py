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


def test_get_product_without_token(client, product):
    response = client.get(f"/products/{product.barcode}")
    assert response.status_code == 401