from setup_helper import (
    setup_user,
    setup_room_class,
    setup_room,
    setup_booking,
    setup_rooms,
    setup_users,
    date_range,
)
from api_helpers import get_request, get_headers_with_auth, put_request


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


def test_complete_booking_workflow():

    # Set up 1 admin and 3 guests
    users, tokens = setup_users(4)
    admin_id, guest_id1, guest_id2, guest_id3 = users
    admin_token, guest_token1, guest_token2, guest_token3 = tokens

    # Set up room class and room
    room_class_id1, _ = setup_room_class(admin_token)
    room_class_id2, _ = setup_room_class(admin_token)
    rooms1 = setup_rooms(admin_token, 3, roomClassId=room_class_id1)
    rooms2 = setup_rooms(admin_token, 3, roomClassId=room_class_id2)

    verify_availability_feed(2, [3, 3])

    booking_id, booking_data = setup_booking(
        guest_token1, {"roomClassId": room_class_id1}
    )

    verify_availability_feed(2, [2, 3])

    booking_id, booking_data = setup_booking(
        guest_token2, {"roomClassId": room_class_id2}
    )
    verify_availability_feed(2, [2, 2])

    booking_id, booking_data = setup_booking(
        guest_token2, {"roomClassId": room_class_id1}
    )
    verify_availability_feed(2, [1, 2])

    booking_id, booking_data = setup_booking(
        guest_token3, {"roomClassId": room_class_id1}
    )
    verify_availability_feed(2, [0, 2])
    booking_id_to_cancel = booking_id

    # It will throw error because the room-class has no available room
    booking_id, booking_data = setup_booking(
        guest_token3, {"roomClassId": room_class_id1}, False
    )
    verify_availability_feed(2, [0, 2])

    booking_id, booking_data = setup_booking(
        guest_token3, {"roomClassId": room_class_id2}
    )
    verify_availability_feed(2, [0, 1])

    # Fetch total bookings via admin
    response = get_request(
        f"bookings/all-bookings", headers=get_headers_with_auth(admin_token)
    ).json()
    assert len(response) == 5

    # Cancel one of the booking
    put_request(f"bookings/cancel/{booking_id_to_cancel}", {}, headers=get_headers_with_auth(guest_token3))
    
    # Check availability feed again
    verify_availability_feed(2, [1, 1])


def verify_availability_feed(noOfRoomClasses, noOfRoomsAvailableArray):
    check_in_date = date_range(2)[0]
    check_out_date = date_range(2)[1]
    response = get_request(
        f"room-classes/findByAvailability?checkInDate={check_in_date}&checkOutDate={check_out_date}"
    ).json()
    assert len(response) == noOfRoomClasses
    for i in range(noOfRoomClasses):
        assert response[i]["availableRooms"] == noOfRoomsAvailableArray[i]

def verify_booking_data(saved_data, retrieved_data):
    assert saved_data["id"] == retrieved_data["id"]
    assert saved_data["room"]["id"] == retrieved_data["room"]["id"]
    assert saved_data["checkInDate"] == retrieved_data["checkInDate"]
    assert saved_data["checkOutDate"] == retrieved_data["checkOutDate"]
    assert saved_data["user"]["id"] == retrieved_data["user"]["id"]
    assert saved_data["bookingStatus"] == retrieved_data["bookingStatus"]
    assert saved_data["paymentStatus"] == retrieved_data["paymentStatus"]
