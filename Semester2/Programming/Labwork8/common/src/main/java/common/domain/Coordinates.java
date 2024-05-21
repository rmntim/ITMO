package common.domain;

import java.io.Serializable;

public record Coordinates(Float x, float y) implements Serializable {
    public boolean validate() {
        if (x == null) return false;
        return x <= 633 && y > -408;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
