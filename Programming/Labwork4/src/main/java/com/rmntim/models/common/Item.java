package com.rmntim.models.common;

public record Item(String name, int weight) {

    @Override
    public String toString() {
        return name + " (" + weight + " кг)";
    }
}
