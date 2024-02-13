package ru.rmntim.common.commands;

public class ShowCommand extends Command {
    public ShowCommand() {
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
