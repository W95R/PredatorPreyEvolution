package org.simulation.predatorpreyevolution;

public class Entity {
    protected Point position;
    protected Environment environment;
    protected float energy;

    public Entity(Environment environment, Point startingPosition) {
        this.position = startingPosition;
        this.environment = environment;
        this.energy = 0;
    }

    public void update() {

    }
    public Point getPosition() { return this.position; }
    public float getEnergy() { return this.energy; }

    public void kill() {}
}
