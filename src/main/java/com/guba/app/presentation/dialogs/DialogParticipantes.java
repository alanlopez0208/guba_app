package com.guba.app.presentation.dialogs;


import com.guba.app.dao.DAOAlumno;
import com.guba.app.models.Estudiante;
import com.guba.app.models.Participante;
import com.guba.app.presentation.utils.Constants;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class DialogParticipantes extends Dialog<List<Participante>> {

    private TableView<AlumnoSeleccion> tableView;

    private ObservableList<AlumnoSeleccion> alumnosAElegir = FXCollections.emptyObservableList();
    private Button button;

    private DAOAlumno daoAlumno = new DAOAlumno();

    public DialogParticipantes(){
        initUi();
        setUpButtons();
        loadAsyncAlumnos();
    }

    public void initUi(){
        this.getDialogPane().getScene().getStylesheets().add(getClass().getResource(Constants.URL_PRESENTATION+"/styles.css").toExternalForm());
        tableView = new TableView<>();

        tableView.getStyleClass().add("table");
        TableColumn<AlumnoSeleccion, Boolean> selectColumn = new TableColumn<>("Seleccionar");
        selectColumn.setPrefWidth(100);
        selectColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().seleccionProperty();
        });
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));


        TableColumn<AlumnoSeleccion, String> matricula = new TableColumn<>("Nombre");
        matricula.setPrefWidth(200);
        matricula.setCellValueFactory(celldata->{
            Estudiante estudiante = celldata.getValue().getEstudiante();
            return Bindings.createStringBinding(()->{
                if (estudiante != null) {
                    return estudiante.getMatricula();
                } else {
                    return "Sin asignar";
                }
            },celldata.getValue().estudianteProperty());
        });
        TableColumn<AlumnoSeleccion, String> nameColumn = new TableColumn<>("Nombre");
        nameColumn.setPrefWidth(200);
        nameColumn.setCellValueFactory(celldata->{
            Estudiante estudiante = celldata.getValue().getEstudiante();
            return Bindings.createStringBinding(()->{
                if (estudiante != null) {
                    return estudiante.getNombre() + " " + estudiante.getApPaterno() + estudiante.getApMaterno();
                } else {
                    return "Sin asignar";
                }
            },celldata.getValue().estudianteProperty());
        });
        tableView.getColumns().addAll(selectColumn, matricula, nameColumn);


        button = new Button("Seleccionar Todos los Alumnos");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (AlumnoSeleccion alumnoSeleccion : alumnosAElegir) {
                    alumnoSeleccion.setSeleccion(true);
                }
            }
        });


        VBox content = new VBox(tableView, button);
        content.setFillWidth(true);
        content.setSpacing(10);
        this.getDialogPane().setContent(content);
        this.setWidth(700);
        tableView.setEditable(true);
    }

    public void setUpButtons(){
        ButtonType savePhotoButtonType = new ButtonType("Guardar Materias", ButtonBar.ButtonData.OK_DONE);
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
    }

    public void loadAsyncAlumnos(){
        Task<List<Estudiante>> task = new Task<List<Estudiante>>() {
            @Override
            protected List<Estudiante> call() throws Exception {
                return daoAlumno.getEstudiantes();
            }
        };

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                try {
                    List<AlumnoSeleccion> materiaSeleccions = task.get().stream()
                            .map(AlumnoSeleccion::new)
                            .toList();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            alumnosAElegir = FXCollections.observableArrayList(materiaSeleccions);
                            tableView.setItems(alumnosAElegir);
                        }
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
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

