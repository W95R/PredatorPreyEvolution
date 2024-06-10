package org.simulation.predatorpreyevolution;

import java.util.ArrayList;

public abstract class Animal extends Entity{
    protected float directionAngle;

    protected final Specie specie;
    protected final int death_time;
    protected final boolean isMale;

    protected float speed;
    protected float fieldOfView;

    protected float nextMoveDirectionAngle;
    protected AnimalStatus status = AnimalStatus.WANDERING;


    public Animal(Point startingPosition, Specie specie) {
        super(startingPosition);
        Environment.addAnimalToAdditionList(this);
        this.isMale = Math.random() < 0.5;
        this.specie = specie;
        this.death_time = Environment.getTime() + this.specie.getRandomLifeDuration();
    }
    public Animal(Point startingPosition, Specie specie, float speed, float fieldOfView) {
        super(startingPosition);
        Environment.addAnimalToAdditionList(this);
        this.isMale = Math.random() < 0.5;
        this.specie = specie;
        this.death_time = Environment.getTime() + this.specie.getRandomLifeDuration();
        this.speed = speed;
        this.fieldOfView = fieldOfView;
    }

    public void predictNextMove() {

    }

    @Override
    public void update() {
        this.getTargets();
        if (death_time > Environment.getTime()) this.kill();
        this.energy -= this.getEnergyUsage();
        // if (energy <= 0) this.kill();
    }

    protected abstract boolean isTarget(Entity entity);
    protected abstract boolean isEnemy(Animal animal);
    protected boolean isReproductoryTarget(Animal animal) {
        return animal.getSpecie().equals(this.specie) && animal.isMale != this.isMale;
    }
    protected boolean isEntityVisible(Entity entity) {
        return this.position.checkIfVisible(
                entity.getPosition(),
                this.directionAngle,
                this.fieldOfView,
                this.getVisibilityDistance()
        );
    }

    protected abstract ArrayList<Entity> getTargets();
    protected ArrayList<Animal> getEnemies() {
        ArrayList<Animal> enemies = new ArrayList<>();
        for (Animal animal : Environment.getAnimals())
            if (this.isEnemy(animal) && this.isEntityVisible(animal))
                enemies.add(animal);
        return enemies;
    }
    protected ArrayList<Animal> getReproductoryTargets() {
        ArrayList<Animal> targets = new ArrayList<>();
        for (Animal animal : Environment.getAnimals())
            if (this.isReproductoryTarget(animal) && this.isEntityVisible(animal))
                targets.add(animal);
        return targets;
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
    public void kill() { Environment.addAnimalToRemovalList(this); }

    public Specie getSpecie() { return this.specie; }
}
