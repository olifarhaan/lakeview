from setup_helper import setup_room_class, setup_user, setup_rooms, get_unique_id
from utils import are_lists_equal
from api_helpers import get_request, get_headers_with_auth, put_request, delete_request


def test_room_class_crud():
    user_id, user_token, _ = setup_user()

    room_class_id1, room_class_data1 = setup_room_class(user_token)
    retrieved_data1 = get_request(
        f"room-classes/{room_class_id1}", headers=get_headers_with_auth(user_token)
    ).json()
    verify_room_class_data(retrieved_data1, room_class_data1)

    room_class_id2, room_class_data2 = setup_room_class(user_token)
    retrieved_data2 = get_request(
        f"room-classes/{room_class_id2}", headers=get_headers_with_auth(user_token)
    ).json()
    verify_room_class_data(retrieved_data2, room_class_data2)

    update_data = {
        "title": f"{get_unique_id('RoomClass')}",
        "description": f"{get_unique_id('RoomClass')}",
        "basePrice": 200.00,
    }
    put_request(
        f"room-classes/{room_class_id1}",
        update_data,
        headers=get_headers_with_auth(user_token),
    )
    retrieved_data1 = get_request(
        f"room-classes/{room_class_id1}", headers=get_headers_with_auth(user_token)
    ).json()
    verify_room_class_data(retrieved_data1, {**room_class_data1, **update_data})

    response = get_request(f"room-classes", headers=get_headers_with_auth(user_token))
    assert response.status_code == 200
    assert len(response.json()) == 2

    delete_request(
        f"room-classes/{room_class_id1}",
        headers=get_headers_with_auth(user_token),
        checkStatus=False,
    )
    assert (
        get_request(
            f"room-classes/{room_class_id1}",
            headers=get_headers_with_auth(user_token),
            checkStatus=False,
        ).status_code
        == 404
    )

    response = get_request(f"room-classes", headers=get_headers_with_auth(user_token))
    assert len(response.json()) == 1


def test_availability():
    user_id, user_token, _ = setup_user()
    rooms = setup_rooms(user_token, 3)
    response = get_request(
        f"room-classes/findByAvailability?checkInDate=2024-01-01&checkOutDate=2024-01-02&roomClassId={rooms[0]['roomClass']['id']}",
        headers=get_headers_with_auth(user_token),
    )
    assert len(response.json()) == 1

    response = get_request(
        f"room-classes/findByAvailability?checkInDate=2024-01-01&checkOutDate=2024-01-02",
        headers=get_headers_with_auth(user_token),
    )
    assert len(response.json()) == 1
    # assert False


def verify_room_class_data(retrieved_data, expected_data):
    assert retrieved_data["id"] == expected_data["id"]
    assert retrieved_data["title"] == expected_data["title"]
    assert retrieved_data["description"] == expected_data["description"]
    assert retrieved_data["basePrice"] == expected_data["basePrice"]
    assert are_lists_equal(retrieved_data["features"], expected_data["features"])
    assert are_lists_equal(retrieved_data["bedTypes"], expected_data["bedTypes"])
    assert are_lists_equal(retrieved_data["images"], expected_data["images"])
