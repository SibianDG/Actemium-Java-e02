module E02.Project2.Java {
    requires javafx.graphics;
    requires javafx.controls;

    requires javafx.base;
    requires javafx.fxml;

    requires java.persistence;
    requires java.instrument;
    requires java.sql;

    requires org.mockito;
    requires mockito.junit.jupiter;
//    requires org.junit.jupiter;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.params;
    requires java.desktop;

    exports domain;
    exports domain.enums;
    exports domain.facades;
    exports domain.manager;
    exports exceptions;
    exports gui;
    exports gui.detailPanels;
    exports gui.tableViewPanels;
    exports languages;
    exports repository;
    exports start;
    exports tests;
    
    opens start to javafx.graphics, javafx.fxml;
    opens gui to javafx.graphics, javafx.fxml;
    opens gui.detailPanels to javafx.graphics, javafx.fxml;
    opens gui.tableViewPanels to javafx.graphics, javafx.fxml;
    
    opens domain;
    
    opens tests;

}