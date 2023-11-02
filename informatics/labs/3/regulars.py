import re
from typing import Optional


def number_of_emoticons(input_str: str) -> int:
    """Finds the number of `X-{(` emoticons in given string

    409682 % 6 = 2

    409682 % 4 = 2

    409682 % 7 = 0
    """
    return len(re.findall(r"X-\{\(", input_str))


def vt_itmo(input_str: str) -> Optional[str]:
    """Finds a fragment in text that starts with `ВТ`,
    ends with `ИТМО` and has 4 words in-between
    """
    pattern = re.compile(r"ВТ(\s+[\w\-]+){,4}\s+ИТМО")
    result = pattern.search(input_str)
    if isinstance(result, re.Match):
        start, end = result.span()
        return input_str[start:end]
    else:
        return None


def genius_cypher(input_str: str) -> Optional[str]:
    """Replaces all integers `x` with the value of `4x^2 - 7`"""
    pattern = r"-?\d+[.,]?\d*"

    def replace(match):
        if any(sym in match.group(0) for sym in ",."):
            return match.group(0)
        integer = int(match.group(0))
        result = 4 * (integer**2) - 7
        return str(result)

    output_str = re.sub(pattern, replace, input_str)

    return output_str if output_str != input_str else None


def main():
    while True:
        choice = input("Выберите задание (1-3): ")
        if choice not in "123":
            continue
        choices = {"1": number_of_emoticons, "2": vt_itmo, "3": genius_cypher}
        input_str = input("Введите входную строку: ")
        print(f"Ответ: {choices[choice](input_str)}")
        break


if __name__ == "__main__":
    main()
