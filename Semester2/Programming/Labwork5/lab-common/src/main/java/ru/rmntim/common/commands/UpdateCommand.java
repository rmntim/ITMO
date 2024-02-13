package ru.rmntim.common.commands;

public class UpdateCommand extends Command {
    private final int id;

    public UpdateCommand(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
