package ru.rmntim.common.models;

import java.io.Serializable;

public record DragonHead(Double eyesCount) implements Serializable  {
    @Override
    public String toString() {
        return "eyesCount: " + eyesCount;
    }
}
