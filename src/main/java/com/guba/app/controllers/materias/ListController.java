package com.guba.app.controllers.materias;

import com.guba.app.data.dao.DAOCarreras;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Carrera;
import com.guba.app.domain.models.Materia;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ListController extends BaseController<Materia> implements Initializable {

    @FXML
    private Button btnAgregar;
    @FXML
    private JFXButton btnActualizar;
    @FXML
    private JFXButton btnBorrarFiltros;
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

    private FilteredList<Materia> filteredList;
    private Filtros filtroSeleccionado = Filtros.CLAVE;
    private Carrera carreraSelecionada;
    private String semestreSeleccioando;


    public ListController( Mediador<Materia> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty, ObservableList<Materia> list) {
        super("/materias/List", mediador, estadoProperty, paginasProperty);
        cargarData();
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
        mediador.loadContent(Paginas.ADD, new Materia());
    }

    private void borrarFiltros(ActionEvent event){
        toggleSemestre.selectToggle(null);
        toggleCarreras.selectToggle(null);
        toggleFiltroNormal.selectToggle(null);
        busquedaSearch.setText("");
    }

    private void cargarData(){
        mediador.loadBD();
        loadCarrerasAsync();
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
                                    mediador.loadContent(Paginas.DETAILS, materia);
                                }
                            });

                            editIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    mediador.loadContent(Paginas.EDIT, materia);
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
                                                mediador.eliminar(materia);
                                                //getLista().remove(materia);
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
            carreraSelecionada = rb == null ? null : (Carrera) rb.getUserData();
            aplicarFiltros();
        });

        toggleFiltroNormal.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioMenuItem rb = (RadioMenuItem) toggleFiltroNormal.getSelectedToggle();
            filtroSeleccionado =  rb == null ? Filtros.NOMBRE : Filtros.valueOf(rb.getUserData().toString());
            aplicarFiltros();
        });

        toggleSemestre.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioMenuItem rb = (RadioMenuItem) toggleSemestre.getSelectedToggle();
            semestreSeleccioando = rb == null ? null :  rb.getUserData().toString();
            aplicarFiltros();
        });
    }

    private void aplicarFiltros() {
        filteredList.setPredicate(materia -> {
            boolean coincideCarrera = carreraSelecionada == null || materia.getCarreraModelo().getIdCarrera().equals(carreraSelecionada.getIdCarrera());
            boolean coicideTexto = busquedaSearch.getText().isEmpty() ||  compararTexto(materia);
            boolean coincidenciaSemestre = semestreSeleccioando == null || materia.getSemestre().equals(semestreSeleccioando);
            return coincideCarrera && coicideTexto && coincidenciaSemestre;
        });
    }

    private boolean compararTexto(Materia materia){
        String busqueda = busquedaSearch.getText().toLowerCase();
        return switch (filtroSeleccionado){
            case NOMBRE -> materia.getNombre().toLowerCase().contains(busqueda);
            case CLAVE -> materia.getClave().toLowerCase().contains(busqueda);
        };
    }

    private void loadCarrerasAsync() {
        Utils.loadAsync(()-> new DAOCarreras().getAllCarreras(), carreras -> {
            carreras.forEach(carrera -> {
                RadioMenuItem radioButton = new RadioMenuItem();
                radioButton.setText(carrera.getNombre() + " " + carrera.getModalidad());
                radioButton.setToggleGroup(toggleCarreras);
                radioButton.setUserData(carrera);
                contextMenu.getItems().add(radioButton);
            });
        });
    }

    @Override
    protected void cleanData() {
        busquedaSearch.setText("");
        toggleSemestre.selectToggle(null);
        toggleCarreras.selectToggle(null);
        toggleFiltroNormal.selectToggle(null);
        cargarData();
    }

    enum Filtros{
        CLAVE,
        NOMBRE
    }

}
