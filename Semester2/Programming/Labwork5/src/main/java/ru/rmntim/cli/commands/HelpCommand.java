package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.logic.ExecutionContext;

import java.util.Collection;
import java.util.List;

/**
 * Prints all available commands and their descriptions
 */
public class HelpCommand extends Command {
    private final Collection<Command> commands;

    public HelpCommand(final Collection<Command> commands) {
        super("help", "prints all available commands");
        this.commands = commands;
    }

    @Override
    public void execute(final List<String> arguments, ExecutionContext context) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " doesn't accept any arguments");
        }
        commands.forEach(System.out::println);
    }
}
