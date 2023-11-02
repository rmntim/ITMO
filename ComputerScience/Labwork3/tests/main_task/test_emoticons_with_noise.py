from regulars import number_of_emoticons


def test_answer():
    assert number_of_emoticons("asldhalksdhkjhkX-{(asdsada X-{(--X-{(") == 3
