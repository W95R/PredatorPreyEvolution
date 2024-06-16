package org.simulation.predatorpreyevolution;

public final class Plant extends Entity {
    private static int expansionRate = 20;
    private static int energyIncreaseRate = 10;
    private static int startingEnergy = 100;

    private static int startingPopulation = 2000;

    /**
     * Creates plant
     * @param startingPosition plant's starting position
     */
    public Plant(Point startingPosition) {
        super(startingPosition);
        Environment.addPlantToAdditionList(this);
        this.energy = startingEnergy;
    }

    /**
     * Increases plant's energy
     */
    @Override public void update() { this.energy += energyIncreaseRate; }

    /**
     * Creates new plants on the map based on expansion rate
     */
    public static void updatePlants() {
        for (int i = Math.min(10000 - Environment.getPlants().size(), expansionRate); i > 0; i--)
            new Plant(Environment.getSize().getRandomPointInBox());
    }

    @Override public float getChanceOfWinning(Animal animal) { return 0f; }
    @Override public float getConsumptionGainedEnergy() { return this.energy; }

    @Override public void kill() { Environment.addPlantToRemovalList(this); }

    public static void setStartingPopulation(int startingPopulation) { Plant.startingPopulation = startingPopulation; }
    public static void setExpansionRate(int expansionRate) { Plant.expansionRate = expansionRate; }
    public static void setEnergyIncreaseRate(int energyIncreaseRate) { Plant.energyIncreaseRate = energyIncreaseRate; }
    public static void setStartingEnergy(int startingEnergy) { Plant.startingEnergy = startingEnergy; }

    public static int getExpansionRate() { return Plant.expansionRate; }
    public static int getEnergyIncreaseRate() { return Plant.energyIncreaseRate; }
    public static int getStartingEnergy() { return Plant.startingEnergy; }
    public static int getStartingPopulation() { return Plant.startingPopulation; }
}
