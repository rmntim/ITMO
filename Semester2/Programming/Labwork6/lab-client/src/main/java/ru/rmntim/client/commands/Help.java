package ru.rmntim.client.commands;

import ru.rmntim.client.exceptions.BadCommandArgumentsException;
import ru.rmntim.client.exceptions.BadResponseException;
import ru.rmntim.client.logic.ExecutionContext;
import ru.rmntim.common.network.requests.HelpRequest;
import ru.rmntim.common.network.responses.Response;

import java.io.IOException;
import java.util.List;

public class Help extends Command {
    public Help() {
        super("help", "shows all available commands");
    }

    @Override
    public Response sendRequest(ExecutionContext ctx, List<String> args) throws BadResponseException {
        if (!args.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " doesn't take any arguments");
        }

        try {
            return ctx.getClient().sendAndReceive(new HelpRequest());
        } catch (IOException e) {
            throw new BadResponseException(getName() + ": " + e.getMessage());
        }
    }
}
