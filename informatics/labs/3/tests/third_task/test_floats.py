from regulars import genius_cypher


def test_answer():
    assert genius_cypher("22.2 + 22,1 = 42") == "22.2 + 22,1 = 7049"
