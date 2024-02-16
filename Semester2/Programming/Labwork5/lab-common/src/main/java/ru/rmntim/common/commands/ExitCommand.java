package ru.rmntim.common.commands;

import ru.rmntim.common.ExitException;

import java.util.List;

public class ExitCommand extends Command {
    public ExitCommand() {
        super("exit", "exits the application");
    }

    @Override
    public void execute(List<String> arguments) {
        throw new ExitException();
    }
}
