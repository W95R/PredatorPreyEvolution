package org.simulation;

public class Point {
    float x;
    float y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public float calculateDistance(Point p) {
        return (float) Math.sqrt(Math.pow(p.x - this.x, 2) + Math.pow(p.y - this.y, 2));
    }
}
