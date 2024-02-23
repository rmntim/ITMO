package ru.rmntim.server.commands;

import java.util.Collection;
import java.util.List;

public class HelpCommand extends Command {
    private final Collection<Command> commands;

    public HelpCommand(final Collection<Command> commands) {
        super("help", "prints all available commands");
        this.commands = commands;
    }

    @Override
    public void execute(final List<String> arguments) {
        if (!arguments.isEmpty()) {
            throw new IllegalArgumentException(getName() + " doesn't accept any arguments");
        }
        commands.forEach(System.out::println);
    }
}
