package ru.rmntim.cli.exceptions;

public class InvalidScriptException extends RuntimeException {
    public InvalidScriptException() {
        super("Invalid script");
    }
}
