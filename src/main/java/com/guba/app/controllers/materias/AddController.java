package com.guba.app.controllers.materias;

import com.guba.app.data.dao.DAOCarreras;
import com.guba.app.data.dao.DAOMaterias;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Carrera;
import com.guba.app.domain.models.Materia;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.guba.app.presentation.utils.ComboCell;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
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
    private Button backButton;
    @FXML
    private Button btnGuardar;
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

    public AddController(Mediador<Materia> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super("/materias/Add", mediador, estadoProperty, paginasProperty);
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
        // Para los campos de texto (TextField)
        txtNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                materia.setNombre(newValue);
            }
        });
        txtModalidad.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                materia.setModalidad(newValue);
            }
        });
        txtHbca.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                materia.setHcba(newValue);
            }
        });
        txtHti.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                materia.setHti(newValue);
            }
        });
        txtSemestre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                materia.setSemestre(newValue);
            }
        });
        txtClave.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                materia.setClave(newValue);
            }
        });
        txtCreditos.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                materia.setCreditos(newValue);
            }
        });

        comboCarreras.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                materia.setCarreraModelo(newValue);
            }
        });

        comboCarreras.setButtonCell(new ComboCell<>());
        backButton.setOnAction(this::regresarAPanel);
        btnGuardar.setOnAction(this::guardar);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void regresarAPanel(ActionEvent actionEvent) {
        materia = null;
        paginasProperty.set(Paginas.LIST);
    }

    private void guardar(ActionEvent event){
        if (mostrarConfirmacion()){
            mediador.guardar(materia);
            materia = null;
            paginasProperty.set(Paginas.LIST);
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

    public void loadData(Materia data) {
        materia = data;
        /*txtNombre.textProperty().bindBidirectional(materia.nombreProperty());
        txtModalidad.textProperty().bindBidirectional(materia.modalidadProperty());
        txtHbca.textProperty().bindBidirectional(materia.hcbaProperty());
        txtHti.textProperty().bindBidirectional(materia.htiProperty());
        txtSemestre.textProperty().bindBidirectional(materia.semestreProperty());
        txtClave.textProperty().bindBidirectional(materia.claveProperty());
        txtCreditos.textProperty().bindBidirectional(materia.creditosProperty());
        Bindings.bindBidirectional(comboCarreras.valueProperty(), materia.carreraModeloProperty());*/
        loadCarrerasAsync(materia.getCarreraModelo());
    }

    private boolean mostrarConfirmacion() {
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estás seguro la carrera: " +"?");
        return dialogConfirmacion.showAndWait().orElse(0) == 1;
    }

    @Override
    protected void cleanData() {
        materia = null;
        txtNombre.setText(null);
        txtModalidad.setText(null);
        txtHbca.setText(null);
        txtHti.setText(null);
        txtSemestre.setText(null);
        txtClave.setText(null);
        txtCreditos.setText(null);
        comboCarreras.setValue(null);
    }
}
