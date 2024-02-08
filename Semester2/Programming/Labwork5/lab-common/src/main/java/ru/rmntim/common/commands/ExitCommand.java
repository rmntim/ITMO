package ru.rmntim.common.commands;

import java.util.List;

public class ExitCommand extends Command {
    public ExitCommand() {
        super("exit", "exits the application without saving");
    }

    @Override
    public StatusCode execute(final List<String> arguments) {
        if (!arguments.isEmpty()) {
            return StatusCode.ERROR;
        }
        return StatusCode.EXIT;
    }
}
