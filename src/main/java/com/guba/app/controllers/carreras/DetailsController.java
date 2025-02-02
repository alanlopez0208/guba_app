package com.guba.app.controllers.carreras;

import com.guba.app.utils.*;
import com.guba.app.domain.models.Carrera;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailsController extends BaseController<Carrera> implements Initializable, Loadable<Carrera> {

    @FXML
    private Button backButton;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtModalidad;
    @FXML
    private TextField txtHbca;
    @FXML
    private TextField txtHti;
    @FXML
    private TextField txtTotalHoras;
    @FXML
    private TextField txtClave;
    @FXML
    private TextField txtCreditos;
    @FXML
    private TextField txtTotalAsignaturas;

    private Carrera carrera;

    public DetailsController(Mediador<Carrera> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super("/carreras/Details", mediador, estadoProperty, paginasProperty);
        txtTotalHoras.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")){
                return change;
            }
            return null;
        }));
        txtTotalAsignaturas.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")){
                return change;
            }
            return null;
        }));
        txtCreditos.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")){
                return change;
            }
            return null;
        }));
        txtHbca.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")){
                return change;
            }
            return null;
        }));
        txtHti.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")){
                return change;
            }
            return null;
        }));
        backButton.setOnAction(this::regresarAPanel);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    private void regresarAPanel(ActionEvent actionEvent) {
        paginasProperty.setValue(Paginas.LIST);
    }

    @Override
    public void loadData(Carrera data) {
        carrera = data;
        txtNombre.textProperty().bind(carrera.nombreProperty());
        txtModalidad.textProperty().bind(carrera.modalidadProperty());
        txtHbca.textProperty().bind(carrera.hbcaProperty());
        txtHti.textProperty().bind(carrera.htiProperty());
        txtTotalHoras.textProperty().bind(carrera.totalHorasProperty());
        txtClave.textProperty().bind(carrera.idClaveProperty());
        txtCreditos.textProperty().bind(carrera.creditosProperty());
        txtTotalAsignaturas.textProperty().bind(carrera.totalAsignaturasProperty());
    }

    @Override
    protected void cleanData() {
        carrera = null;
    }
}
