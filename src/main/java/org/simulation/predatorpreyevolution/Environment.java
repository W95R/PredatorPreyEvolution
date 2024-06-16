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
    private static boolean isRunning = false;
    private static MainViewController mainViewController;

    private final static AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if(lastFrame + (1000000000 / simulationFramerate) < now) {
                lastFrame = now;
                Environment.update();
            }
        }
    };

    /**
     * Resets simulation and sets up the environment
     */
    public static void setup() {
        Environment.time = 0;
        plantsToAdd.clear();
        plantsToRemove.clear();
        animalsToAdd.clear();
        animalsToRemove.clear();
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

        printBoardState();
    }

    /**
     * Runs simulation
     */
    public static void run() {
        if (Environment.isRunning) return;
        Environment.isRunning = true;
        animationTimer.start();
    }

    /**
     * Pauses simulation
     */
    public static void stop() {
        if (!Environment.isRunning) return;
        Environment.isRunning = false;
        animationTimer.stop();
    }

    /**
     * Updates every entity
     */
    private static void update() {
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
        Environment.mainViewController.updateGraphs();
    }

    /**
     * Adds animal to addition list
     * @param animal added animal
     */
    public static void addAnimalToAdditionList(Animal animal) { Environment.animalsToAdd.add(animal); }
    /**
     * Adds animals from addition list to environment
     */
    public static void commitAnimalAddition() {
        if (Environment.animals.size() > 1000) Environment.animalsToAdd.clear();
        Environment.animals.addAll(Environment.animalsToAdd);
        Environment.animalsToAdd.clear();
    }
    /**
     * Adds animal to removal list
     * @param animal removed animal
     */
    public static void addAnimalToRemovalList(Animal animal) { Environment.animalsToRemove.add(animal); }
    /**
     * Removes animals from removal list from environment
     */
    public static void commitAnimalRemoval() {
        Environment.animals.removeAll(Environment.animalsToRemove);
        Environment.animalsToRemove.clear();
    }
    /**
     * Returns animals currently in environment
     * @return animals currently in environment
     */
    public static List<Animal> getAnimals() { return Environment.animals; }

    /**
     * Adds plant to addition list
     * @param plant added animal
     */
    public static void addPlantToAdditionList(Plant plant) { Environment.plantsToAdd.add(plant); }
    /**
     * Adds plants from addition list to environment
     */
    public static void commitPlantAddition() {
        Environment.plants.addAll(Environment.plantsToAdd);
        Environment.plantsToAdd.clear();
    }
    /**
     * Adds plant to removal list
     * @param plant removed animal
     */
    public static void addPlantToRemovalList(Plant plant) { Environment.plantsToRemove.add(plant); }
    /**
     * Removes plants from removal list from environment
     */
    public static void commitPlantRemoval() {
        Environment.plants.removeAll(Environment.plantsToRemove);
        Environment.plantsToRemove.clear();
    }
    /**
     * Returns plants currently in environment
     * @return plants currently in environment
     */
    public static List<Plant> getPlants() { return Environment.plants; }

    /**
     * checks if entity isn't scheduled to be removed
     * @param entity checked entity
     * @return is the entity being removed
     */
    public static boolean isAlive(Entity entity) {
        if (entity instanceof Animal)
            return !Environment.animalsToRemove.contains(entity);
        if (entity instanceof Plant)
            return !Environment.plantsToRemove.contains(entity);
        return false;
    }

    /**
     * Returns herbivorous species currently in the environment
     * @return list of herbivorous species
     */
    public static List<Specie> getHerbivorousSpecies() { return Environment.herbivorousSpecies; }
    /**
     * Returns carnivorous species currently in the environment
     * @return list of carnivorous species
     */
    public static List<Specie> getCarnivorousSpecies() { return Environment.carnivorousSpecies; }
    /**
     * Returns specie based on name and animal type
     * @param name specie's name
     * @param animalType animal type (Herbivore.class/Carnivore.class)
     * @return specie
     */
    public static Specie getSpecieByName(String name, Class<? extends Animal> animalType) {
        for (Specie specie : animalType == Carnivore.class ? Environment.carnivorousSpecies : Environment.herbivorousSpecies)
            if (specie.getName().equals(name)) return specie;
        return null;
    }

    /**
     * Adds specie to the environment
     * @param specie added specie
     */
    public static void addSpecie(Specie specie) {
        if (specie.isNameNull() || specie.isAddedToEnvironment()) return;
        if (specie.getAnimalType() == Herbivore.class)
            Environment.herbivorousSpecies.add(specie);
        else if (specie.getAnimalType() == Carnivore.class)
            Environment.carnivorousSpecies.add(specie);
        Environment.mainViewController.updateSpecies();
    }
    /**
     * Removes specie from the environment
     * @param specie removed specie
     */
    public static void removeSpecie(Specie specie) {
        if (specie.getAnimalType() == Herbivore.class)
            Environment.herbivorousSpecies.remove(specie);
        else if (specie.getAnimalType() == Carnivore.class)
            Environment.carnivorousSpecies.remove(specie);
        Environment.mainViewController.updateSpecies();
    }

    /**
     * Sets environment size
     * @param size size
     */
    public static void setSize(Point size) {
        Environment.size = size;
        Environment.mainViewController.setCanvasSize(Environment.size);
    }

    /**
     * Prints current simulation state to canvas
     */
    public static void printBoardState() {
        Environment.mainViewController.clearCanvas();
        for (Plant plant : Environment.plants)
            Environment.mainViewController.drawPlant(plant);
        for (Animal animal : Environment.animals)
            Environment.mainViewController.drawAnimal(animal);
    }

    public static Point getSize() { return Environment.size; }
    public static int getTime() { return Environment.time; }
    public static int getSimulationFramerate() { return simulationFramerate; }
    public static MainViewController getMainViewController() { return mainViewController; }

    public static void setSimulationFramerate(int simulationFramerate) {
        Environment.simulationFramerate = Math.max(simulationFramerate, 1);
    }
    public static void setMainViewController(MainViewController mainViewController) { Environment.mainViewController = mainViewController; }
}
