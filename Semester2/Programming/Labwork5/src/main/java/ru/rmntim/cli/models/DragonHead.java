package ru.rmntim.cli.models;

public record DragonHead(Double eyesCount) {
    public DragonHead {
        if (eyesCount == null) {
            throw new IllegalArgumentException("Invalid eyes count (must be non-null)");
        }
    }

    @Override
    public String toString() {
        return "eyesCount: " + eyesCount;
    }
}
