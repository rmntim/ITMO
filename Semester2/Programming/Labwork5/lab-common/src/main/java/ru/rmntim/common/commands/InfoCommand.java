package ru.rmntim.common.commands;

public class InfoCommand extends Command {
    public InfoCommand() {
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
