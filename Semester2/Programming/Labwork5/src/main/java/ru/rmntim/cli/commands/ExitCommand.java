package ru.rmntim.cli.commands;

import ru.rmntim.cli.ExitException;

import java.util.List;

public class ExitCommand extends Command {
    public ExitCommand() {
        super("exit", "exits the application");
    }

    @Override
    public void execute(final List<String> arguments) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " doesn't accept any arguments");
        }
        throw new ExitException();
    }
}