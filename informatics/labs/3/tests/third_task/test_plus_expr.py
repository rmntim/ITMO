from regulars import genius_cypher


def test_answer():
    assert genius_cypher("20 + 22 = 42") == "1593 + 1929 = 7049"
