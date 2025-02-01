package com.guba.app.controllers.grupos;

import com.guba.app.data.dao.DAOCarreras;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Carrera;
import com.guba.app.domain.models.Grupo;
import com.guba.app.presentation.dialogs.DialogGrupo;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.guba.app.data.local.database.Service;
import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class ListController extends BaseController<Grupo> implements Initializable {

    @FXML
    private Button btnAgregar;
    @FXML
    private JFXButton btnActualizar;
    @FXML
    private JFXButton btnBorrarFiltros;
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

    private FilteredList<Grupo> filteredList;
    private Filtros filtroSeleccionado = Filtros.NOMBRE;
    private Carrera carreraSeleccionada;
    private Dialog<Grupo> addDialog;

    public ListController(Mediador<Grupo> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty, ObservableList<Grupo> list) {
        super("/grupos/List", mediador, estadoProperty, paginasProperty);
        cargarGruposBd();
        setCellColumns();
        setFiltro();
        estadoProperty.addListener((observableValue, oldValue, newValue) -> {
            if (newValue.equals(Estado.CARGANDO)){
                label.setVisible(true);
                tableView.setVisible(false);
            } else if (newValue.equals(Estado.CARGADO)) {
                label.setVisible(false);
                filteredList = new FilteredList<>(list, estudiante -> true);
                tableView.setVisible(true);
                tableView.setItems(filteredList);
            }
        });
        btnAgregar.setOnAction(this::openPaneAdd);
        btnBorrarFiltros.setOnAction(this::borrarFiltros);
        btnActualizar.setOnAction(actionEvent -> {
            mediador.loadBD();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void openPaneAdd(ActionEvent event){
        DialogGrupo dialogGrupo = new DialogGrupo(new Grupo());
        Optional<Grupo> optionalGrupo = dialogGrupo.showAndWait();
        optionalGrupo.ifPresent(new Consumer<Grupo>() {
            @Override
            public void accept(Grupo grupo) {
                boolean seAgrego = mediador.guardar(grupo);
            }
        });
    }

    private void borrarFiltros(ActionEvent event){
        busquedaSearch.setText("");
        toggleCarreras.selectToggle(null);
        toggleFiltroNormal.selectToggle(null);
    }

    private void cargarGruposBd(){
       mediador.loadBD();
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
                                        mediador.actualizar(grupo);
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
                                                boolean seElimino = mediador.eliminar(grupo);
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

        toggleCarreras.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioMenuItem rb = (RadioMenuItem) toggleCarreras.getSelectedToggle();
            carreraSeleccionada = rb == null ? null : (Carrera) rb.getUserData();
            aplicarFiltros();
        });

        toggleFiltroNormal.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioMenuItem rb = (RadioMenuItem) toggleFiltroNormal.getSelectedToggle();
            filtroSeleccionado = (rb == null ) ? Filtros.NOMBRE : Filtros.valueOf(rb.getUserData().toString());
            aplicarFiltros();
        });

        Utils.loadAsync(()-> new DAOCarreras().getAllCarreras(),carreras -> {
            carreras.forEach(carrera -> {
                RadioMenuItem radioButton = new RadioMenuItem();
                radioButton.setText(carrera.getNombre() + " " + carrera.getModalidad());
                radioButton.setToggleGroup(toggleCarreras);
                radioButton.setUserData(carrera);
                contextMenu.getItems().add(radioButton);
            });
        });
    }

    private void aplicarFiltros() {
        filteredList.setPredicate(grupo->{
            boolean carreraCompatible = carreraSeleccionada == null || grupo.getCarrera().getIdCarrera().equals(carreraSeleccionada.getIdCarrera());
            boolean textoCompatible = busquedaSearch.getText().isEmpty() || cumpleFiltroTexto(grupo);

            return carreraCompatible && textoCompatible;
        });
    }

    private boolean cumpleFiltroTexto(Grupo grupo){
        String texto = busquedaSearch.getText().toLowerCase();
        return switch (filtroSeleccionado){
            case NOMBRE -> grupo.getNombre().toLowerCase().contains(texto);
            case SEMESTRE -> grupo.getSemestre().equals(texto);
        };
    }

    @Override
    protected void cleanData() {
        cargarGruposBd();
    }

    enum Filtros{
        NOMBRE,
        SEMESTRE,
    }
}




