import os
import bonus2.myjson as json
import bonus2.myxml as xml


def parse(string):
    return xml.dump(json.loads(string), add_tag=True)


if __name__ == "__main__":
    input_file = os.path.join(os.path.dirname(__file__), "../../input.json")
    output_file = os.path.join(os.path.dirname(__file__), "../../output.xml")

    string = open(input_file, "r").read()
    open(output_file, "w").write(parse(string))
    print("bonus task 2 complete!")
