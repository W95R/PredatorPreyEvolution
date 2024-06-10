package org.simulation.predatorpreyevolution;

public class Point {
    public float x;
    public float y;
    static public float FULL_ANGLE = (float) (2 * Math.PI);

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

    public Point getRandomPointInRadius(float radius) {
        double randomRadius = Math.random() * radius;
        double randomAngle = Math.random() * Point.FULL_ANGLE;
        return new Point(this.x + (float) (randomRadius*Math.cos(randomAngle)), this.y + (float) (randomRadius*Math.sin(randomAngle)));
    }

    public boolean checkIfVisible(Point p, float direction, float fieldOfView, float visibilityDistance) {
        if (this.calculateDistanceSquared(p) > visibilityDistance * visibilityDistance)
            return false;
        float angleDifference = (direction - (float) Math.atan2(p.y - this.y, p.x - this.x) + Point.FULL_ANGLE) % Point.FULL_ANGLE;
        return angleDifference < fieldOfView / 2 || angleDifference > Point.FULL_ANGLE - fieldOfView / 2;
    }
}
