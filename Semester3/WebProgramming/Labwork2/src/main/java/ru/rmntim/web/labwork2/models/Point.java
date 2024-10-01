package ru.rmntim.web.labwork2.models;

public record Point(double x, double y, double r, boolean isInside) {
    public Point(double x, double y, double r) {
        this(x, y, r, isInside(x, y, r));
    }

    private static boolean isInside(double x, double y, double r) {
        if (x > 0 && y > 0) {
            return false;
        }

        if (x > 0 && y < 0) {
            if (x * x + y * y > (r * r) / 4) {
                return false;
            }
        }

        if (x < 0 && y < 0) {
            if (x < -r || y < -(r / 2)) {
                return false;
            }
        }

        if (x < 0 && y > 0) {
            if (y - 2 * x > r) {
                return false;
            }
        }

        return true;
    }
}
