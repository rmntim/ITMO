package ru.rmntim.cli.exceptions;

/**
 * Signals that bad arguments were passed to the command
 */
public class BadCommandArgumentsException extends RuntimeException {
    public BadCommandArgumentsException(final String message) {
        super(message);
    }
}
