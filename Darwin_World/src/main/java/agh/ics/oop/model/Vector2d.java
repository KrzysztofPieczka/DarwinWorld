package agh.ics.oop.model;

import java.util.Objects;

public class Vector2d {
    private int x;
    private final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public String toString() {
        return "(%d, %d)".formatted(x,y);
    }

    public boolean precedes(Vector2d other) {
        return other.getX() >= getX() && other.getY() >= getY();
    }

    public boolean follows(Vector2d other) {
        return other.getX() <= getX() && other.getY() <= getY();
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(x + other.getX(), y + other.getY());
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(x - other.getX(), y - other.getY());
    }

    public Vector2d upperRight(Vector2d other) {
        int y = Math.max(other.getY(), getY());
        int x = Math.max(other.getX(), getX());
        return new Vector2d(x, y);
    }

    public Vector2d lowerLeft(Vector2d other) {
        int y = Math.min(other.getY(), getY());
        int x = Math.min(other.getX(), getX());
        return new Vector2d(x, y);
    }

    public Vector2d opposite() {
        int x = getX();
        int y = getY();
        return new Vector2d(-x, -y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2d vector2d = (Vector2d) o;
        return x == vector2d.x && y == vector2d.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
