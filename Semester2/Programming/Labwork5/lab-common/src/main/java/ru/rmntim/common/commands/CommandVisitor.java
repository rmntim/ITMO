package ru.rmntim.common.commands;

public interface CommandVisitor<T> {
    T visit(ExitCommand command);

    T visit(HelpCommand command);
}
