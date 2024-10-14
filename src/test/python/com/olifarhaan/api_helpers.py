import json
import requests
from datetime import datetime

base_url = "http://localhost:8080/api/v1"
working_dir = "src/test/python/com/olifarhaan"

import random
import string

def auth_token_demo():
    return "wiywoaeiurqir"

def get_unique_id(base_id):
    timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
    random_str = "".join(random.choices(string.ascii_letters + string.digits, k=8))
    joined_base_id = base_id.replace(" ", "_").lower()
    return f"{joined_base_id}_{timestamp}_{random_str}"

def clean_up():
    response = delete_request("cleanup", checkStatus=False)
    if response.status_code != 204:
        print("\033[91mWARNING: Failed to clean the test database. You must clean the test DB to run tests again, otherwise unexpected results may occur.\033[0m")
    assert response.status_code == 204

def post_request(endpoint, data=None, headers=None, checkStatus=True):
    url = f"{base_url}/{endpoint}"
    response = requests.post(url, json=data, headers=headers)
    if response.status_code not in [200, 201, 202, 204] and checkStatus:
        print(f"Failed POST request to {url} with data {json.dumps(data, indent=4)}")
        print(f"Error: {response.status_code} - {response.text}")
        assert response.status_code in [200, 201, 202, 204]
    else:
        response_content = response.json() if response.text else "No content"
        print(
            f"Successful POST request to {url} with data {json.dumps(data, indent=4)} and response {json.dumps(response_content, indent=4)}"
        )
    return response


def get_request(endpoint, headers=None, params=None, checkStatus=True):
    url = f"{base_url}/{endpoint}"
    response = requests.get(url, headers=headers, params=params)
    if response.status_code not in [200, 201] and checkStatus:
        print(f"Failed GET request to {url}")
        print(f"Error: {response.status_code} - {response.text}")
        assert response.status_code in [200, 201]
    else:
        response_content = response.json() if response.text else "No content"
        print(
            f"Successful GET request to {url} with params {params} and response {json.dumps(response_content, indent=4)}"
        )
    return response


def put_request(endpoint, data, headers=None, checkStatus=True):
    url = f"{base_url}/{endpoint}"
    response = requests.put(url, json=data, headers=headers)
    if response.status_code not in [200, 201, 202, 204] and checkStatus:
        print(f"Failed PUT request to {url} with data {json.dumps(data, indent=4)}")
        print(f"Error: {response.status_code} - {response.text}")
        assert response.status_code in [200, 201, 202, 204]
    else:
        response_content = response.json() if response.text else "No content"
        print(
            f"Successful PUT request to {url} with data {json.dumps(data, indent=4)} and response {json.dumps(response_content, indent=4)}"
        )
    return response


def authenticate_and_retrieve_token(email, password):
    auth_uri = "auth/login"
    auth_data = {"email": email, "password": password}
    headers = {"Content-Type": "application/json"}
    response = post_request(auth_uri, auth_data, headers=headers)
    return response.json()["token"]

def get_headers_with_auth(token):
    return {"Authorization": f"Bearer {token}", "Content-Type": "application/json"}


def delete_request(endpoint, headers=None, checkStatus=True):
    url = f"{base_url}/{endpoint}"
    response = requests.delete(url, headers=headers)
    if response.status_code not in [200, 204] and checkStatus:
        print(f"Failed DELETE request to {url}")
        print(f"Error: {response.status_code} - {response.text}")
        assert response.status_code in [200, 204]
    else:
        print(f"Successful DELETE request to {url}")
    return response
