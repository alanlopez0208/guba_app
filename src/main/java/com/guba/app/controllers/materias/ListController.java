package com.guba.app.controllers.materias;

import com.guba.app.dao.DAOCarreras;
import com.guba.app.dao.DAOMaterias;
import com.guba.app.controllers.BaseController;
import com.guba.app.controllers.Paginas;
import com.guba.app.models.Carrera;
import com.guba.app.models.Estudiante;
import com.guba.app.models.Materia;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class ListController extends BaseController<Materia> implements Initializable {

    @FXML
    private Label label;
    @FXML
    private TableView<Materia> tableView;
    @FXML
    TableColumn<Materia, String> clave, nombre, creditos,carrera, modalidad,acciones;
    @FXML
    private TextField busquedaSearch;
    @FXML
    private JFXButton btnFiltros;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private ToggleGroup toggleFiltroNormal;
    @FXML
    private ToggleGroup toggleSemestre;
    private ToggleGroup toggleCarreras = new ToggleGroup();
    private Filtros filtros = Filtros.CLAVE;
    private List<Materia> materiaList = new ArrayList<>();
    private ObservableList<Materia> listaFiltros = FXCollections.observableArrayList();
    private DAOMaterias daoMaterias = new DAOMaterias();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarMaterias();
        setCellColumns();
        setFiltro();
    }

    @FXML
    private void openPaneAddAlumno(ActionEvent event){
        mediador.loadData(Paginas.ADD, new Materia());
    }


    @FXML
    private void borrarFiltros(ActionEvent event){
        busquedaSearch.setText("");
        getLista().setAll(materiaList);
        toggleSemestre.selectToggle(null);
        toggleCarreras.selectToggle(null);
        toggleFiltroNormal.selectToggle(null);

    }

    private void cargarMaterias(){
        Task<List<Materia>> task = new Task<List<Materia>>() {
            @Override
            protected List<Materia> call() throws Exception {
                return daoMaterias.getMaterias();
            }
        };
        task.setOnSucceeded(event -> {
            try {
                label.setVisible(false);
                materiaList = task.get();
                listaFiltros.setAll(materiaList);
                getLista().setAll(listaFiltros);
                tableView.setItems(getLista());
                tableView.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Error al carrgar las materias" +task.getException());
            }
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }



    private void setCellColumns(){
        clave.setCellValueFactory(new PropertyValueFactory<>("clave"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        creditos.setCellValueFactory(new PropertyValueFactory<>("creditos"));
        modalidad.setCellValueFactory(new PropertyValueFactory<>("modalidad"));
        carrera.setCellValueFactory(cellData -> {
            Materia materia = cellData.getValue();
            return Bindings.createStringBinding(() -> {
                Carrera carrera = materia.getCarreraModelo();
                if (carrera != null) {
                    return carrera.getNombre();
                } else {
                    return "Sin asignar";
                }
            }, materia.carreraModeloProperty());
        });

        acciones.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Materia, String> call(TableColumn<Materia, String> estudianteStringTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            Materia materia = getTableView().getItems().get(getIndex());

                            Button openIcon = new Button();
                            Button deletIcon = new Button();
                            Button editIcon = new Button();

                            FontIcon open = new FontIcon("mdi-eye");
                            open.setFill(Color.WHITE);
                            openIcon.setGraphic(open);
                            openIcon.getStyleClass().add("btnVer");

                            FontIcon fontIcon = new FontIcon("mdi-border-color");
                            fontIcon.setFill(Color.WHITE);
                            editIcon.setGraphic(fontIcon);
                            editIcon.getStyleClass().add("btnEdit");

                            FontIcon delete = new FontIcon("mdi-delete");
                            delete.setFill(Color.WHITE);
                            deletIcon.setGraphic(delete);
                            deletIcon.getStyleClass().add("btnBorrar");


                            openIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    mediador.loadData(Paginas.DETAILS, materia);
                                }
                            });

                            editIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    mediador.loadData(Paginas.EDIT, materia);
                                }
                            });

                            deletIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estas Seguro de eliminar la materia:"+  materia.getNombre()+" " + materia.getModalidad() +"?");
                                    Optional<Integer> option = dialogConfirmacion.showAndWait();

                                    option.ifPresent(new Consumer<Integer>() {
                                        @Override
                                        public void accept(Integer integer) {
                                            if (integer.equals(1)){
                                                daoMaterias.eliminarMateria(materia.getIdMateria());
                                                getLista().remove(materia);
                                            }
                                        }
                                    });
                                }
                            });
                            HBox hBox = new HBox();
                            hBox.getChildren().addAll(openIcon,editIcon, deletIcon);
                            hBox.setSpacing(10);
                            hBox.setPrefWidth(320);
                            hBox.setAlignment(Pos.CENTER);
                            setText("");
                            setGraphic(hBox);
                        }
                    }
                };
            }
        });
    }


    private void setFiltro(){
        busquedaSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (t1.isEmpty()){
                    getLista().setAll(listaFiltros);
                    return;
                }
                switch (filtros){
                    case NOMBRE -> {
                        List<Materia> filtro = listaFiltros.stream()
                                .filter(materia -> materia.getNombre().toLowerCase().contains(t1.toLowerCase()))
                                .toList();

                        getLista().setAll(filtro);
                    }
                    case CLAVE -> {
                        List<Materia> filtro = listaFiltros.stream()
                                .filter(materia -> materia.getClave().toLowerCase().contains(t1.toLowerCase()))
                                .toList();
                        getLista().setAll(filtro);
                    }
                }
            }
        });

        loadCarrerasAsync(carreras -> {
            carreras.forEach(carrera -> {
                RadioMenuItem radioButton = new RadioMenuItem();
                radioButton.setText(carrera.getNombre() + " " + carrera.getModalidad());
                radioButton.setToggleGroup(toggleCarreras);
                radioButton.setUserData(carrera);
                contextMenu.getItems().add(radioButton);
            });
        });

        toggleCarreras.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioMenuItem rb = (RadioMenuItem) toggleCarreras.getSelectedToggle();
            if (rb == null){
                return;
            }
            Carrera carrera = (Carrera) rb.getUserData();
            listaFiltros.setAll(materiaList.stream()
                    .filter(e->{
                        if (toggleSemestre.getSelectedToggle() != null){
                            RadioMenuItem rbSemestre = (RadioMenuItem) toggleSemestre.getSelectedToggle();
                            String semestre = rbSemestre.getUserData().toString();
                            return e.getCarreraModelo().getIdCarrera().equals(carrera.getIdCarrera()) && e.getSemestre().equals(semestre);
                        }
                        return e.getCarreraModelo().getIdCarrera().equals(carrera.getIdCarrera());
                    })
                    .toList());
            getLista().setAll(listaFiltros);
        });

        toggleFiltroNormal.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioMenuItem rb = (RadioMenuItem) toggleFiltroNormal.getSelectedToggle();
            if (rb == null){
                return;
            }
            int index = Integer.parseInt(rb.getUserData().toString());
            switch (index){
                case 1->{
                    filtros = Filtros.CLAVE;
                }
                case 2->{
                    filtros = Filtros.NOMBRE;
                }
            }
        });

        toggleSemestre.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {

            RadioMenuItem rb = (RadioMenuItem) toggleSemestre.getSelectedToggle();
            if (rb == null){
                return;
            }
            String semestre = rb.getUserData().toString();
            listaFiltros.setAll(materiaList.stream()
                    .filter(m->{
                        if (toggleCarreras.getSelectedToggle() != null){
                            RadioMenuItem rbCarreras = (RadioMenuItem) toggleCarreras.getSelectedToggle();
                            Carrera carrera = (Carrera) rbCarreras.getUserData();
                            return m.getCarreraModelo().getIdCarrera().equals(carrera.getIdCarrera()) && m.getSemestre().equals(semestre);
                        }
                        return m.getSemestre().equals(semestre);
                    })
                    .toList());

            getLista().setAll(listaFiltros);
        });
    }
    private void loadCarrerasAsync(Consumer<List<Carrera>> cosumer) {
        Task<List<Carrera>> task = new Task<>() {
            @Override
            protected List<Carrera> call() throws Exception {
                return new DAOCarreras().getAllCarreras();
            }
        };
        task.setOnSucceeded(event -> {
            try {
                cosumer.accept(task.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(task).start();
    }
}
enum Filtros{
    CLAVE,
    NOMBRE
}
