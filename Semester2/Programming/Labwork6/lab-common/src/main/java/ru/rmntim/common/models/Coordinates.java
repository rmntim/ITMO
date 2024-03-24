package ru.rmntim.common.models;

import java.io.Serializable;

public record Coordinates(Float x, float y) implements Serializable {
    @Override
    public String toString() {
        return "x: " + x + ", y: " + y;
    }
}
