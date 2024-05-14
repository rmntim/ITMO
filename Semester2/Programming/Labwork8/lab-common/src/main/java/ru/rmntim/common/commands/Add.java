package ru.rmntim.common.commands;

import ru.rmntim.common.models.Dragon;
import ru.rmntim.common.network.Response;
import ru.rmntim.common.network.UserCredentials;

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
}
