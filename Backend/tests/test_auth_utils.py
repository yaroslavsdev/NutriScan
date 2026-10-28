import pytest

from jose import jwt
from app.auth_utils import (
    ALGORITHM,
    SECRET_KEY,
    create_access_token,
    create_refresh_token,
    hash_password,
    hash_token,
    verify_password,
)

@pytest.mark.parametrize("password", ["pass!", "Qwerty12345", "2026"])
def test_hash_password(password: str):
    hashed_password = hash_password(password)
    assert len(hashed_password) > 0 and password != hashed_password

@pytest.mark.parametrize("password", ["pass!", "Qwerty12345", "2026"])
def test_verify_password(password: str):
    hashed_password = hash_password(password)
    assert verify_password(password, hashed_password)

@pytest.mark.parametrize(
    ("password", "real_password"),
    [
        ("pass!", "nutriscan"),
        ("Qwerty12345", "abacaba!"),
        ("2026", "2025")
    ]
)
def test_verify_password_fail(password: str, real_password: str):
    hashed_password = hash_password(real_password)
    assert not verify_password(password, hashed_password)

@pytest.mark.parametrize("user_id", [1, 15, 999])
def test_create_access_token(user_id: int):
    token = create_access_token(user_id)
    payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
    assert payload["sub"] == str(user_id)

def test_hash_password_is_salted():
    assert hash_password("pass") != hash_password("pass")


def test_access_token_wrong_key_rejected():
    token = create_access_token(1)
    with pytest.raises(Exception):
        jwt.decode(token, "other-key", algorithms=[ALGORITHM])


def test_refresh_token_is_unique():
    assert create_refresh_token() != create_refresh_token()


def test_hash_token_is_stable():
    assert hash_token("abc") == hash_token("abc")
    assert hash_token("abc") != hash_token("abd")