package com.guba.app.controllers.configuracion;

import com.guba.app.data.dao.DAOAcuerdo;
import com.guba.app.data.dao.DAOPeriodo;
import com.guba.app.domain.models.Acuerdo;
import com.guba.app.domain.models.Periodo;
import com.guba.app.presentation.dialogs.DialogAcuerdo;
import com.guba.app.presentation.dialogs.DialogPeriodo;
import com.guba.app.data.local.database.Service;
import com.guba.app.utils.IPane;
import com.guba.app.utils.Utils;
import com.jfoenix.controls.JFXButton;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ConfiguracionController implements Initializable, IPane {
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
        btnActualizarCiclo.setOnAction(this::handleActualizarCiclo);
        btnActualizarAcuerdo.setOnAction(this::handleActualizarAcuerdo);
    }

    public void handleActualizarCiclo(ActionEvent event) {
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

    public void handleActualizarAcuerdo(ActionEvent event) {
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
        Utils.loadAsync(()-> daoAcuerdo.getAcuerdo(), a->{
            if (a == null){
                return;
            }
            acuerdo = a;
            txtNumeroAcuerdo.setText(acuerdo.getNumero());
            txtCCT.setText(acuerdo.getCc());
            dateFecha.setValue(acuerdo.getDate());
        });
    }

    public void loadPeriod(){
        Utils.loadAsync(()-> daoPeriodo.getUltimoPeriodo(),p -> {
            periodo = p;
            txtNombreCiclo.textProperty().bindBidirectional(periodo.nombreProperty());
            txtInicioCiclo.textProperty().bindBidirectional(periodo.inicioProperty());
            txtFinCiclo.textProperty().bindBidirectional(periodo.finProperty() );
        });
    }

    @Override
    public void openPane() {
        loadPeriod();
        loadAcuerdoAsync();
    }
}
