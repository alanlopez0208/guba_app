package com.guba.app.controllers.pago_alumnos;

import com.guba.app.data.dao.DAOAlumno;
import com.guba.app.data.dao.DAOPagoAlumnos;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Estudiante;
import com.guba.app.domain.models.PagoAlumno;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.guba.app.presentation.utils.ComboCell;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
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


public class AddController extends BaseController<PagoAlumno> implements Loadable<PagoAlumno> {
    @FXML
    private Button backButton;
    @FXML
    private Button btnGuardar;
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

    public AddController(Mediador<PagoAlumno> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super("/pago_alumnos/Add", mediador, estadoProperty, paginasProperty);
        txtCantidad.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1){
                    containerMoney.getStyleClass().add("select");
                }else{
                    containerMoney.getStyleClass().remove("select");
                }
            }
        });
        comboAlumnos.setCellFactory(new Callback<ListView<Estudiante>, ListCell<Estudiante>>() {
            @Override
            public ListCell<Estudiante> call(ListView<Estudiante> carreraListView) {
                return new ComboCell<Estudiante>();
            }
        });
        txtCantidad.textProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null) {
                pagoAlumno.setCantidad(t1);
            }
        });
        txtConcepto.textProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null) {
                pagoAlumno.setConcepto(t1);
            }
        });
        txtFactura.textProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null) {
                pagoAlumno.setFactura(t1);
            }
        });
        comboAlumnos.setButtonCell(new ComboCell<>());
        backButton.setOnAction(this::regresarAPanel);
        btnGuardar.setOnAction(this::guardar);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void regresarAPanel(ActionEvent actionEvent) {
        cleanData();
        paginasProperty.set(Paginas.LIST);
    }

    private void guardar(ActionEvent event){
        if (mostrarConfirmacion()){
            pagoAlumno.setAlumno(comboAlumnos.getValue());
            pagoAlumno.setDate(dateFeha.getValue());
            boolean seAgrego = mediador.guardar(pagoAlumno);
            if (seAgrego){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Se agrego Correctamente");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error al guardar contacta a soporte");
                alert.showAndWait();
            }
            paginasProperty.set(Paginas.LIST);
            cleanData();
        }
    }

    private void loadAlumnosAsync() {
        Utils.loadAsync(()->{
            comboAlumnos.setDisable(true);
            return daoAlumno.getEstudiantes();
        }, estudiantes -> {
            comboAlumnos.getItems().setAll(estudiantes);
            comboAlumnos.setDisable(false);
        });
    }

    @Override
    public void loadData(PagoAlumno data) {
        pagoAlumno = data;
        loadAlumnosAsync();
    }

    private boolean mostrarConfirmacion() {
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estás seguro de añadir el pago: " +"?");
        return dialogConfirmacion.showAndWait().orElse(0) == 1;
    }

    @Override
    protected void cleanData() {
        pagoAlumno = null;
        txtCantidad.setText(null);
        txtConcepto.setText(null);
        txtFactura.setText(null);
        dateFeha.setValue(null);
    }
}
