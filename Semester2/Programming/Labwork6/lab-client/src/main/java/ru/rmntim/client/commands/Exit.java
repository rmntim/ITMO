package ru.rmntim.client.commands;

import ru.rmntim.client.exceptions.BadCommandArgumentsException;
import ru.rmntim.client.logic.ExecutionContext;

import java.util.List;

public class Exit extends Command {
    public Exit() {
        super("exit", "stops the client");
    }

    @Override
    public void sendRequest(ExecutionContext ctx, List<String> args) {
        if (!args.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " doesn't take any arguments");
        }
        System.exit(0);
    }
}
