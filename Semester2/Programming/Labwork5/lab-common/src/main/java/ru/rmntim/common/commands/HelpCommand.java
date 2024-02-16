package ru.rmntim.common.commands;

import java.util.List;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "prints all available commands");
    }

    @Override
    public void execute(final List<String> arguments) {
        throw new UnsupportedOperationException();
    }
}
