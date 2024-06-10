package org.simulation.predatorpreyevolution;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Environment.setMainViewController(fxmlLoader.<MainViewController>getController());
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