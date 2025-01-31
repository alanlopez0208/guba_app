package com.guba.app.controllers.estudiantes;

import com.guba.app.data.local.database.conexion.Config;
import com.guba.app.utils.Estado;
import com.guba.app.utils.*;
import com.guba.app.data.dao.DAOCarreras;
import com.guba.app.domain.models.Carrera;
import com.guba.app.domain.models.Estudiante;
import com.guba.app.presentation.dialogs.DialogCamara;
import com.guba.app.presentation.utils.Constants;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class EditController extends BaseController<Estudiante> implements Loadable<Estudiante>, Initializable {

    @FXML
    private Button backButton;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnCamara;
    @FXML
    private Button btnArchivos;
    @FXML
    private Button btnBorrarFoto;
    @FXML
    private VBox panelDatos;
    @FXML
    private Circle fotoEstudiante;
    @FXML
    private TextField txtMatricula;
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
    private TextField txtGeneracion;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtEscuelaProc;
    @FXML
    private TextField txtGrado;
    @FXML
    private TextField txtMunicipio;
    @FXML
    private TextField txtEstado;
    @FXML
    private TextField txtCelular;
    @FXML
    private ComboBox<Carrera> comboCarrera;
    @FXML
    private ComboBox<String> comboStatus;
    @FXML
    private ComboBox<String> comboSemestre;
    @FXML
    private ComboBox<String> comboSexo;
    @FXML
    private DatePicker comboNacimiento;
    private Estudiante estudiante;
    private BufferedImage bufferedImage;


    public EditController(Mediador<Estudiante> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super("/estudiantes/EditEstudiante", mediador, estadoProperty, paginasProperty);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(this::regresarAPanel);
        btnEdit.setOnAction(this::guardarEstudiante);
        btnCamara.setOnAction(this::abrirCamara);
        btnArchivos.setOnAction(this::abrirArchivos);
        btnBorrarFoto.setOnAction(this::borrarFoto);
        txtCelular.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")){
                return change;
            }
            return null;
        }));

    }

    private void abrirCamara(ActionEvent event) {
        DialogCamara dialogCamara = new DialogCamara();
        Optional<BufferedImage> optionBuffer = dialogCamara.showAndWait();
        optionBuffer.ifPresent(bufferedImage -> {
            this.bufferedImage = bufferedImage;
            WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight()));
            fotoEstudiante.setFill(new ImagePattern(writableImage));
        });
    }

    private String guardarFoto() {
        try {
            File outputFile = new File(Config.getConif().obtenerConfiguracion("04 RUTA IMAGENES PROFESORES") + "\\" + estudiante.getMatricula() + ".jpg");
            ImageIO.write(bufferedImage, "jpg", outputFile);
            return  outputFile.getAbsolutePath();
        } catch (IOException ex) {
            return null;
        }
    }

    private void abrirArchivos(ActionEvent event) {
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
            task.setOnSucceeded(workerStateEvent ->
                    Platform.runLater(() -> {
                        try {
                            this.bufferedImage = task.get();
                            WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight()));
                            fotoEstudiante.setFill(new ImagePattern(writableImage));
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

    public void guardarEstudiante(ActionEvent event) {
        if (!esValido()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ingrese toda la información para continuar");
            alert.showAndWait();
            return;
        }
        if (!mostrarConfirmacion()) {
            return;
        }
        estudiante.setMatricula(txtMatricula.getText());
        estudiante.setNombre(txtNombre.getText());
        estudiante.setApMaterno(txtApMat.getText());
        estudiante.setApPaterno(txtApPat.getText());
        estudiante.setEmailInstitucional(txtCorreoInst.getText());
        estudiante.setEmailPersonal(txtCorreoPer.getText());
        estudiante.setGeneracion(txtGeneracion.getText());
        estudiante.setDireccion(txtDireccion.getText());
        estudiante.setEscProcedencia(txtEscuelaProc.getText());
        estudiante.setGrado(txtGrado.getText());
        estudiante.setMunicipio(txtMunicipio.getText());
        estudiante.setEstado(txtEstado.getText());
        estudiante.setNumCelular(txtCelular.getText());
        estudiante.setCarrera(comboCarrera.getValue());
        estudiante.setStatus(comboStatus.getValue());
        estudiante.setSemestre(comboSemestre.getValue());
        estudiante.setSexo(comboSexo.getValue());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (comboNacimiento.getValue() != null) {
            estudiante.setNacimiento(comboNacimiento.getValue().format(formatter));
        }
        if (bufferedImage != null) {
            estudiante.setFoto(guardarFoto());
        }
        boolean seGuardo = mediador.actualizar(estudiante);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(seGuardo ? "Se ha Actualizado el alumno con exito":  "Hay un error al guardar al Alumno intente mas tarde porfavor" );

        alert.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType buttonType) {
                if (buttonType == ButtonType.OK){
                    paginasProperty.set(Paginas.LIST);
                }
            }
        });

    }

    private void borrarFoto(ActionEvent event){
        bufferedImage = null;
        fotoEstudiante.setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResource(Constants.URL_FOTO_PREDETERMINADA)).toString())));
        estudiante.setFoto(null);
        estudiante.setFotoPerfil(null);
    }

    private void regresarAPanel(ActionEvent actionEvent) {
        paginasProperty.set(Paginas.LIST);
    }

    @Override
    public void loadData(Estudiante data) {
        this.estudiante = data;
        ImagePattern imagePattern = new ImagePattern(estudiante.getFotoPerfil() != null ?  estudiante.getFotoPerfil() : new Image(Objects.requireNonNull(getClass().getResource(Constants.URL_ASSETS + "img/usuario.png")).toString()));
        fotoEstudiante.setFill(imagePattern);
        txtMatricula.setText(estudiante.getMatricula());
        txtNombre.setText(estudiante.getNombre());
        txtApMat.setText(estudiante.getApMaterno());
        txtApPat.setText(estudiante.getApPaterno());
        txtCorreoInst.setText(estudiante.getEmailInstitucional());
        txtCorreoPer.setText(estudiante.getEmailPersonal());
        txtGeneracion.setText(estudiante.getGeneracion());
        txtDireccion.setText(estudiante.getDireccion());
        txtEscuelaProc.setText(estudiante.getEscProcedencia());
        txtGrado.setText(estudiante.getGrado());
        txtMunicipio.setText(estudiante.getMunicipio());
        txtEstado.setText(estudiante.getEstado());
        txtCelular.setText(estudiante.getNumCelular());
        comboCarrera.setValue(estudiante.getCarrera());
        comboStatus.setValue(estudiante.getStatus());
        comboSemestre.setValue(estudiante.getSemestre());
        comboSexo.setValue(estudiante.getSexo());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        comboNacimiento.setValue(LocalDate.parse(estudiante.getNacimiento(), formatter));
        loadCarrerasAsync();
    }

    private boolean mostrarConfirmacion() {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setContentText("¿Estás seguro de añadir al alumno: " + txtNombre.getText() + " con matrícula: " + txtMatricula.getText() + "?");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(buttonType -> (buttonType == ButtonType.OK) ? 1 : 0);
        return dialog.showAndWait().orElse(0) == 1;
    }


    private boolean esValido() {
        System.out.println(estudiante.getNombre());
        System.out.println(estudiante.getMatricula());
        if (estudiante.getNombre() == null || estudiante.getNombre().isEmpty()) {
            return false;
        }
        if (estudiante.getMatricula() == null || estudiante.getMatricula().isEmpty()) {
            return false;
        }
        if (estudiante.getEscProcedencia() == null || estudiante.getEscProcedencia().isEmpty()) {
            return false;
        }
        if (estudiante.getMunicipio() == null || estudiante.getMunicipio().isEmpty()) {
            return false;
        }
        if (estudiante.getCarrera() == null) {
            return false;
        }
        if (estudiante.getNacimiento() == null) {
            return false;
        }
        return true;
    }


    private void loadCarrerasAsync() {
        Task<List<Carrera>> task = new Task<>() {
            @Override
            protected List<Carrera> call() throws Exception {
                return new DAOCarreras().getAllCarreras();
            }
        };
        task.setOnSucceeded(event -> {
            try {
                comboCarrera.getItems().addAll(task.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(task).start();
    }

    @Override
    protected void cleanData() {
        fotoEstudiante.setFill(null);
        txtMatricula.setText(null);
        txtNombre.setText(null);
        txtApMat.setText(null);
        txtApPat.setText(null);
        txtCorreoInst.setText(null);
        txtCorreoPer.setText(null);
        txtGeneracion.setText(null);
        txtDireccion.setText(null);
        txtEscuelaProc.setText(null);
        txtGrado.setText(null);
        txtMunicipio.setText(null);
        txtEstado.setText(null);
        txtCelular.setText(null);
        comboCarrera.setValue(null);
        comboStatus.setValue(null);
        comboSemestre.setValue(null);
        comboSexo.setValue(null);
        comboNacimiento.setValue(null);
    }
}
