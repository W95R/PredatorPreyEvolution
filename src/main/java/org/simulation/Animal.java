package org.simulation;

import java.util.ArrayList;

public class Animal extends Entity{
    protected final Specie specie;
    protected final int death_time;
    protected final boolean isMale;

    protected float speed;
    protected float FieldOfView;

    public Animal(Environment environment, Point startingPosition, Specie specie, boolean assignRandomAge) {
        super(environment, startingPosition);
        this.environment.addAnimal(this);
        this.specie = specie;
        this.death_time = 100 - (assignRandomAge ? 0 : 50);
        this.isMale = Math.random() < 0.5;
    }

    @Override
    public void update() {
        if (death_time > this.environment.getTime()) this.kill();
        this.energy -= this.getEnergyUsage();
        if (energy <= 0) this.kill();
    }

    protected ArrayList<Entity> getTargets() {
        return new ArrayList<>();
    }
    protected ArrayList<Animal> getReproductoryTargets() { return new ArrayList<>(); }
    protected ArrayList<Entity> getEnemies() {
        return new ArrayList<>();
    }
    protected Point getDirection() {
        return new Point(0, 0);
    }
    protected void eat(Entity target) {}
    protected void reproduce(Entity target) {}
    protected float getEnergyUsage(){
        return 0.0f;
    }

    @Override
    public void kill() { this.environment.removeAnimal(this); }
}
