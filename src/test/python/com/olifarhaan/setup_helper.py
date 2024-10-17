from api_helpers import (
    post_request,
    get_request,
    get_unique_id,
    authenticate_and_retrieve_token,
    get_headers_with_auth,
)
from datetime import datetime, timedelta
import random
full_names = [
    "Ali Farhan",
    "John Doe",
    "Jane Doe",
    "Alice Johnson",
    "Bob Smith",
    "Charlie Brown",
    "Diana Green",
    "Ethan White",
    "Fiona Black",  
]

def setup_user(data={}):
    name = random.choice(full_names)
    user_data = {
        "fullName": f"{name}",
        "email": f"{get_unique_id(name)}@example.com",
        "password": "SecurePassword123@"
    }
    user_data.update(data)
    response = post_request("auth/signup", user_data)
    retrieved_data = response.json()
    user_id = retrieved_data["id"]
    user_token = authenticate_and_retrieve_token(retrieved_data["email"], user_data["password"])
    return user_id, user_token, retrieved_data


def setup_users(noOfUsers):
    users = []
    user_tokens = []
    for i in range(noOfUsers):
        user_id, user_token, user_data = setup_user()
        users.append(user_data)
        user_tokens.append(user_token)
    return users, user_tokens

def setup_floor(user_token, data={}):
    floor_data = {
        "name": f"{get_unique_id('Floor')}"
    }
    floor_data.update(data)
    response = post_request("floors", floor_data, headers=get_headers_with_auth(user_token))
    return response.json()["id"], response.json()


room_class_prices = [3000.00, 4000.00, 4500.00, 5000.00, 10000.00]

def setup_room_class(user_token, data={}):
    room_class_data = {
        "title": f"{get_unique_id('RoomClass')}",
        "description": f"{get_unique_id('RoomClass')}",
        "basePrice": random.choice(room_class_prices),
        "features": ['HAIR_DRYER', 'HYPOALLERGENIC', 'LINENS', 'PRIVATE_BATHROOM', 'TV'],
        "bedTypes": ['SINGLE', 'KING'],
        "maxGuestCount": 3
    }
    room_class_data.update(data)
    response = post_request("room-classes", room_class_data, headers=get_headers_with_auth(user_token))
    return response.json()["id"], response.json()


def setup_room(user_token, data={}):
    room_data = {
        "roomClassId": data.get("roomClassId") or setup_room_class(user_token)[0],
        "floorId": data.get("floorId") or setup_floor(user_token)[0],
        "roomNumber": get_unique_id("Room"),
        "roomStatus": "AVAILABLE"
    }
    room_data.update(data)
    response = post_request("rooms", room_data, headers=get_headers_with_auth(user_token))
    return response.json()["id"], response.json()

def setup_rooms(user_token, noOfRooms, roomClassId=None, floorId=None):
    if roomClassId is None:
        roomClassId, _ = setup_room_class(user_token)
    if floorId is None:
        floorId, _ = setup_floor(user_token)
    rooms = []
    for i in range(noOfRooms):
        room_data = {
            "floorId": floorId,
            "roomClassId": roomClassId,
        }
        room_id, room_data = setup_room(user_token, room_data)
        rooms.append(room_data)
    rooms.reverse()
    return rooms, roomClassId, floorId


def setup_booking(user_token, data={}, checkStatus=True):
    booking_data = {
        "checkInDate": date_range(2)[0],
        "checkOutDate": date_range(2)[1],
        "roomClassId": data.get("roomClassId") or setup_room_class(user_token)[0],
        "guestCount": 2
    }
    booking_data.update(data)
    response = post_request("bookings", booking_data, headers=get_headers_with_auth(user_token), checkStatus=checkStatus)
    if response.status_code == 200:
        saved_booking_data = response.json()
        return saved_booking_data["id"], saved_booking_data
    else:
        return None, None


def date_range(n):
    tomorrow = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%d")
    n_days_after = (datetime.now() + timedelta(days=n)).strftime("%Y-%m-%d")
    return tomorrow, n_days_after
