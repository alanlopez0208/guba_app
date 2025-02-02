package com.guba.app.controllers.estudiantes;

import com.dlsc.gemsfx.CalendarPicker;
import com.dlsc.gemsfx.TimePicker;
import com.guba.app.data.dao.DAOAlumno;
import com.guba.app.data.dao.DAOCalificiaciones;
import com.guba.app.data.dao.DAOCarreras;
import com.guba.app.utils.Estado;
import com.guba.app.utils.*;
import com.guba.app.data.dao.DAOTitulo;
import com.guba.app.domain.dto.AlumnoDto;
import com.guba.app.domain.models.*;
import com.guba.app.presentation.dialogs.DialogLogin;
import com.guba.app.presentation.utils.ComboCell;
import com.guba.app.presentation.utils.Constants;
import com.guba.app.data.local.poi.Documentos;
import com.guba.app.data.local.poi.WordModifier;
import com.guba.app.data.remote.Converter;
import com.guba.app.data.remote.Http;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class DetailsController extends BaseController<Estudiante> implements Loadable<Estudiante> {

    @FXML
    private VBox panelDatos;
    @FXML
    private VBox panelCalificaciones;
    @FXML
    private VBox panelTitulo;
    @FXML
    private VBox panelAdicionales;
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
    @FXML
    private TableView<Calificacion> tablaCalificaciones;
    @FXML
    private TableColumn<Calificacion, String> columnClaveMateria;
    @FXML
    private TableColumn<Calificacion, String> columnNombreMateria, columnU1P1,columnU1P2,columnU1P3,columnU1P4,columnU1PM,
            columnU2P1,columnU2P2,columnU2P3,columnU2P4,columnU2PM,
            columnU3P1,columnU3P2,columnU3P3,columnU3P4,columnU3PM,
            columnU4P1,columnU4P2,columnU4P3,columnU4P4,columnU4PM,
            columnTF,columnPromedio;
    @FXML
    private TextField libroField;
    @FXML
    private TextField actaField;
    @FXML
    private TextField fojaField;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField folioFiled;
    @FXML
    private TextField tipoField;
    @FXML
    private CalendarPicker dateAplicacion;
    @FXML
    private TimePicker timeInicio;
    @FXML
    private TimePicker timeFinal;
    @FXML
    private TextField registroField;
    @FXML
    private TextField presidenteField;
    @FXML
    private TextField secretarioField;
    @FXML
    private TextField vocalField;
    @FXML
    private TextField usuarioField;
    @FXML
    private PasswordField paswordField;
    @FXML
    private ComboBox<Documentos> comboDocumentos;
    @FXML
    private CalendarPicker dateFechaDoc;
    @FXML
    private JFXButton btnGuardarTitulo;
    @FXML
    private JFXButton btnEditarTitulo;
    @FXML
    private JFXButton btnGenerarDocumento;
    @FXML
    private JFXButton btnPassword;
    @FXML
    private Button btnDetals,btnCalificaciones,btnTitulacion,btnAdicionales,backButton;


    private Estudiante estudiante;
    private Titulo titulo;
    private DAOTitulo daoTitulo = new DAOTitulo();
    private DAOAlumno daoAlumno = new DAOAlumno();
    private WordModifier wordModifier = new WordModifier();
    private DAOCalificiaciones daoCalificiaciones = new DAOCalificiaciones();
    private ObservableList<Calificacion> calificacions;
    private Http http = new Http();
    private Converter converter = new Converter();

    public DetailsController(Mediador<Estudiante> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super(  "/estudiantes/DetailsEstudiante", mediador, estadoProperty, paginasProperty);
        inicializarPaneles();
        inicializarCombos();
        setColumns();
        settimePickers();
        setColumnsTableCalificaciones();
        backButton.setOnAction(this::backPanel);
        btnDetals.setOnAction(this::openDatos);
        btnCalificaciones.setOnAction(this::openCalificaciones);
        btnTitulacion.setOnAction(this::openTitulo);
        btnAdicionales.setOnAction(this::openAdicionales);
        btnGuardarTitulo.setOnAction(this::guardarTitulo);
        btnEditarTitulo.setOnAction(this::editarTitulo);
        btnGenerarDocumento.setOnAction(this::generarDocumentos);
        btnPassword.setOnAction(this::verPassword);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void backPanel(ActionEvent event){
        paginasProperty.set(Paginas.LIST);
    }

    private void openDatos(ActionEvent event) {
        if (panelDatos.isVisible()){
            return;
        }
        actualizarSeleccion(btnDetals, panelDatos, true);
        actualizarSeleccion(btnCalificaciones, panelCalificaciones, false);
        actualizarSeleccion(btnTitulacion, panelTitulo, false);
        actualizarSeleccion(btnAdicionales, panelAdicionales, false);
    }

    private void openCalificaciones(ActionEvent event) {
        if (panelCalificaciones.isVisible()){
            return;
        }
        actualizarSeleccion(btnDetals, panelDatos, false);
        actualizarSeleccion(btnCalificaciones, panelCalificaciones, true);
        actualizarSeleccion(btnTitulacion, panelTitulo, false);
        actualizarSeleccion(btnAdicionales, panelAdicionales, false);
    }

    private void openTitulo(ActionEvent event) {
        if (panelTitulo.isVisible()){
            return;
        }
        actualizarSeleccion(btnDetals, panelDatos, false);
        actualizarSeleccion(btnCalificaciones, panelCalificaciones, false);
        actualizarSeleccion(btnTitulacion, panelTitulo, true);
        actualizarSeleccion(btnAdicionales, panelAdicionales, false);
    }


    private void openAdicionales(ActionEvent event) {
        if (panelAdicionales.isVisible()){
            return;
        }
        actualizarSeleccion(btnDetals, panelDatos, false);
        actualizarSeleccion(btnCalificaciones, panelCalificaciones, false);
        actualizarSeleccion(btnTitulacion, panelTitulo, false);
        actualizarSeleccion(btnAdicionales, panelAdicionales, true);
    }


    private void actualizarSeleccion(Button boton, Pane panel, boolean seleccionado) {
        if (seleccionado) {
            boton.getStyleClass().add("seleccionado");
            panel.setVisible(true);
        } else {
            boton.getStyleClass().remove("seleccionado");
            panel.setVisible(false);
        }
    }

    private void guardarTitulo(ActionEvent event){
        loadDataToTiulo();
        System.out.println("ESTE ES EL ACTA DEL TITULO: " + titulo.getActa());
        boolean seCreo = daoTitulo.crearTitulacion(titulo);
        if (seCreo){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Se ha creado el titulo con exito");
            alert.show();
            btnGuardarTitulo.setVisible(false);
            btnEditarTitulo.setVisible(true);
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    wordModifier.rellarTitulo(estudiante, titulo);
                    return null;
                }
            };
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error al crear el titulo intentelo de nuevo porfavor");
            alert.show();
        }
    }

    private void editarTitulo(ActionEvent event){
        loadDataToTiulo();
        boolean seCreo = daoTitulo.updateTitulacionByIdAlumno(titulo);
        if (seCreo){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Se ha actualizado el titulo con exito");
            alert.show();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error al actualizar el titulo intentelo de nuevo porfavor");
            alert.show();
        }
    }

    private void generarDocumentos(ActionEvent event){
        if (!comboDocumentos.getSelectionModel().isEmpty() && dateFechaDoc.getValue() != null){
            Documentos documento = comboDocumentos.getSelectionModel().getSelectedItem();
            DateTimeFormatter formatterEsp = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale.Builder().setLanguage("es").setRegion("MX").build());
            wordModifier.modifyDocument(documento,estudiante,formatterEsp.format(dateFechaDoc.getValue()));
        }
    }


    private void verPassword(ActionEvent event){
           DialogLogin dialogLogin = new DialogLogin();
           dialogLogin.showAndWait().ifPresent(aBoolean -> {
               Task<Estudiante> task = new Task<Estudiante>() {
                   @Override
                   protected Estudiante call() throws Exception {
                       String body = http.request("SELECT * FROM Estudiantes");
                       AlumnoDto alumnoDto = converter.getData(body, AlumnoDto.class );
                       return new Estudiante(alumnoDto);
                   }
               };
               task.setOnSucceeded(workerStateEvent -> {
                   try {
                       daoAlumno.updatePassword(task.get());
                       Alert alert = new Alert(Alert.AlertType.INFORMATION);
                       alert.setTitle("Contraseña del usuario");
                       alert.setContentText(estudiante.getPassword());
                       alert.show();
                   } catch (InterruptedException e) {
                       throw new RuntimeException(e);
                   } catch (ExecutionException e) {
                       throw new RuntimeException(e);
                   }
               });
               Thread thread = new Thread(task);
               thread.setDaemon(true);
               thread.start();
           });
    }

    private void inicializarPaneles(){
        panelDatos.setVisible(true);
        panelCalificaciones.setVisible(false);
        panelTitulo.setVisible(false);
        panelAdicionales.setVisible(false);
    }


    private void inicializarCombos(){
        comboSemestre.getItems().addAll(IntStream.range(1, 10).boxed().map(Object::toString).toList());
        comboSexo.getItems().addAll("Hombre", "Mujer");
        comboStatus.getItems().addAll("Activo", "Baja", "Baja Temporal");
        comboCarrera.setButtonCell(new ComboCell<>());
        comboCarrera.setCellFactory(carreraListView -> new ListCell<>() {
            @Override
            protected void updateItem(Carrera item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.getNombre());
            }
        });
        comboDocumentos.getItems().addAll(Documentos.values());
        comboDocumentos.setCellFactory(new Callback<ListView<Documentos>, ListCell<Documentos>>() {
            @Override
            public ListCell<Documentos> call(ListView<Documentos> documentosListView) {
                return new ListCell<>(){
                    @Override
                    protected void updateItem(Documentos item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty){
                            setText(null);
                        }else{
                            setText(item.getNombre());
                        }
                    }
                };
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


    public void loadData(Estudiante e) {
        this.estudiante = e;
        Image img = new Image(
                String.valueOf(estudiante.getFoto() != null ?
                        new File(estudiante.getFoto()).toURI().toString() :
                        getClass().getResource(Constants.URL_ASSETS+"img/usuario.png")));

        ImagePattern imagePattern = new ImagePattern(img);
        fotoEstudiante.setFill(imagePattern);
        System.out.println(estudiante.getCarrera());
        txtMatricula.setText(estudiante.getMatricula());
        txtNombre.setText(estudiante.getNombre());
        txtApPat.setText(estudiante.getApPaterno());
        txtApMat.setText(estudiante.getApMaterno());
        txtCorreoInst.setText(estudiante.getEmailInstitucional());
        txtCorreoPer.setText(estudiante.getEmailPersonal());
        txtGeneracion.setText(estudiante.getGeneracion());
        txtDireccion.setText(estudiante.getDireccion());
        txtEscuelaProc.setText(estudiante.getEscProcedencia());
        txtGrado.setText(estudiante.getGrado());
        txtMunicipio.setText(estudiante.getMunicipio());
        txtEstado.setText(estudiante.getEstado());
        txtCelular.setText(estudiante.getNumCelular());
        comboCarrera.getSelectionModel().select(estudiante.getCarrera());
        comboStatus.setValue(estudiante.getStatus());
        comboSemestre.setValue(estudiante.getSemestre());
        comboSexo.setValue(estudiante.getSexo());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        comboNacimiento.setValue(LocalDate.parse(estudiante.getNacimiento(), formatter));
        loadTitutoAsync();
        loadCalificaciones(estudiante.getId(), list -> {
            calificacions = FXCollections.observableArrayList(list);
            tablaCalificaciones.setItems(calificacions);
        });
        setUserAndPasword();
        loadCarrerasAsync();
    }


    private void setColumns(){
        columnU1P1.setCellValueFactory(new PropertyValueFactory<>("p1U1"));
    }

    private void settimePickers(){
        timeInicio.setStepRateInMinutes(10);
        timeFinal.setStepRateInMinutes(10);

    }

    private void loadTitutoAsync(){
        Task<Titulo> task = new Task<Titulo>() {
            @Override
            protected Titulo call() throws Exception {
                Titulo titulo1 = daoTitulo.getTitulacionByIdAlumno(estudiante.getId());
               return titulo1;
            }
        };
        task.setOnSucceeded(event -> {
            try {
                this.titulo = task.get();
                if (titulo == null){
                    titulo = new Titulo();
                    btnGuardarTitulo.setVisible(true);
                    btnEditarTitulo.setVisible(false);
                }else{
                    btnEditarTitulo.setVisible(true);
                    btnGuardarTitulo.setVisible(false);
                }
                setFieldTitulo();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        task.setOnFailed(event -> System.out.println("Error al obtener el titulo" + task.getException()));
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void loadCalificaciones(String idAlumno, Consumer<List<Calificacion>> callback){
        Task<List<Calificacion>> task = new Task<List<Calificacion>>() {
            @Override
            protected List<Calificacion> call() throws Exception {
                return daoCalificiaciones.obtenerCalificaciones(idAlumno);
            }
        };

        task.setOnSucceeded(event -> {
            try {
                callback.accept(task.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        task.setOnFailed(event -> System.out.println("ERROR: "+ task.getException()));

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }


    private void loadDataToTiulo(){
        titulo.setIdAlumno(estudiante.getId());
        titulo.setActa(actaField.getText());
        titulo.setRegistro(registroField.getText());
        titulo.setLibro(libroField.getText());
        titulo.setFoja(fojaField.getText());
        titulo.setFolio(folioFiled.getText());
        titulo.setTipoExamen(tipoField.getText());
        titulo.setFechaAplicacion(titulo.toStringDate(dateAplicacion.getValue()));
        titulo.setHoraAplicacion(timeInicio.getTime().toString());
        titulo.setHoraFinalizacion(timeFinal.getTime().toString());
        titulo.setDuracion(String.valueOf(timeInicio.getTime().until(timeFinal.getTime(), ChronoUnit.MINUTES)));
        titulo.setSecretario(secretarioField.getText());
        titulo.setPresidente(presidenteField.getText());
        titulo.setVocal(vocalField.getText());
        titulo.setNombre(nombreField.getText());
    }


    private void setFieldTitulo(){
        actaField.setText(titulo.getActa());
        registroField.setText(titulo.getRegistro());
        libroField.setText(titulo.getLibro());
        fojaField.setText(titulo.getFoja());
        tipoField.setText(titulo.getTipoExamen());
        dateAplicacion.setValue(titulo.toDateString(titulo.getFechaAplicacion()));
        timeInicio.setTime(titulo.stringToTime(titulo.getHoraAplicacion()));
        timeFinal.setTime(titulo.stringToTime(titulo.getHoraFinalizacion()));
        secretarioField.setText(titulo.getSecretario());
        presidenteField.setText(titulo.getPresidente());
        vocalField.setText(titulo.getVocal());
        nombreField.setText(titulo.getNombre());
    }


    private void setColumnsTableCalificaciones(){
        columnNombreMateria.setCellValueFactory(calificacionStringCellDataFeatures -> {
            Calificacion calificacion = calificacionStringCellDataFeatures.getValue();
            return Bindings.createStringBinding(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    if (calificacion.getMateria() != null){
                        return calificacion.getMateria().getNombre();
                    }
                    return null;
                }
            },calificacion.materiaProperty());
        });
        columnClaveMateria.setCellValueFactory(calificacionStringCellDataFeatures -> {
            Calificacion calificacion = calificacionStringCellDataFeatures.getValue();
            return Bindings.createStringBinding(() -> {
                if (calificacion.getMateria() != null){
                    return calificacion.getMateria().getClave();
                }
                return null;
            },calificacion.materiaProperty());
        });
        columnU1PM.setCellValueFactory(calificacionStringCellDataFeatures -> {
            Calificacion calificacion = calificacionStringCellDataFeatures.getValue();
            return Bindings.createStringBinding(
                    () -> {
                        if (calificacion.getPromedioU1() != null){
                            return String.format("%.2f", calificacion.getPromedioU1());
                        }
                        return "";
                    }, calificacion.promedioU1Property()
            );
        });
        columnU2PM.setCellValueFactory(calificacionStringCellDataFeatures -> {
            Calificacion calificacion = calificacionStringCellDataFeatures.getValue();
            return Bindings.createStringBinding(
                    () -> {
                        if (calificacion.getPromedioU2() != null){
                            return String.format("%.2f", calificacion.getPromedioU2());
                        }
                        return "";
                    }, calificacion.promedioU2Property()
            );
        });
        columnU3PM.setCellValueFactory(calificacionStringCellDataFeatures -> {
            Calificacion calificacion = calificacionStringCellDataFeatures.getValue();
            return Bindings.createStringBinding(
                    () -> {
                        if (calificacion.getPromedioU3() != null){
                            return String.format("%.2f", calificacion.getPromedioU3());
                        }
                        return "";
                    }, calificacion.promedioU3Property()
            );
        });

        columnU4PM.setCellValueFactory(calificacionStringCellDataFeatures -> {
            Calificacion calificacion = calificacionStringCellDataFeatures.getValue();
            return Bindings.createStringBinding(
                    () -> {
                        if (calificacion.getPromedioU4() != null){
                            return String.format("%.2f", calificacion.getPromedioU4());
                        }
                        return "";
                    }, calificacion.promedioU4Property()
            );
        });

        columnPromedio.setCellValueFactory(calificacionStringCellDataFeatures -> {
            Calificacion calificacion = calificacionStringCellDataFeatures.getValue();
            return Bindings.createStringBinding(
                    () -> {
                        if (calificacion.getPromedioFinal() != null){
                            return String.format("%.2f", calificacion.getPromedioFinal());
                        }
                        return "";
                    }, calificacion.promedioFinalProperty()
            );
        });

        setUpColumnsEditable(columnU1P1, Calificacion::p1U1Property);
        setUpColumnsEditable(columnU1P2, Calificacion::p2U1Property);
        setUpColumnsEditable(columnU1P3, Calificacion::p3U1Property);
        setUpColumnsEditable(columnU1P4, Calificacion::p4U1Property);
        setUpColumnsEditable(columnU2P1, Calificacion::p1U2Property);
        setUpColumnsEditable(columnU2P2, Calificacion::p2U2Property);
        setUpColumnsEditable(columnU2P3, Calificacion::p3U2Property);
        setUpColumnsEditable(columnU2P4, Calificacion::p4U2Property);
        setUpColumnsEditable(columnU3P1, Calificacion::p1U3Property);
        setUpColumnsEditable(columnU3P2, Calificacion::p2U3Property);
        setUpColumnsEditable(columnU3P3, Calificacion::p3U3Property);
        setUpColumnsEditable(columnU3P4, Calificacion::p4U3Property);
        setUpColumnsEditable(columnU4P1, Calificacion::p1U4Property);
        setUpColumnsEditable(columnU4P2, Calificacion::p2U4Property);
        setUpColumnsEditable(columnU4P3, Calificacion::p3U4Property);
        setUpColumnsEditable(columnU4P4, Calificacion::p4U4Property);
        setUpColumnsEditable(columnTF, Calificacion::trabjoFinalProperty);
    }


    private void setUpColumnsEditable(TableColumn<Calificacion,String> tableColumn,
                                      Callback<Calificacion, ObjectProperty<Float>> propertyCellDataFeatures){

        tableColumn.setCellValueFactory(calificacionStringCellDataFeatures -> {
            Calificacion calificacion = calificacionStringCellDataFeatures.getValue();
            ObjectProperty<Float> property = propertyCellDataFeatures.call(calificacion);
            return Bindings.createStringBinding(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    if (property.get() != null){
                        return String.valueOf(property.get());
                    }
                    return "";
                }
            },property);
        });
        tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Calificacion, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Calificacion, String> event) {
                Calificacion calificacion = event.getRowValue();
                ObjectProperty<Float> property = propertyCellDataFeatures.call(calificacion);
                try {
                    Float valor = Float.parseFloat(event.getNewValue());
                    property.setValue(valor);
                }catch (NumberFormatException e){
                    property.set(null);
                    Platform.runLater(() -> {
                        event.getTableView().refresh();
                    });
                }
                calificacion.establecerPromedioFinal();
                System.out.println(calificacion.getTrabjoFinal()+" " +calificacion.getPromedioU1());
                daoCalificiaciones.updateCalificacion(calificacion);
            }
        });
    }

    private void setUserAndPasword(){
        usuarioField.setText(estudiante.getMatricula());
        paswordField.setText(estudiante.getMatricula());
    }

    @Override
    protected void cleanData() {

    }
}


