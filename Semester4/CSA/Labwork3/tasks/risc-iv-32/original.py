def little_to_big_endian(n):
    """Convert a 32-bit integer from little-endian to big-endian format"""
    return int.from_bytes(n.to_bytes(4, byteorder="little"), byteorder="big")


assert little_to_big_endian(305419896) == 2018915346
assert little_to_big_endian(2864434397) == 3721182122
