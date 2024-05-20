package server.managers;

import common.utility.Commands;
import server.commands.Command;
import server.commands.Help;

import java.util.HashMap;
import java.util.Map;

public class CommandManagerBuilder {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandManagerBuilder register(String commandName, Command command) {
        commands.put(commandName, command);
        return this;
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    public Map<String, Command> build() {
        register(Commands.HELP, new Help(getCommands()));
        return getCommands();
    }
}
