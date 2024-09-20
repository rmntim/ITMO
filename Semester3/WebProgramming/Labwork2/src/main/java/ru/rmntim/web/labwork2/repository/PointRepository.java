package ru.rmntim.web.labwork2.repository;

import jakarta.ejb.Stateful;
import jakarta.enterprise.context.SessionScoped;
import ru.rmntim.web.labwork2.models.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateful
@SessionScoped
public class PointRepository implements Serializable {
    private final List<Point> points = new ArrayList<>();

    public void addPoint(Point point) {
        points.add(point);
    }

    public List<Point> getPoints() {
        return points;
    }
}
