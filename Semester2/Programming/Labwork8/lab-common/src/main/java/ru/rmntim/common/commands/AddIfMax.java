package ru.rmntim.common.commands;

import ru.rmntim.common.models.Dragon;
import ru.rmntim.common.network.Response;
import ru.rmntim.common.network.UserCredentials;

public class AddIfMax extends Command {
    public static final String NAME = "add_if_max";
    public static final String DESCRIPTION
            = "Adds new element to the collection if it's greater than current maximum";

    private final Dragon dragon;

    public AddIfMax(Dragon dragon, UserCredentials userCredentials) {
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