package ru.rmntim.server.commands;

import ru.rmntim.server.exceptions.BadCommandArgumentsException;
import ru.rmntim.server.exceptions.ExitException;
import ru.rmntim.server.logic.ExecutionContext;

import java.util.List;

/**
 * Exits the program
 */
public class ExitCommand extends Command {
    public ExitCommand() {
        super("exit", "exits the application");
    }

    /**
     * @param arguments list of arguments to the command, not including the command name
     * @param context   execution context
     * @throws ExitException as a signal to exit the application
     */
    @Override
    public void execute(final List<String> arguments, ExecutionContext context) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " doesn't accept any arguments");
        }
        throw new ExitException();
    }
}
