package com.bloidonia.asciiimage.parts;

import java.awt.geom.Point2D;
import java.util.List;

public class Element {
    private final String name;
    private final Type type;
    private final List<Point2D> points;

    public Element(List<Point2D> points, Type type, String name) {
        this.type = type;
        this.points = points;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public List<Point2D> getPoints() {
        return points;
    }

    public double maxX() {
        return points.stream().mapToDouble(Point2D::getX).max().getAsDouble();
    }

    public double minX() {
        return points.stream().mapToDouble(Point2D::getX).min().getAsDouble();
    }

    public double maxY() {
        return points.stream().mapToDouble(Point2D::getY).max().getAsDouble();
    }

    public double minY() {
        return points.stream().mapToDouble(Point2D::getY).min().getAsDouble();
    }

    @Override
    public String toString() {
        return "E{" + name + " " + type + " " + points + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Element element = (Element) o;

        if (!name.equals(element.name)) {
            return false;
        }
        if (type != element.type) {
            return false;
        }
        return points.equals(element.points);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + points.hashCode();
        return result;
    }
}
