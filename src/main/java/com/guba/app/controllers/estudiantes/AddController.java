package com.guba.app.controllers.estudiantes;

import com.guba.app.dao.DAOAlumno;
import com.guba.app.dao.DAOCarreras;
import com.guba.app.conexion.Config;
import com.guba.app.controllers.BaseController;
import com.guba.app.controllers.Loadable;
import com.guba.app.controllers.Paginas;
import com.guba.app.models.Carrera;
import com.guba.app.models.Estudiante;
import com.guba.app.presentation.dialogs.DialogCamara;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.guba.app.presentation.utils.ComboCell;
import com.guba.app.presentation.utils.Constants;
import com.guba.app.service.local.database.Service;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

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
import java.util.stream.IntStream;


public class AddController extends BaseController implements Initializable, Loadable<Estudiante> {

    @FXML
    private Button backButton;
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
    private BufferedImage bufferedImage;
    private Estudiante estudiante;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCombos();
    }

    @FXML
    private void regresarAPanel(ActionEvent actionEvent) {
        estudiante = null;
        mediador.changePane(Paginas.LIST);
    }



    @FXML
    private void abrirCamara() {
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


    @FXML
    private void borrarFoto(){
        bufferedImage = null;
        fotoEstudiante.setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResource(Constants.URL_FOTO_PREDETERMINADA)).toString())));
        estudiante.setFoto(null);
    }

    @FXML
    public void guardarEstudiante(ActionEvent event) {
        if (!esValido()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ingrese toda la información para continuar");
            alert.showAndWait();
            return;
        }
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estas Seguro de Añadir al Estudiate?");
        dialogConfirmacion.showAndWait().ifPresent(integer -> {
            if (bufferedImage != null) {
                estudiante.setFoto(guardarFoto());
            }

            boolean seAgrego = Service.getService().agregarAlumno(estudiante, integer);

            if (seAgrego){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Alumno creado Correctamente");
                alert.showAndWait();
                mediador.changePane(Paginas.LIST);
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error al crear intente contactase por favor");
                alert.showAndWait();
            }
        });

    }


    private void initializeCombos() {
        comboSemestre.getItems().addAll(IntStream.range(1, 10).boxed().map(Object::toString).toList());
        comboSexo.getItems().addAll("Hombre", "Mujer");
        comboStatus.getItems().addAll("Activo", "Baja", "Baja Temporal");
        initializeComboCarrera();
    }

    private void initializeComboCarrera() {
        comboCarrera.setButtonCell(new ComboCell<>());
        comboCarrera.setCellFactory(new Callback<ListView<Carrera>, ListCell<Carrera>>() {
            @Override
            public ListCell<Carrera> call(ListView<Carrera> carreraListView) {
                return new ComboCell<>();
            }
        });
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

    private boolean mostrarConfirmacion() {
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estás seguro de añadir al alumno: " + txtNombre.getText() + " con matrícula: " + txtMatricula.getText() + "?");
        return dialogConfirmacion.showAndWait().orElse(0) == 1;
    }



    @Override
    public void loadData(Estudiante data) {
        this.estudiante = data;
        System.out.println(estudiante);
        ImagePattern imagePattern = new ImagePattern(estudiante.getFotoPerfil() != null ? estudiante.fotoPerfilProperty().get() : new Image(Objects.requireNonNull(getClass().getResource(Constants.URL_FOTO_PREDETERMINADA)).toString()));
        fotoEstudiante.setFill(imagePattern);
        txtMatricula.textProperty().bindBidirectional(estudiante.matriculaProperty());
        txtNombre.textProperty().bindBidirectional(estudiante.nombreProperty());
        txtApPat.textProperty().bindBidirectional(estudiante.apPaternoProperty());
        txtApMat.textProperty().bindBidirectional(estudiante.apMaternoProperty());
        txtCorreoInst.textProperty().bindBidirectional(estudiante.emailInstitucionalProperty());
        txtCorreoPer.textProperty().bindBidirectional(estudiante.emailPersonalProperty());
        txtGeneracion.textProperty().bindBidirectional(estudiante.generacionProperty());
        txtDireccion.textProperty().bindBidirectional(estudiante.direccionProperty());
        txtEscuelaProc.textProperty().bindBidirectional(estudiante.escProcedenciaProperty());
        txtGrado.textProperty().bindBidirectional(estudiante.gradoProperty());
        txtMunicipio.textProperty().bindBidirectional(estudiante.municipioProperty());
        txtEstado.textProperty().bindBidirectional(estudiante.estadoProperty());
        txtCelular.textProperty().bindBidirectional(estudiante.numCelularProperty());
        Bindings.bindBidirectional(comboStatus.valueProperty(), estudiante.statusProperty());
        Bindings.bindBidirectional(comboCarrera.valueProperty(), estudiante.carreraProperty());
        Bindings.bindBidirectional(comboSemestre.valueProperty(), estudiante.semestreProperty());
        estudiante.passwordTemporalProperty().bind(txtMatricula.textProperty());
        comboNacimiento.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                DateTimeFormatter format =  DateTimeFormatter.ofPattern("dd/MM/yyyy");
                estudiante.nacimientoProperty().set(format.format(t1));
            }
        });
        loadCarrerasAsync();
    }
}
