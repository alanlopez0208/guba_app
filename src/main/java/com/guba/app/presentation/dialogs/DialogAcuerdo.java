package com.guba.app.presentation.dialogs;


import com.guba.app.data.dao.DAOCarreras;
import com.guba.app.domain.models.Acuerdo;
import com.guba.app.domain.models.Carrera;
import com.guba.app.presentation.utils.ComboCell;
import com.guba.app.presentation.utils.Constants;
import com.guba.app.utils.Utils;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.apache.commons.compress.harmony.unpack200.bytecode.forms.ThisFieldRefForm;

import java.util.List;

public class DialogAcuerdo extends Dialog<Acuerdo> {

    private TextField txtNumero;
    private TextField txtCC;
    private DatePicker dateFecha;
    private ComboBox<Carrera> carreraComboBox;
    private boolean isEditMode = false;

    private Acuerdo acuerdo = new Acuerdo();

    // Constructor privado
    private DialogAcuerdo(boolean isEditMode, Acuerdo acuerdo) {
        this.isEditMode = isEditMode;
        this.acuerdo = acuerdo;
        inicializarUI();
        setUpButtons();
    }

    public static DialogAcuerdo crear() {
        return new DialogAcuerdo(false, new Acuerdo());
    }

    public static DialogAcuerdo editar(Acuerdo acuerdo) {
        return new DialogAcuerdo(true, acuerdo);
    }

    private void inicializarUI() {
        this.getDialogPane().getScene().getStylesheets().add(getClass().getResource(Constants.URL_PRESENTATION + "/styles.css").toExternalForm());

        txtNumero = new TextField();
        txtNumero.setPromptText("Número de Acuerdo");
        txtNumero.getStyleClass().add("textField");

        txtCC = new TextField();
        txtCC.setPromptText("CCT");
        txtCC.getStyleClass().add("textField");

        dateFecha = new DatePicker();
        dateFecha.setPromptText("Fecha");
        dateFecha.getStyleClass().add("calendario");

        carreraComboBox = new ComboBox<>();
        carreraComboBox.setPromptText("Selecciona una carrera");
        carreraComboBox.getStyleClass().add("combo");
        carreraComboBox.setButtonCell(new ComboCell<Carrera>());
        carreraComboBox.setCellFactory(carreraListView -> new ComboCell<Carrera>());


        VBox content = new VBox(10);

        if(!isEditMode){
            loadCarrerasAsync();
            content.getChildren().add(wrapContainer("Carrera:", carreraComboBox));
        }else{
            txtNumero.setText(acuerdo.getNumero());
            txtCC.setText(acuerdo.getCc());
            dateFecha.setValue(acuerdo.getDate());
        }

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
            if (dialogButton != okButtonType) {
                return null;
            }

            if (!validarCamposObligatorios()) {
                mostrarError("Por favor complete todos los campos obligatorios");
                return null;
            }

            acuerdo.setNumero(txtNumero.getText());
            acuerdo.setCc(txtCC.getText());
            acuerdo.setDate(dateFecha.getValue());

            if (!isEditMode) {
                if (carreraComboBox.getSelectionModel().isEmpty()) {
                    mostrarError("Por favor seleccione una carrera");
                    return null;
                }
                acuerdo.setCarrera(carreraComboBox.getValue());
            }

            return acuerdo;
        });
    }

    private boolean validarCamposObligatorios() {
        return txtNumero.getText() != null && !txtNumero.getText().isEmpty()
                && txtCC.getText() != null && !txtCC.getText().isEmpty()
                && dateFecha.getValue() != null;
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(mensaje);
        alert.show();
    }

    private VBox wrapContainer(String titulo, Node node) {
        VBox wrapper = new VBox();
        wrapper.setSpacing(10);
        wrapper.setFillWidth(false);
        wrapper.getChildren().addAll(new Label(titulo), node);
        return wrapper;
    }

    private void loadCarrerasAsync() {
        Utils.loadAsync(()-> new DAOCarreras().getCarreraWithoutAcuerdo(), carreras -> {
            carreraComboBox.getItems().setAll(carreras);
        });
    }
}
