package ru.rmntim.common.commands;

import ru.rmntim.common.models.Dragon;
import ru.rmntim.common.network.Response;
import ru.rmntim.common.parsers.DragonParser;

import java.util.List;

public class AddIfMin extends Command {
    public static final String NAME = "add_if_min";
    public static final String DESCRIPTION
            = "Adds new element to the collection if it's lower than current minimum";

    private final Dragon dragon;

    public AddIfMin(Dragon dragon) {
        this.dragon = dragon;
    }

    public Dragon getDragon() {
        return dragon;
    }

    @Override
    public Response accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public static AddIfMin create(List<String> args) {
        if (!args.isEmpty()) {
            throw new IllegalArgumentException(NAME + " accepts 0 arguments");
        }
        var dragon = DragonParser.parse();
        return new AddIfMin(dragon);
    }
}
