package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;

import java.io.BufferedReader;
import java.util.Collection;
import java.util.List;

public class HelpCommand extends Command {
    private final Collection<Command> commands;

    public HelpCommand(final Collection<Command> commands) {
        super("help", "prints all available commands");
        this.commands = commands;
    }

    @Override
    public void execute(final List<String> arguments, BufferedReader reader) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " doesn't accept any arguments");
        }
        commands.forEach(System.out::println);
    }
}
