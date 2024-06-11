package org.simulation.predatorpreyevolution;

import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;

public class Specie {
    private final Class<? extends Animal> animalType;
    private String name;
    private Color color;
    private int startingPopulation;

    private float startingSpeed;
    private float startingFieldOfView;
    private float startingViewArea;

    private boolean doEvolveSpeed;
    private boolean doEvolveFieldOfView;
    private boolean doEvolveViewArea;

    private float speedEvolutionRate;
    private float fieldOfViewEvolutionRate;
    private float viewAreaEvolutionRate;

    private float lifeDurationAverage;
    private float lifeDurationStandardDeviation;

    private int reproductionRate;
    private boolean isCannibalistic;
    private boolean reproduceByDivision;

    public Specie(Class<? extends Animal> type) {
        this.animalType = type;
        this.color = Color.RED;
        this.startingPopulation = 200;
        this.startingSpeed = 4f;
        this.startingFieldOfView = 2.0944f;
        this.startingViewArea = 5000f;
        this.doEvolveSpeed = true;
        this.doEvolveFieldOfView = true;
        this.doEvolveViewArea = true;
        this.speedEvolutionRate = 0.3f;
        this.fieldOfViewEvolutionRate = 0.05f * 10;
        this.viewAreaEvolutionRate = 50f * 10;
        this.lifeDurationAverage = 300;
        this.lifeDurationStandardDeviation = 50;
        this.reproductionRate = 1;
        this.isCannibalistic = false;
        this.reproduceByDivision = true;
    }

    /**
     * Get random life duration for given specie based on specie's characteristics
     * @return random life duration
     */
    public int getRandomLifeDuration() {
        return (int) ( new Random().nextGaussian() * this.lifeDurationStandardDeviation + this.lifeDurationAverage);
    }

    public List<Specie> getOtherSpecies() {
        if (this.animalType == Herbivore.class)
            return Environment.getHerbivorousSpecies();
        if (this.animalType == Carnivore.class)
            return Environment.getCarnivorousSpecies();
        throw new RuntimeException("Specie is not assigned to correct animal type");
    }

    /**
     * Checks if specie is saved in environment
     * @return is specie saved in environemnt
     */
    public boolean isAddedToEnvironment() {
        if (this.name == null) return false;
        for (Specie specie: this.getOtherSpecies())
            if (this.name.equals(specie.getName()))
                return true;
        return false;
    }

    /**
     * Checks if tested name is unique in species saved in environment with the same animal type
     * @param tested_name tested name
     * @return is the name unique
     */
    public boolean isNameUnique(String tested_name) {
        if (tested_name == null) return false;
        if (tested_name.isEmpty()) return false;
        for (Specie specie: this.getOtherSpecies())
            if (this != specie && specie.getName().equals(tested_name))
                return false;
        return true;
    }

    /**
     * Checks if specie's name is null
     * @return is specie's ame null
     */
    public boolean isNameNull() { return this.name == null; }

    /**
     * Calculates random speed evolved from baseSpeed if doEvolveSpeed is set
     * @param baseSpeed speed to be evolved
     * @return calculated random speed
     */
    public float getEvolvedSpeed(float baseSpeed) {
        return this.doEvolveSpeed ? ( (float) new Random().nextGaussian() * this.speedEvolutionRate + baseSpeed) : baseSpeed;
    }
    /**
     * Calculates random FOV evolved from baseFieldOfView if doEvolveFieldOfView is set
     * @param baseFieldOfView FOV to be evolved
     * @return calculated random FOV
     */
    public float getEvolvedFieldOfView(float baseFieldOfView) {
        return this.doEvolveFieldOfView ? ( (float) new Random().nextGaussian() * this.fieldOfViewEvolutionRate + baseFieldOfView) : baseFieldOfView;
    }
    /**
     * Calculates random VA evolved from baseViewArea if doEvolveViewArea is set
     * @param baseViewArea VA to be evolved
     * @return calculated random VA
     */
    public float getEvolvedViewArea(float baseViewArea) {
        return this.doEvolveViewArea ? ( (float) new Random().nextGaussian() * this.viewAreaEvolutionRate + baseViewArea) : baseViewArea;
    }

    // Setters
    public void setName(String name) {
        if (!isNameUnique(name))
            throw new RuntimeException("Specie name must be unique and not null");
        this.name = name;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public void setStartingPopulation(int startingPopulation) { this.startingPopulation = startingPopulation; }

    public void setStartingSpeed(float startingSpeed) { this.startingSpeed = startingSpeed; }
    public void setStartingFieldOfView(float startingFieldOfView) { this.startingFieldOfView = startingFieldOfView; }
    public void setStartingViewArea(float startingViewArea) { this.startingViewArea = startingViewArea; }

    public void setDoEvolveSpeed(boolean doEvolveSpeed) { this.doEvolveSpeed = doEvolveSpeed; }
    public void setDoEvolveFieldOfView(boolean doEvolveFieldOfView) { this.doEvolveFieldOfView = doEvolveFieldOfView; }
    public void setDoEvolveViewArea(boolean doEvolveViewArea) { this.doEvolveViewArea = doEvolveViewArea; }

    public void setSpeedEvolutionRate(float speedEvolutionRate) { this.speedEvolutionRate = speedEvolutionRate; }
    public void setFieldOfViewEvolutionRate(float fieldOfViewEvolutionRate) { this.fieldOfViewEvolutionRate = fieldOfViewEvolutionRate; }
    public void setViewAreaEvolutionRate(float viewAreaEvolutionRate) { this.viewAreaEvolutionRate = viewAreaEvolutionRate; }

    public void setLifeDurationAverage(float lifeDurationAverage) { this.lifeDurationAverage = lifeDurationAverage; }
    public void setLifeDurationStandardDeviation(float lifeDurationStandardDeviation) { this.lifeDurationStandardDeviation = lifeDurationStandardDeviation; }

    public void setReproductionRate(int reproductionRate) { this.reproductionRate = reproductionRate; }
    public void setIsCannibalistic(boolean isCannibalistic) { this.isCannibalistic = isCannibalistic; }

    // Getters
    public Class<? extends Animal> getAnimalType() { return animalType; }
    public String getName() { return this.name; }
    public Color getColor() { return color; }

    public int getStartingPopulation() { return this.startingPopulation; }

    public float getStartingSpeed() { return this.startingSpeed; }
    public float getStartingFieldOfView() { return this.startingFieldOfView; }
    public float getStartingViewArea() { return this.startingViewArea; }

    public boolean getDoEvolveSpeed() { return this.doEvolveSpeed; }
    public boolean getDoEvolveFieldOfView() { return this.doEvolveFieldOfView; }
    public boolean getDoEvolveViewArea() { return this.doEvolveViewArea; }

    public float getSpeedEvolutionRate() { return speedEvolutionRate; }
    public float getFieldOfViewEvolutionRate() { return fieldOfViewEvolutionRate; }
    public float getViewAreaEvolutionRate() { return viewAreaEvolutionRate; }

    public float getLifeDurationAverage() { return this.lifeDurationAverage; }
    public float getLifeDurationStandardDeviation() { return this.lifeDurationStandardDeviation; }

    public int getReproductionRate() { return reproductionRate; }
    public boolean getIsCannibalistic() { return this.isCannibalistic;}

    public boolean isReproduceByDivision() {
        return reproduceByDivision;
    }

    public void setReproduceByDivision(boolean reproduceByDivision) {
        this.reproduceByDivision = reproduceByDivision;
    }
}
