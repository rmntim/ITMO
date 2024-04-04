package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;

public class Info extends Command {
    public Info() {
        super("info", "display information about collection");
    }

    @Override
    public Response accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
