def reverse_bits(n):
    """Reverse the bits of a number"""
    result = 0
    inv = n & 0x01
    for _ in range(32):
        result <<= 1
        result |= n & 1
        n >>= 1
    if inv == 1:
        result = -result
    return result


assert reverse_bits(1) == -2147483648
assert reverse_bits(2) == 1073741824
