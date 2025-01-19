from setup_helper import setup_floor, setup_user, get_headers_with_auth
from api_helpers import get_request, delete_request, put_request

def test_floor_crud():
    user_id, user_token, user_data = setup_user({"role": "ADMIN"})
    floor_id, floor_data = setup_floor(user_token)
    response = get_request(f"floors/{floor_id}", headers=get_headers_with_auth(user_token))
    verify_floor_data(response.json(), floor_data)

    floor_id1, floor_data1 = setup_floor(user_token)
    response = get_request(f"floors", headers=get_headers_with_auth(user_token))
    assert len(response.json()) == 2

    response = delete_request(f"floors/{floor_id}", headers=get_headers_with_auth(user_token))

    response = get_request(f"floors/{floor_id}", headers=get_headers_with_auth(user_token), checkStatus=False)
    assert response.status_code == 404


    update_data = {"name": "New Floor"}
    response = put_request(f"floors/{floor_id1}", data=update_data, headers=get_headers_with_auth(user_token))
    floor_data1.update(update_data)
    
    verify_floor_data(response.json(), floor_data1)


    response = get_request(f"floors/{floor_id1}", headers=get_headers_with_auth(user_token))
    verify_floor_data(response.json(), floor_data1)

    response = delete_request(f"floors/{floor_id1}", headers=get_headers_with_auth(user_token))

    response = get_request(f"floors", headers=get_headers_with_auth(user_token))
    assert len(response.json()) == 0

def verify_floor_data(response_data, expected_data):
    assert response_data["id"] == expected_data["id"]
    assert response_data["name"] == expected_data["name"]
