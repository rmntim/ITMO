def upper_case_pstr(s):
    """Convert a Pascal string to upper case.

    Args:
        s (str): The input Pascal string.

    Returns:
        tuple: A tuple containing the upper case string and an empty string.
    """
    return (s.upper(), "")


assert upper_case_pstr('hello') == ('HELLO', '')
assert upper_case_pstr('world') == ('WORLD', '')
