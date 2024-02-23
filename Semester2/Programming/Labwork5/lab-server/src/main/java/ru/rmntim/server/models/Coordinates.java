package ru.rmntim.server.models;

public class Coordinates {
    private static final float MAX_X = 633;
    private static final float MIN_Y = -408;

    private Float x; //Максимальное значение поля: 633, Поле не может быть null
    private float y; //Значение поля должно быть больше -408

    public Coordinates(float x, float y) {
        setX(x);
        setY(y);
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        if (x == null) {
            throw new IllegalArgumentException("X cannot be null");
        }
        if (x.floatValue() > MAX_X) {
            throw new IllegalArgumentException("X must be <= " + MAX_X);
        }
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        if (y <= MIN_Y) {
            throw new IllegalArgumentException("Y must be > " + MIN_Y);
        }
        this.y = y;
    }
}
