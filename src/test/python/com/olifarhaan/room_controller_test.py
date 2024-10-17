from setup_helper import setup_user, setup_room, setup_floor, setup_room_class, setup_rooms
from api_helpers import get_request, get_headers_with_auth, delete_request, put_request
from floor_controller_test import verify_floor_data
from room_class_controller_test import verify_room_class_data


def test_room_crud():
    user_id, user_token, _ = setup_user()
    rooms, roomClassId, floorId = setup_rooms(user_token, 3)
    response = get_request(f"rooms", headers=get_headers_with_auth(user_token))
    assert len(response.json()) == 3
    dataList = response.json()
    for room, data in zip(rooms, dataList):
        verify_room_data(room, data)

    for index, data in enumerate(dataList):
        room_id = data["id"]
        update_data = {"roomStatus": "CLEANING"}
        put_request(
            f"rooms/{room_id}", update_data, headers=get_headers_with_auth(user_token)
        )
        dataList[index].update(update_data)

    response = get_request(f"rooms", headers=get_headers_with_auth(user_token))
    assert len(response.json()) == 3
    updated_dataList = response.json()
    for data, updated_data in zip(dataList, updated_dataList):
        verify_room_data(updated_data, data)

    for data in dataList:
        room_id = data["id"]
        delete_request(
            f"rooms/{room_id}", headers=get_headers_with_auth(user_token)
        )
        response = get_request(
            f"rooms/{room_id}", headers=get_headers_with_auth(user_token), checkStatus=False
        )
        assert response.status_code == 404


def verify_room_data(retrieved_data, room_data):
    assert retrieved_data["id"] == room_data["id"]
    verify_floor_data(retrieved_data["floor"], room_data["floor"])
    assert retrieved_data["roomNumber"] == room_data["roomNumber"]
    assert retrieved_data["roomStatus"] == room_data["roomStatus"]
