package org.simulation.predatorpreyevolution;

import java.util.ArrayList;

public final class Carnivore extends Animal {
    public Carnivore(Point startingPosition, Specie specie) {
        super(startingPosition, specie);
    }
    public Carnivore(Point startingPosition, Specie specie, float baseSpeed, float baseFieldOfView, float baseViewArea) {
        super(startingPosition, specie, baseSpeed, baseFieldOfView, baseViewArea);
    }

    @Override
    protected boolean isTarget(Entity entity) {
        if (!(entity instanceof Animal)) return false;
        if(!((Animal)entity).getSpecie().equals(this.specie)) return true;
        if (!this.specie.getIsCannibalistic()) return false;
        return this.isMale != ((Animal)entity).isMale;
    }

    @Override
    protected boolean isEnemy(Animal animal) {
        if (!(animal instanceof Carnivore)) return false;
        if(!animal.getSpecie().equals(this.specie)) return true;
        if (!this.specie.getIsCannibalistic()) return false;
        return this.isMale != animal.isMale;
    }

    @Override
    protected ArrayList<Entity> getTargets() {
        ArrayList<Entity> targets = new ArrayList<>();
        for (Entity entity : Environment.getAnimals())
            if (this.isTarget(entity) && this.isEntityVisible(entity))
                targets.add(entity);
        return targets;
    }
}
