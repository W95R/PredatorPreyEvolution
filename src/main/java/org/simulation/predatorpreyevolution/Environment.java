package org.simulation.predatorpreyevolution;

import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.List;

public class Environment {
    private static Point size = new Point(0, 0);
    private static final List<Animal> animals = new ArrayList<>();
    private static final List<Plant> plants = new ArrayList<>();
    private static final List<Specie> herbivorousSpecies = new ArrayList<>();
    private static final List<Specie> carnivorousSpecies = new ArrayList<>();
    private static final List<Animal> animalsToAdd = new ArrayList<>();
    private static final List<Plant> plantsToAdd = new ArrayList<>();
    private static final List<Animal> animalsToRemove = new ArrayList<>();
    private static final List<Plant> plantsToRemove = new ArrayList<>();
    private static int time = 0;
    private static int simulationFramerate = 100;
    private static long lastFrame = 0;

    private final static AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if(lastFrame + (1000000000 / simulationFramerate) < now) {
                lastFrame = now;
                Environment.update();
            }
        }
    };

    private static boolean isRunning = false;

    private static MainViewController mainViewController;

    public static void setup() {
        plants.clear();
        for (int i = Plant.getStartingPopulation(); i > 0; i--)
            new Plant(Environment.size.getRandomPointInBox());

        animals.clear();
        for(Specie specie : Environment.carnivorousSpecies)
            for (int i = specie.getStartingPopulation(); i > 0; i--)
                new Carnivore(Environment.size.getRandomPointInBox(), specie);
        for(Specie specie : Environment.herbivorousSpecies)
            for (int i = specie.getStartingPopulation(); i > 0; i--)
                new Herbivore(Environment.size.getRandomPointInBox(), specie);

        Environment.commitPlantAddition();
        Environment.commitAnimalAddition();

        Environment.time = 0;
        printBoardState();
    }

    public static void run() {
        if (Environment.isRunning) return;
        Environment.isRunning = true;
        animationTimer.start();
    }

    public static void stop() {
        if (!Environment.isRunning) return;
        Environment.isRunning = false;
        animationTimer.stop();
    }

    private static void update() {
        System.out.println("Update: " + Environment.time);
        Environment.time++;
        for (Animal animal : Environment.animals) animal.predictNextMove();
        for (Animal animal : Environment.animals) animal.update();
        Environment.commitAnimalRemoval();
        Environment.commitAnimalAddition();
        Environment.commitPlantRemoval();
        for (Plant plant : Environment.plants) plant.update();
        Plant.updatePlants();
        Environment.commitPlantAddition();
        Environment.printBoardState();
    }

    public static void addAnimalToAdditionList(Animal animal) { Environment.animalsToAdd.add(animal); }
    public static void commitAnimalAddition() {
        Environment.animals.addAll(Environment.animalsToAdd);
        Environment.animalsToAdd.clear();
    }
    public static void addAnimalToRemovalList(Animal animal) { Environment.animalsToRemove.add(animal); }
    public static void commitAnimalRemoval() {
        Environment.animals.removeAll(Environment.animalsToRemove);
        Environment.animalsToRemove.clear();
    }
    public static List<Animal> getAnimals() { return Environment.animals; }

    public static void addPlantToAdditionList(Plant plant) { Environment.plantsToAdd.add(plant); }
    public static void commitPlantAddition() {
        Environment.plants.addAll(Environment.plantsToAdd);
        Environment.plantsToAdd.clear();
    }
    public static void addPlantToRemovalList(Plant plant) { Environment.plantsToRemove.add(plant); }
    public static void commitPlantRemoval() {
        Environment.plants.removeAll(Environment.plantsToRemove);
        Environment.plantsToRemove.clear();
    }
    public static List<Plant> getPlants() { return Environment.plants; }

    public static List<Specie> getHerbivorousSpecies() { return Environment.herbivorousSpecies; }
    public static List<Specie> getCarnivorousSpecies() { return Environment.carnivorousSpecies; }
    public static Specie getSpecieByName(String name, Class<? extends Animal> animalType) {
        for (Specie specie : animalType == Carnivore.class ? Environment.carnivorousSpecies : Environment.herbivorousSpecies)
            if (specie.getName().equals(name)) return specie;
        return null;
    }

    public static void addSpecie(Specie specie) {
        if (specie.isNameNull() || specie.isAddedToEnvironment()) return;
        if (specie.getAnimalType() == Herbivore.class)
            Environment.herbivorousSpecies.add(specie);
        else if (specie.getAnimalType() == Carnivore.class)
            Environment.carnivorousSpecies.add(specie);
        Environment.mainViewController.updateSpecies();
    }
    public static void removeSpecie(Specie specie) {
        if (specie.getAnimalType() == Herbivore.class)
            Environment.herbivorousSpecies.remove(specie);
        else if (specie.getAnimalType() == Carnivore.class)
            Environment.carnivorousSpecies.remove(specie);
        Environment.mainViewController.updateSpecies();
    }

    public static void setSize(Point size) {
        Environment.size = size;
        Environment.mainViewController.setCanvasSize(Environment.size);
    }

    public static Point getSize() { return Environment.size; }
    public static int getTime() { return Environment.time; }
    public static boolean isRunning() { return Environment.isRunning; }

    public static MainViewController getMainViewController() { return mainViewController; }
    public static void setMainViewController(MainViewController mainViewController) { Environment.mainViewController = mainViewController; }
    public static void printBoardState() {
        Environment.mainViewController.clearCanvas();
        for (Plant plant : Environment.plants)
            Environment.mainViewController.drawPlant(plant);
        for (Animal animal : Environment.animals)
            Environment.mainViewController.drawAnimal(animal);
    }

    public static int getSimulationFramerate() {
        return simulationFramerate;
    }

    public static void setSimulationFramerate(int simulationFramerate) {
        Environment.simulationFramerate = Math.max(simulationFramerate, 1);
    }
}
