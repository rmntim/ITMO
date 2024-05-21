package common.domain;

import java.io.Serializable;

/**
 * @param x Поле не может быть null
 * @param y Поле не может быть null
 */
public record Coordinates(Integer x, Long y) implements Serializable {
    public boolean validate() {
        if (x == null) return false;
        return y != null;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
