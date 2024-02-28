package ru.rmntim.cli.exceptions;

public class BuildCancelledException extends RuntimeException {
    public BuildCancelledException() {
        super("Build cancelled");
    }

    public BuildCancelledException(String message) {
        super(message);
    }
}
