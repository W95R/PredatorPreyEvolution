package org.simulation;

public class Point {
    public float x;
    public float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float calculateDistance(Point p) {
        return (float) Math.sqrt(Math.pow(p.x - this.x, 2) + Math.pow(p.y - this.y, 2));
    }

    public float calculateDistanceSquared(Point p) {
        return (float) (Math.pow(p.x - this.x, 2) + Math.pow(p.y - this.y, 2));
    }

    public Point getRandomPointInBox() {
        return new Point((float) Math.random() * this.x, (float) Math.random() * this.y);
    }

    public boolean checkIfVisible(Point p, float direction, float fieldOfView, float visibilityDistance) {
        if (this.calculateDistanceSquared(p) > visibilityDistance * visibilityDistance)
            return false;

        float angleDifference = direction - (float) ((Math.atan2(p.y - this.y, p.x - this.x) + (2 * Math.PI)) % (2 * Math.PI));
        angleDifference = Math.abs((angleDifference > (float) Math.PI) ? angleDifference : 2 - angleDifference);
        if (angleDifference > (fieldOfView / 2))
            return false;

        return true;
    }
}
