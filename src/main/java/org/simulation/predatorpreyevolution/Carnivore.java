package org.simulation.predatorpreyevolution;

import java.util.ArrayList;

public class Carnivore extends Animal {
    public Carnivore(Point startingPosition, Specie specie) {
        super(startingPosition, specie);
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
