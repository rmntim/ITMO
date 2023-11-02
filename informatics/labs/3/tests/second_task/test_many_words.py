from regulars import vt_itmo


def test_answer():
    assert (
        vt_itmo("ВТ это кафедра популярного петербуржского университета ИТМО") is None
    )
