package ru.rmntim.cli.exceptions;

/**
 * Signals that script being executed has a syntax error
 */
public class InvalidScriptException extends RuntimeException {
    public InvalidScriptException() {
        super("Invalid script");
    }
}
