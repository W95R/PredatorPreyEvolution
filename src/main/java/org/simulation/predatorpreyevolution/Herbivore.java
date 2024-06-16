package org.simulation.predatorpreyevolution;

import java.util.ArrayList;

public final class Herbivore extends Animal {
    /**
     * Creates herbivore with starting speed, fieldOfView and viewArea values from specie
     * @param startingPosition herbivore's staring position
     * @param specie specie to which herbivore belongs
     */
    public Herbivore(Point startingPosition, Specie specie) {
        super(startingPosition, specie);
    }
    /**
     * Creates herbivore with speed, fieldOfView and viewArea values mutated based on mutation rate from specie
     * @param startingPosition herbivore's staring position
     * @param specie specie to which herbivore belongs
     * @param baseSpeed speed to be mutated
     * @param baseFieldOfView fieldOfView to be mutated
     * @param baseViewArea viewArea to be mutated
     */
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
