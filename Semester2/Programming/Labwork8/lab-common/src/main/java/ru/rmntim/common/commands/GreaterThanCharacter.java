package ru.rmntim.common.commands;

import ru.rmntim.common.models.Dragon;
import ru.rmntim.common.network.Response;
import ru.rmntim.common.network.UserCredentials;
import ru.rmntim.common.parsers.DragonParser;

import java.util.List;

public class GreaterThanCharacter extends Command {
    public static final String NAME = "count_greater_than_character";
    public static final String DESCRIPTION
            = "Displays number of elements with character greater that given";

    private final Dragon dragon;

    public GreaterThanCharacter(Dragon dragon, UserCredentials userCredentials) {
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

    public static GreaterThanCharacter create(List<String> args, UserCredentials userCredentials) {
        if (!args.isEmpty()) {
            throw new IllegalArgumentException(NAME + " accepts 0 arguments");
        }
        var dragon = DragonParser.parse();
        return new GreaterThanCharacter(dragon, userCredentials);
    }
}
