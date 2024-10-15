from setup_helper import setup_user, setup_room_class, setup_room, setup_booking
from api_helpers import get_request, get_headers_with_auth


def test_create():
    user_id, user_token, _ = setup_user()
    room_class_id, _ = setup_room_class(user_token)
    room_id, _ = setup_room(user_token, {"roomClassId": room_class_id})
    guest_id, guest_token, _ = setup_user()
    booking_id, booking_data = setup_booking(
        guest_token, {"roomClassId": room_class_id}
    )

    retrieved_booking_data = get_request(
        f"bookings/{booking_id}", headers=get_headers_with_auth(guest_token)
    ).json()
    verify_booking_data(booking_data, retrieved_booking_data)

    retrieved_booking_data = get_request(
        f"bookings", headers=get_headers_with_auth(guest_token)
    ).json()
    assert len(retrieved_booking_data) == 1
    verify_booking_data(booking_data, retrieved_booking_data[0])


def verify_booking_data(saved_data, retrieved_data):
    assert saved_data["id"] == retrieved_data["id"]
    assert saved_data["room"]["id"] == retrieved_data["room"]["id"]
    assert saved_data["checkInDate"] == retrieved_data["checkInDate"]
    assert saved_data["checkOutDate"] == retrieved_data["checkOutDate"]
    assert saved_data["user"]["id"] == retrieved_data["user"]["id"]
    assert saved_data["bookingStatus"] == retrieved_data["bookingStatus"]
    assert saved_data["paymentStatus"] == retrieved_data["paymentStatus"]
