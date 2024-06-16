module org.simulation.predatorpreyevolution {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.simulation.predatorpreyevolution to javafx.fxml;
    exports org.simulation.predatorpreyevolution;
}