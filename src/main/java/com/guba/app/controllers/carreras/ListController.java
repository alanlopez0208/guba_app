package com.guba.app.controllers.carreras;

import com.guba.app.data.dao.DAOCarreras;
import com.guba.app.utils.BaseController;
import com.guba.app.utils.Mediador;
import com.guba.app.utils.Paginas;
import com.guba.app.domain.models.Carrera;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.guba.app.utils.Estado;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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

public class ListController extends BaseController<Carrera>{

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

    private FilteredList<Carrera> filteredList;
    private Filtros filtroSeleccionado = Filtros.NOMBRE;
    private List<Carrera> carreraList = new ArrayList<>();
    private ObservableList<Carrera> listaFiltros = FXCollections.observableArrayList();

    private DAOCarreras daoCarreras = new DAOCarreras();


    public ListController(String ruta, Mediador<Carrera> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super(ruta, mediador, estadoProperty, paginasProperty);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarCarrerasBD();
        setCellColumns();
        setFiltro();
        estadoProperty.addListener((observableValue, oldValue, newValue) -> {
            if (newValue.equals(Estado.CARGANDO)){
                label.setVisible(true);
                tableView.setVisible(false);
            } else if (newValue.equals(Estado.CARGADO)) {
                filteredList = new FilteredList<>(listaFiltros,estudiante -> true);
                tableView.setVisible(true);
                tableView.setItems(filteredList);
            }
        });
    }

    @FXML
    private void openPaneAddAlumno(ActionEvent event){
        mediador.loadContent(Paginas.ADD, new Carrera());
    }

    @FXML
    private void borrarFiltros(ActionEvent event){
        busquedaSearch.setText("");
        mediador.loadBD();
        filteredList.filtered(c -> true);
        toggleFiltroNormal.selectToggle(null);

    }

    private void cargarCarrerasBD(){
        mediador.loadBD();
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
                                    paginasProperty.set(Paginas.DETAILS);
                                }
                            });

                            editIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    paginasProperty.set(Paginas.EDIT);
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
                                               mediador.eliminar(carrera);
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
        busquedaSearch.textProperty().addListener((observableValue, s, t1) -> {
            aplicarFiltros();
        });

        toggleFiltroNormal.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioMenuItem rb = (RadioMenuItem) toggleFiltroNormal.getSelectedToggle();
            filtroSeleccionado  = (rb != null) ? (Filtros) rb.getUserData() : null;
        });
    }

    private void aplicarFiltros(){
        filteredList.filtered(carrera -> {
            String filtro = busquedaSearch.getText().toLowerCase();
            return switch (filtroSeleccionado){
                case NOMBRE -> carrera.getNombre().toLowerCase().contains(filtro);
                case CLAVE -> carrera.getIdClave().toLowerCase().contains(filtro);
                case CREDITOS -> carrera.getCreditos().toLowerCase().contains(filtro);
            };
        });
    }

    @Override
    protected void cleanData() {

    }
}
enum Filtros{
    CLAVE,
    NOMBRE,
    CREDITOS
}
