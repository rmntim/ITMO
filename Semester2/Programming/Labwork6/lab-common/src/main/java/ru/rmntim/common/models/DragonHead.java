package ru.rmntim.common.models;

public record DragonHead(Double eyesCount) {
    @Override
    public String toString() {
        return "eyesCount: " + eyesCount;
    }
}
