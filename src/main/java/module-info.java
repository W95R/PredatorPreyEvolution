module org.simulation.predatorpreyevolution {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;
    requires eu.hansolo.tilesfx;

    opens org.simulation.predatorpreyevolution to javafx.fxml;
    exports org.simulation.predatorpreyevolution;
}