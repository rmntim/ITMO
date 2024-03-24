package ru.rmntim.server.exceptions;

/**
 * Signals that an error has occurred during validation
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
