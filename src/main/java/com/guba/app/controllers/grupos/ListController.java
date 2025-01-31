package com.guba.app.controllers.grupos;

import com.guba.app.data.dao.DAOCarreras;
import com.guba.app.data.dao.DAOGrupoMateria;
import com.guba.app.data.dao.DAOMaterias;
import com.guba.app.utils.BaseController;
import com.guba.app.utils.Paginas;
import com.guba.app.domain.models.Carrera;
import com.guba.app.domain.models.Grupo;
import com.guba.app.presentation.dialogs.DialogGrupo;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.guba.app.data.local.database.Service;
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

public class ListController extends BaseController<Grupo> implements Initializable {

    @FXML
    private Label label;
    @FXML
    private TableView<Grupo> tableView;
    @FXML
    private TableColumn<Grupo, String> nombre,semestre,carrera, acciones;
    @FXML
    private TextField busquedaSearch;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private ToggleGroup toggleFiltroNormal;
    private ToggleGroup toggleCarreras = new ToggleGroup();
    private Filtros filtros = Filtros.NOMBRE;
    private List<Grupo> grupoList = new ArrayList<>();
    private ObservableList<Grupo> listaFiltros = FXCollections.observableArrayList();

    private DAOCarreras daoCarreras = new DAOCarreras();
    private DAOGrupoMateria daoGrupoMateria = new DAOGrupoMateria();
    private DAOMaterias daoMaterias = new DAOMaterias();
    private Dialog<Grupo> addDialog;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarGruposBd();
        setCellColumns();
        setFiltro();
    }

    @FXML
    private void openPaneAddAlumno(ActionEvent event){
        DialogGrupo dialogGrupo = new DialogGrupo(new Grupo());
        Optional<Grupo> optionalGrupo = dialogGrupo.showAndWait();
        optionalGrupo.ifPresent(new Consumer<Grupo>() {
            @Override
            public void accept(Grupo grupo) {
                boolean seAgrego = Service.getService().agregarGrupo(grupo);

            }
        });
    }

    @FXML
    private void borrarFiltros(ActionEvent event){
        busquedaSearch.setText("");
        Service.getService().getGrupos().setAll(grupoList);
        toggleCarreras.selectToggle(null);
        toggleFiltroNormal.selectToggle(null);

    }

    private void cargarGruposBd(){
        Task<List<Grupo>> task = new Task<List<Grupo>>() {
            @Override
            protected List<Grupo> call() throws Exception {
                return Service.getService().cargarGrupos();
            }
        };
        task.setOnSucceeded(event -> {
            try {
                label.setVisible(false);
                grupoList = task.get();
                listaFiltros.setAll(task.get());
                tableView.setItems(Service.service.getGrupos());
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
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        semestre.setCellValueFactory(new PropertyValueFactory<>("semestre"));
        carrera.setCellValueFactory(cellData -> {
            Grupo grupo = cellData.getValue();
            return Bindings.createStringBinding(() -> {
                Carrera carrera = grupo.getCarrera();
                if (carrera != null) {
                    return carrera.getNombre() +" " + carrera.getModalidad();
                } else {
                    return "Sin asignar";
                }
            }, grupo.carreraProperty());
        });

        acciones.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Grupo, String> call(TableColumn<Grupo, String> estudianteStringTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            Grupo grupo = getTableView().getItems().get(getIndex());

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
                                    mediador.loadContent(Paginas.DETAILS, grupo);
                                }
                            });

                            editIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    DialogGrupo dialogAddGrupo = new DialogGrupo(grupo);
                                    dialogAddGrupo.showAndWait().ifPresent(g -> {
                                        Service.getService().actualizarGrupo(grupo);
                                    });
                                }
                            });

                            deletIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estas Seguro de eliminar el grupo:"+  grupo.getNombre()+" " + "?");
                                    Optional<Integer> option = dialogConfirmacion.showAndWait();

                                    option.ifPresent(new Consumer<Integer>() {
                                        @Override
                                        public void accept(Integer integer) {
                                            if (integer.equals(1)){
                                                boolean seElimino = Service.getService().eliminarCurso(grupo);
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
                    Service.getService().getGrupos().setAll(grupoList);
                    return;
                }

                switch (filtros){

                    case NOMBRE -> {
                        List<Grupo> filtro = listaFiltros.stream()
                                .filter(grupo -> grupo.getNombre().toLowerCase().contains(t1.toLowerCase()))
                                .toList();

                        Service.getService().getGrupos().setAll(filtro);
                    }
                    case SEMESTRE -> {
                        List<Grupo> filtro = listaFiltros.stream()
                                .filter(grupo -> grupo.getSemestre().toLowerCase().contains(t1.toLowerCase()))
                                .toList();
                        Service.getService().getGrupos().setAll(filtro);
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
            listaFiltros.setAll(grupoList.stream()
                    .filter(e-> e.getCarrera().getIdCarrera().equals(carrera.getIdCarrera()))
                    .toList());

            Service.getService().getGrupos().setAll(listaFiltros);
        });


        toggleFiltroNormal.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioMenuItem rb = (RadioMenuItem) toggleFiltroNormal.getSelectedToggle();
            if (rb == null){
                return;
            }
            int index = Integer.parseInt(rb.getUserData().toString());
            switch (index){
                case 1->{
                    filtros = Filtros.NOMBRE;
                }
                case 2->{
                    filtros = Filtros.SEMESTRE;
                }
            }
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

    @Override
    protected void cleanData() {

    }
}


enum Filtros{
    NOMBRE,
    SEMESTRE,
}

