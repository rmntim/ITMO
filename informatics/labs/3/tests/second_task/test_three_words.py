from regulars import vt_itmo


def test_answer():
    assert (
        vt_itmo("А ты знал, что ВТ - лучшая кафедра в ИТМО?")
        == "ВТ - лучшая кафедра в ИТМО"
    )
