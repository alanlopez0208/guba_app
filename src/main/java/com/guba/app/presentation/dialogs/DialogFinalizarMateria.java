package com.guba.app.presentation.dialogs;


import com.guba.app.data.dao.DAOCalificiaciones;
import com.guba.app.data.dao.DAOGrupoMateria;
import com.guba.app.data.dao.DAOMaestro;
import com.guba.app.domain.models.GrupoMateria;
import com.guba.app.domain.models.Maestro;
import com.guba.app.presentation.utils.ComboCell;
import com.guba.app.presentation.utils.Constants;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class DialogFinalizarMateria extends Dialog<Boolean> {

    private DatePicker datePicker;
    private TextField textFieldTipo;
    private TextField textFieldFolio;
    private DAOGrupoMateria daoGrupoMateria = new DAOGrupoMateria();
    private GrupoMateria grupoMateria;

    public DialogFinalizarMateria(GrupoMateria grupoMateria){
        this.grupoMateria = grupoMateria;
        inicializarUI();
        setUpButtons();
    }

    private void inicializarUI(){
        this.getDialogPane().getScene().getStylesheets().add(getClass().getResource(Constants.URL_PRESENTATION+"/styles.css").toExternalForm());
        VBox contendor = new VBox();
        contendor.setSpacing(25);
        contendor.setFillWidth(false);

        datePicker=  new DatePicker();

        textFieldTipo = new TextField();
        textFieldTipo.getStyleClass().add("textField");
        textFieldTipo.minWidth(500);
        textFieldTipo.setPromptText("Ingresa el tipo de examen");
        textFieldTipo.getStyleClass().add("textField");

        textFieldFolio = new TextField();
        textFieldFolio.getStyleClass().add("textField");
        textFieldFolio.minWidth(500);
        textFieldFolio.setPromptText("Ingresa el Folio");
        textFieldFolio.getStyleClass().add("textField");


        contendor.getChildren().addAll(wrapContainer("Fecha", datePicker) ,wrapContainer("Nombre", textFieldTipo), wrapContainer("Folio", textFieldFolio));
        getDialogPane().setContent(contendor);
    }

    private void setUpButtons(){
        ButtonType savePhotoButtonType = new ButtonType("Finzalizar Materia", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(savePhotoButtonType, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType == savePhotoButtonType) {
                if (datePicker.getValue() != null && !textFieldTipo.getText().isEmpty() && !textFieldFolio.getText().isEmpty()) {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String fecha = dateTimeFormatter.format(datePicker.getValue());
                    return daoGrupoMateria.finalizarMateria(grupoMateria,fecha, textFieldTipo.getText(), textFieldFolio.getText());
                }
                return false;
            }
            return false;
        });
    }

    private VBox wrapContainer(String titulo, Node node){
        VBox wraper = new VBox();
        wraper.setSpacing(10);
        wraper.setFillWidth(false);
        wraper.getChildren().addAll(new Label(titulo),node);
        return wraper;
    }


}
