package org.simulation.predatorpreyevolution;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    /**
     * Sets up window and starting simulation parameters
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException .
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Environment.setMainViewController(fxmlLoader.getController());

        Specie defaultHerbivoreSpecie = new Specie(Herbivore.class);
        defaultHerbivoreSpecie.setName("Rabbit");
        defaultHerbivoreSpecie.setColor(Color.rgb(150, 150, 150));
        defaultHerbivoreSpecie.setStartingPopulation(500);
        defaultHerbivoreSpecie.setStartingSpeed(4f);
        defaultHerbivoreSpecie.setStartingFieldOfView(6f);
        Environment.addSpecie(defaultHerbivoreSpecie);

        Specie defaultCarnivoreSpecie = new Specie(Carnivore.class);
        defaultCarnivoreSpecie.setName("Wolf");
        defaultCarnivoreSpecie.setColor(Color.rgb(0, 0, 0));
        defaultCarnivoreSpecie.setStartingPopulation(10);
        defaultCarnivoreSpecie.setStartingSpeed(5.5f);
        defaultCarnivoreSpecie.setStartingViewArea(7000f);
        Environment.addSpecie(defaultCarnivoreSpecie);

        stage.setTitle("Predator Prey Evolution");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        Environment.setSize(new Point(1000f, 1000f));
    }

    /**
     * Launches and kills application
     * @param args unused
     */
    public static void main(String[] args) {
        launch();
        Environment.stop();
        System.exit(0);
    }
}