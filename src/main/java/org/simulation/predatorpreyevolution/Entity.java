package org.simulation.predatorpreyevolution;

public abstract sealed class Entity permits Animal, Plant {
    protected Point position;
    protected float energy;

    public Entity(Point startingPosition) {
        this.position = startingPosition;
        this.energy = 0;
    }

    public abstract void update();
    public Point getPosition() { return this.position; }
    public float getEnergy() { return this.energy; }
    public abstract float getChanceOfWinning(Animal animal);
    public abstract float getConsumptionGainedEnergy();

    public abstract void kill();
}
