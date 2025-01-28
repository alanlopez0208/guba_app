package com.guba.app.controllers.maestro;

import com.guba.app.dao.DAOMaestro;
import com.guba.app.conexion.Config;
import com.guba.app.controllers.BaseController;
import com.guba.app.controllers.Loadable;
import com.guba.app.controllers.Paginas;
import com.guba.app.models.Maestro;
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
    private Maestro maestro;
    private BufferedImage bufferedImage;
    private File file;
    private DAOMaestro daoMaestro = new DAOMaestro();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboSexo.getItems().addAll("Hombre", "Mujer");
    }

    @FXML
    private void backPanel(){
        mediador.changePane(Paginas.LIST);

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
                maestro.setFoto(guardarFoto());
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
            Optional<Integer> optionalId = daoMaestro.crearDocenteId(maestro);
            optionalId.ifPresentOrElse(integer -> {
                maestro.setId(integer.toString());
                getLista().add(maestro);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Maestro creado Correctamente");
                alert.showAndWait();
                mediador.changePane(Paginas.LIST);
            }, () -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error al crear intente contactase por favor");
                alert.showAndWait();
            });
        });
    }

    private boolean validar(){
        return !maestro.getRfc().isEmpty();
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
            File outputFile = new File(Config.getConif().obtenerConfiguracion("04 RUTA IMAGENES PROFESORES") + "\\" + maestro.getRfc() + ".jpg");
            ImageIO.write(bufferedImage, "jpg", outputFile);
            return  outputFile.getAbsolutePath();
        } catch (IOException ex) {
            return null;
        }
    }



    @FXML
    private void borrarFoto(){
        bufferedImage = null;
        foto.setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResource(Constants.URL_FOTO_PREDETERMINADA)).toString())));
        maestro.setFoto(null);
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

    @FXML
    private void addCurriculo(){
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

}
