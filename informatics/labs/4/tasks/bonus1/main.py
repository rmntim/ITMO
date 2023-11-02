import os
import json
import xml.etree.ElementTree as ET


def dict_to_xml(dictionary, root=None):
    if root is None:
        root = ET.Element("root")

    for key, value in dictionary.items():
        if isinstance(value, dict):
            sub_element = ET.Element(key)
            root.append(dict_to_xml(value, sub_element))
        elif isinstance(value, list):
            element = ET.Element(key)
            for item in value:
                element.append(dict_to_xml(item, ET.Element("item")))
            root.append(element)
        else:
            element = ET.Element(key)
            element.text = str(value)
            root.append(element)

    return root


def parse(string):
    json_obj = json.loads(string)
    return ET.tostring(
        dict_to_xml(json_obj), xml_declaration=True, encoding="utf-8"
    ).decode("utf-8")


if __name__ == "__main__":
    input_file = os.path.join(os.path.dirname(__file__), "../../input.json")
    output_file = os.path.join(os.path.dirname(__file__), "../../output.xml")

    string = open(input_file, "r").read()
    open(output_file, "w").write(parse(string))
    print("bonus task 1 complete!")
