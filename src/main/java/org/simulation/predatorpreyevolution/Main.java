package org.simulation.predatorpreyevolution;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Environment.setMainViewController(fxmlLoader.<MainViewController>getController());

        Specie defaultHerbivoreSpecie = new Specie(Herbivore.class);
        defaultHerbivoreSpecie.setName("Rabbit");
        defaultHerbivoreSpecie.setColor(Color.rgb(150, 150, 150));
        defaultHerbivoreSpecie.setStartingPopulation(200);
        defaultHerbivoreSpecie.setStartingFieldOfView(6f);
        Environment.addSpecie(defaultHerbivoreSpecie);

        Specie defaultCarnivoreSpecie = new Specie(Carnivore.class);
        defaultCarnivoreSpecie.setName("Wolf");
        defaultCarnivoreSpecie.setColor(Color.rgb(0, 0, 0));
        defaultCarnivoreSpecie.setStartingPopulation(50);
        Environment.addSpecie(defaultCarnivoreSpecie);

        stage.setTitle("PPE");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        Environment.setSize(new Point(1000f, 1000f));
    }

    public static void main(String[] args) {
        launch();
        Environment.stop();
        System.exit(0);
    }
}