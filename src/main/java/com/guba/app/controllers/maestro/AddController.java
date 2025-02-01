package com.guba.app.controllers.maestro;

import com.guba.app.data.dao.DAOMaestro;
import com.guba.app.data.local.database.conexion.Config;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Maestro;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class AddController extends BaseController<Maestro> implements Initializable, Loadable<Maestro> {
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
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCamara;
    @FXML
    private Button btnArchivos;
    @FXML
    private Button btnBorrarFoto;
    @FXML
    private Button btnAddCurriculo;

    private Maestro maestro;
    private BufferedImage bufferedImage;
    private File file;

    public AddController(Mediador<Maestro> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super("/maestros/Add", mediador, estadoProperty, paginasProperty);
        this.btnCamara = btnCamara;
        comboSexo.getItems().addAll("Hombre", "Mujer");
        backButton.setOnAction(this::backPanel);
        btnGuardar.setOnAction(this::guardar);
        btnCamara.setOnAction(this::abrirCamara);
        btnArchivos.setOnAction(this::abrirArchivos);
        btnBorrarFoto.setOnAction(this::borrarFoto);
        btnAddCurriculo.setOnAction(this::addCurriculo);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void backPanel(ActionEvent actionEvent){
        cleanData();
        paginasProperty.set(Paginas.LIST);
    }

    private void guardar(ActionEvent actionEvent){
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Esta seguro de guardar?");
        dialogConfirmacion.showAndWait().ifPresent(integer1 -> {
            if (!validar()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No Has ingresado datos correcto, porfavor verifica");
                alert.showAndWait();
            }
            if (bufferedImage != null) {
                maestro.setFoto(
                        Utils.guardarFoto(
                                Config.getConif().obtenerConfiguracion("04 RUTA IMAGENES PROFESORES") + "\\" + maestro.getRfc() + ".jpg",
                                bufferedImage)
                );
            }
            if (file != null) {
                String destino = Config.getConif().obtenerConfiguracion("05 RUTA PDF PROFESORES") + "\\" + maestro.getRfc() + ".pdf";

                Path pathDestino = Paths.get(destino);

                String origen = file.getPath();
                Path pathOrigen = Paths.get(origen);
                try {
                    Files.copy(pathOrigen, pathDestino);
                    maestro.setCv(maestro.getRfc());
                } catch (IOException ex) {
                    maestro.setCv(null);
                    throw new RuntimeException(ex);
                }
            }
            boolean seAgrego = mediador.guardar(maestro);

            if (seAgrego) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Maestro creado Correctamente");
                alert.showAndWait();
                paginasProperty.set(Paginas.LIST);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error al crear intente contactase por favor");
                alert.showAndWait();
                paginasProperty.set(Paginas.LIST);
            }
        });
    }

    private boolean validar(){
        return !maestro.getRfc().isEmpty();
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

    private void borrarFoto(ActionEvent actionEvent){
        bufferedImage = null;
        foto.setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResource(Constants.URL_FOTO_PREDETERMINADA)).toString())));
        maestro.setFoto(null);
    }

    private void addCurriculo(ActionEvent actionEvent){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos pdf", "*.pdf")
        );
        file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            txtPathCurriculo.setText(file.getName()+".pdf");
        }
    }

    @Override
    public void loadData(Maestro data) {
        maestro = data;
        Image img = new Image(String.valueOf(maestro.getFoto() != null ?  new File(maestro.getFoto()).toURI().toString() : getClass().getResource(Constants.URL_ASSETS+"img/usuario.png")));
        ImagePattern imagePattern = new ImagePattern(img);
        this.foto.setFill(imagePattern);
        txtRfc.textProperty().bindBidirectional(maestro.rfcProperty());
        txtNombre.textProperty().bindBidirectional(maestro.nombreProperty());
        txtApPat.textProperty().bindBidirectional(maestro.apPatProperty());
        txtApMat.textProperty().bindBidirectional(maestro.apMatProperty());
        txtCorreoInst.textProperty().bindBidirectional(maestro.correoInsProperty());
        txtCorreoPer.textProperty().bindBidirectional(maestro.correoPerProperty());
        txtDireccion.textProperty().bindBidirectional(maestro.domicilioProperty());
        txtGrado.textProperty().bindBidirectional(maestro.gradoProperty());
        txtMunicipio.textProperty().bindBidirectional(maestro.municipioProperty());
        txtEstado.textProperty().bindBidirectional(maestro.estadoProperty());
        txtCelular.textProperty().bindBidirectional(maestro.celularProperty());
        txtCurp.textProperty().bindBidirectional(maestro.curpProperty());
        comboSexo.valueProperty().bindBidirectional(maestro.generoProperty());
    }

    @Override
    protected void cleanData() {
        maestro = null;
    }
}
