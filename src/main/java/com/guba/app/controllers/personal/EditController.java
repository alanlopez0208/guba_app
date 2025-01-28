package com.guba.app.controllers.personal;

import com.guba.app.dao.DAOPersonal;
import com.guba.app.conexion.Config;
import com.guba.app.controllers.BaseController;
import com.guba.app.controllers.Loadable;
import com.guba.app.controllers.Paginas;
import com.guba.app.models.Personal;
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

public class EditController extends BaseController<Personal> implements Initializable, Loadable<Personal> {
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
        mediador.changePane(Paginas.LIST);

    }

    @FXML
    private void actualizar(){
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Esta seguro de actualizar?");
        dialogConfirmacion.showAndWait().ifPresent(integer1 -> {
            if (!validar()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No Has ingresado datos correcto, porfavor verifica");
                alert.showAndWait();
            }
            personal.setRfc(txtRfc.getText());
            personal.setCurp(txtCurp.getText());
            personal.setNombre(txtNombre.getText());
            personal.setApPat(txtApPat.getText());
            personal.setApMat(txtApMat.getText());
            personal.setCorreoIns(txtCorreoInst.getText());
            personal.setCorreoPer(txtCorreoPer.getText());
            personal.setDomicilio(txtDireccion.getText());
            personal.setMatricula(txtMatricula.getText());
            personal.setMunicipio(txtMunicipio.getText());
            personal.setEstado(txtEstado.getText());
            personal.setCelular(txtCelular.getText());
            personal.setGenero(comboSexo.getValue());
            if (bufferedImage != null) {
                personal.setFoto(guardarFoto());
            }

            boolean seActualizo = daoPersonal.updatePersonal(personal);
            Alert alert = new Alert(seActualizo ? Alert.AlertType.CONFIRMATION : Alert.AlertType.ERROR);
            alert.setContentText(seActualizo ? "Se ha registrado el alumno con exito" : "Hay un error al guardar al Alumno intente mas tarde porfavor");
            alert.showAndWait();
            mediador.changePane(Paginas.LIST);
        });
    }

    private boolean validar(){
        return !txtRfc.getText().isEmpty();
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
            return  outputFile.getAbsolutePath();
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
        if (personal.getFoto() != null){
            String  path= "file:///"+personal.getFoto().replace("\\","/");
            System.out.println(path);
            File file = new File(path);
            System.out.println(file.exists());
            Image img = new Image(String.valueOf(file.exists() ?  file : getClass().getResource(Constants.URL_ASSETS+"img/usuario.png")));
            ImagePattern imagePattern = new ImagePattern(img);
            foto.setFill(imagePattern);
        }else{
            Image img = new Image(String.valueOf(getClass().getResource(Constants.URL_ASSETS+"img/usuario.png")));
            ImagePattern imagePattern = new ImagePattern(img);
            foto.setFill(imagePattern);
        }
        txtRfc.setText(personal.getRfc());
        txtNombre.setText(personal.getNombre());
        txtApPat.setText(personal.getApPat());
        txtApMat.setText(personal.getApMat());
        txtCorreoInst.setText(personal.getCorreoIns());
        txtCorreoPer.setText(personal.getCorreoPer());
        txtDireccion.setText(personal.getDomicilio());
        txtMatricula.setText(personal.getMatricula());
        txtMunicipio.setText(personal.getMunicipio());
        txtEstado.setText(personal.getEstado());
        txtCelular.setText(personal.getCelular());
        txtCurp.setText(personal.getCurp());
        comboSexo.setValue(personal.getGenero());
    }

}
