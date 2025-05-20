package ru.rmntim.web.service;

import jakarta.ejb.Stateless;

@Stateless
public class AreaCheckerService {
    public boolean isInArea(double x, double y, double r) {
        if (x <= 0 && y <= 0) {
            return x >= -r && y >= -r;
        } else if (x >= 0 && y >= 0) {
            return (x * x + y * y) <= r * r;
        } else if (x >= 0 && y <= 0) {
            return 2 * y >= (x - r);
        }
        return false;
    }
}
