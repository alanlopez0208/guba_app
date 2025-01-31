package com.guba.app.presentation.dialogs;

import com.guba.app.data.dao.DAOMaterias;
import com.guba.app.domain.models.Grupo;
import com.guba.app.domain.models.Materia;
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
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DialogMaterias extends Dialog<List<Materia>> {

    private TableView<MateriaSeleccion> tableView;

    private ObservableList<MateriaSeleccion> materiasASeleccionar = FXCollections.emptyObservableList();

    private Grupo grupo;

    public DialogMaterias(Grupo grupo){
        this.grupo = grupo;
        initUi();
        setUpButtons();
        loadAsyncMaterias();
    }

    public void initUi(){
        this.getDialogPane().getScene().getStylesheets().add(getClass().getResource(Constants.URL_PRESENTATION+"/styles.css").toExternalForm());
        tableView = new TableView<>();
        tableView.getStyleClass().add("table");
        TableColumn<MateriaSeleccion, Boolean> selectColumn = new TableColumn<>("Seleccionar");
        selectColumn.setPrefWidth(100);
        selectColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().seleccionProperty();
        });
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        TableColumn<MateriaSeleccion, String> nameColumn = new TableColumn<>("Nombre");
        nameColumn.setPrefWidth(200);
        nameColumn.setCellValueFactory(celldata->{
            Materia materia = celldata.getValue().getMateria();
            return Bindings.createStringBinding(()->{
                if (materia != null) {
                    return materia.getNombre();
                } else {
                    return "Sin asignar";
                }
            },celldata.getValue().materiaProperty());
        });
        tableView.getColumns().addAll(selectColumn, nameColumn);

        VBox content = new VBox(tableView);
        content.setSpacing(10);
        getDialogPane().setContent(content);
        setWidth(500);
        tableView.setEditable(true);
    }

    public void setUpButtons(){
        ButtonType savePhotoButtonType = new ButtonType("Guardar Materias", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(savePhotoButtonType, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType == savePhotoButtonType) {
                return tableView.getItems().stream()
                        .filter(MateriaSeleccion::isSeleccion)
                        .map(MateriaSeleccion::getMateria)
                        .toList();
            }
            return null;
        });
    }

    public void loadAsyncMaterias(){
        Task<List<Materia>> task = new Task<List<Materia>>() {
            @Override
            protected List<Materia> call() throws Exception {
                return new DAOMaterias().getMateriasByCarreraSemestreSinElegir(grupo.getCarrera().getIdCarrera(), grupo.getSemestre(), grupo.getId());
            }
        };
        
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                try {
                    List<MateriaSeleccion> materiaSeleccions = task.get().stream()
                                    .map(MateriaSeleccion::new)
                                            .toList();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            tableView.getItems().setAll(materiaSeleccions);
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

    public class MateriaSeleccion{
        private SimpleBooleanProperty seleccion = new SimpleBooleanProperty(false);
        private ObjectProperty<Materia> materia = new SimpleObjectProperty<>();

        public MateriaSeleccion(Materia materia) {
            this.setMateria(materia);
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

        public Materia getMateria() {
            return materia.get();
        }

        public ObjectProperty<Materia> materiaProperty() {
            return materia;
        }

        public void setMateria(Materia materia) {
            this.materia.set(materia);
        }
    }
}
