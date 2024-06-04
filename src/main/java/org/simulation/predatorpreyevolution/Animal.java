package org.simulation.predatorpreyevolution;

import java.util.ArrayList;
import java.util.List;

public class Animal extends Entity{
    protected float directionAngle;

    protected final Specie specie;
    protected final int death_time;
    protected final boolean isMale;

    protected float speed;
    protected float fieldOfView;

    protected float nextMoveDirectionAngle;
    protected AnimalStatus status = AnimalStatus.WANDERING;


    public Animal(Environment environment, Point startingPosition, Specie specie) {
        super(environment, startingPosition);
        this.environment.addAnimal(this);
        this.isMale = Math.random() < 0.5;
        this.specie = specie;
        this.death_time = this.environment.getTime() + this.specie.getRandomLifeDuration();
    }
    public Animal(Environment environment, Point startingPosition, Specie specie, float speed, float fieldOfView) {
        super(environment, startingPosition);
        this.environment.addAnimal(this);
        this.isMale = Math.random() < 0.5;
        this.specie = specie;
        this.death_time = this.environment.getTime() + this.specie.getRandomLifeDuration();
        this.speed = speed;
        this.fieldOfView = fieldOfView;
    }

    public void predictNextMove() {

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
    protected float getVisibilityDistance() {
        return 1f;
    }

    @Override
    public void kill() { this.environment.removeAnimal(this); }

    public Specie getSpecie() { return this.specie; }
}
