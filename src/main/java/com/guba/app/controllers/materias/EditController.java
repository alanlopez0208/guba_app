package com.guba.app.controllers.materias;

import com.guba.app.data.dao.DAOCarreras;
import com.guba.app.data.dao.DAOMaterias;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Carrera;
import com.guba.app.domain.models.Materia;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.guba.app.presentation.utils.ComboCell;
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

public class EditController extends BaseController<Materia> implements Initializable, Loadable<Materia> {

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
    private TextField txtSemestre;
    @FXML
    private TextField txtClave;
    @FXML
    private TextField txtCreditos;
    @FXML
    private ComboBox<Carrera> comboCarreras;

    private Materia materia;

    private DAOCarreras daoCarreras = new DAOCarreras();


    public EditController(Mediador<Materia> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super("/materias/Edit", mediador, estadoProperty, paginasProperty);
    }

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
        backButton.setOnAction(this::regresarAPanel);
        btnActualizar.setOnAction(this::actualizar);
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
                comboCarreras.setCellFactory(new Callback<ListView<Carrera>, ListCell<Carrera>>() {
                    @Override
                    public ListCell<Carrera> call(ListView<Carrera> carreraListView) {
                        return new ComboCell<Carrera>();
                    }
                });
                comboCarreras.setButtonCell(new ComboCell<Carrera>());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(task).start();
    }

    @Override
    public void loadData(Materia data) {
        materia = data;
        txtNombre.setText(materia.getNombre());
        txtModalidad.setText(materia.getModalidad());
        txtHbca.setText(materia.getHcba());
        txtHti.setText(materia.getHti());
        txtSemestre.setText(materia.getSemestre());
        txtClave.setText(materia.getClave());
        txtCreditos.setText(materia.getCreditos());
        comboCarreras.getSelectionModel().select(materia.getCarreraModelo());
        loadCarrerasAsync(materia.getCarreraModelo());
    }

    private void regresarAPanel(ActionEvent actionEvent) {
        materia= null;
        paginasProperty.set(Paginas.LIST);
    }

    private void actualizar(ActionEvent event){
        if (mostrarConfirmacion()){
            materia.setNombre(txtNombre.getText());
            materia.setModalidad(txtModalidad.getText());
            materia.setHcba(txtHbca.getText());
            materia.setHti(txtHti.getText());
            materia.setSemestre(txtSemestre.getText());
            materia.setClave(txtClave.getText());
            materia.setCreditos(txtCreditos.getText());
            materia.setCarreraModelo(comboCarreras.getSelectionModel().getSelectedItem());
            mediador.actualizar(materia);
            paginasProperty.set(Paginas.LIST);
            materia = null;
        }
    }

    private boolean mostrarConfirmacion() {
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estás seguro de actualzar la carrera " +"?");
        return dialogConfirmacion.showAndWait().orElse(0) == 1;
    }

    @Override
    protected void cleanData() {
        txtNombre.setText(null);
        txtModalidad.setText(null);
        txtHbca.setText(null);
        txtHti.setText(null);
        txtSemestre.setText(null);
        txtClave.setText(null);
        txtCreditos.setText(null);
        comboCarreras.getSelectionModel().select(null);
    }
}
