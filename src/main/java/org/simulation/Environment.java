package org.simulation;

import java.util.ArrayList;
import java.util.List;

public class Environment {
    private Point size;
    private List<Animal> animals;
    private List<Plant> plants;
    private List<Specie> herbivorousSpecies;
    private List<Specie> carnivorousSpecies;
    private int time;

    public Environment() {
        this.animals = new ArrayList<>();
        this.plants = new ArrayList<>();
        this.herbivorousSpecies = new ArrayList<>();
        this.carnivorousSpecies = new ArrayList<>();
    }

    public void update() {
        this.time++;
        for (Plant plant : this.plants) plant.update();
        for (Animal animal : this.animals) animal.update();
    }

    public void addAnimal(Animal animal) { this.animals.add(animal); }
    public void removeAnimal(Animal animal) { this.animals.remove(animal); }
    public List<Animal> getAnimals() { return this.animals; }

    public void addPlant(Plant plant) { this.plants.add(plant); }
    public void removePlant(Plant plant) { this.plants.remove(plant); }
    public List<Plant> getPlants() { return this.plants; }

    public void addHerbivorousSpecie(Specie specie) { this.herbivorousSpecies.add(specie); }
    public void removeHerbivorousSpecie(Specie specie) { this.herbivorousSpecies.remove(specie); }
    public List<Specie> getHerbivorousSpecies() { return this.herbivorousSpecies; }

    public void addCarnivorousSpecie(Specie specie) { this.carnivorousSpecies.add(specie); }
    public void removeCarnivorousSpecie(Specie specie) { this.carnivorousSpecies.remove(specie); }
    public List<Specie> getCarnivorousSpecies() { return this.carnivorousSpecies; }


    public void setSize(Point size) { this.size = size; }
    public Point getSize() { return this.size; }

    public int getTime() { return this.time; }
}
