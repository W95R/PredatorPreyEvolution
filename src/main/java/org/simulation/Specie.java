package org.simulation;

import java.util.Random;

public class Specie {
    private Class<? extends Animal> animalType;
    private String name;
    private int startingPopulation;

    private int startingSpeed;
    private float startingFieldOfView;
    private float startingReproductionRate;

    private boolean doEvolveSpeed;
    private boolean doEvolveFieldOfView;
    private boolean doEvolveReproductionRate;

    private float lifeDurationAverage;
    private float lifeDurationStandardDeviation;

    private boolean isCannibalistic;

    public Specie(Class<? extends Animal> type) {
        this.animalType = type;
    }

    public void setName(String name) {   this.name = name; }
    public void setStartingPopulation(int startingPopulation) { this.startingPopulation = startingPopulation; }

    public void setStartingSpeed(int startingSpeed) { this.startingSpeed = startingSpeed; }
    public void setStartingFieldOfView(float startingFieldOfView) { this.startingFieldOfView = startingFieldOfView; }
    public void setStartingReproductionRate(float startingReproductionRate) { this.startingReproductionRate = startingReproductionRate; }

    public void setDoEvolveSpeed(boolean doEvolveSpeed) { this.doEvolveSpeed = doEvolveSpeed; }
    public void setDoEvolveFieldOfView(boolean doEvolveFieldOfView) { this.doEvolveFieldOfView = doEvolveFieldOfView; }
    public void setDoEvolveReproductionRate(boolean doEvolveReproductionRate) { this.doEvolveReproductionRate = doEvolveReproductionRate; }

    public void setLifeDurationAverage(float lifeDurationAverage) { this.lifeDurationAverage = lifeDurationAverage; }
    public void setLifeDurationStandardDeviation(float lifeDurationStandardDeviation) { this.lifeDurationStandardDeviation = lifeDurationStandardDeviation; }

    public void setIsCannibalistic(boolean isCannibalistic) { this.isCannibalistic = isCannibalistic; }

    public String getName() { return this.name; }
    public int getStartingPopulation() { return this.startingPopulation; }

    public int getStartingSpeed() { return this.startingSpeed; }
    public float getStartingFieldOfView() { return this.startingFieldOfView; }
    public float getStartingReproductionRate() { return this.startingReproductionRate; }

    public boolean getDoEvolveSpeed() { return this.doEvolveSpeed; }
    public boolean getDoEvolveFieldOfView() { return this.doEvolveFieldOfView; }
    public boolean getDoEvolveReproductionRate() { return this.doEvolveReproductionRate; }

    public float getLifeDurationAverage() { return this.lifeDurationAverage; }
    public float getLifeDurationStandardDeviation() { return this.lifeDurationStandardDeviation; }
    public int getRandomLifeDuration() {
        Random rand = new Random();
        return (int) (rand.nextGaussian() * this.lifeDurationStandardDeviation + this.lifeDurationAverage);
    }

    public boolean getIsCannibalistic() {return this.isCannibalistic;}
}
