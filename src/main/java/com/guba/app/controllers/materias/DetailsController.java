package com.guba.app.controllers.materias;

import com.guba.app.data.dao.DAOCarreras;
import com.guba.app.utils.BaseController;
import com.guba.app.utils.Loadable;
import com.guba.app.utils.Paginas;
import com.guba.app.domain.models.Carrera;
import com.guba.app.domain.models.Materia;
import com.guba.app.presentation.utils.ComboCell;
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

public class DetailsController extends BaseController<Materia> implements Initializable, Loadable<Materia> {
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
        loadCarrerasAsync();
    }


    private void loadCarrerasAsync() {
        Task<List<Carrera>> task = new Task<>() {
            @Override
            protected List<Carrera> call() throws Exception {
                return new DAOCarreras().getAllCarreras();
            }
        };

        task.setOnSucceeded(event -> {
            try {
                comboCarreras.getItems().addAll(task.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        new Thread(task).start();
    }


    @FXML
    private void regresarAPanel(ActionEvent actionEvent) {
        paginasProperty.set(Paginas.LIST);
    }


    @Override
    public void loadData(Materia data) {
        materia = data;
        txtNombre.textProperty().bind(materia.nombreProperty());
        txtModalidad.textProperty().bind(materia.modalidadProperty());
        txtHbca.textProperty().bind(materia.hcbaProperty());
        txtHti.textProperty().bind(materia.htiProperty());
        txtSemestre.textProperty().bind(materia.semestreProperty());
        txtClave.textProperty().bind(materia.claveProperty());
        txtCreditos.textProperty().bind(materia.creditosProperty());
        comboCarreras.getSelectionModel().select(materia.getCarreraModelo());
    }

    @Override
    protected void cleanData() {

    }
}
