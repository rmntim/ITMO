def dump(json_object, root_name="root", add_tag=False, indent=1):
    xml_string = '<?xml version="1.0" encoding="utf-8"?>\n' if add_tag else ""

    if add_tag:
        xml_string += "<" + root_name + ">\n"
    if isinstance(json_object, dict):
        for key, value in json_object.items():
            xml_string += f"{' ' * 2 * indent}<{key}>\n"
            xml_string += dump(value, key, indent=indent + 1)
            xml_string += f"{' ' * 2 * indent}</{key}>\n"
    elif isinstance(json_object, list):
        for item in json_object:
            xml_string += f"{' ' * 2 * indent}<item>"
            xml_string += dump(item, root_name, indent=indent + 1)
            xml_string += f"{' ' * 2 * indent}</item>"
    else:
        return " " * 2 * indent + str(json_object) + "\n"

    if add_tag:
        xml_string += "</" + root_name + ">"

    return xml_string
