package com.guba.app.controllers.grupos;

import com.guba.app.data.dao.DAOAlumno;
import com.guba.app.data.dao.DAOCalificiaciones;
import com.guba.app.data.dao.DAOGrupoMateria;
import com.guba.app.presentation.dialogs.*;
import com.guba.app.utils.*;
import com.guba.app.data.dao.DAOPeriodo;
import com.guba.app.domain.models.*;
import com.guba.app.presentation.utils.ComboCell;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class DetailsController extends BaseController<Grupo> implements Initializable, Loadable<Grupo> {

    @FXML
    private Button backButton;
    @FXML
    private Button btnAddMaterias;
    @FXML
    private Button btnAddAlumnos;
    @FXML
    private JFXButton btnVerMaterias;
    @FXML
    private JFXButton btnVerEstudiantes;
    @FXML
    private TextField txtGrupo;
    @FXML
    private TextField txtSemestre;
    @FXML
    private TextField txtCarrera;
    @FXML
    private TableView<GrupoMateria> tableMaterias;
    @FXML
    private TableColumn<GrupoMateria, String> columnClave;
    @FXML
    private TableColumn<GrupoMateria, String> columnNombre;
    @FXML
    private TableColumn<GrupoMateria, String> columnSemestre;
    @FXML
    private TableColumn<GrupoMateria, String> columnProfesor;
    @FXML
    private TableColumn<GrupoMateria, String> columnAcciones;
    @FXML
    private VBox containerAlumnos;
    @FXML
    private VBox containerMaterias;
    @FXML
    private ComboBox<GrupoMateria> materiasComboBox;
    @FXML
    private TableView<Calificacion> tablaCalificaciones;
    @FXML
    private TableColumn<Calificacion, String> columnNombreEstudiante;
    @FXML
    private TableColumn<Calificacion, String> columnMatricula, columnU1P1,columnU1P2,columnU1P3,columnU1P4,columnU1PM,
            columnU2P1,columnU2P2,columnU2P3,columnU2P4,columnU2PM,
            columnU3P1,columnU3P2,columnU3P3,columnU3P4,columnU3PM,
            columnU4P1,columnU4P2,columnU4P3,columnU4P4,columnU4PM,
            columnTF,columnPromedio,columnAccionesEstudiantes;
    private Grupo grupo;
    private Periodo periodo;
    private ObservableList<GrupoMateria> grupoMaterias;
    private ObservableList<Calificacion> calificacions = FXCollections.observableArrayList();
    private DAOGrupoMateria daoGrupoMateria = new DAOGrupoMateria();
    private DAOAlumno daoAlumno = new DAOAlumno();
    private DAOCalificiaciones daoCalificiaciones = new DAOCalificiaciones();
    private DAOPeriodo daoPeriodo = new DAOPeriodo();


    public DetailsController( Mediador<Grupo> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super("/grupos/Details", mediador, estadoProperty, paginasProperty);
        setColumnsTableMaterias();
        setColumnTableEstudiantes();
        setComboBox();
        periodo = daoPeriodo.getUltimoPeriodo();
        backButton.setOnAction(this::regresarAPanel);
        btnVerMaterias.setOnAction(this::verMaterias);
        btnVerEstudiantes.setOnAction(this::verEstudiantes);
        btnAddMaterias.setOnAction(this::addMaterias);
        btnAddAlumnos.setOnAction(this::agregarAlumnos);
        tablaCalificaciones.setItems(calificacions);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private void verMaterias(ActionEvent event){
        containerMaterias.setVisible(true);
        containerAlumnos.setVisible(false);
    }

    private void verEstudiantes(ActionEvent event){
        containerMaterias.setVisible(false);
        containerAlumnos.setVisible(true);
    }

    private void regresarAPanel(ActionEvent actionEvent) {
        cleanData();
        paginasProperty.set(Paginas.LIST);
    }


    private void addMaterias(ActionEvent actionEvent){
        DialogMaterias dialogMaterias = new DialogMaterias(grupo);
        dialogMaterias.showAndWait().ifPresent(materias -> {
            boolean seAgregaron = daoGrupoMateria.agregarMaterias(materias, grupo.getId());
            System.out.println(seAgregaron);
            if (seAgregaron){
                grupoMaterias.setAll(daoGrupoMateria.obtenerGruposMateriasPorIdGrupo(grupo.getId()));
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error al Agregar Materias contacte a soporte");
                alert.showAndWait();
            }
        });
    }


    private void agregarAlumnos(ActionEvent actionEvent){
        if (materiasComboBox.getSelectionModel().isEmpty()){
            return;
        }
        GrupoMateria grupoMateria = materiasComboBox.getValue();
        DialogAlumnos dialogAlumnos = new DialogAlumnos(grupo, grupoMateria.getMateria().getIdMateria());
        dialogAlumnos.showAndWait().ifPresent(estudiantes -> {
            if (estudiantes.isEmpty()){
                return;
            }
            daoCalificiaciones.insertarCalificaciones(estudiantes , grupoMateria.getMaestro().getId(), grupoMateria.getMateria().getIdMateria(), grupo.getId()).
                    ifPresentOrElse(aInteger -> {
                        List<Calificacion> calificacions1 =daoCalificiaciones.obtenerByMateriaAndGrupo(grupoMateria.getMateria().getIdMateria(), grupo.getId());
                        calificacions.setAll(calificacions1);
                    }, () -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("No se pudo agregar los alumnos, porfavor verifia mas tarde");
                        alert.show();
                    });
        });
    }

    @Override
    public void loadData(Grupo data) {
        grupo = data;
        loadGrupoMateriasAsync(grupo, g -> {
            this.grupoMaterias = FXCollections.observableArrayList(g);
            tableMaterias.setItems(grupoMaterias);
            materiasComboBox.setItems(grupoMaterias);
            tableMaterias.refresh();
        });
        txtCarrera.setText(grupo.getCarrera().toComboCell());
        txtSemestre.setText(grupo.getSemestre());
        txtGrupo.setText(grupo.getNombre());

    }

    private void loadGrupoMateriasAsync(Grupo grupo,Consumer<List<GrupoMateria>> callback){
        Utils.loadAsync(()-> daoGrupoMateria.obtenerGruposMateriasPorIdGrupo(grupo.getId()),callback);
    }

    private void loadCalificaciones(Materia materia,Consumer<List<Calificacion>> callback){
        Utils.loadAsync(()->  daoCalificiaciones.obtenerByMateriaAndGrupo(materia.getIdMateria(), grupo.getId()),callback);
    }

    private void setColumnsTableMaterias(){
        tableMaterias.setRowFactory(grupoMateriaTableView -> {
            return new TableRow<>(){
                @Override
                protected void updateItem(GrupoMateria item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty){
                        this.setText(null);
                        this.setGraphic(null);
                    }else{
                        if (item.getCursada() == 1){
                            this.getStyleClass().add("cursada");
                        }
                    }
                }
            };
        });

        columnClave.setCellValueFactory(cellData -> {
            Materia materia =  cellData.getValue().getMateria();
            return Bindings.createStringBinding(() -> {
                if (materia != null) {
                    return materia.getClave();
                } else {
                    return null;
                }
            }, materia.claveProperty());
        });
        columnNombre.setCellValueFactory(cellData->{
            Materia materia =  cellData.getValue().getMateria();
            return Bindings.createStringBinding(() ->{
                if (materia != null) {
                    return materia.getNombre();
                } else {
                    return null;
                }
            }, materia.nombreProperty());
        });
        columnSemestre.setCellValueFactory(cellData->{
            Materia materia =  cellData.getValue().getMateria();
            return Bindings.createStringBinding(() ->{
                if (materia != null) {
                    return materia.getSemestre();
                } else {
                    return null;
                }
            }, materia.semestreProperty());
        });
        columnProfesor.setCellValueFactory(cellData->{
            Maestro maestro =  cellData.getValue().getMaestro();
            return Bindings.createStringBinding(() ->{
                if (maestro.getNombre() != null ) {
                    return maestro.getNombre() + " " + maestro.getApPat() + " " + maestro.getApMat();
                } else {
                    return null;
                }
            }, cellData.getValue().maestroProperty());
        });
        columnAcciones.setCellFactory(new Callback<>() {
            @Override
            public TableCell<GrupoMateria, String> call(TableColumn<GrupoMateria, String> estudianteStringTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            GrupoMateria grupoMateria = getTableView().getItems().get(getIndex());

                            Button deletIcon = new Button();
                            Button editIcon = new Button();
                            Button finalizarIcon = new Button();

                            FontIcon fontIcon = new FontIcon("mdi-border-color");
                            fontIcon.setFill(Color.WHITE);
                            editIcon.setGraphic(fontIcon);
                            editIcon.getStyleClass().add("btnEdit");

                            FontIcon delete = new FontIcon("mdi-delete");
                            delete.setFill(Color.WHITE);
                            deletIcon.setGraphic(delete);
                            deletIcon.getStyleClass().add("btnBorrar");

                            FontIcon marked = new FontIcon("mdi-checkbox-marked-circle");
                            marked.setFill(Color.WHITE);
                            finalizarIcon.setGraphic(marked);
                            finalizarIcon.getStyleClass().add("btnVer");


                            editIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    DialogProfesores dialogProfesores = new DialogProfesores();
                                    dialogProfesores.showAndWait()
                                            .ifPresent(maestro -> {
                                                daoGrupoMateria.actualizarIdDocente(grupoMateria, maestro.getId()).
                                                        ifPresent(actualizado -> {
                                                            grupoMateria.setMaestro(maestro);
                                                        });
                                            });
                                }
                            });

                            deletIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estas Seguro de eliminar la materia:"+  grupoMateria.getMateria().getNombre()+" " + grupoMateria.getMateria().getModalidad() +"?");
                                    Optional<Integer> option = dialogConfirmacion.showAndWait();

                                    option.ifPresent(new Consumer<Integer>() {
                                        @Override
                                        public void accept(Integer integer) {
                                            if (integer.equals(1)){
                                                daoGrupoMateria.eliminarGrupoMateria(grupoMateria.getIdGrupoMateria());
                                                grupoMaterias.remove(grupoMateria);
                                            }
                                        }
                                    });
                                }
                            });

                            finalizarIcon.setOnAction(event -> {
                                boolean seActualizo  = new DialogFinalizarMateria(grupoMateria).showAndWait().orElse(false);
                                if (seActualizo) {
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Confirmacion");
                                    alert.setContentText("Se finalizo la materia");
                                    grupoMateria.setCursada(1);
                                    tableMaterias.refresh();
                                }
                            });

                            HBox hBox = new HBox();
                            hBox.getChildren().addAll(editIcon, deletIcon, finalizarIcon);
                            hBox.setSpacing(10);
                            hBox.setPrefWidth(120);
                            hBox.setAlignment(Pos.CENTER);
                            setText("");
                            setGraphic(grupoMateria.getCursada() == 1 ? new HBox() : hBox);
                        }
                    }
                };
            }
        });
    }

    private void setColumnTableEstudiantes(){
        columnMatricula.setCellValueFactory(calificacionStringCellDataFeatures -> {
            Calificacion calificacion = calificacionStringCellDataFeatures.getValue();
            return Bindings.createStringBinding(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    if (calificacion.getEstudiante() != null){
                        return calificacion.getEstudiante().getMatricula();
                    }
                    return null;
                }
            },calificacion.estudianteProperty());
        });
        columnNombreEstudiante.setCellValueFactory(calificacionStringCellDataFeatures -> {
            Calificacion calificacion = calificacionStringCellDataFeatures.getValue();
            return Bindings.createStringBinding(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    if (calificacion.getEstudiante() != null){
                        return calificacion.getEstudiante().getNombre()+" " + calificacion.getEstudiante().getApMaterno() + " " + calificacion.getEstudiante().getApMaterno();
                    }
                    return null;
                }
            },calificacion.estudianteProperty());
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
        columnAccionesEstudiantes.setCellFactory(calificacionStringTableColumn ->  {
                return new TableCell<Calificacion,String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            Calificacion calificacion = getTableView().getItems().get(getIndex());

                            Button deletIcon = new Button();

                            FontIcon delete = new FontIcon("mdi-delete");
                            delete.setFill(Color.WHITE);
                            deletIcon.setGraphic(delete);
                            deletIcon.getStyleClass().add("btnBorrar");

                            deletIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estas Seguro de eliminar la calificacion ?");
                                    Optional<Integer> option = dialogConfirmacion.showAndWait();

                                    option.ifPresent(new Consumer<Integer>() {
                                        @Override
                                        public void accept(Integer integer) {
                                            if (integer.equals(1)){
                                                daoCalificiaciones.deleteCalificacion(calificacion.getIdCalificacion());
                                                calificacions.remove(calificacion);
                                            }
                                        }
                                    });
                                }
                            });
                            HBox hBox = new HBox();
                            hBox.getChildren().add(deletIcon);
                            hBox.setSpacing(10);
                            hBox.setPrefWidth(120);
                            hBox.setAlignment(Pos.CENTER);
                            setText("");
                            setGraphic(hBox);
                        }
                    }
                };
        });
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
        tableColumn.setCellFactory(new Callback<TableColumn<Calificacion, String>, TableCell<Calificacion, String>>() {
            @Override
            public TableCell<Calificacion, String> call(TableColumn<Calificacion, String> calificacionStringTableColumn) {
                return new TextFieldTableCell<>(){
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty){
                            setText(null);
                            setGraphic(null);
                        }else{
                            Calificacion calificacion = calificacionStringTableColumn.getTableView().getItems().get(getIndex());
                            System.out.println(calificacion.getIdCalificacion() +"-> "+ calificacion.getPerido());
                            if (!calificacion.getPerido().getId().equals(periodo.getId())){
                                setEditable(false);
                            }
                        }
                    }
                };
            }
        });
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
                daoCalificiaciones.updateCalificacion(calificacion);
            }
        });
    }

    private void setComboBox(){
        materiasComboBox.setCellFactory(materiaListView -> new ComboCell<GrupoMateria>());
        materiasComboBox.setButtonCell(new ComboCell<GrupoMateria>());
        materiasComboBox.valueProperty().addListener((observableValue, materia, t1) -> {
            if (t1 != null){
                loadCalificaciones(t1.getMateria(), new Consumer<List<Calificacion>>() {
                    @Override
                    public void accept(List<Calificacion> c) {
                        Platform.runLater(() -> {
                            calificacions.setAll(c);
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void cleanData() {
        calificacions.clear();
        materiasComboBox.setValue(null);
        containerMaterias.setVisible(true);
        containerAlumnos.setVisible(false);
    }
}
