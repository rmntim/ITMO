package ru.rmntim.cli.exceptions;

public class BadCommandArgumentsException extends RuntimeException {
    public BadCommandArgumentsException(final String message) {
        super(message);
    }
}
