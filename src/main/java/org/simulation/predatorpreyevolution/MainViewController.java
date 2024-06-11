package org.simulation.predatorpreyevolution;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewController {

    public ComboBox<String> herbivorousSpeciesComboBox;
    public ComboBox<String> carnivorousSpeciesComboBox;
    public ComboBox<String> allSpeciesComboBox;

    public TextField plantStartingPopulationField;
    public TextField plantExpansionRateField;
    public TextField plantEnergyIncreaseRateField;
    public TextField plantStartingEnergyField;
    public Button runButton;
    public Button pauseButton;
    public Slider simulationSpeedSlider;
    public Label speedLabel;
    public Label fovLabel;
    public Label vaLabel;
    public ScatterChart<Float, Float> speedToFOVChart;
    public ScatterChart<Float, Float> speedToVAChart;
    public ScatterChart<Float, Float> FOVToVAChart;

    private float pointSize = 5f;

    private boolean isRunning = false;
    private boolean isPaused = false;

    private GraphicsContext canvasGraphicsContext;

    @FXML
    private Canvas canvas;
    @FXML
    private TextField pointSizeField;
    @FXML
    private ColorPicker colorPicker;

    @FXML
    public void initialize() {
        this.loadPlantSettings();
        pauseButton.setDisable(true);
        simulationSpeedSlider.setValue(Environment.getSimulationFramerate());
        simulationSpeedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {Environment.setSimulationFramerate(newValue.intValue());});
        allSpeciesComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {this.updateGraphs();});
        this.canvasGraphicsContext = canvas.getGraphicsContext2D();
    }

    public void drawPlant(Plant plant) {
        Point p = new Point(500f, 500f);
        this.canvasGraphicsContext.setFill(Color.rgb(0, 255, 0));
        this.canvasGraphicsContext.fillOval(plant.getPosition().x - pointSize / 2, plant.getPosition().y - pointSize / 2, pointSize, pointSize);
    }

    public void drawAnimal(Animal animal) {
        this.canvasGraphicsContext.setFill(animal.getSpecie().getColor());
        this.canvasGraphicsContext.fillOval(animal.getPosition().x - pointSize / 2, animal.getPosition().y - pointSize / 2, pointSize, pointSize);
    }

    public void clearCanvas() {
        this.canvasGraphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        this.canvasGraphicsContext.setFill(Color.rgb(0, 100, 0));
        this.canvasGraphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void setCanvasSize(Point size) {
        canvas.setWidth(size.x);
        canvas.setHeight(size.y);
        this.clearCanvas();
    }

    public void updateSpecies() {
        String herbivorousSpeciesValue = this.herbivorousSpeciesComboBox.getValue();
        String carnivorousSpeciesValue = this.carnivorousSpeciesComboBox.getValue();
        String allSpeciesValue = this.allSpeciesComboBox.getValue();
        herbivorousSpeciesComboBox.getItems().clear();
        carnivorousSpeciesComboBox.getItems().clear();
        allSpeciesComboBox.getItems().clear();
        for (Specie specie: Environment.getHerbivorousSpecies())
            herbivorousSpeciesComboBox.getItems().add(specie.getName());
        for (Specie specie: Environment.getCarnivorousSpecies())
            carnivorousSpeciesComboBox.getItems().add(specie.getName());
        allSpeciesComboBox.getItems().addAll(herbivorousSpeciesComboBox.getItems());
        allSpeciesComboBox.getItems().addAll(carnivorousSpeciesComboBox.getItems());
        herbivorousSpeciesComboBox.setValue(herbivorousSpeciesValue);
        carnivorousSpeciesComboBox.setValue(carnivorousSpeciesValue);
        allSpeciesComboBox.setValue(allSpeciesValue);
        if (herbivorousSpeciesValue == null)
            herbivorousSpeciesComboBox.getSelectionModel().select(0);
        if (carnivorousSpeciesValue == null)
            carnivorousSpeciesComboBox.getSelectionModel().select(0);
        if (allSpeciesValue == null)
            allSpeciesComboBox.getSelectionModel().select(0);
    }

    public void addHerbivorousSpecie(ActionEvent actionEvent) {
        Specie addedSpecie = new Specie(Herbivore.class);
        this.showSpecieView(actionEvent, addedSpecie);
    }

    public void addCarnivorousSpecie(ActionEvent actionEvent) {
        Specie addedSpecie = new Specie(Carnivore.class);
        this.showSpecieView(actionEvent, addedSpecie);
    }

    public void editHerbivorousSpecie(ActionEvent actionEvent) {
        Specie specie = Environment.getSpecieByName(this.herbivorousSpeciesComboBox.getValue(), Herbivore.class);
        if (specie != null)
            showSpecieView(actionEvent, specie);
    }

    public void editCarnivorousSpecie(ActionEvent actionEvent) {
        Specie specie = Environment.getSpecieByName(this.carnivorousSpeciesComboBox.getValue(), Carnivore.class);
        if (specie != null)
            showSpecieView(actionEvent, specie);
    }

    private void showSpecieView(ActionEvent actionEvent, Specie specie) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("specie-view.fxml"));
            SpecieViewController specieViewController = new SpecieViewController();
            specieViewController.initData(specie);
            fxmlLoader.setController(specieViewController);
            Stage stage = new Stage();
            stage.setTitle(specie.getAnimalType() == Carnivore.class ? "Carnivorous" : "Herbivorous" + " specie editor");
            stage.setScene(new Scene(fxmlLoader.load(), 450, 450));
            stage.show();
        }
        catch (IOException e) {
            return;
        }
    }

    public void updatePlantSettings(ActionEvent actionEvent) {
        Plant.setStartingPopulation(Integer.parseInt(this.plantStartingPopulationField.getText()));
        Plant.setExpansionRate(Integer.parseInt(this.plantExpansionRateField.getText()));
        Plant.setEnergyIncreaseRate(Integer.parseInt(this.plantEnergyIncreaseRateField.getText()));
        Plant.setStartingEnergy(Integer.parseInt(this.plantStartingEnergyField.getText()));
    }

    public void loadPlantSettings() {
        this.plantStartingPopulationField.setText(String.valueOf(Plant.getStartingPopulation()));
        this.plantExpansionRateField.setText(String.valueOf(Plant.getExpansionRate()));
        this.plantEnergyIncreaseRateField.setText(String.valueOf(Plant.getEnergyIncreaseRate()));
        this.plantStartingEnergyField.setText(String.valueOf(Plant.getStartingEnergy()));
    }

    public void runSimulation(ActionEvent actionEvent) {
        if (!isRunning) {
            this.isRunning = true;
            this.runButton.setText("Reset");
            this.isPaused = false;
            this.pauseButton.setDisable(false);
            this.pauseButton.setText("Pause");
            Environment.setup();
            Environment.run();
            return;
        }
        this.isRunning = false;
        this.runButton.setText("Run");
        this.isPaused = false;
        this.pauseButton.setDisable(true);
        this.pauseButton.setText("Pause");
        Environment.stop();
        this.clearCanvas();
    }

    public void pauseSimulation(ActionEvent actionEvent) {
        if (!isPaused) {
            this.isPaused = true;
            this.pauseButton.setText("Unpause");
            Environment.stop();
            return;
        }
        this.isPaused = false;
        this.pauseButton.setText("Pause");
        Environment.run();
    }

    public void forceClear(ActionEvent actionEvent) {
        clearCanvas();
    }

    public void updateGraphs() {
        this.speedToFOVChart.getData().clear();
        this.speedToVAChart.getData().clear();
        this.FOVToVAChart.getData().clear();
        if (allSpeciesComboBox.getValue() == null || allSpeciesComboBox.getValue().isEmpty()) return;
        Specie specie = Environment.getSpecieByName(this.allSpeciesComboBox.getValue(), Herbivore.class);
        if (specie == null) {
            specie = Environment.getSpecieByName(this.allSpeciesComboBox.getValue(), Carnivore.class);
            if (specie == null) return;
        }
        XYChart.Series<Float, Float> speedToFOVSeries = new XYChart.Series<>();
        XYChart.Series<Float, Float> speedToVASeries = new XYChart.Series<>();
        XYChart.Series<Float, Float> FOVToVASeries = new XYChart.Series<>();

        for (Animal animal : Environment.getAnimals()) {
            if (!animal.getSpecie().equals(specie)) continue;
            speedToFOVSeries.getData().add(new XYChart.Data<>(animal.speed, animal.fieldOfView));
            speedToVASeries.getData().add(new XYChart.Data<>(animal.speed, animal.viewArea));
            FOVToVASeries.getData().add(new XYChart.Data<>(animal.fieldOfView, animal.viewArea));
        }
        this.speedToFOVChart.getData().add(speedToFOVSeries);
        this.speedToVAChart.getData().add(speedToVASeries);
        this.FOVToVAChart.getData().add(FOVToVASeries);
    }

    public void setSpeedLabel(float speed) { speedLabel.setText(String.valueOf(speed)); }
    public void setFOVLabel(float FOV) { fovLabel.setText(String.valueOf(FOV)); }
    public void setVaLabel(float VA) { vaLabel.setText(String.valueOf(VA)); }
}
