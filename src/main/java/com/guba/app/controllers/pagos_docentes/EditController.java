package com.guba.app.controllers.pagos_docentes;

import com.guba.app.data.dao.DAOMaestro;
import com.guba.app.data.dao.DAOPagoDocentes;
import com.guba.app.domain.models.Estudiante;
import com.guba.app.domain.models.PagoDocente;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Maestro;
import com.guba.app.domain.models.PagoDocente;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.guba.app.presentation.utils.ComboCell;
import javafx.beans.property.ObjectProperty;
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

public class EditController extends BaseController<PagoDocente> implements Loadable<PagoDocente> {

    @FXML
    private Button backButton;
    @FXML
    private Button btnActualizar;
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

    public EditController(Mediador<PagoDocente> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super("/pago_alumnos/Edit", mediador, estadoProperty, paginasProperty);
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
        comboMaestros.setButtonCell(new ComboCell<Maestro>());
        backButton.setOnAction(this::regresarAPanel);
        btnActualizar.setOnAction(this::actualizar);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
                comboMaestros.getItems().setAll(task.get());
                comboMaestros.getSelectionModel().select(maestro);
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
    public void loadData(PagoDocente data) {
        pagoDocente = data;
        txtCantidad.setText(pagoDocente.getCantidad());
        txtConcepto.setText(pagoDocente.getConcepto());
        txtFactura.setText(pagoDocente.getFactura());
        dateFeha.valueProperty().set(pagoDocente.getDate());
        loadAlumnosAsync(pagoDocente.getMaestro());
    }

    @FXML
    private void regresarAPanel(ActionEvent actionEvent) {
        pagoDocente = null;
        paginasProperty.set(Paginas.LIST);
    }

    @FXML
    private void actualizar(ActionEvent event){
        if (mostrarConfirmacion()){
            pagoDocente.setCantidad(txtCantidad.getText());
            pagoDocente.setConcepto(txtConcepto.getText());
            pagoDocente.setDate(dateFeha.getValue());
            pagoDocente.setMaestro(comboMaestros.getSelectionModel().getSelectedItem());
            pagoDocente.setDate(dateFeha.getValue());
            paginasProperty.set(Paginas.LIST);
            boolean seActualizo = mediador.actualizar(pagoDocente);
            Alert alert = new Alert(seActualizo ? Alert.AlertType.CONFIRMATION : Alert.AlertType.ERROR);
            alert.setContentText(seActualizo ? "Se actulizo Correctamente" : "Error al actualizar contacte con soporte");
            alert.showAndWait();
            //mediador.changePane(Paginas.LIST);
            paginasProperty.set(Paginas.LIST);
        }
    }

    private boolean mostrarConfirmacion() {
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estás seguro de actualzar el pago  " +"?");
        return dialogConfirmacion.showAndWait().orElse(0) == 1;
    }

    @Override
    protected void cleanData() {

    }
}
