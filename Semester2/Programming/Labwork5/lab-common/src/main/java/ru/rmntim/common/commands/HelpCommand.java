package ru.rmntim.common.commands;

import java.util.List;

public class HelpCommand extends Command {
    private final List<Command> commands;

    public HelpCommand(final List<Command> commands) {
        super("help", "prints all available commands");
        this.commands = commands;
    }

    @Override
    public StatusCode execute(final List<String> arguments) {
        if (!arguments.isEmpty()) {
            return StatusCode.ERROR;
        }
        commands.forEach(System.out::println);
        System.out.println(this);
        return StatusCode.OK;
    }
}
