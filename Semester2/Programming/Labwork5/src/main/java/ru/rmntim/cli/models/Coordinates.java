package ru.rmntim.cli.models;

public record Coordinates(Float x, float y) {
    @Override
    public String toString() {
        return "x: " + x + ", y: " + y;
    }
}
