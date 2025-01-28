package com.guba.app.controllers.grupos;

import com.guba.app.dao.DAOCarreras;
import com.guba.app.dao.DAOMaterias;
import com.guba.app.controllers.BaseController;
import com.guba.app.controllers.Loadable;
import com.guba.app.controllers.Paginas;
import com.guba.app.models.Carrera;
import com.guba.app.models.Materia;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.guba.app.presentation.utils.ComboCell;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;


public class AddController extends BaseController<Materia> implements Initializable, Loadable<Materia> {
    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtModalidad;

    @FXML
    private TextField txtHbca;

    @FXML
    private TextField txtHti;

    @FXML
    private TextField txtSemestre;

    @FXML
    private TextField txtClave;

    @FXML
    private TextField txtCreditos;

    @FXML
    private ComboBox<Carrera> comboCarreras;

    private Materia materia;

    private DAOCarreras daoCarreras = new DAOCarreras();
    private DAOMaterias daoMaterias = new DAOMaterias();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        comboCarreras.setCellFactory(new Callback<ListView<Carrera>, ListCell<Carrera>>() {
            @Override
            public ListCell<Carrera> call(ListView<Carrera> carreraListView) {
                return new ComboCell<Carrera>();
            }
        });
        comboCarreras.setButtonCell(new ComboCell<>());
    }


    @FXML
    private void regresarAPanel(ActionEvent actionEvent) {
        materia = null;
        mediador.changePane(Paginas.LIST);
    }

    @FXML
    private void guardar(ActionEvent event){
        if (mostrarConfirmacion()){
            daoMaterias.insertMateria(materia);
            getLista().add(materia);
            materia = null;
            mediador.changePane(Paginas.LIST);
        }
    }

    private void loadCarrerasAsync(Carrera carrera) {
        comboCarreras.setDisable(true);
        Task<List<Carrera>> task = new Task<>() {
            @Override
            protected List<Carrera> call() throws Exception {
                return daoCarreras.getAllCarreras();
            }
        };
        task.setOnSucceeded(event -> {
            try {
                comboCarreras.getItems().setAll(task.get());
                comboCarreras.getSelectionModel().select(carrera);
                comboCarreras.setDisable(false);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(task).start();
    }


    @Override
    public void loadData(Materia data) {
        materia = data;
        txtNombre.textProperty().bindBidirectional(materia.nombreProperty());
        txtModalidad.textProperty().bindBidirectional(materia.modalidadProperty());
        txtHbca.textProperty().bindBidirectional(materia.hcbaProperty());
        txtHti.textProperty().bindBidirectional(materia.htiProperty());
        txtSemestre.textProperty().bindBidirectional(materia.semestreProperty());
        txtClave.textProperty().bindBidirectional(materia.claveProperty());
        txtCreditos.textProperty().bindBidirectional(materia.creditosProperty());
        Bindings.bindBidirectional(comboCarreras.valueProperty(), materia.carreraModeloProperty());
        loadCarrerasAsync(materia.getCarreraModelo());
    }

    private boolean mostrarConfirmacion() {
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estás seguro la carrera: " +"?");
        return dialogConfirmacion.showAndWait().orElse(0) == 1;
    }

}
