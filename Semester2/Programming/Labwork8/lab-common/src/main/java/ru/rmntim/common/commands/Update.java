package ru.rmntim.common.commands;

import ru.rmntim.common.models.Dragon;
import ru.rmntim.common.network.Response;
import ru.rmntim.common.network.UserCredentials;

public class Update extends Command {
    public static final String NAME = "update";
    public static final String DESCRIPTION = "Update element with given id";

    private final int id;
    private final Dragon dragon;

    public Update(int id, Dragon dragon, UserCredentials userCredentials) {
        super(userCredentials);
        this.id = id;
        this.dragon = dragon;
    }

    public int getId() {
        return id;
    }

    public Dragon getDragon() {
        return dragon;
    }

    @Override
    public Response accept(Command.Visitor visitor) {
        return visitor.visit(this);
    }
}
