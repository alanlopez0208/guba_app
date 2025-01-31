package com.guba.app.controllers.carreras;

import com.guba.app.data.dao.DAOCarreras;
import com.guba.app.utils.BaseController;
import com.guba.app.utils.Loadable;
import com.guba.app.utils.Paginas;
import com.guba.app.domain.models.Carrera;
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


public class AddController extends BaseController<Carrera> implements Initializable, Loadable<Carrera> {

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
      carrera.nombreProperty().bindBidirectional(txtNombre.textProperty());
      carrera.modalidadProperty().bindBidirectional(txtModalidad.textProperty());
      carrera.hbcaProperty().bindBidirectional(txtHbca.textProperty());
      carrera.htiProperty().bindBidirectional(txtHti.textProperty());
      carrera.totalHorasProperty().bindBidirectional(txtTotalHoras.textProperty());
      carrera.idClaveProperty().bindBidirectional(txtClave.textProperty());
      carrera.creditosProperty().bindBidirectional(txtCreditos.textProperty());
      carrera.totalAsignaturasProperty().bindBidirectional(txtTotalAsignaturas.textProperty());
    }

    @FXML
    private void regresarAPanel(ActionEvent actionEvent) {
        carrera = null;
        paginasProperty.set(Paginas.LIST);
    }

    @FXML
    private void guardarCarrera(ActionEvent event){
        if (mostrarConfirmacion()){
            mediador.guardar(carrera);
        }
    }

    private boolean mostrarConfirmacion() {
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estás seguro la carrera: " +"?");
        return dialogConfirmacion.showAndWait().orElse(0) == 1;
    }

    @Override
    protected void cleanData() {

    }
}
