package com.guba.app.controllers.personal;

import com.guba.app.utils.BaseController;
import com.guba.app.utils.Loadable;
import com.guba.app.utils.Paginas;
import com.guba.app.domain.models.Personal;
import com.guba.app.presentation.utils.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailsController extends BaseController<Personal> implements Initializable, Loadable<Personal> {
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
    private TextField txtMunicipio;
    @FXML
    private TextField txtEstado;
    @FXML
    private TextField txtCelular;
    @FXML
    private TextField txtCurp;
    @FXML
    private TextField txtMatricula;
    @FXML
    private ComboBox<String> comboSexo;
    private Personal personal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboSexo.getItems().addAll("Hombre", "Mujer");
    }

    @FXML
    private void backPanel(){
        paginasProperty.set(Paginas.LIST);
    }

    @Override
    public void loadData(Personal data) {
        personal = data;
        Image img = new Image(String.valueOf(personal.getFoto() != null ?  "file:///"+personal.getFoto() : getClass().getResource(Constants.URL_ASSETS+"img/usuario.png")));
        ImagePattern imagePattern = new ImagePattern(img);
        foto.setFill(imagePattern);

        txtRfc.textProperty().bind(personal.rfcProperty());
        txtNombre.textProperty().bind(personal.nombreProperty());
        txtApPat.textProperty().bind(personal.apPatProperty());
        txtApMat.textProperty().bind(personal.apMatProperty());
        txtCorreoInst.textProperty().bind(personal.correoInsProperty());
        txtCorreoPer.textProperty().bind(personal.correoPerProperty());
        txtDireccion.textProperty().bind(personal.domicilioProperty());
        txtMatricula.textProperty().bind(personal.matriculaProperty());
        txtMunicipio.textProperty().bind(personal.municipioProperty());
        txtEstado.textProperty().bind(personal.estadoProperty());
        txtCelular.textProperty().bind(personal.celularProperty());
        txtCurp.textProperty().bind(personal.curpProperty());
        comboSexo.valueProperty().bind(personal.generoProperty());
    }

    @Override
    protected void cleanData() {

    }
}
