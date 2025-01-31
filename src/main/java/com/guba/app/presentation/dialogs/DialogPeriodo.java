package com.guba.app.presentation.dialogs;

import com.guba.app.domain.models.Periodo;
import com.guba.app.presentation.utils.Constants;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class DialogPeriodo extends Dialog<Periodo> {

    private TextField txtNombre;
    private TextField txtInicio;
    private TextField txtFin;

    private Periodo periodo = new Periodo();

    public DialogPeriodo() {
        inicializarUI();
        setUpButtons();
    }

    private void inicializarUI(){
        this.getDialogPane().getScene().getStylesheets().add(getClass().getResource(Constants.URL_PRESENTATION+"/styles.css").toExternalForm());
        txtNombre = new TextField();
        txtNombre.setPromptText("Nombre");
        txtNombre.getStyleClass().add("textField");
        txtNombre.textProperty().bindBidirectional(periodo.nombreProperty());

        txtInicio = new TextField();
        txtInicio.setPromptText("Inicio del Periodo");
        txtInicio.getStyleClass().add("textField");
        txtInicio.textProperty().bindBidirectional(periodo.inicioProperty());

        txtFin = new TextField();
        txtFin.setPromptText("Fin del Periodo");
        txtFin.getStyleClass().add("textField");
        txtFin.textProperty().bindBidirectional(periodo.finProperty());


        VBox content = new VBox(10);
        content.getChildren().addAll(
                wrapContainer("Nombre:", txtNombre),
                wrapContainer("Inicio:",txtInicio),
                wrapContainer("Fin:", txtFin)
        );

        getDialogPane().setContent(content);
    }

    private void setUpButtons(){

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);


        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                if ((periodo.getNombre() != null && !periodo.getNombre().isEmpty()) &&
                        (periodo.getFin() != null && !periodo.getFin().isEmpty()) &&
                        (periodo.getInicio() != null && !periodo.getInicio().isEmpty())){
                    return periodo;
                }
                return null;
            }
            return null;
        });
    }

    private VBox wrapContainer(String titulo, Node node) {
        VBox wraper = new VBox();
        wraper.setSpacing(10);
        wraper.setFillWidth(false);
        wraper.getChildren().addAll(new Label(titulo), node);
        return wraper;
    }

}
