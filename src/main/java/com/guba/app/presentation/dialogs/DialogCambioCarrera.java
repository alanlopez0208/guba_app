package com.guba.app.presentation.dialogs;

import com.guba.app.data.dao.DAOAlumno;
import com.guba.app.data.dao.DAOCarreras;
import com.guba.app.domain.dto.AlumnoCarreraDTO;
import com.guba.app.domain.models.Carrera;
import com.guba.app.domain.models.Estudiante;
import com.guba.app.presentation.utils.Constants;
import com.guba.app.utils.Utils;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;


public class DialogCambioCarrera extends Dialog<AlumnoCarreraDTO> {

    private Carrera carrera;
    private ComboBox<Carrera> carreraComboBox;
    private TextField matriculaTextField;
    private DAOCarreras daoCarreras;
    private DAOAlumno daoAlumno;
    private Estudiante estudiante;


    public DialogCambioCarrera(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.daoCarreras = new DAOCarreras();
        this.daoAlumno = new DAOAlumno();
        initUi();
        setButtons();
    }


    private void initUi() {
        this.getDialogPane().getScene().getStylesheets().add(getClass().getResource(Constants.URL_PRESENTATION+"/styles.css").toExternalForm());
        carreraComboBox = new ComboBox<>();
        carreraComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            carrera = newValue;
        });
        carreraComboBox.getStyleClass().add("combo");
        matriculaTextField = new TextField();
        matriculaTextField.getStyleClass().add("textField");
        loadCarreras();

        VBox panel = new VBox();
        panel.getChildren().addAll(wrapContainer("Carrera: ", carreraComboBox), wrapContainer("Matricula: ", matriculaTextField));
        panel.setSpacing(20);
        getDialogPane().setContent(panel);
    }


    private void loadCarreras(){
        Utils.loadAsync(()-> daoCarreras.getAllCarreras(), carreras -> {
            carreraComboBox.getItems().setAll(carreras);
        });
    }

    private void setButtons(){
        ButtonType saveButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                if (!matriculaTextField.getText().isEmpty() && carrera != null) {
                    String matricula = matriculaTextField.getText();
                    boolean seActualizo = daoAlumno.changeDegree(matricula, carrera.getIdCarrera(), estudiante.getId());
                    return seActualizo ? new AlumnoCarreraDTO(matricula, carrera) : null;
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
