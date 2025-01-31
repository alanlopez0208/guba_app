package com.guba.app.controllers.pago_alumnos;

import com.guba.app.data.dao.DAOAlumno;
import com.guba.app.data.dao.DAOPagoAlumnos;
import com.guba.app.utils.BaseController;
import com.guba.app.utils.Loadable;
import com.guba.app.utils.Paginas;
import com.guba.app.domain.models.Estudiante;
import com.guba.app.domain.models.PagoAlumno;
import com.guba.app.presentation.utils.ComboCell;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class DetailsController extends BaseController<PagoAlumno> implements Initializable, Loadable<PagoAlumno> {

    @FXML
    private HBox containerMoney;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TextField txtConcepto;

    @FXML
    private TextField txtFactura;

    @FXML
    private ComboBox<Estudiante> comboAlumnos;

    @FXML
    private DatePicker dateFeha;


    private PagoAlumno pagoAlumno;

    private DAOAlumno daoAlumno = new DAOAlumno();
    private DAOPagoAlumnos daoPagoAlumno = new DAOPagoAlumnos();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtCantidad.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                System.out.println(t1);
                if (t1){
                    containerMoney.getStyleClass().add("select");
                }else{
                    containerMoney.getStyleClass().remove("select");
                }
            }
        });
        txtCantidad.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d+)?$")){
                return change;
            }
            return null;
        }));
    }


    private void loadAlumnosAsync(Estudiante estudiante) {
        Task<List<Estudiante>> task = new Task<>() {
            @Override
            protected List<Estudiante> call() throws Exception {
                return daoAlumno.getEstudiantes();
            }
        };
        task.setOnSucceeded(event -> {
            try {
                comboAlumnos.setCellFactory(new Callback<ListView<Estudiante>, ListCell<Estudiante>>() {
                    @Override
                    public ListCell<Estudiante> call(ListView<Estudiante> carreraListView) {
                        return new ComboCell<Estudiante>();
                    }
                });
                comboAlumnos.setButtonCell(new ComboCell<Estudiante>());
                comboAlumnos.getItems().setAll(task.get());
                comboAlumnos.getSelectionModel().select(estudiante);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("TAREA FALLIDA" + task.getException());
            }
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void regresarAPanel(ActionEvent actionEvent) {
        paginasProperty.set(Paginas.LIST);
    }


    @Override
    public void loadData(PagoAlumno data) {
        pagoAlumno = data;
        txtCantidad.textProperty().bindBidirectional(pagoAlumno.cantidadProperty());
        txtConcepto.textProperty().bindBidirectional(pagoAlumno.conceptoProperty());
        txtFactura.textProperty().bindBidirectional(pagoAlumno.facturaProperty());
        Bindings.bindBidirectional(dateFeha.valueProperty(),pagoAlumno.dateProperty());
        loadAlumnosAsync(pagoAlumno.getAlumno());
    }

    @Override
    protected void cleanData() {

    }
}
