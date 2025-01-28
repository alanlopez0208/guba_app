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
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;


public class AddController extends BaseController<PagoAlumno> implements Initializable, Loadable<PagoAlumno> {

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
        comboAlumnos.setCellFactory(new Callback<ListView<Estudiante>, ListCell<Estudiante>>() {
            @Override
            public ListCell<Estudiante> call(ListView<Estudiante> carreraListView) {
                return new ComboCell<Estudiante>();
            }
        });
        comboAlumnos.setButtonCell(new ComboCell<>());
    }


    @FXML
    private void regresarAPanel(ActionEvent actionEvent) {
        pagoAlumno = null;
        mediador.changePane(Paginas.LIST);
    }

    @FXML
    private void guardar(ActionEvent event){
        if (mostrarConfirmacion()){
            pagoAlumno.setAlumno(comboAlumnos.getValue());
            daoPagoAlumno.crearPago(pagoAlumno).ifPresentOrElse(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) {
                    pagoAlumno.setIdPago(integer.toString());
                    System.out.println(pagoAlumno.getAlumno().getMatricula());
                    getLista().add(pagoAlumno);
                    pagoAlumno = null;
                    mediador.changePane(Paginas.LIST);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Se agrego Correctamente");
                    alert.showAndWait();
                    mediador.changePane(Paginas.LIST);
                }
            }, new Runnable() {
                @Override
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error al guardar contecta con soporte");
                    alert.showAndWait();
                    mediador.changePane(Paginas.LIST);
                }
            });

        }
    }

    private void loadAlumnosAsync() {
        comboAlumnos.setDisable(true);
        Task<List<Estudiante>> task = new Task<>() {
            @Override
            protected List<Estudiante> call() throws Exception {
                return daoAlumno.getEstudiantes();
            }
        };
        task.setOnSucceeded(event -> {
            try {
                comboAlumnos.getItems().setAll(task.get());
                comboAlumnos.setDisable(false);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(task).start();
    }


    @Override
    public void loadData(PagoAlumno data) {
        pagoAlumno = data;
        txtCantidad.textProperty().bindBidirectional(pagoAlumno.cantidadProperty());
        txtConcepto.textProperty().bindBidirectional(pagoAlumno.conceptoProperty());
        txtFactura.textProperty().bindBidirectional(pagoAlumno.facturaProperty());
        Bindings.bindBidirectional(dateFeha.valueProperty(),pagoAlumno.dateProperty());
        loadAlumnosAsync();
    }

    private boolean mostrarConfirmacion() {
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estás seguro de añadir el pago: " +"?");
        return dialogConfirmacion.showAndWait().orElse(0) == 1;
    }

}
