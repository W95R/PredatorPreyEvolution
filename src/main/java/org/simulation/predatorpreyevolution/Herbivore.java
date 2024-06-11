package org.simulation.predatorpreyevolution;

import java.util.ArrayList;

public final class Herbivore extends Animal {
    public Herbivore(Point startingPosition, Specie specie) {
        super(startingPosition, specie);
    }
    public Herbivore(Point startingPosition, Specie specie, float baseSpeed, float baseFieldOfView, float baseViewArea) {
        super(startingPosition, specie, baseSpeed, baseFieldOfView, baseViewArea);
    }

    @Override
    protected boolean isTarget(Entity entity) {
        return entity instanceof Plant;
    }

    @Override
    protected boolean isEnemy(Animal animal) {
        return animal instanceof Carnivore;
    }

    @Override
    protected ArrayList<Entity> getTargets() {
        ArrayList<Entity> targets = new ArrayList<>();
        for (Entity entity : Environment.getPlants())
            if (this.position.checkIfVisible(entity.getPosition(), this.directionAngle, this.fieldOfView, this.getVisibilityDistance()))
                targets.add(entity);
        return targets;
    }
}
