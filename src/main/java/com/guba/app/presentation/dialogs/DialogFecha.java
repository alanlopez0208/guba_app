package com.guba.app.presentation.dialogs;

import com.dlsc.gemsfx.YearMonthPicker;
import com.guba.app.domain.dto.RangoDTO;
import com.guba.app.presentation.utils.Constants;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.YearMonth;

public class DialogFecha extends Dialog<RangoDTO> {

    private YearMonthPicker datePickerInicio;
    private YearMonthPicker datePickerFin;

    public DialogFecha() {
        inicializarUI();
        setUpButtons();
    }

    private void inicializarUI() {
        this.getDialogPane().getScene().getStylesheets().add(getClass().getResource(Constants.URL_PRESENTATION+"/styles.css").toExternalForm());
        VBox contenedor = new VBox();
        contenedor.setSpacing(25);
        contenedor.setFillWidth(false);

        datePickerInicio = new YearMonthPicker();
        datePickerInicio.setPromptText("Selecciona la fecha de inicio");

        datePickerFin = new YearMonthPicker();
        datePickerFin.setValue(null);
        datePickerFin.setPromptText("Selecciona la fecha de fin");

        contenedor.getChildren().addAll(wrapContainer("Fecha de inicio:", datePickerInicio),
                wrapContainer("Fecha de fin:", datePickerFin));
        getDialogPane().setContent(contenedor);
    }

    private void setUpButtons() {
        ButtonType saveDateButtonType = new ButtonType("Guardar Fechas", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveDateButtonType, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType == saveDateButtonType) {
                YearMonth fechaInicio = datePickerInicio.getValue();
                YearMonth fechaFin = datePickerFin.getValue();

                if (fechaInicio != null) {
                    return new RangoDTO(fechaInicio, fechaFin);
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
