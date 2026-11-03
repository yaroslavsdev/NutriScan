import pytest


def register(client, username="user", email="user@mail.ru", password="pass1234"):
    response = client.post("/auth/register", json={"username": username, "email": email, "password": password})
    return response


def login(client, email, password):
    response = client.post("/auth/login", json={"email": email, "password": password})
    return response


@pytest.mark.parametrize(
    ("username", "email", "password"),
    [
        ("user4", "user4@mail.ru", "pass1234"),
        ("Anna", "anna@yandex.ru", "Qwerty12345"),
        ("Михаил", "misha09@gmail.com", "abacaba09"),
    ]
)
def test_simple_registration(client, username: str, email: str, password: str):
    response = register(client, username, email, password)
    assert response.status_code == 200

    data = response.json()
    assert "access_token" in data
    assert "refresh_token" in data
    assert data["token_type"] == "bearer"


def test_registration_with_same_email(client):
    first = register(client)
    second = register(client)
    assert first.status_code == 200
    assert second.status_code == 400


def test_user_can_login(client):
    register(client)
    response = login(client, "user@mail.ru", "pass1234")
    assert response.status_code == 200
    assert "refresh_token" in response.json()


def test_login_wrong_password(client):
    register(client)
    assert login(client, "user@mail.ru", "wrong-pass").status_code == 401


def test_login_incorrect_email(client):
    assert login(client, "incorrect@mail.ru", "pass1234").status_code == 401


def test_get_user_without_token(client):
    assert client.get("/auth/user").status_code == 401


def test_get_user_with_incorrect_token(client):
    response = client.get("/auth/user", headers={"Authorization": "Bearer ...incorrect..."})
    assert response.status_code == 401


def test_get_user_with_token(client, auth_headers):
    response = client.get("/auth/user", headers=auth_headers)

    assert response.status_code == 200
    assert response.json()["email"] == "simple_user@mail.ru"


def test_refresh_sends_new_tokens(client):
    tokens = register(client)
    assert tokens.status_code == 200
    data = tokens.json()

    response = client.post("/auth/refresh", json={"refresh_token": data["refresh_token"]})
    assert response.status_code == 200
    assert response.json()["refresh_token"] != data["refresh_token"]


def test_refresh_with_incorrect_token(client):
    response = client.post("/auth/refresh", json={"refresh_token": "...incorrect..."})
    assert response.status_code == 401


def test_refresh_after_logout(client):
    tokens = register(client)
    data = tokens.json()
    body = {"refresh_token": data["refresh_token"]}
    client.post("/auth/logout", json=body)
    response = client.post("/auth/refresh", json=body)

    assert response.status_code == 401


def test_change_password_with_wrong_current_password(client, auth_headers):
    response = client.post("/auth/password", headers=auth_headers, json={
        "current_password": "wrong", "new_password": "newpass123"
    })
    assert response.status_code == 400


def test_change_password(client):
    tokens = register(client)
    data = tokens.json()
    headers = {"Authorization": f"Bearer {data['access_token']}"}

    response = client.post("/auth/password", headers=headers, json={
        "current_password": "pass1234", "new_password": "newpass123"
    })

    assert response.status_code == 200
    assert login(client, "user@mail.ru", "pass1234").status_code == 401
    assert login(client, "user@mail.ru", "newpass123").status_code == 200
    old = client.post("/auth/refresh", json={"refresh_token": data["refresh_token"]})
    assert old.status_code == 401


def test_change_username(client, auth_headers):
    response = client.post("/auth/username", headers=auth_headers, json={"username": "NewName"})
    assert response.status_code == 200
    assert client.get("/auth/user", headers=auth_headers).json()["username"] == "NewName"


def test_change_username_empty(client, auth_headers):
    response = client.post("/auth/username", headers=auth_headers, json={"username": ""})
    assert response.status_code == 422


@pytest.mark.parametrize(
    ("goal", "status"),
    [(499, 422), (500, 200), (10000, 200), (10001, 422)]
)
def test_calorie_goal_limits(client, auth_headers, goal: int, status: int):
    response = client.post("/auth/calories", headers=auth_headers, json={"daily_calorie_goal": goal})
    assert response.status_code == status