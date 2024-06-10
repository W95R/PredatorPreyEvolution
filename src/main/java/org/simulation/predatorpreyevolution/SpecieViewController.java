package org.simulation.predatorpreyevolution;


import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SpecieViewController {
    @FXML
    public Label savedStatus;
    @FXML
    private TextField nameField;
    @FXML
    private ColorPicker colorColorPicker;
    @FXML
    private TextField startingPopulationField;
    @FXML
    private TextField startingSpeedField;
    @FXML
    private TextField startingFieldOfViewField;
    @FXML
    private TextField startingReproductionRateField;
    @FXML
    private CheckBox doEvolveSpeedCheckBox;
    @FXML
    private CheckBox doEvolveFieldOfViewCheckBox;
    @FXML
    private CheckBox doEvolveReproductionRateCheckBox;
    @FXML
    private TextField lifeDurationAverageField;
    @FXML
    private TextField lifeDurationStandardDeviationField;
    @FXML
    private CheckBox isCannibalisticCheckBox;

    private Specie specie;
    private boolean isInEnvironment;

    /**
     * Initializes specie object with provided Specie
     * @param specie Specie that is edited
     */
    void initData(Specie specie) {
        this.specie = specie;
    }

    /**
     * Initializes starting values for fields and creates listeners for TextField value change
     */
    @FXML
    public void initialize() {
        this.isInEnvironment = specie.isAddedToEnvironment();
        if (specie.getAnimalType() == Herbivore.class)
            isCannibalisticCheckBox.setDisable(true);
        this.loadSpecie();
        this.setSavedStatus(this.isInEnvironment);

        ChangeListener<String> textFieldChangeListener = (obs, oldText, newText) -> this.setSavedStatus(false);
        this.nameField.textProperty().addListener(textFieldChangeListener);
        this.startingPopulationField.textProperty().addListener(textFieldChangeListener);
        this.startingSpeedField.textProperty().addListener(textFieldChangeListener);
        this.startingFieldOfViewField.textProperty().addListener(textFieldChangeListener);
        this.startingReproductionRateField.textProperty().addListener(textFieldChangeListener);
        this.lifeDurationAverageField.textProperty().addListener(textFieldChangeListener);
        this.lifeDurationStandardDeviationField.textProperty().addListener(textFieldChangeListener);
    }

    /**
     * Loads Specie data from object into input fields
     */
    public void loadSpecie() {
        this.nameField.setText(specie.getName());
        this.colorColorPicker.setValue(specie.getColor());
        this.startingPopulationField.setText(Integer.toString(specie.getStartingPopulation()));
        this.startingSpeedField.setText(Integer.toString(specie.getStartingSpeed()));
        this.startingFieldOfViewField.setText(Float.toString(specie.getStartingFieldOfView()));
        this.startingReproductionRateField.setText(Float.toString(specie.getStartingReproductionRate()));
        this.doEvolveSpeedCheckBox.setSelected(specie.getDoEvolveSpeed());
        this.doEvolveFieldOfViewCheckBox.setSelected(specie.getDoEvolveFieldOfView());
        this.doEvolveReproductionRateCheckBox.setSelected(specie.getDoEvolveReproductionRate());
        this.lifeDurationAverageField.setText(Float.toString(specie.getLifeDurationAverage()));
        this.lifeDurationStandardDeviationField.setText(Float.toString(specie.getLifeDurationStandardDeviation()));
        this.isCannibalisticCheckBox.setSelected(specie.getIsCannibalistic());
    }

    /**
     * Saves values from input fields to Specie object, and adds specie to Environment if needed
     */
    @FXML
    private void saveSpecie() {
        this.specie.setName(this.nameField.getText());
        this.specie.setColor(this.colorColorPicker.getValue());
        this.specie.setStartingPopulation(Integer.parseInt(this.startingPopulationField.getText()));
        this.specie.setStartingSpeed(Integer.parseInt(this.startingSpeedField.getText()));
        this.specie.setStartingFieldOfView(Float.parseFloat(this.startingFieldOfViewField.getText()));
        this.specie.setStartingReproductionRate(Float.parseFloat(this.startingReproductionRateField.getText()));
        this.specie.setDoEvolveSpeed(this.doEvolveSpeedCheckBox.isSelected());
        this.specie.setDoEvolveFieldOfView(this.doEvolveFieldOfViewCheckBox.isSelected());
        this.specie.setDoEvolveReproductionRate(this.doEvolveReproductionRateCheckBox.isSelected());
        this.specie.setLifeDurationAverage(Float.parseFloat(this.lifeDurationAverageField.getText()));
        this.specie.setLifeDurationStandardDeviation(Float.parseFloat(this.lifeDurationStandardDeviationField.getText()));
        this.specie.setIsCannibalistic(this.isCannibalisticCheckBox.isSelected());
        if (!this.isInEnvironment) {
            Environment.addSpecie(this.specie);
            this.isInEnvironment = true;
        }
        Environment.getMainViewController().updateSpecies();
        this.setSavedStatus(true);
    }

    /**
     * Deletes Specie from Environment and closes the Specie View window
     */
    @FXML
    private void deleteSpecie() {
        Environment.removeSpecie(this.specie);
        Environment.getMainViewController().updateSpecies();
        ((Stage) this.nameField.getScene().getWindow()).close();
    }

    /**
     * Displays saved status when needed
     * @param isSaved are the Specie's params saved
     */
    private void setSavedStatus(boolean isSaved) {
        this.savedStatus.setText(isSaved ? "Saved" : "Pending");
        this.savedStatus.setStyle(isSaved ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }

    /**
     * Invokes when CheckBox or ColorPicker value is changed, and sets saved status to false
     */
    @FXML
    public void valueChanged() {
        this.setSavedStatus(false);
    }
}
