package ru.rmntim.common.commands;

public abstract class Command {
    public abstract <T> T accept(Visitor<T> visitor);

    public interface Visitor<T> {
        T visit(ExitCommand command);

        T visit(HelpCommand command);

        T visit(InfoCommand command);

        T visit(ShowCommand command);

        T visit(AddCommand command);

        T visit(UpdateCommand command);
    }
}
