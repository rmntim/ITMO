package ru.rmntim.client.commands;

import ru.rmntim.client.exceptions.BadCommandArgumentsException;
import ru.rmntim.client.lib.ExecutionContext;
import ru.rmntim.common.network.responses.Response;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class Help extends Command {
    private final Map<String, Command> commands;

    public Help(Map<String, Command> commands) {
        super("help", "shows all available commands");
        this.commands = commands;
    }

    @Override
    public Response sendRequest(ExecutionContext ctx, List<String> args) {
        if (!args.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " doesn't take any arguments");
        }

        var sj = new StringJoiner("\n");
        for (var command : commands.values()) {
            sj.add(command.getName() + " - " + command.getDescription());
        }

        return new Response(sj.toString());
    }
}
