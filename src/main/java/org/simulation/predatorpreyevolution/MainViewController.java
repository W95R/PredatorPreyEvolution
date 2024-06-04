package org.simulation.predatorpreyevolution;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class MainViewController {

    private float pointSize = 15f;

    @FXML
    private Canvas canvas;
    @FXML
    private TextField pointSizeField;
    @FXML
    private ColorPicker colorPicker;

    @FXML
    public void initialize() {
        this.clearCanvas();
            // GraphicsContext gc = canvas.getGraphicsContext2D();
    //        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
    //            // double size = Double.parseDouble(pointSizeField.getText());
    //            Color color = colorPicker.getValue();
    //            gc.setFill(color);
    //            gc.fillOval(event.getX() - pointSize / 2, event.getY() - pointSize / 2, pointSize, pointSize);
    //        });
    }

    public void drawPlant(Plant plant) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.rgb(0, 255, 0));
        gc.fillOval(plant.getPosition().x - pointSize / 2, plant.getPosition().y - pointSize / 2, pointSize, pointSize);
    }

    public void drawAnimal(Animal animal) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(animal.getSpecie().getColor());
        gc.fillOval(animal.getPosition().x - pointSize / 2, animal.getPosition().y - pointSize / 2, pointSize, pointSize);
    }

    @FXML
    private void clearCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.rgb(0, 100, 0));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void setCanvasSize(Point size) {
        canvas.setWidth(size.x);
        canvas.setHeight(size.y);
        this.clearCanvas();
    }
}
