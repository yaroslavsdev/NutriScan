HISTORY_URL = "/history/scans"


def scan(client, headers, barcode, for_viewing=False):
    url = f"/products/{barcode}"
    if for_viewing:
        url += "?for_viewing=true"
    return client.get(url, headers=headers)


def get_history(client, headers):
    return client.get(HISTORY_URL, headers=headers)


def register_other_user(client):
    response = client.post("/auth/register", json={
        "username": "other",
        "email": "other@mail.ru",
        "password": "test1234",
    })
    token = response.json()["access_token"]
    return {"Authorization": f"Bearer {token}"}


def test_start_history_is_empty(client, auth_headers):
    response = get_history(client, auth_headers)
    assert response.status_code == 200
    assert response.json() == []


def test_history_without_token(client):
    response = client.get(HISTORY_URL)
    assert response.status_code == 401


def test_scan_product_is_saved(client, auth_headers, product):
    scan(client, auth_headers, product.barcode)
    items = get_history(client, auth_headers).json()

    assert len(items) == 1
    assert items[0]["status"] == "success"
    assert items[0]["barcode"] == product.barcode
    assert items[0]["name"] == "Молоко"


def test_scan_unknown_product_is_saved_as_not_found(client, auth_headers):
    response = scan(client, auth_headers, "0000000000000")
    assert response.status_code == 404
    items = get_history(client, auth_headers).json()

    assert len(items) == 1
    assert items[0]["status"] == "not_found"
    assert items[0]["barcode"] == "0000000000000"


def test_for_viewing_does_not_save_scan(client, auth_headers, product):
    scan(client, auth_headers, product.barcode, for_viewing=True)
    items = get_history(client, auth_headers).json()

    assert items == []


def test_history_limits_100_scans(client, auth_headers, product):
    for i in range(101):
        scan(client, auth_headers, product.barcode)
    items = get_history(client, auth_headers).json()

    assert len(items) == 100


def test_history_of_other_user_is_ignored(client, auth_headers, product):
    other_headers = register_other_user(client)
    scan(client, other_headers, product.barcode)
    items = get_history(client, auth_headers).json()

    assert items == []