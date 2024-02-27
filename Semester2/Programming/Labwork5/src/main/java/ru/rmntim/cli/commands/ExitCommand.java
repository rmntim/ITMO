package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.exceptions.ExitException;

import java.io.InputStream;
import java.util.List;

public class ExitCommand extends Command {
    public ExitCommand() {
        super("exit", "exits the application");
    }

    @Override
    public void execute(final List<String> arguments, final InputStream inputStream) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " doesn't accept any arguments");
        }
        throw new ExitException();
    }
}
