package ru.rmntim.server.models;

public record DragonHead(Double eyesCount) {
    @Override
    public String toString() {
        return "eyesCount: " + eyesCount;
    }
}
