package com.guba.app.controllers.maestro;

import com.guba.app.utils.*;
import com.guba.app.domain.models.Maestro;
import com.guba.app.presentation.utils.Constants;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class DetailsController extends BaseController<Maestro> implements Initializable, Loadable<Maestro> {
    @FXML
    private Circle foto;
    @FXML
    private TextField txtRfc;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApPat;
    @FXML
    private TextField txtApMat;
    @FXML
    private TextField txtCorreoInst;
    @FXML
    private TextField txtCorreoPer;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtGrado;
    @FXML
    private TextField txtMunicipio;
    @FXML
    private TextField txtEstado;
    @FXML
    private TextField txtCelular;
    @FXML
    private TextField txtCurp;
    @FXML
    private TextField txtPathCurriculo;
    @FXML
    private ComboBox<String> comboSexo;
    @FXML
    private Button backButton;

    private Maestro maestro;

    public DetailsController(Mediador<Maestro> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super("/maestros/Details", mediador, estadoProperty, paginasProperty);
        backButton.setOnAction(this::backPanel);
        comboSexo.getItems().addAll("Hombre", "Mujer");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    }

    private void backPanel(ActionEvent event){
        cleanData();
        paginasProperty.set(Paginas.LIST);
    }

    @Override
    public void loadData(Maestro data) {
        maestro = data;
        Image img = new Image(String.valueOf(maestro.getFoto() != null ?  new File(maestro.getFoto()).toURI().toString() : getClass().getResource(Constants.URL_ASSETS+"img/usuario.png")));
        ImagePattern imagePattern = new ImagePattern(img);
        this.foto.setFill(imagePattern);
        txtRfc.textProperty().bind(maestro.rfcProperty());
        txtNombre.textProperty().bind(maestro.nombreProperty());
        txtApPat.textProperty().bind(maestro.apPatProperty());
        txtApMat.textProperty().bind(maestro.apMatProperty());
        txtCorreoInst.textProperty().bind(maestro.correoInsProperty());
        txtCorreoPer.textProperty().bind(maestro.correoPerProperty());
        txtDireccion.textProperty().bind(maestro.domicilioProperty());
        txtGrado.textProperty().bind(maestro.gradoProperty());
        txtMunicipio.textProperty().bind(maestro.municipioProperty());
        txtEstado.textProperty().bind(maestro.estadoProperty());
        txtCelular.textProperty().bind(maestro.celularProperty());
        txtPathCurriculo.textProperty().bind(maestro.cvProperty());
        txtCurp.textProperty().bind(maestro.curpProperty());
        comboSexo.setValue(maestro.getGenero());
    }

    @Override
    protected void cleanData() {
        maestro = null;
    }
}
