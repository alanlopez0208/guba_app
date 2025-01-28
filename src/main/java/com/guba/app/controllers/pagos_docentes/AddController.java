package com.guba.app.controllers.pagos_docentes;

import com.guba.app.dao.DAOMaestro;
import com.guba.app.dao.DAOPagoDocentes;
import com.guba.app.controllers.BaseController;
import com.guba.app.controllers.Loadable;
import com.guba.app.controllers.Paginas;
import com.guba.app.models.Maestro;
import com.guba.app.models.PagoDocente;
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


public class AddController extends BaseController<PagoDocente> implements Initializable, Loadable<PagoDocente> {

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
    private DAOPagoDocentes daoPagoDocentes = new DAOPagoDocentes();


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
        comboMaestros.setCellFactory(new Callback<ListView<Maestro>, ListCell<Maestro>>() {
            @Override
            public ListCell<Maestro> call(ListView<Maestro> carreraListView) {
                return new ComboCell<Maestro>();
            }
        });
        comboMaestros.setButtonCell(new ComboCell<>());
    }


    @FXML
    private void regresarAPanel(ActionEvent actionEvent) {
        pagoDocente = null;
        mediador.changePane(Paginas.LIST);
    }

    @FXML
    private void guardar(ActionEvent event){
        if (mostrarConfirmacion()){
            daoPagoDocentes.crearPago(pagoDocente).ifPresentOrElse(integer -> {
                pagoDocente.setIdPago(integer.toString());
                getLista().add(pagoDocente);
                System.out.println(pagoDocente.getFecha());
                pagoDocente = null;
                mediador.changePane(Paginas.LIST);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Se actulizo Correctamente");
                //alert.showAndWait();
                //mediador.changePane(Paginas.LIST);
            }, () -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText( "Error al actualizar contacte con soporte");
                alert.showAndWait();
                mediador.changePane(Paginas.LIST);
            });
        }
    }

    private void loadAlumnosAsync() {
        comboMaestros.setDisable(true);
        Task<List<Maestro>> task = new Task<>() {
            @Override
            protected List<Maestro> call() throws Exception {
                return daoMaestro.getDocentes();
            }
        };
        task.setOnSucceeded(event -> {
            try {
                comboMaestros.getItems().setAll(task.get());
                comboMaestros.setDisable(false);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(task).start();
    }


    @Override
    public void loadData(PagoDocente data) {
        pagoDocente = data;
        txtCantidad.textProperty().bindBidirectional(pagoDocente.cantidadProperty());
        txtConcepto.textProperty().bindBidirectional(pagoDocente.conceptoProperty());
        txtFactura.textProperty().bindBidirectional(pagoDocente.facturaProperty());
        Bindings.bindBidirectional(comboMaestros.valueProperty(), pagoDocente.maestroProperty());
        Bindings.bindBidirectional(dateFeha.valueProperty(), pagoDocente.dateProperty());
        loadAlumnosAsync();
    }

    private boolean mostrarConfirmacion() {
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estás seguro de añadir el pago: " +"?");
        return dialogConfirmacion.showAndWait().orElse(0) == 1;
    }

}
