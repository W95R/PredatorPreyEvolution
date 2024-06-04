package org.simulation.predatorpreyevolution;

import java.util.ArrayList;

public class Herbivore extends Animal{
    public Herbivore(Environment environment, Point startingPosition, Specie specie) {
        super(environment, startingPosition, specie);
    }
    @Override
    protected ArrayList<Entity> getTargets() {
        ArrayList<Entity> targets = new ArrayList<>();
        for (Entity entity : this.environment.getPlants())
            if (this.position.checkIfVisible(entity.getPosition(), this.directionAngle, this.fieldOfView, this.getVisibilityDistance()))
                targets.add(entity);
        return targets;
    }
    @Override
    protected ArrayList<Entity> getEnemies() {
        ArrayList<Entity> enemies = new ArrayList<>();
        for (Entity entity : this.environment.getPlants())
            if (this.position.checkIfVisible(entity.getPosition(), this.directionAngle, this.fieldOfView, this.getVisibilityDistance()))
                enemies.add(entity);
        return enemies;
    }
}
