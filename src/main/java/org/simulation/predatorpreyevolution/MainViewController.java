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
import java.util.Arrays;

public class MainViewController {

    @FXML public ComboBox<String> herbivorousSpeciesComboBox;
    @FXML public ComboBox<String> carnivorousSpeciesComboBox;
    @FXML public TextField plantStartingPopulationField;
    @FXML public TextField plantExpansionRateField;
    @FXML public TextField plantEnergyIncreaseRateField;
    @FXML public TextField plantStartingEnergyField;
    @FXML public Slider simulationSpeedSlider;
    @FXML public Button runButton;
    @FXML public Button pauseButton;

    @FXML private Canvas canvas;

    @FXML public ComboBox<String> allSpeciesComboBox;
    @FXML public ScatterChart<Float, Float> speedToFOVChart;
    @FXML public ScatterChart<Float, Float> speedToVAChart;
    @FXML public ScatterChart<Float, Float> FOVToVAChart;

    private final float pointSize = 5f;

    private boolean isRunning = false;
    private boolean isPaused = false;

    private GraphicsContext canvasGraphicsContext;


    @FXML
    public void initialize() {
        this.loadPlantSettings();
        pauseButton.setDisable(true);
        simulationSpeedSlider.setValue(Environment.getSimulationFramerate());
        simulationSpeedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {Environment.setSimulationFramerate(newValue.intValue());});
        allSpeciesComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {this.updateGraphs();});
        this.canvasGraphicsContext = canvas.getGraphicsContext2D();
    }
    @FXML
    public void editHerbivorousSpecie(ActionEvent actionEvent) {
        Specie specie = Environment.getSpecieByName(this.herbivorousSpeciesComboBox.getValue(), Herbivore.class);
        if (specie != null)
            showSpecieView(specie);
    }
    @FXML
    public void addHerbivorousSpecie(ActionEvent actionEvent) {
        Specie addedSpecie = new Specie(Herbivore.class);
        this.showSpecieView(addedSpecie);
    }
    @FXML
    public void editCarnivorousSpecie(ActionEvent actionEvent) {
        Specie specie = Environment.getSpecieByName(this.carnivorousSpeciesComboBox.getValue(), Carnivore.class);
        if (specie != null)
            showSpecieView(specie);
    }
    @FXML
    public void addCarnivorousSpecie(ActionEvent actionEvent) {
        Specie addedSpecie = new Specie(Carnivore.class);
        this.showSpecieView(addedSpecie);
    }
    @FXML
    public void updatePlantSettings(ActionEvent actionEvent) {
        Plant.setStartingPopulation(Integer.parseInt(this.plantStartingPopulationField.getText()));
        Plant.setExpansionRate(Integer.parseInt(this.plantExpansionRateField.getText()));
        Plant.setEnergyIncreaseRate(Integer.parseInt(this.plantEnergyIncreaseRateField.getText()));
        Plant.setStartingEnergy(Integer.parseInt(this.plantStartingEnergyField.getText()));
    }
    @FXML
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
    @FXML
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

    /**
     * Draws plant on canvas
     * @param plant plant to draw
     */
    public void drawPlant(Plant plant) {
        this.canvasGraphicsContext.setFill(Color.rgb(0, 255, 0));
        this.canvasGraphicsContext.fillOval(plant.getPosition().x - pointSize / 2, plant.getPosition().y - pointSize / 2, pointSize, pointSize);
    }
    /**
     * Draws animal on canvas
     * @param animal animal to draw
     */
    public void drawAnimal(Animal animal) {
        this.canvasGraphicsContext.setFill(animal.getSpecie().getColor());
        this.canvasGraphicsContext.fillOval(animal.getPosition().x - pointSize / 2, animal.getPosition().y - pointSize / 2, pointSize, pointSize);
    }
    /**
     * Clears canvas
     */
    public void clearCanvas() {
        this.canvasGraphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        this.canvasGraphicsContext.setFill(Color.rgb(0, 100, 0));
        this.canvasGraphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    /**
     * Sets canvas size
     * @param size size
     */
    public void setCanvasSize(Point size) {
        canvas.setWidth(size.x);
        canvas.setHeight(size.y);
        this.clearCanvas();
    }

    /**
     * Updates specie select combo-boxes
     */
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
    /**
     * Updates graphs based on environment state
     */
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

    /**
     * Loads plant settings from Plant class
     */
    public void loadPlantSettings() {
        this.plantStartingPopulationField.setText(String.valueOf(Plant.getStartingPopulation()));
        this.plantExpansionRateField.setText(String.valueOf(Plant.getExpansionRate()));
        this.plantEnergyIncreaseRateField.setText(String.valueOf(Plant.getEnergyIncreaseRate()));
        this.plantStartingEnergyField.setText(String.valueOf(Plant.getStartingEnergy()));
    }

    /**
     * Opens specie editor window
     * @param specie edited specie
     */
    private void showSpecieView(Specie specie) {
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
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
