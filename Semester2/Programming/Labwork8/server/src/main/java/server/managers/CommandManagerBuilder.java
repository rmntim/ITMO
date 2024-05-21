package server.managers;

import common.utility.CommandName;
import server.commands.Command;
import server.commands.Help;

import java.util.HashMap;
import java.util.Map;

public class CommandManagerBuilder {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandManagerBuilder register(CommandName commandName, Command command) {
        commands.put(commandName.getCommandName(), command);
        return this;
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    public Map<String, Command> build() {
        register(CommandName.HELP, new Help(getCommands()));
        return getCommands();
    }
}
