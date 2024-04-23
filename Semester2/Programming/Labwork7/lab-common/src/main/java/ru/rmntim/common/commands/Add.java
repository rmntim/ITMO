package ru.rmntim.common.commands;

import ru.rmntim.common.models.Dragon;
import ru.rmntim.common.network.Response;
import ru.rmntim.common.network.UserCredentials;
import ru.rmntim.common.parsers.DragonParser;

import java.util.List;

public class Add extends Command {
    public static final String NAME = "add";
    public static final String DESCRIPTION = "Adds an element to the collection";

    private final Dragon dragon;

    public Add(Dragon dragon, UserCredentials userCredentials) {
        super(userCredentials);
        this.dragon = dragon;
    }

    public Dragon getDragon() {
        return dragon;
    }

    @Override
    public Response accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public static Add create(List<String> args, UserCredentials userCredentials) {
        if (!args.isEmpty()) {
            throw new IllegalArgumentException(NAME + " accepts 0 arguments");
        }
        var dragon = DragonParser.parse();
        return new Add(dragon, userCredentials);
    }
}
