package org.simulation;

import java.util.ArrayList;

public class Herbivore extends Animal{
    public Herbivore(Environment environment, Point startingPosition, Specie specie, boolean assignRandomAge) {
        super(environment, startingPosition, specie, assignRandomAge);
    }
    @Override
    protected ArrayList<Entity> getTargets() {
        return new ArrayList<>();
    }
    @Override
    protected ArrayList<Entity> getEnemies() {
        return new ArrayList<>();
    }
}
