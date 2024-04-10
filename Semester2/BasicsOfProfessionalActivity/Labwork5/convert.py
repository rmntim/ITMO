"""convert.py - convert utf-8 encoded message to iso-8859-5 bytes.

This script converts string provided as first cmd argument to iso-8859-5
and prints converted bytes as 16 bit hex digits.
"""
import sys
import argparse

def iso(msg: str) -> list[str]:
    byts = list(map(lambda b: hex(int(b)), msg.encode("iso-8859-5")))
    byts.append("0x0a")
    res = []
    if len(byts) % 2 == 1:
        byts.append("0x00")
    for p in list(zip(byts, byts[1:]))[::2]:
        h, l = p
        res.append(h + l.removeprefix("0x"))
    return res

def main():
    parser = argparse.ArgumentParser(
            prog=sys.argv[0],
            description="convert utf-8 encoded message to iso-8859-5 bytes")
    parser.add_argument("message")
    parser.add_argument("-v", "--verbose",
                        action="store_true",
                        help="also display initial message")

    args = parser.parse_args()
    message = args.message
    encoded = ", ".join(iso(message))

    if args.verbose:
        print(f"Message: {message}")
        print(f"Encoded: {encoded}")
    else:
        print(encoded)

if __name__ == "__main__":
    main()
