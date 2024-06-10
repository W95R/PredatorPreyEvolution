package org.simulation.predatorpreyevolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;

public class Specie {
    private Class<? extends Animal> animalType;
    private String name;
    private Color color;
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
        this.color = Color.RED;
        this.startingPopulation = 0;
        this.startingSpeed = 0;
        this.startingFieldOfView = 0;
        this.startingReproductionRate = 0;
        this.doEvolveSpeed = false;
        this.doEvolveFieldOfView = false;
        this.doEvolveReproductionRate = false;
        this.lifeDurationAverage = 0;
        this.lifeDurationStandardDeviation = 0;
        this.isCannibalistic = false;
    }

    public void setName(String name) {
        if (!isNameUnique(name))
            throw new RuntimeException("Specie name must be unique and not null");
        this.name = name;
    }
    public void setColor(Color color) {
        this.color = color;
    }
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


    public Class<? extends Animal> getAnimalType() { return animalType; }
    public String getName() { return this.name; }
    public Color getColor() { return color; }
    public int getStartingPopulation() { return this.startingPopulation; }

    public int getStartingSpeed() { return this.startingSpeed; }
    public float getStartingFieldOfView() { return this.startingFieldOfView; }
    public float getStartingReproductionRate() { return this.startingReproductionRate; }

    public boolean getDoEvolveSpeed() { return this.doEvolveSpeed; }
    public boolean getDoEvolveFieldOfView() { return this.doEvolveFieldOfView; }
    public boolean getDoEvolveReproductionRate() { return this.doEvolveReproductionRate; }

    public float getLifeDurationAverage() { return this.lifeDurationAverage; }
    public float getLifeDurationStandardDeviation() { return this.lifeDurationStandardDeviation; }

    public boolean getIsCannibalistic() {return this.isCannibalistic;}


    public int getRandomLifeDuration() {
        Random rand = new Random();
        return (int) (rand.nextGaussian() * this.lifeDurationStandardDeviation + this.lifeDurationAverage);
    }

    public List<Specie> getOtherSpecies() {
        if (this.animalType == Herbivore.class)
            return Environment.getHerbivorousSpecies();
        if (this.animalType == Carnivore.class)
            return Environment.getCarnivorousSpecies();
        return new ArrayList<Specie>();
    }

    public boolean isAddedToEnvironment() {
        if (this.name == null) return false;
        for (Specie specie: this.getOtherSpecies())
            if (this.name.equals(specie.getName()))
                return true;
        return false;
    }

    public boolean isNameUnique(String tested_name) {
        System.out.println(this.name);
        if (tested_name == null) return false;
        if (tested_name.isEmpty()) return false;
        for (Specie specie: this.getOtherSpecies())
            if (this != specie && specie.getName().equals(tested_name))
                return false;
        return true;
    }
    public boolean isNameNull() { return this.name == null; }
}
