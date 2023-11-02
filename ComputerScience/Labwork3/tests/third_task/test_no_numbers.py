from regulars import genius_cypher


def test_answer():
    assert genius_cypher("Вася конечно умен, но данный шифр легко сломать.") is None
