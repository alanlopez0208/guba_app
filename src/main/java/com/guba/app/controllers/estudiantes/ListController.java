package com.guba.app.controllers.estudiantes;

import com.guba.app.utils.Estado;
import com.guba.app.utils.*;
import com.guba.app.data.dao.DAOCarreras;
import com.guba.app.domain.models.Carrera;
import com.guba.app.domain.models.Estudiante;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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

import java.util.*;
import java.util.function.Consumer;

public class ListController extends BaseController<Estudiante> {
    @FXML
    private Label label;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnBorrarFiltros;
    @FXML
    private JFXButton btnActualizar;
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

    private ToggleGroup toggleCarreras;
    private FilteredList<Estudiante> filteredList;
    private ObservableList<Estudiante> list;
    private Filtros filtroSeleccioado = Filtros.MATRICULA;
    private Carrera carreraSeleccionada;


    public ListController(Mediador<Estudiante> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty, ObservableList<Estudiante> list) {
        super("/estudiantes/ListEstudiantes", mediador, estadoProperty, paginasProperty);
        this.list = list;
        toggleCarreras = new ToggleGroup();
        btnAgregar.setOnAction(this::openPaneAddAlumno);
        btnBorrarFiltros.setOnAction(this::borrarFiltros);
        btnActualizar.setOnAction(event -> {
            loadEstudiantesFromBD();
        });
        loadEstudiantesFromBD();
        setCollumnCell();
        setFiltro();
        estadoProperty.addListener((observableValue, oldValue, newValue) -> {
            if (newValue.equals(Estado.CARGANDO)){
                label.setVisible(true);
                tableView.setVisible(false);
            } else if (newValue.equals(Estado.CARGADO)) {
                filteredList = new FilteredList<>(list, estudiante -> true);
                label.setVisible(false);
                tableView.setVisible(true);
                tableView.setItems(filteredList);
            }
        });
    }

    private void openPaneAddAlumno(ActionEvent event){
        mediador.loadContent(Paginas.ADD, new Estudiante());
    }

    private void borrarFiltros(ActionEvent event){
        busquedaSearch.setText("");
        loadEstudiantesFromBD();
        toggleCarreras.selectToggle(null);
        toggleFiltroNormal.selectToggle(null);
    }

    private void loadEstudiantesFromBD(){
        mediador.loadBD();
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
                            Estudiante estudiante = tableView.getItems().get(getIndex());

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
                                   mediador.loadContent(Paginas.DETAILS, estudiante);
                                }
                            });

                            editIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    mediador.loadContent(Paginas.EDIT, estudiante);
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

                                    dialog.showAndWait().ifPresent(integer -> {
                                        mediador.eliminar(estudiante);
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
                aplicarFiltros();
            }
        });

        Utils.loadAsync(
                () -> new DAOCarreras().getAllCarreras(),
                carreras -> {
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
            carreraSeleccionada = (rb != null) ? (Carrera) rb.getUserData() : null;
            aplicarFiltros();
        });


        toggleFiltroNormal.selectedToggleProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue instanceof RadioMenuItem rb) {
                filtroSeleccioado = Filtros.valueOf(rb.getUserData().toString().toUpperCase());
                aplicarFiltros();
            }
        });
    }


    private void aplicarFiltros() {
        filteredList.setPredicate(estudiante -> {
            boolean coincideCarrera = (carreraSeleccionada == null || estudiante.getCarrera().getIdCarrera().equals(carreraSeleccionada.getIdCarrera()));
            boolean coincideTexto = busquedaSearch.getText().isEmpty() || cumpleFiltroTexto(estudiante);
            return coincideCarrera && coincideTexto;
        });
    }

    private boolean cumpleFiltroTexto(Estudiante estudiante) {
        String filtro = busquedaSearch.getText().toLowerCase();
        return switch (filtroSeleccioado){
            case NOMBRE  -> estudiante.getNombre().toLowerCase().contains(filtro);
            case MATRICULA -> estudiante.getMatricula().toLowerCase().contains(filtro);
            case APELLIDOS -> estudiante.getApPaterno().toLowerCase().contains(filtro);
        };
    }


    @Override
    protected void cleanData() {
        mediador.loadBD();
    }

    enum Filtros{
        NOMBRE,
        MATRICULA,
        APELLIDOS
    }
}

