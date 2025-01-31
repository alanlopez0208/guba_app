package com.guba.app.presentation.dialogs;


import com.guba.app.data.dao.DAOAlumno;
import com.guba.app.domain.models.Estudiante;
import com.guba.app.domain.models.Participante;
import com.guba.app.presentation.utils.Constants;
import com.guba.app.utils.Utils;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.kordamp.ikonli.Ikonli;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;

public class DialogParticipantes extends Dialog<List<Participante>> {

    private TableView<AlumnoSeleccion> tableView;
    private HBox filterBar;
    private Button button;
    private ToggleGroup toggleGroup;

    private ObservableList<AlumnoSeleccion> alumnosAElegir;
    private FilteredList<AlumnoSeleccion> filteredAlumnos;
    int filtroSeleccionado = 1;

    private DAOAlumno daoAlumno = new DAOAlumno();


    public DialogParticipantes(){
        tableView = new TableView<>();
        filterBar = new HBox();
        button = new Button();
        alumnosAElegir = FXCollections.observableArrayList();
        filteredAlumnos = new FilteredList<>(alumnosAElegir);
        toggleGroup = new ToggleGroup();

        setUpButtons();
        setBarFilter();
        initUi();
        loadAsyncAlumnos();
    }

    public void setUpButtons(){
        ButtonType savePhotoButtonType = new ButtonType("Guardar Alumnos", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(savePhotoButtonType, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType == savePhotoButtonType) {
                return tableView.getItems().stream()
                        .filter(AlumnoSeleccion::isSeleccion)
                        .map(a-> new Participante(a.estudiante.get()))
                        .toList();
            }
            return null;
        });

        button = new Button("Seleccionar Todos los Alumnos");
        button.getStyleClass().add("btnsAgregar");
        button.setOnAction(event -> {
            for (AlumnoSeleccion alumnoSeleccion : alumnosAElegir) {
                alumnoSeleccion.setSeleccion(true);
            }
        });

    }

    public void setBarFilter(){
        FontIcon iconLupa = new FontIcon("mdi-magnify");
        TextField barraBusqueda = new TextField();

        HBox.setHgrow(barraBusqueda, Priority.ALWAYS);
        barraBusqueda.setPromptText("Buscar...");
        barraBusqueda.getStyleClass().add("textFieldSearch");
        barraBusqueda.textProperty().addListener((observableValue, s, t1) -> {
            filteredAlumnos.setPredicate(a->{
                String comparar = t1.toLowerCase();
                Estudiante estudiante = a.getEstudiante();
                return switch (filtroSeleccionado){
                    case 1-> comparar.isEmpty() || estudiante.getMatricula().toLowerCase().contains(comparar);
                    case 2-> comparar.isEmpty() || estudiante.getNombre().toLowerCase().contains(comparar);
                    default -> true;
                };
            });
        });

        ContextMenu contextMenu = new ContextMenu();
        RadioMenuItem radioMatricula = new RadioMenuItem("Matricula");
        radioMatricula.setUserData(1);
        radioMatricula.setToggleGroup(toggleGroup);
        RadioMenuItem radioNombre = new RadioMenuItem("Nombre");
        radioNombre.setUserData(2);
        radioNombre.setToggleGroup(toggleGroup);
        contextMenu.getItems().setAll(radioMatricula, radioNombre);
        toggleGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            filtroSeleccionado = (int) t1.getUserData();
        });

        JFXButton btnFiltro = new JFXButton();
        FontIcon iconFiltro = new FontIcon("mdi-filter");
        iconFiltro.setIconSize(15);
        iconFiltro.setFill(Color.BLACK);
        btnFiltro.setGraphic(iconFiltro);
        btnFiltro.setContextMenu(contextMenu);
        btnFiltro.getStyleClass().add("btnFiltro");

        HBox barra = new HBox();
        barra.setSpacing(10);
        HBox.setHgrow(barra, Priority.ALWAYS);
        barra.setAlignment(Pos.CENTER);
        barra.getChildren().addAll(iconLupa,barraBusqueda);

        filterBar.getChildren().addAll(barra, btnFiltro);
    }




    public void initUi(){
        this.getDialogPane().getScene().getStylesheets().add(getClass().getResource(Constants.URL_PRESENTATION+"/styles.css").toExternalForm());
        this.getDialogPane().getStyleClass().add("root");

        tableView.getStyleClass().add("table");
        TableColumn<AlumnoSeleccion, Boolean> selectColumn = new TableColumn<>("Seleccionar");
        selectColumn.setPrefWidth(100);
        selectColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().seleccionProperty();
        });
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        TableColumn<AlumnoSeleccion, String> matriculaColumn  = setColumnTable("Matricula", 200, Estudiante::getMatricula);
        TableColumn<AlumnoSeleccion, String> nameColumn  = setColumnTable("Nombre", 200,estudiante -> {
            return estudiante.getNombre() + " " + estudiante.getApPaterno() + estudiante.getApMaterno();
        });
        TableColumn<AlumnoSeleccion, String> carreraColumn  = setColumnTable("Carrera", 200,estudiante -> {
            return estudiante.getCarrera().toString();
        });

        tableView.getColumns().addAll(selectColumn, matriculaColumn, nameColumn, carreraColumn);
        tableView.setEditable(true);
        tableView.setItems(filteredAlumnos);

        VBox content = new VBox(filterBar,tableView, button);
        content.setFillWidth(true);
        content.setSpacing(10);
        this.getDialogPane().setContent(content);
        this.setWidth(1000);

    }


    private TableColumn<AlumnoSeleccion, String> setColumnTable(String nombreColumna, double width, Function<Estudiante,String> function){
        TableColumn<AlumnoSeleccion, String> columna = new TableColumn<>(nombreColumna);
        columna.setPrefWidth(width);
        columna.setCellValueFactory(cellData -> {
            Estudiante estudiante = cellData.getValue().getEstudiante();
            return Bindings.createStringBinding(()->{
                if (estudiante != null) {
                    return function.apply(estudiante);
                }
                return "Sin asignar";
            },cellData.getValue().estudianteProperty());
        });
        return columna;
    }

    public void loadAsyncAlumnos(){
        Utils.loadAsync(
                () -> daoAlumno.getEstudiantes(),
                estudiantes -> {
                    List<AlumnoSeleccion> materiaSeleccions = estudiantes.stream()
                            .map(AlumnoSeleccion::new)
                            .toList();
                    alumnosAElegir.setAll(materiaSeleccions);
                });
    }

    public class AlumnoSeleccion{
        private SimpleBooleanProperty seleccion = new SimpleBooleanProperty(false);
        private ObjectProperty<Estudiante> estudiante = new SimpleObjectProperty<>();

        public AlumnoSeleccion(Estudiante estudiante) {
            this.estudiante.set(estudiante);
        }


        public boolean isSeleccion() {
            return seleccion.get();
        }

        public SimpleBooleanProperty seleccionProperty() {
            return seleccion;
        }

        public void setSeleccion(boolean seleccion) {
            this.seleccion.set(seleccion);
        }

        public Estudiante getEstudiante() {
            return estudiante.get();
        }

        public ObjectProperty<Estudiante> estudianteProperty() {
            return estudiante;
        }

        public void setEstudiante(Estudiante estudiante) {
            this.estudiante.set(estudiante);
        }
    }
}

