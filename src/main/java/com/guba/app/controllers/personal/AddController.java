package com.guba.app.controllers.personal;

import com.guba.app.data.dao.DAOPersonal;
import com.guba.app.data.local.database.conexion.Config;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Personal;
import com.guba.app.presentation.dialogs.DialogCamara;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.guba.app.presentation.utils.Constants;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class AddController extends BaseController<Personal> implements Initializable, Loadable<Personal> {

    @FXML
    private Button backButton;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCamera;
    @FXML
    private Button btnArchivos;
    @FXML
    private Button btnBorrar;
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
    private BufferedImage bufferedImage;
    private File file;

    public AddController(Mediador<Personal> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super( "/personal/Add", mediador, estadoProperty, paginasProperty);
        comboSexo.getItems().addAll("Hombre", "Mujer");
        backButton.setOnAction(this::backPanel);
        btnGuardar.setOnAction(this::guardar);
        btnCamera.setOnAction(this::abrirCamara);
        btnArchivos.setOnAction(this::abrirArchivos);
        btnBorrar.setOnAction(this::borrarFoto);
        txtRfc.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                personal.setRfc(newValue);
            }
        });
        txtNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                personal.setNombre(newValue);
            }
        });
        txtApPat.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                personal.setApPat(newValue);
            }
        });
        txtApMat.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                personal.setApMat(newValue);
            }
        });
        txtCorreoInst.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                personal.setCorreoIns(newValue);
            }
        });
        txtCorreoPer.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                personal.setCorreoPer(newValue);
            }
        });
        txtDireccion.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                personal.setDomicilio(newValue);
            }
        });
        txtMatricula.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                personal.setMatricula(newValue);
            }
        });
        txtMunicipio.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                personal.setMunicipio(newValue);
            }
        });
        txtEstado.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                personal.setEstado(newValue);
            }
        });
        txtCelular.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                personal.setCelular(newValue);
            }
        });
        txtCurp.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                personal.setCurp(newValue);
            }
        });
        comboSexo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                personal.setGenero(newValue);
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void backPanel(ActionEvent actionEvent){
        paginasProperty.set(Paginas.LIST);
    }

    private void guardar(ActionEvent actionEvent){
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Esta seguro de agregar?");
        dialogConfirmacion.showAndWait().ifPresent(integer1 -> {
            if (!validar()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No Has ingresado datos correcto, porfavor verifica");
                alert.showAndWait();
            }
            if (bufferedImage != null) {
                personal.setFoto(Utils.guardarFoto(
                        Config.getConif().obtenerConfiguracion("05 RUTA IMAGENES PERSONAL") + "\\" + personal.getRfc() + ".jpg",
                        bufferedImage));
            }
            boolean seAgrego = mediador.guardar(personal);
            if (seAgrego){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Personal creado Correctamente");
                alert.showAndWait();
                paginasProperty.set(Paginas.LIST);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error al crear intente contactase por favor");
                alert.showAndWait();
            }
        });
    }

    private boolean validar(){
        return !personal.getRfc().isEmpty();
    }

    private void abrirCamara(ActionEvent actionEvent) {
        DialogCamara dialogCamara = new DialogCamara();
        Optional<BufferedImage> optionBuffer = dialogCamara.showAndWait();
        optionBuffer.ifPresent(bufferedImage -> {
            this.bufferedImage = bufferedImage;
            WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight()));
            foto.setFill(new ImagePattern(writableImage));
        });
    }

    private void borrarFoto(ActionEvent actionEvent){
        bufferedImage = null;
        foto.setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResource(Constants.URL_FOTO_PREDETERMINADA)).toString())));
        personal.setFoto(null);
    }

    private void abrirArchivos(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de imagen", "*.jpg")
        );
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            Task<BufferedImage> task = new Task<BufferedImage>() {
                @Override
                protected BufferedImage call() throws Exception {
                    return ImageIO.read(file);
                }
            };
            task.setOnSucceeded(event ->
                    Platform.runLater(() -> {
                        try {
                            this.bufferedImage = task.get();
                            WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight()));
                            foto.setFill(new ImagePattern(writableImage));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        }

                    })
            );
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }


    @Override
    public void loadData(Personal data) {
        personal = data;
        Image img = new Image(String.valueOf(personal.getFoto() != null ?  new File(personal.getFoto()).toURI().toString() : getClass().getResource(Constants.URL_ASSETS+"img/usuario.png")));
        ImagePattern imagePattern = new ImagePattern(img);
        this.foto.setFill(imagePattern);
        /*txtRfc.textProperty().bindBidirectional(personal.rfcProperty());
        txtNombre.textProperty().bindBidirectional(personal.nombreProperty());
        txtApPat.textProperty().bindBidirectional(personal.apPatProperty());
        txtApMat.textProperty().bindBidirectional(personal.apMatProperty());
        txtCorreoInst.textProperty().bindBidirectional(personal.correoInsProperty());
        txtCorreoPer.textProperty().bindBidirectional(personal.correoPerProperty());
        txtDireccion.textProperty().bindBidirectional(personal.domicilioProperty());
        txtMatricula.textProperty().bindBidirectional(personal.matriculaProperty());
        txtMunicipio.textProperty().bindBidirectional(personal.municipioProperty());
        txtEstado.textProperty().bindBidirectional(personal.estadoProperty());
        txtCelular.textProperty().bindBidirectional(personal.celularProperty());
        txtCurp.textProperty().bindBidirectional(personal.curpProperty());
        comboSexo.valueProperty().bindBidirectional(personal.generoProperty());*/
    }

    @Override
    protected void cleanData() {
        personal = null;
        txtRfc.setText(null);
        txtNombre.setText(null);
        txtApPat.setText(null);
        txtApMat.setText(null);
        txtCorreoInst.setText(null);
        txtCorreoPer.setText(null);
        txtDireccion.setText(null);
        txtMatricula.setText(null);
        txtMunicipio.setText(null);
        txtEstado.setText(null);
        txtCelular.setText(null);
        txtCurp.setText(null);
        comboSexo.setValue(null);

    }
}
