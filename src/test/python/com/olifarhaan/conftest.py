import pytest
from api_helpers import clean_up

# the fixture is use to run automatically after each test in order to clean the test DB, we will figure out a different strategy for this in the future
@pytest.fixture(autouse=True)
def cleanup_after_test():
    # Code before yield is executed before each test
    yield
    # Code after yield is executed after each test
    clean_up()