package ru.rmntim.client.commands;

import ru.rmntim.client.exceptions.BadCommandArgumentsException;
import ru.rmntim.client.lib.ExecutionContext;
import ru.rmntim.common.network.responses.Response;

import java.util.List;

public class Exit extends Command {
    public Exit() {
        super("exit", "stops the client");
    }

    @Override
    public Response sendRequest(ExecutionContext ctx, List<String> args) {
        if (!args.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " doesn't take any arguments");
        }
        System.exit(0);
        return null;
    }
}
