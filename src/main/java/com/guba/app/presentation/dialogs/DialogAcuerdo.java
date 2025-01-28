package com.guba.app.presentation.dialogs;


import com.guba.app.models.Acuerdo;
import com.guba.app.presentation.utils.Constants;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DialogAcuerdo extends Dialog<Acuerdo> {

    private TextField txtNumero;
    private TextField txtCC;
    private DatePicker dateFecha;

    private Acuerdo acuerdo = new Acuerdo();

    public DialogAcuerdo() {
        inicializarUI();
        setUpButtons();
    }

    private void inicializarUI() {
        this.getDialogPane().getScene().getStylesheets().add(getClass().getResource(Constants.URL_PRESENTATION + "/styles.css").toExternalForm());

        txtNumero = new TextField();
        txtNumero.setPromptText("Número de Acuerdo");
        txtNumero.getStyleClass().add("textField");
        txtNumero.textProperty().bindBidirectional(acuerdo.numeroProperty());

        txtCC = new TextField();
        txtCC.setPromptText("CCT");
        txtCC.getStyleClass().add("textField");
        txtCC.textProperty().bindBidirectional(acuerdo.ccProperty());

        dateFecha = new DatePicker();
        dateFecha.setPromptText("Fecha");
        dateFecha.getStyleClass().add("calendario");
        dateFecha.valueProperty().bindBidirectional(acuerdo.dateProperty());

        VBox content = new VBox(10);
        content.getChildren().addAll(
                wrapContainer("Número:", txtNumero),
                wrapContainer("Fecha:", dateFecha),
                wrapContainer("CCT:", txtCC)
        );

        getDialogPane().setContent(content);
    }

    private void setUpButtons() {
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                if ((acuerdo.getNumero() != null && !acuerdo.getNumero().isEmpty()) &&
                        (acuerdo.getCc() != null && !acuerdo.getCc().isEmpty()) &&
                        (acuerdo.getDate() != null)) {
                    return acuerdo;
                }
                return null;
            }
            return null;
        });
    }

    private VBox wrapContainer(String titulo, Node node) {
        VBox wrapper = new VBox();
        wrapper.setSpacing(10);
        wrapper.setFillWidth(false);
        wrapper.getChildren().addAll(new Label(titulo), node);
        return wrapper;
    }
}
