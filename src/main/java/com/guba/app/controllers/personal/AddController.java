package com.guba.app.controllers.personal;

import com.guba.app.data.dao.DAOPersonal;
import com.guba.app.data.local.database.conexion.Config;
import com.guba.app.utils.BaseController;
import com.guba.app.utils.Loadable;
import com.guba.app.utils.Paginas;
import com.guba.app.domain.models.Personal;
import com.guba.app.presentation.dialogs.DialogCamara;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.guba.app.presentation.utils.Constants;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
    private DAOPersonal daoPersonal = new DAOPersonal();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboSexo.getItems().addAll("Hombre", "Mujer");
    }

    @FXML
    private void backPanel(){
        paginasProperty.set(Paginas.LIST);
    }

    @FXML
    private void guardar(){
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Esta seguro de actualizar?");
        dialogConfirmacion.showAndWait().ifPresent(integer1 -> {
            if (!validar()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No Has ingresado datos correcto, porfavor verifica");
                alert.showAndWait();
            }
            if (bufferedImage != null) {
                personal.setFoto(guardarFoto());
            }
            Optional<Integer> optionalId = daoPersonal.crearPersonal(personal);
            optionalId.ifPresentOrElse(integer -> {
                personal.setId(integer.toString());
                //getLista().add(personal);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Personal creado Correctamente");
                alert.showAndWait();
                paginasProperty.set(Paginas.LIST);
            }, () -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error al crear intente contactase por favor");
                alert.showAndWait();
            });
        });
    }

    private boolean validar(){
        return !personal.getRfc().isEmpty();
    }

    @FXML
    private void abrirCamara() {
        DialogCamara dialogCamara = new DialogCamara();
        Optional<BufferedImage> optionBuffer = dialogCamara.showAndWait();
        optionBuffer.ifPresent(bufferedImage -> {
            this.bufferedImage = bufferedImage;
            WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight()));
            foto.setFill(new ImagePattern(writableImage));
        });
    }


    private String guardarFoto() {
        try {
            File outputFile = new File(Config.getConif().obtenerConfiguracion("05 RUTA IMAGENES PERSONAL") + "\\" + personal.getRfc() + ".jpg");
            ImageIO.write(bufferedImage, "jpg", outputFile);
            return  outputFile.getPath();
        } catch (IOException ex) {
            return null;
        }
    }



    @FXML
    private void borrarFoto(){
        bufferedImage = null;
        foto.setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResource(Constants.URL_FOTO_PREDETERMINADA)).toString())));
        personal.setFoto(null);
    }

    @FXML
    private void abrirArchivos() {
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
        txtRfc.textProperty().bindBidirectional(personal.rfcProperty());
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
        comboSexo.valueProperty().bindBidirectional(personal.generoProperty());
    }

    @Override
    protected void cleanData() {

    }
}
