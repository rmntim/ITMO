package ru.rmntim.cli.commands;

public class BadCommandArgumentsException extends RuntimeException {
    public BadCommandArgumentsException(final String message) {
        super(message);
    }
}
