package com.guba.app.controllers.carreras;

import com.guba.app.data.dao.DAOCarreras;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Carrera;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.net.URL;
import java.util.ResourceBundle;

public class EditController extends BaseController<Carrera> implements Initializable, Loadable<Carrera> {

    @FXML
    private Button backButton;
    @FXML
    private Button btnActualizar;
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
    @FXML
    private Label titulo;
    private Carrera carrera;

    public EditController(Mediador<Carrera> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super("/carreras/Edit", mediador, estadoProperty, paginasProperty);
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
        btnActualizar.setOnAction(this::actualizarCarrera);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void loadData(Carrera data) {
        this.carrera = data;
        txtNombre.setText(carrera.getNombre());
        txtModalidad.setText(carrera.getModalidad());
        txtHbca.setText(carrera.getHbca());
        txtHti.setText(carrera.getHti());
        txtTotalHoras.setText(carrera.getTotalHoras());
        txtClave.setText(carrera.getIdClave());
        txtCreditos.setText(carrera.getCreditos());
        txtTotalAsignaturas.setText(carrera.getTotalHoras());
    }


    private void regresarAPanel(ActionEvent actionEvent) {
        carrera = null;
        paginasProperty.setValue(Paginas.LIST);
    }


    private void actualizarCarrera(ActionEvent event){
        if (mostrarConfirmacion()){
            carrera.setNombre(txtNombre.getText());
            carrera.setModalidad(txtModalidad.getText());
            carrera.setHbca(txtHbca.getText());
            carrera.setHti(txtHti.getText());
            carrera.setTotalHoras(txtTotalHoras.getText());
            carrera.setIdClave(txtClave.getText());
            carrera.setCreditos(txtCreditos.getText());
            carrera.setTotalAsignaturas(txtTotalAsignaturas.getText());
            mediador.actualizar(carrera);
            carrera = null;
            paginasProperty.setValue(Paginas.LIST);
        }
    }

    private boolean mostrarConfirmacion() {
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estás seguro de actualzar la carrera " +"?");
        return dialogConfirmacion.showAndWait().orElse(0) == 1;
    }

    @Override
    protected void cleanData() {
        carrera = null;
        txtNombre.setText(null);
        txtModalidad.setText(null);
        txtHbca.setText(null);
        txtHti.setText(null);
        txtTotalHoras.setText(null);
        txtClave.setText(null);
        txtCreditos.setText(null);
        txtTotalAsignaturas.setText(null);
    }
}
