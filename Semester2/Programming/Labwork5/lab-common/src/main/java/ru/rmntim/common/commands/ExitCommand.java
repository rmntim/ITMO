package ru.rmntim.common.commands;

public class ExitCommand extends Command {
    public ExitCommand() {
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
