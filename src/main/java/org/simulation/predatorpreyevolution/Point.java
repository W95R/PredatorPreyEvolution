package org.simulation.predatorpreyevolution;

public class Point {
    public float x;
    public float y;
    static public final float FULL_ANGLE = (float) (2 * Math.PI);

    /**
     * Creates new point
     * @param x x value
     * @param y y value
     */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates distance between this point and provided point
     * @param p point to which distance is calculated
     * @return distance between points
     */
    public float calculateDistance(Point p) {
        return (float) Math.sqrt(Math.pow(p.x - this.x, 2) + Math.pow(p.y - this.y, 2));
    }
    /**
     * Calculates distance squared between this point and provided point
     * @param p point to which distance is calculated
     * @return distance squared between points
     */
    public float calculateDistanceSquared(Point p) {
        return (float) (Math.pow(p.x - this.x, 2) + Math.pow(p.y - this.y, 2));
    }

    /**
     * Creates random point in rectangle defined from (0,0) to (this.x,this.y)
     * @return created point
     */
    public Point getRandomPointInBox() {
        return new Point((float) Math.random() * this.x, (float) Math.random() * this.y);
    }

    /**
     * Creates random point in radius from this point
     * @param radius radius
     * @return created point
     */
    public Point getRandomPointInRadius(float radius) {
        double randomRadius = Math.random() * radius;
        double randomAngle = Math.random() * Point.FULL_ANGLE;
        return new Point(this.x + (float) (randomRadius*Math.cos(randomAngle)), this.y + (float) (randomRadius*Math.sin(randomAngle)));
    }

    /**
     * Checks if given point is visible from this point
     * @param p checked point
     * @param direction vision direction in range (0;2pi)
     * @param fieldOfView field of view in range (0;2pi)
     * @param visibilityDistance visibility distance
     * @return Is the point visible
     */
    public boolean checkIfVisible(Point p, float direction, float fieldOfView, float visibilityDistance) {
        if (this.calculateDistanceSquared(p) > visibilityDistance * visibilityDistance)
            return false;
        float angleDifference = (direction - (float) Math.atan2(p.y - this.y, p.x - this.x) + Point.FULL_ANGLE) % Point.FULL_ANGLE;
        return angleDifference < fieldOfView / 2 || angleDifference > Point.FULL_ANGLE - fieldOfView / 2;
    }

    /**
     * Returns angle from this point to given point
     * @param p given point
     * @return calculated angle in range (0;2pi)
     */
    public float getAngle(Point p) {
        return ((float) Math.atan2(p.y - this.y, p.x - this.x) + Point.FULL_ANGLE) % Point.FULL_ANGLE;
    }

    /**
     * Calculates difference between angles
     * @param angle1 first angle
     * @param angle2 second angle
     * @return angle difference in range (0;2pi)
     */
    public static float getAngleDifference(float angle1, float angle2) {
        return (angle1 - angle2 + Point.FULL_ANGLE) % Point.FULL_ANGLE;
    }

    /**
     * Returns random angle in range (baseAngle - range/2;baseAngle + range/2)
     * @param baseAngle base angle
     * @param range angle range width
     * @return random angle in range (0;2pi)
     */
    public static float getRandomAngleInRange(float baseAngle, float range) {
        return (baseAngle + ((float) Math.random() * range - (range / 2) + Point.FULL_ANGLE)) % Point.FULL_ANGLE;
    }
}
