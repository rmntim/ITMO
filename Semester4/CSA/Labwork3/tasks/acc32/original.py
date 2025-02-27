def sum_even_n(n):
    """Calculate the sum of even numbers from 1 to n"""
    if n <= 0:
        return -1
    total = 0
    for i in range(1, n + 1):
        if i % 2 == 0:
            total += i
    return total


assert sum_even_n(5) == 6
assert sum_even_n(10) == 30
assert sum_even_n(90000) == 2025045000
