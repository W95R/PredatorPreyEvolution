package org.simulation.predatorpreyevolution;

public class Plant extends Entity{
    private static int expansionRate = 5;
    private static int energyIncreaseRate = 10;
    private static int startingEnergy = 10;

    private static int startingPopulation = 100;

    public Plant(Point startingPosition) {
        super(startingPosition);
        Environment.addPlantToAdditionList(this);
        this.energy = startingEnergy;
    }

    @Override
    public void update() {
        super.update();
        this.energy += energyIncreaseRate;
    }

    public static void updatePlants() {
        for (int i = Math.min(10000 - Environment.getPlants().size(), expansionRate); i > 0; i--)
            new Plant(Environment.getSize().getRandomPointInBox());
        System.out.println(Environment.getPlants().size());
    }

    public static void setExpansionRate(int expansionRate) { Plant.expansionRate = expansionRate; }
    public static void setEnergyIncreaseRate(int energyIncreaseRate) { Plant.energyIncreaseRate = energyIncreaseRate; }
    public static void setStartingEnergy(int startingEnergy) { Plant.startingEnergy = startingEnergy; }
    public static void setStartingPopulation(int startingPopulation) { Plant.startingPopulation = startingPopulation; }

    public static int getExpansionRate() { return Plant.expansionRate; }
    public static int getEnergyIncreaseRate() { return Plant.energyIncreaseRate; }
    public static int getStartingEnergy() { return Plant.startingEnergy; }
    public static int getStartingPopulation() { return Plant.startingPopulation; }

    @Override public void kill() { Environment.addPlantToRemovalList(this); }
}
