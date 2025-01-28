package com.guba.app.controllers.carreras;

import com.guba.app.dao.DAOCarreras;
import com.guba.app.controllers.BaseController;
import com.guba.app.controllers.Loadable;
import com.guba.app.controllers.Paginas;
import com.guba.app.models.Carrera;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
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
    private DAOCarreras daoCarreras = new DAOCarreras();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    @FXML
    private void regresarAPanel(ActionEvent actionEvent) {
        carrera = null;
        mediador.changePane(Paginas.LIST);
    }

    @FXML
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
            daoCarreras.updateCarrera(carrera);
            carrera = null;
            mediador.changePane(Paginas.LIST);
        }
    }

    private boolean mostrarConfirmacion() {
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estás seguro de actualzar la carrera " +"?");
        return dialogConfirmacion.showAndWait().orElse(0) == 1;
    }

}
