package ru.rmntim.common.commands;

public class AddCommand extends Command {
    public AddCommand() {
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
