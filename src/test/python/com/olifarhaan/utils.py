
def are_lists_equal(list1, list2):
    """Check if two lists are equal regardless of order, considering nullability."""
    if list1 is None and list2 is None:
        return True
    if list1 is None or list2 is None:
        return False
    return sorted(list1) == sorted(list2)