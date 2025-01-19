from setup_helper import (
    setup_user,
    setup_room_class,
    setup_room,
    setup_booking,
    setup_rooms,
    setup_users,
    date_range,
    setup_floor,
)
from api_helpers import get_request, get_headers_with_auth, put_request
from concurrent.futures import ThreadPoolExecutor
import pytest


def test_create():
    user_id, user_token, _ = setup_user({"role": "ADMIN"})
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
        f"bookings",
        headers=get_headers_with_auth(guest_token),
        params={"userId": user_id},
    ).json()
    assert len(retrieved_booking_data) == 1
    verify_booking_data(booking_data, retrieved_booking_data[0])


def test_complete_booking_workflow():

    # Set up 1 admin and 3 guests
    admin_id, admin_token, _ = setup_user({"role": "ADMIN"})
    users, tokens = setup_users(3)
    guest_id1, guest_id2, guest_id3 = users
    guest_token1, guest_token2, guest_token3 = tokens

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
        f"bookings", headers=get_headers_with_auth(admin_token)
    ).json()
    assert len(response) == 5

    # Fetch all bookings via guest
    response = get_request(
        f"bookings",
        headers=get_headers_with_auth(guest_token1),
        params={"userId": guest_id1},
    ).json()
    assert len(response) == 1

    # Fetch all bookings via guest 2
    response = get_request(
        f"bookings",
        headers=get_headers_with_auth(guest_token2),
        params={"userId": guest_id2},
    ).json()
    assert len(response) == 2

    # Fetch all bookings via guest 3
    response = get_request(
        f"bookings",
        headers=get_headers_with_auth(guest_token3),
        params={"userId": guest_id3},
    ).json()
    assert len(response) == 2

    # Cancel one of the booking
    put_request(
        f"bookings/cancel/{booking_id_to_cancel}",
        {},
        headers=get_headers_with_auth(guest_token3),
    )

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


@pytest.mark.parametrize("no_of_bookings", [10, 100000])
def test_concurrent_booking_stress(no_of_bookings):
    """
    This test simulates a high volume of concurrent bookings to stress test the booking system.
    It sets up a large number of users and rooms, then attempts to book rooms concurrently using a ThreadPoolExecutor.
    After all bookings are attempted, it verifies that the expected number of bookings were successful and that no room was double booked.

    :param no_of_bookings: The total number of bookings to attempt.
    """
    no_of_users = no_of_bookings < 10 and 2 or no_of_bookings // 10
    no_of_rooms = no_of_bookings // 2
    total_no_of_bookings = no_of_bookings * 2

    """
    Setup 1 admin and @no_of_users users
    """
    admin_id, admin_token, _ = setup_user({"role": "ADMIN"})
    users, tokens = setup_users(no_of_users)

    """
    Setup floor, room class and rooms
    """
    room_class_id1, _ = setup_room_class(admin_token)
    room_class_id2, _ = setup_room_class(admin_token)

    floor_id, _ = setup_floor(admin_token)

    """
    Create @no_of_rooms rooms for each room class
    """
    rooms1 = setup_rooms(
        admin_token, no_of_rooms, roomClassId=room_class_id1, floorId=floor_id
    )
    rooms2 = setup_rooms(
        admin_token, no_of_rooms, roomClassId=room_class_id2, floorId=floor_id
    )

    """
    Initial availability check all rooms are available
    """
    verify_availability_feed(2, [no_of_rooms, no_of_rooms])

    def attempt_booking(i):
        """
        Attempts to book a room for a given user. The room class is alternated between two predefined room classes.

        :param i: The index of the booking attempt, used to alternate between room classes.
        :return: The booking ID if successful, None otherwise.
        """
        room_class_id = room_class_id1 if i % 2 == 0 else room_class_id2
        guest_token = tokens[i % len(tokens)]
        booking_id, booking_data = setup_booking(
            guest_token,
            {"roomClassId": room_class_id, "guestCount": 2},
            False,  # Don't throw error on failure
        )
        return booking_id

    with ThreadPoolExecutor() as executor:
        list(executor.map(attempt_booking, range(total_no_of_bookings)))

    """
    Verify all bookings via admin
    """
    response = get_request(
        f"bookings", headers=get_headers_with_auth(admin_token)
    ).json()
    assert len(response) == no_of_bookings

    """
    Verify availability feed, all rooms are booked
    """
    verify_availability_feed(2, [0, 0])

    """
    Verify no double bookings for same room
    """
    room_bookings = {}
    duplicate_room_ids = []
    for booking in response:
        room_id = booking["room"]["id"]
        if room_id in room_bookings:
            duplicate_room_ids.append(room_id)
        room_bookings[room_id] = booking
    assert len(duplicate_room_ids) == 0, f"Duplicate room ids: {duplicate_room_ids}"
