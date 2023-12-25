from regulars import genius_cypher


def test_answer():
    input_str = """The Maurya Empire was a geographically 
                         extensive Iron Age historical power in South 
                         Asia based in Magadha. Founded by Chandragupta 
                         Maurya in 322 BCE, it existed in loose-knit 
                         fashion until 185 BCE. The empire was 
                         centralized by the conquest of the Indo-Gangetic 
                         Plain; its capital city was located at 
                         Pataliputra (modern Patna)."""
    result_str = """The Maurya Empire was a geographically 
                         extensive Iron Age historical power in South 
                         Asia based in Magadha. Founded by Chandragupta 
                         Maurya in 414729 BCE, it existed in loose-knit 
                         fashion until 136893 BCE. The empire was 
                         centralized by the conquest of the Indo-Gangetic 
                         Plain; its capital city was located at 
                         Pataliputra (modern Patna)."""
    assert genius_cypher(input_str) == result_str
