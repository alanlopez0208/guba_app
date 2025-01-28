package com.guba.app.presentation.dialogs;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class DialogConfirmacion extends Dialog<Integer> {

    public DialogConfirmacion(String contenido){
        this.setContentText(contenido);
        this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        this.setResultConverter(buttonType -> (buttonType == ButtonType.OK) ? 1 : null);
    }
}
