package ru.rmntim.common.commands;

public class ExitCommand extends Command {
    public ExitCommand() {
        super("exit", "exits the application without saving");
    }

    @Override
    public <T> T accept(CommandVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
