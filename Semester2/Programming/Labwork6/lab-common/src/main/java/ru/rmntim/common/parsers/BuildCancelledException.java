package ru.rmntim.common.parsers;

public class BuildCancelledException extends RuntimeException {
    public BuildCancelledException() {
        super();
    }

    public BuildCancelledException(String message) {
        super(message);
    }
}
