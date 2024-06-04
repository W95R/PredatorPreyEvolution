package org.simulation.predatorpreyevolution;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Main extends Application {

    private MainViewController testViewController;

//    public static void main(String[] args) {
//        System.out.println("Hello world!");
//        Plant.startingPopulation = 10;
//        Environment env = new Environment();
//        env.setSize(new Point(10f, 10f));
//        env.setup();
//        List<Plant> plants = env.getPlants();
//        for (Plant plant : plants) {
//            System.out.println("Plant: " + plant.position.x + "X" + plant.position.y);
//        }
//    }

    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("PPE");
//        stage.setScene(scene);
//        stage.show();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        this.testViewController = fxmlLoader.<MainViewController>getController();
        stage.setTitle("PPE");
        stage.setScene(scene);
        stage.show();
        Plant.startingPopulation = 10;
        Environment env = new Environment();
        env.setSize(new Point(1000f, 1000f));
        this.testViewController.setCanvasSize(env.getSize());
        env.setup();
        List<Plant> plants = env.getPlants();
        for (Plant plant : plants) {
            this.testViewController.drawPlant(plant);
        }
    }

    public static void main(String[] args) {
        launch();
    }

    public MainViewController getTestViewController() {
        return testViewController;
    }
}