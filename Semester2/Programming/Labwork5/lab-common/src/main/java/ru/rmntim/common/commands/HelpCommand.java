package ru.rmntim.common.commands;

public class HelpCommand extends Command {
    public HelpCommand() {
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
