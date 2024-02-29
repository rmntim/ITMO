package ru.rmntim.cli.exceptions;

/**
 * Signals that a loop has been detected while executing scripts.
 */
public class RecursionException extends RuntimeException {
    public RecursionException(String fileName) {
        super("Recursion detected: " + fileName);
    }
}
