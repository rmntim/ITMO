package ru.rmntim.cli.exceptions;

public class RecursionException extends RuntimeException {
    public RecursionException(String fileName) {
        super(String.format("Recursion detected: %s", fileName));
    }
}
