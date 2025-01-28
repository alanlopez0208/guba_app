module com.guba.app {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.material;
    requires org.kordamp.ikonli.materialdesign;
    requires org.kordamp.ikonli.bootstrapicons;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;


    requires webcam.capture;
    requires org.slf4j;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.dlsc.gemsfx;
    requires n2w;

    requires org.controlsfx.controls;
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;
    requires java.net.http;
    requires org.apache.commons.net;
    requires org.apache.poi.ooxml;

    opens com.guba.app to javafx.fxml;
    opens com.guba.app.presentation.componets to javafx.fxml;
    opens com.guba.app.controllers to javafx.fxml;
    opens com.guba.app.controllers.estudiantes to javafx.fxml;
    opens com.guba.app.controllers.carreras to javafx.fxml;
    opens com.guba.app.controllers.materias to javafx.fxml;
    opens com.guba.app.controllers.grupos to javafx.fxml;
    opens com.guba.app.controllers.maestro to javafx.fxml;
    opens com.guba.app.controllers.personal to javafx.fxml;
    opens com.guba.app.controllers.pago_alumnos to javafx.fxml;
    opens com.guba.app.controllers.pagos_docentes to javafx.fxml;
    opens com.guba.app.controllers.web to javafx.fxml;
    opens com.guba.app.controllers.cursos to javafx.fxml;
    opens com.guba.app.controllers.configuracion to javafx.fxml;
    opens com.guba.app.presentation.dialogs to javafx.fxml;
    opens com.guba.app.models to javafx.fxml;


    exports com.guba.app;
    exports com.guba.app.presentation.componets;
    exports com.guba.app.models;
    exports com.guba.app.presentation.dialogs;
    exports com.guba.app.dto;
}