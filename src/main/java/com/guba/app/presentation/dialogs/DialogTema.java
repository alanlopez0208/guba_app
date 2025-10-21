package com.guba.app.presentation.dialogs;


import com.guba.app.data.dao.DAOGrupoMateria;
import com.guba.app.domain.models.GrupoMateria;
import com.guba.app.domain.models.Tema;
import com.guba.app.presentation.utils.Constants;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;

public class DialogTema extends Dialog<Tema> {

    private TextField textFieldNombre;
    private TextField textFieldDuracion;

    public DialogTema(){
        inicializarUI();
        setUpButtons();
    }

    private void inicializarUI(){
        this.getDialogPane().getScene().getStylesheets().add(getClass().getResource(Constants.URL_PRESENTATION+"/styles.css").toExternalForm());
        VBox contendor = new VBox();
        contendor.setSpacing(25);
        contendor.setFillWidth(false);



        textFieldNombre = new TextField();
        textFieldNombre.getStyleClass().add("textField");
        textFieldNombre.minWidth(500);
        textFieldNombre.setPromptText("Ingresa el nombre del Tema");
        textFieldNombre.getStyleClass().add("textField");

        textFieldDuracion = new TextField();
        textFieldDuracion.getStyleClass().add("textField");
        textFieldDuracion.minWidth(500);
        textFieldDuracion.setPromptText("Ingresa duracion");
        textFieldDuracion.getStyleClass().add("textField");
        textFieldDuracion.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")){
                return change;
            }
            return null;
        }));


        contendor.getChildren().addAll(wrapContainer("Nombre:", textFieldNombre), wrapContainer("Duracion:", textFieldDuracion));
        getDialogPane().setContent(contendor);
    }

    private void setUpButtons(){
        ButtonType saveThemeButtonType = new ButtonType("Guardar Tema", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveThemeButtonType, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType == saveThemeButtonType) {
                if (!textFieldNombre.getText().isEmpty() && !textFieldDuracion.getText().isEmpty()) {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    Tema tema = new Tema();
                    tema.setNombre(textFieldNombre.getText());
                    tema.setDuracionHoras(textFieldDuracion.getText());
                    return tema;
                }
                return null;
            }
            return null;
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
