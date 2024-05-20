package server.commands;

import common.network.requests.Request;
import common.network.responses.HelpResponse;
import common.network.responses.Response;

import java.util.Map;

public class Help extends Command {
    private final Map<String, Command> commands;

    public Help(Map<String, Command> commands) {
        super("help", "prints all available commands");
        this.commands = commands;
    }

    @Override
    public Response apply(Request request) {
        var helpMessage = new StringBuilder();
        commands.values().forEach(command -> helpMessage.append(" %-35s%-1s%n".formatted(command.getName(), command.getDescription())));
        return new HelpResponse(helpMessage.toString(), null);
    }
}
