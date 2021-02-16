
module E02.Project2.Java {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.fxml;

    requires org.junit.jupiter.api;
    requires org.mockito;
    requires mockito.junit.jupiter;
    requires org.junit.jupiter.params;

    requires java.persistence;

    opens start to javafx.graphics, javafx.fxml;
    opens gui to javafx.graphics, javafx.fxml;
}