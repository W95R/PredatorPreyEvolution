package org.simulation.predatorpreyevolution;

public class Entity {
    protected Point position;
    protected float energy;

    public Entity(Point startingPosition) {
        this.position = startingPosition;
        this.energy = 0;
    }

    public void update() {

    }
    public Point getPosition() { return this.position; }
    public float getEnergy() { return this.energy; }

    public void kill() {}
}
