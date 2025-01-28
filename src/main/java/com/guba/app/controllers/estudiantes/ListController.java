package com.guba.app.controllers.estudiantes;

import com.guba.app.dao.DAOAlumno;
import com.guba.app.controllers.BaseController;
import com.guba.app.controllers.Paginas;
import com.guba.app.dao.DAOCarreras;
import com.guba.app.models.Carrera;
import com.guba.app.models.Estudiante;
import com.guba.app.service.local.database.Service;
import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class ListController extends BaseController<Estudiante> implements Initializable {
    @FXML
    private Label label;
    @FXML
    private HBox filtrosContainer;
    @FXML
    private TableView<Estudiante> tableView;
    @FXML
    private TableColumn<Estudiante, String> matricula, nombre, apPaterno,apMaterno,acciones;
    @FXML
    private TextField busquedaSearch;
    @FXML
    private JFXButton btnFiltros;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private ToggleGroup toggleFiltroNormal;
    private ToggleGroup toggleCarreras = new ToggleGroup();
    private Filtros filtros = Filtros.NOMBRE;
    private List<Estudiante> estudiantesList = new ArrayList<>();
    private ObservableList<Estudiante> listaFiltros = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadEstudiantesFromBD();
        setCollumnCell();
        setFiltro();
    }

    @FXML
    private void openPaneAddAlumno(ActionEvent event){
        mediador.loadData(Paginas.ADD, new Estudiante());
    }

    @FXML
    private void borrarFiltros(ActionEvent event){
        busquedaSearch.setText("");
        Service.getService().getEstudiantes().setAll(estudiantesList);
        toggleCarreras.selectToggle(null);
        toggleFiltroNormal.selectToggle(null);

    }


    private void loadEstudiantesFromBD(){
       Task<List<Estudiante>> task = new Task<List<Estudiante>>() {
           @Override
           protected List<Estudiante> call() throws Exception {
               return Service.getService().cargarEstudiantes();
           }
       };
       task.setOnSucceeded(event -> {
            try {
                label.setVisible(false);
                estudiantesList = task.get();
                listaFiltros.setAll(estudiantesList);
                tableView.setItems(Service.service.getEstudiantes());
                tableView.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void setCollumnCell(){
        matricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apPaterno.setCellValueFactory(new PropertyValueFactory<>("apPaterno"));
        apMaterno.setCellValueFactory(new PropertyValueFactory<>("apMaterno"));
        acciones.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Estudiante, String> call(TableColumn<Estudiante, String> estudianteStringTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            Estudiante estudiante = getTableView().getItems().get(getIndex());

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
                                   mediador.loadData(Paginas.DETAILS, estudiante);
                                }
                            });

                            editIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                   mediador.loadData(Paginas.EDIT, estudiante);
                                }
                            });

                            deletIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    Dialog<Integer> dialog = new Dialog<Integer>();
                                    dialog.setContentText("¿Estas Seguro de eliminar al alumo:"+  estudiante.getNombre() + " con mátricula: "+ estudiante.getMatricula()+"?");
                                    ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                                    dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
                                    dialog.setResultConverter(buttonType -> {
                                        if (buttonType == okButtonType){
                                            return 1;
                                        }
                                        return 0;
                                    });

                                    Optional<Integer> option = dialog.showAndWait();

                                    if (option.isPresent() && option.get().equals(1)){
                                        Service.getService().eliminarAlumno(estudiante);
                                    }
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
                    Service.getService().getEstudiantes().setAll(listaFiltros);
                    return;
                }
                System.out.println(filtros);
                switch (filtros){

                    case NOMBRE -> {
                        System.out.println("SE VA A BUSCAR POR EL NOMBRE PA");
                        List<Estudiante> filtro = listaFiltros.stream()
                                .filter(estudiante -> estudiante.getNombre().toLowerCase().contains(t1.toLowerCase()))
                                .toList();

                        Service.getService().getEstudiantes().setAll(filtro);
                    }
                    case MATRICULA -> {
                        List<Estudiante> filtro = listaFiltros.stream()
                                .filter(estudiante -> estudiante.getMatricula().toLowerCase().contains(t1.toLowerCase()))
                                .toList();
                        Service.getService().getEstudiantes().setAll(filtro);
                    }
                    case APELLIDOS -> {
                        List<Estudiante> filtro = listaFiltros.stream()
                                .filter(estudiante -> estudiante.getApPaterno().toLowerCase().contains(t1.toLowerCase()))
                                .toList();
                        Service.getService().getEstudiantes().setAll(filtro);
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
            listaFiltros.setAll(estudiantesList.stream()
                    .filter(e-> e.getCarrera().getIdCarrera().equals(carrera.getIdCarrera()))
                    .toList());

            Service.getService().getEstudiantes().setAll(listaFiltros);
        });


        toggleFiltroNormal.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {

            RadioMenuItem rb = (RadioMenuItem) toggleFiltroNormal.getSelectedToggle();
            if (rb == null){
                filtros = Filtros.NOMBRE;
                return;
            }
            int index = Integer.parseInt(rb.getUserData().toString());
            switch (index){
                case 1->{
                    filtros = Filtros.MATRICULA;
                }
                case 2->{
                    filtros = Filtros.NOMBRE;
                }
                case 3->{
                    filtros = Filtros.APELLIDOS;
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
}

enum Filtros{
    NOMBRE,
    MATRICULA,
    APELLIDOS
}
