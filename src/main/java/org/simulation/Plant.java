package org.simulation;

public class Plant extends Entity{
    protected static int expansionRate;
    protected static int energyIncreaseRate;
    protected static int startingEnergy;

    protected static int startingPopulation;

    public Plant(Environment environment, Point startingPosition) {
        super(environment, startingPosition);
        this.environment.addPlant(this);
        this.energy = startingEnergy;
    }

    @Override
    public void update() {
        super.update();
        this.energy += energyIncreaseRate;
    }

    public static void setExpansionRate(int expansionRate) { Plant.expansionRate = expansionRate; }
    public static void setEnergyIncreaseRate(int energyIncreaseRate) { Plant.energyIncreaseRate = energyIncreaseRate; }
    public static void setStartingEnergy(int startingEnergy) { Plant.startingEnergy = startingEnergy; }
    public static void setStartingPopulation(int startingPopulation) { Plant.startingPopulation = startingPopulation; }

    public static int getExpansionRate() { return Plant.expansionRate; }
    public static int getEnergyIncreaseRate() { return Plant.energyIncreaseRate; }
    public static int getStartingEnergy() { return Plant.startingEnergy; }
    public static int getStartingPopulation() { return Plant.startingPopulation; }

    @Override public void kill() { this.environment.removePlant(this); }
}
