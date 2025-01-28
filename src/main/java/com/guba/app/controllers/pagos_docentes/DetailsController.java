package com.guba.app.controllers.pagos_docentes;

import com.guba.app.dao.DAOMaestro;
import com.guba.app.controllers.BaseController;
import com.guba.app.controllers.Loadable;
import com.guba.app.controllers.Paginas;
import com.guba.app.models.Maestro;
import com.guba.app.models.PagoDocente;
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

public class DetailsController extends BaseController<PagoDocente> implements Initializable, Loadable<PagoDocente> {

    @FXML
    private HBox containerMoney;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtConcepto;
    @FXML
    private TextField txtFactura;
    @FXML
    private ComboBox<Maestro> comboMaestro;
    @FXML
    private DatePicker dateFeha;
    private PagoDocente pagoDocente;
    private DAOMaestro daoMaestro = new DAOMaestro();

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


    private void loadAlumnosAsync(Maestro maestro) {
        Task<List<Maestro>> task = new Task<>() {
            @Override
            protected List<Maestro> call() throws Exception {
                return daoMaestro.getDocentes();
            }
        };
        task.setOnSucceeded(event -> {
            try {
                comboMaestro.setCellFactory(new Callback<ListView<Maestro>, ListCell<Maestro>>() {
                    @Override
                    public ListCell<Maestro> call(ListView<Maestro> carreraListView) {
                        return new ComboCell<Maestro>();
                    }
                });
                comboMaestro.setButtonCell(new ComboCell<Maestro>());
                comboMaestro.getItems().setAll(task.get());
                comboMaestro.getSelectionModel().select(maestro);
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
        mediador.changePane(Paginas.LIST);
    }


    @Override
    public void loadData(PagoDocente data) {
        pagoDocente = data;
        txtCantidad.textProperty().bindBidirectional(pagoDocente.cantidadProperty());
        txtConcepto.textProperty().bindBidirectional(pagoDocente.conceptoProperty());
        txtFactura.textProperty().bindBidirectional(pagoDocente.facturaProperty());
        Bindings.bindBidirectional(dateFeha.valueProperty(), pagoDocente.dateProperty());
        loadAlumnosAsync(pagoDocente.getMaestro());
    }
}
