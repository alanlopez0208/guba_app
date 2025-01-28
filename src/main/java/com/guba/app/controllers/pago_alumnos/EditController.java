package com.guba.app.controllers.pago_alumnos;

import com.guba.app.dao.DAOAlumno;
import com.guba.app.dao.DAOPagoAlumnos;
import com.guba.app.controllers.BaseController;
import com.guba.app.controllers.Loadable;
import com.guba.app.controllers.Paginas;
import com.guba.app.models.Estudiante;
import com.guba.app.models.PagoAlumno;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.guba.app.presentation.utils.ComboCell;
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

public class EditController extends BaseController<PagoAlumno> implements Initializable, Loadable<PagoAlumno> {
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

    private DAOPagoAlumnos daoPagoAlumnos = new DAOPagoAlumnos();

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
        comboAlumnos.setCellFactory(new Callback<ListView<Estudiante>, ListCell<Estudiante>>() {
            @Override
            public ListCell<Estudiante> call(ListView<Estudiante> carreraListView) {
                return new ComboCell<Estudiante>();
            }
        });
        comboAlumnos.setButtonCell(new ComboCell<Estudiante>());
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

    @Override
    public void loadData(PagoAlumno data) {
        pagoAlumno = data;
        txtCantidad.setText(pagoAlumno.getCantidad());
        txtConcepto.setText(pagoAlumno.getConcepto());
        txtFactura.setText(pagoAlumno.getFactura());
        dateFeha.valueProperty().set(pagoAlumno.getDate());
        loadAlumnosAsync(pagoAlumno.getAlumno());
    }


    @FXML
    private void regresarAPanel(ActionEvent actionEvent) {
        pagoAlumno= null;
        mediador.changePane(Paginas.LIST);
    }

    @FXML
    private void actualizar(ActionEvent event){
        if (mostrarConfirmacion()){
            pagoAlumno.setCantidad(txtCantidad.getText());
            pagoAlumno.setConcepto(txtConcepto.getText());
            pagoAlumno.setDate(dateFeha.getValue());
            pagoAlumno.setAlumno(comboAlumnos.getValue());
            pagoAlumno.setDate(dateFeha.getValue());
            mediador.changePane(Paginas.LIST);
            boolean seActualizo = daoPagoAlumnos.updatePago(pagoAlumno);
            Alert alert = new Alert(seActualizo ? Alert.AlertType.CONFIRMATION : Alert.AlertType.ERROR);
            alert.setContentText(seActualizo ? "Se actulizo Correctamente" : "Error al actualizar contacte con soporte");
            alert.showAndWait();
            mediador.changePane(Paginas.LIST);
        }
    }

    private boolean mostrarConfirmacion() {
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estás seguro de actualzar la carrera " +"?");
        return dialogConfirmacion.showAndWait().orElse(0) == 1;
    }

}
