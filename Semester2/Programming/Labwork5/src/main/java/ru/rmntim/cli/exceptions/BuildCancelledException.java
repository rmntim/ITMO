package ru.rmntim.cli.exceptions;

/**
 * Signals that object build was cancelled, either by user or because of invalid script syntax
 */
public class BuildCancelledException extends RuntimeException {
    public BuildCancelledException() {
        super("Build cancelled");
    }

    public BuildCancelledException(String message) {
        super(message);
    }
}
