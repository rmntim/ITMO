package ru.rmntim.common.commands;

import java.util.List;

public class HelpCommand extends Command {
    private final List<Command> commands;

    public HelpCommand(final List<Command> commands) {
        super("help", "prints all available commands");
        this.commands = commands;
    }

    public List<Command> getCommands() {
        return commands;
    }

    @Override
    public <T> T accept(CommandVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
