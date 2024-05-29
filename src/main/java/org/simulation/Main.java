package org.simulation;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Plant.startingPopulation = 10;
        Environment env = new Environment();
        env.setSize(new Point(10f, 10f));
        env.setup();
        List<Plant> plants = env.getPlants();
        for (Plant plant : plants) {
            System.out.println("Plant: " + plant.position.x + "X" + plant.position.y);
        }
    }
}