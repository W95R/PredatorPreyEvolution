package org.simulation.predatorpreyevolution;


import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SpecieViewController {
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

    private Stage dialogStage;
    private Specie specie;
    private boolean saveClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void loadSpecie(Specie specie) {
        this.specie = specie;

        nameField.setText(specie.getName());
        colorColorPicker.setValue(specie.getColor());
        startingPopulationField.setText(Integer.toString(specie.getStartingPopulation()));
        startingSpeedField.setText(Integer.toString(specie.getStartingSpeed()));
        startingFieldOfViewField.setText(Float.toString(specie.getStartingFieldOfView()));
        startingReproductionRateField.setText(Float.toString(specie.getStartingReproductionRate()));
        doEvolveSpeedCheckBox.setSelected(specie.getDoEvolveSpeed());
        doEvolveFieldOfViewCheckBox.setSelected(specie.getDoEvolveFieldOfView());
        doEvolveReproductionRateCheckBox.setSelected(specie.getDoEvolveReproductionRate());
        lifeDurationAverageField.setText(Float.toString(specie.getLifeDurationAverage()));
        lifeDurationStandardDeviationField.setText(Float.toString(specie.getLifeDurationStandardDeviation()));
        isCannibalisticCheckBox.setSelected(specie.getIsCannibalistic());
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void saveSpecie() {
        specie.setName(nameField.getText());
        specie.setColor(colorColorPicker.getValue());
        specie.setStartingPopulation(Integer.parseInt(startingPopulationField.getText()));
        specie.setStartingSpeed(Integer.parseInt(startingSpeedField.getText()));
        specie.setStartingFieldOfView(Float.parseFloat(startingFieldOfViewField.getText()));
        specie.setStartingReproductionRate(Float.parseFloat(startingReproductionRateField.getText()));
        specie.setDoEvolveSpeed(doEvolveSpeedCheckBox.isSelected());
        specie.setDoEvolveFieldOfView(doEvolveFieldOfViewCheckBox.isSelected());
        specie.setDoEvolveReproductionRate(doEvolveReproductionRateCheckBox.isSelected());
        specie.setLifeDurationAverage(Float.parseFloat(lifeDurationAverageField.getText()));
        specie.setLifeDurationStandardDeviation(Float.parseFloat(lifeDurationStandardDeviationField.getText()));
        specie.setIsCannibalistic(isCannibalisticCheckBox.isSelected());
    }
}
