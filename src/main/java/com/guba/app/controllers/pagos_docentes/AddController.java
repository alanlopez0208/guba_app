package com.guba.app.controllers.pagos_docentes;

import com.guba.app.data.dao.DAOMaestro;
import com.guba.app.domain.models.PagoDocente;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Maestro;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.guba.app.presentation.utils.ComboCell;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;


public class AddController extends BaseController<PagoDocente> implements Initializable, Loadable<PagoDocente> {

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
    private ComboBox<Maestro> comboMaestros;
    @FXML
    private DatePicker dateFeha;
    private PagoDocente pagoDocente;
    private DAOMaestro daoMaestro = new DAOMaestro();


    public AddController(Mediador<PagoDocente> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super("/pago_docentes/Add", mediador, estadoProperty, paginasProperty);
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
        txtCantidad.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d+)?$")){
                return change;
            }
            return null;
        }));
        comboMaestros.setCellFactory(new Callback<ListView<Maestro>, ListCell<Maestro>>() {
            @Override
            public ListCell<Maestro> call(ListView<Maestro> carreraListView) {
                return new ComboCell<Maestro>();
            }
        });
        txtCantidad.textProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null) {
                pagoDocente.setCantidad(t1);
            }
        });
        txtConcepto.textProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null) {
                pagoDocente.setConcepto(t1);
            }
        });
        txtFactura.textProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null) {
                pagoDocente.setFactura(t1);
            }
        });
        comboMaestros.setButtonCell(new ComboCell<>());
        comboMaestros.setButtonCell(new ComboCell<>());
        backButton.setOnAction(this::regresarAPanel);
        btnGuardar.setOnAction(this::guardar);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    @FXML
    private void regresarAPanel(ActionEvent actionEvent) {
        pagoDocente = null;
        paginasProperty.set(Paginas.LIST);
    }

    @FXML
    private void guardar(ActionEvent event){
        if (mostrarConfirmacion()){
            pagoDocente.setMaestro(comboMaestros.getValue());
            pagoDocente.setDate(dateFeha.getValue());
            boolean seAgrego = mediador.guardar(pagoDocente);
            if (seAgrego){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Se agrego Correctamente");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error al guardar contecta con soporte");
                alert.showAndWait();
            }
            cleanData();
            paginasProperty.set(Paginas.LIST);
        }
    }

    private void loadMaestrosAsync() {
        Utils.loadAsync(()->{
            comboMaestros.setDisable(true);
            return daoMaestro.getDocentes();
        },maestros -> {
            comboMaestros.getItems().setAll(maestros);
            comboMaestros.setDisable(false);
        } );
    }


    @Override
    public void loadData(PagoDocente data) {
        pagoDocente = data;
        loadMaestrosAsync();
    }

    private boolean mostrarConfirmacion() {
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estás seguro de añadir el pago: " +"?");
        return dialogConfirmacion.showAndWait().orElse(0) == 1;
    }

    @Override
    protected void cleanData() {
        pagoDocente = null;
        dateFeha.setValue(null);
        comboMaestros.setValue(null);
        txtCantidad.setText(null);
        txtConcepto.setText(null);
        txtFactura.setText(null);
        dateFeha.setValue(null);
    }
}
