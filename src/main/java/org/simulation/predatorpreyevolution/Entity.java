package org.simulation.predatorpreyevolution;

public abstract sealed class Entity permits Animal, Plant {
    protected final Point position;
    protected float energy;

    /**
     * Creates entity with specified position
     * @param startingPosition starting position
     */
    public Entity(Point startingPosition) {
        this.position = startingPosition;
        this.energy = 0;
    }

    /**
     * Function invoked on every frame of simulation
     */
    public abstract void update();
    public Point getPosition() { return this.position; }
    public float getEnergy() { return this.energy; }
    /**
     * Returns chance of winning with animal provided
     * @param animal animal
     * @return chance of winning in range 0f - 1f
     */
    public abstract float getChanceOfWinning(Animal animal);
    /**
     * Calculates energy gained by consuming this entity
     * @return gained energy
     */
    public abstract float getConsumptionGainedEnergy();

    /**
     * Kills entity
     */
    public abstract void kill();
}
