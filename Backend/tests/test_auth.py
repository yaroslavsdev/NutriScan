import pytest


@pytest.mark.parametrize(
    ("username", "email", "password"),
    [
        ("user4", "user4@mail.ru", "pass1234"),
        ("Anna", "anna@yandex.ru", "Qwerty12345"),
        ("Михаил", "misha09@gmail.com", "abacaba09"),
    ]
)
def test_simple_registration(client, username: str, email: str, password: str):
    response = client.post("/auth/register", json={
        "username": username,
        "email": email,
        "password": password
    })
    assert response.status_code == 200

    data = response.json()
    assert "access_token" in data
    assert "refresh_token" in data
    assert data["token_type"] == "bearer"


def test_registration_with_same_email(client):
    body = {"username": "user4", "email": "user4@mail.ru", "password": "pass1234"}

    first = client.post("/auth/register", json=body)
    second = client.post("/auth/register", json=body)

    assert first.status_code == 200
    assert second.status_code == 400


def test_user_can_login(client):
    body = {"username": "user4", "email": "user4@mail.ru", "password": "pass1234"}
    client.post("/auth/register", json=body)

    response = client.post("/auth/login", json={
        "email": "user4@mail.ru",
        "password": "pass1234",
    })

    assert response.status_code == 200
    assert "refresh_token" in response.json()