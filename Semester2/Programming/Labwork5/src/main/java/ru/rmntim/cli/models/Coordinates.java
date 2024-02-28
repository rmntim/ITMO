package ru.rmntim.cli.models;

public record Coordinates(Float x, float y) {
    private static final float MAX_X = 633;
    private static final float MIN_Y = -408;

    public Coordinates {
        if (x == null || x > MAX_X) {
            throw new IllegalArgumentException("X must be <= " + MAX_X);
        }
        if (y <= MIN_Y) {
            throw new IllegalArgumentException("Y must be > " + MIN_Y);
        }
    }

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y;
    }
}
