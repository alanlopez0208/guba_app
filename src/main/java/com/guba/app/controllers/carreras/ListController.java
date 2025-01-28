package com.guba.app.controllers.carreras;

import com.guba.app.dao.DAOCarreras;
import com.guba.app.controllers.BaseController;
import com.guba.app.controllers.Paginas;
import com.guba.app.models.Carrera;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
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
import java.util.function.Consumer;

public class ListController extends BaseController<Carrera> implements Initializable {

    @FXML
    private Label label;
    @FXML
    private TableView<Carrera> tableView;
    @FXML
    TableColumn<Carrera, String> clave, nombre, creditos,modalidad,acciones;
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
    private List<Carrera> carreraList = new ArrayList<>();
    private ObservableList<Carrera> listaFiltros = FXCollections.observableArrayList();

    private DAOCarreras daoCarreras = new DAOCarreras();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarCarrerasBD();
        setCellColumns();
        setFiltro();
    }

    @FXML
    private void openPaneAddAlumno(ActionEvent event){
        mediador.loadData(Paginas.ADD, new Carrera());
    }

    @FXML
    private void borrarFiltros(ActionEvent event){
        busquedaSearch.setText("");
        getLista().setAll(carreraList);
        toggleCarreras.selectToggle(null);
        toggleFiltroNormal.selectToggle(null);

    }


    private void cargarCarrerasBD(){
        Task<List<Carrera>> task = new Task<List<Carrera>>() {
            @Override
            protected List<Carrera> call() throws Exception {
                return daoCarreras.getAllCarreras();
            }
        };
        task.setOnSucceeded(event -> {
            try {
                label.setVisible(false);
                carreraList = task.get();
                listaFiltros.setAll(carreraList);
                getLista().setAll(listaFiltros);
                tableView.setItems(getLista());
                tableView.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }



    private void setCellColumns(){
        clave.setCellValueFactory(new PropertyValueFactory<>("idClave"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        creditos.setCellValueFactory(new PropertyValueFactory<>("creditos"));
        modalidad.setCellValueFactory(new PropertyValueFactory<>("modalidad"));
        acciones.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Carrera, String> call(TableColumn<Carrera, String> estudianteStringTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            Carrera carrera = getTableView().getItems().get(getIndex());

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
                                    mediador.loadData(Paginas.DETAILS, carrera);
                                }
                            });

                            editIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    mediador.loadData(Paginas.EDIT, carrera);
                                }
                            });

                            deletIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estas Seguro de eliminar la carrera:"+  carrera.getNombre()+" " + carrera.getModalidad() +"?");
                                    Optional<Integer> option = dialogConfirmacion.showAndWait();

                                    option.ifPresent(new Consumer<Integer>() {
                                        @Override
                                        public void accept(Integer integer) {
                                            if (integer.equals(1)){
                                                daoCarreras.eliminarCarrera(carrera.getIdCarrera());
                                                getLista().remove(carrera);
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
                        System.out.println("SE VA A BUSCAR POR EL NOMBRE PA");
                        List<Carrera> filtro = listaFiltros.stream()
                                .filter(carrera -> carrera.getNombre().toLowerCase().contains(t1.toLowerCase()))
                                .toList();

                        getLista().setAll(filtro);
                    }
                    case CLAVE -> {
                        List<Carrera> filtro = listaFiltros.stream()
                                .filter(carrera -> carrera.getIdClave().toLowerCase().contains(t1.toLowerCase()))
                                .toList();
                        getLista().setAll(filtro);
                    }
                    case CREDITOS -> {
                        List<Carrera> filtro = listaFiltros.stream()
                                .filter(carrera -> carrera.getCreditos().toLowerCase().contains(t1.toLowerCase()))
                                .toList();
                        getLista().setAll(filtro);
                    }
                }
            }
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
                    filtros = Filtros.CLAVE;
                }
                case 2->{
                    filtros = Filtros.NOMBRE;
                }
                case 3->{
                    filtros = Filtros.CREDITOS;
                }
            }
        });
    }
}
enum Filtros{
    CLAVE,
    NOMBRE,
    CREDITOS
}
