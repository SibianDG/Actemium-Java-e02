module E02.Project2.Java {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.fxml;
    requires java.logging;

    opens start to javafx.graphics, javafx.fxml;
    opens gui to javafx.graphics, javafx.fxml;
}