package com.guba.app.controllers.configuracion;

import com.guba.app.dao.DAOAcuerdo;
import com.guba.app.dao.DAOPeriodo;
import com.guba.app.models.Acuerdo;
import com.guba.app.models.Periodo;
import com.guba.app.models.Personal;
import com.guba.app.presentation.dialogs.DialogAcuerdo;
import com.guba.app.presentation.dialogs.DialogPeriodo;
import com.guba.app.service.local.database.Service;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class ConfiguracionController implements Initializable {
    @FXML
    private TextField txtNombreCiclo, txtInicioCiclo, txtFinCiclo, txtNumeroAcuerdo, txtCCT;
    @FXML
    private DatePicker dateFecha;
    @FXML
    private JFXButton btnActualizarCiclo, btnActualizarAcuerdo;
    private DAOPeriodo daoPeriodo = new DAOPeriodo();
    private DAOAcuerdo daoAcuerdo = new DAOAcuerdo();
    private Periodo periodo;
    private Acuerdo acuerdo;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAcuerdoAsync();
        loadPeriod();

    }
    @FXML
    public void handleActualizarCiclo() {
        DialogPeriodo dialogPeriodo = new DialogPeriodo();
        dialogPeriodo.showAndWait().ifPresent(p -> {
            Service.getService().crearPeriodo(p).ifPresentOrElse(aBoolean -> {
                periodo.setInicio(p.getInicio());
                periodo.setFin(p.getFin());
                periodo.setNombre(p.getNombre());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Se actualizo el perido de maner correcta");
                alert.show();
            }, () -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error al actualizar porfavor contacte a soporte");
                alert.show();
            });
        });
    }

    @FXML
    public void handleActualizarAcuerdo() {
        DialogAcuerdo dialogAcuerdo = new DialogAcuerdo();
        dialogAcuerdo.showAndWait().ifPresent(a -> {
            boolean seActualizo = daoAcuerdo.actualizarAcuerdo(a);
            if (seActualizo){
                acuerdo.setCc(a.getCc());
                acuerdo.setDate(a.getDate());
                acuerdo.setNumero(a.getNumero());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Se actualizo el perido de maner correcta");
                alert.show();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error al actualizar porfavor contacte a soporte");
                alert.show();
            }
        });
    }

    public void loadAcuerdoAsync(){
        Task<Acuerdo> task = new Task<Acuerdo>() {
            @Override
            protected Acuerdo call() throws Exception {
                return daoAcuerdo.getAcuerdo();
            }
        };
        task.setOnSucceeded(event -> {
            try {
                acuerdo = task.get();
                txtNumeroAcuerdo.textProperty().bindBidirectional(acuerdo.numeroProperty());
                txtCCT.textProperty().bindBidirectional(acuerdo.ccProperty());
                dateFecha.valueProperty().bindBidirectional(acuerdo.dateProperty());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }

        });

        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Error al cargar el acuerdo: " + task.getException());
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }



    public void loadPeriod(){
        Task<Periodo> task = new Task<Periodo>() {
            @Override
            protected Periodo call() throws Exception {
                return daoPeriodo.getUltimoPeriodo();
            }
        };
        task.setOnSucceeded(event -> {
            try {
                periodo = task.get();
                txtNombreCiclo.textProperty().bindBidirectional(periodo.nombreProperty());
                txtInicioCiclo.textProperty().bindBidirectional(periodo.inicioProperty());
                txtFinCiclo.textProperty().bindBidirectional(periodo.finProperty() );
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Error al cargar el peido" + task.getException());
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

}
