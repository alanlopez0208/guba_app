package com.guba.app.controllers.maestro;


import com.guba.app.utils.BaseController;
import com.guba.app.utils.Estado;
import com.guba.app.utils.Mediador;
import com.guba.app.utils.Paginas;
import com.guba.app.domain.models.Maestro;
import com.jfoenix.controls.JFXButton;
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
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListController extends BaseController<Maestro> implements Initializable {

    @FXML
    private Button btnAgregar;
    @FXML
    private JFXButton btnBorrarFiltros;
    @FXML
    private TableView<Maestro> tableView;
    @FXML
    TableColumn<Maestro, String> rfc, nombre, apPaterno,apMaterno,acciones;
    @FXML
    private TextField busquedaSearch;
    @FXML
    private ToggleGroup toggleFiltroNormal;
    @FXML
    private JFXButton btnActualizar;

    private ObservableList<Maestro> list;
    private FilteredList<Maestro> filteredList;
    private Filtros filtroSeleccionado = Filtros.NOMBRE;

    public ListController(Mediador<Maestro> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty, ObservableList<Maestro> list) {
        super("/maestros/List", mediador, estadoProperty, paginasProperty);
        this.list = list;
        setColumns();
        mediador.loadBD();
        setFiltro();
        estadoProperty.addListener((observableValue, oldValue, newValue) -> {
            if (newValue.equals(Estado.CARGANDO)){

                tableView.setVisible(false);
            } else if (newValue.equals(Estado.CARGADO)) {
                filteredList = new FilteredList<>(list, estudiante -> true);
                tableView.setVisible(true);
                tableView.setItems(filteredList);
            }
        });
        btnAgregar.setOnAction(this::openPanelAdd);
        btnBorrarFiltros.setOnAction(this::borrarFiltros);
        btnActualizar.setOnAction(actionEvent -> {
            mediador.loadBD();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private void openPanelAdd(ActionEvent event){
        mediador.loadContent(Paginas.ADD, new Maestro());
    }

    private void borrarFiltros(ActionEvent event){
        busquedaSearch.setText("");
        toggleFiltroNormal.selectToggle(null);
    }



    private void setColumns(){
        rfc.setCellValueFactory(new PropertyValueFactory<>("rfc"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apPaterno.setCellValueFactory(new PropertyValueFactory<>("apPat"));
        apMaterno.setCellValueFactory(new PropertyValueFactory<>("apMat"));
        acciones.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Maestro, String> call(TableColumn<Maestro, String> estudianteStringTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            Maestro maestro = getTableView().getItems().get(getIndex());

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
                                    mediador.loadContent(Paginas.DETAILS, maestro);
                                }
                            });

                            editIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    mediador.loadContent(Paginas.EDIT, maestro);
                                }
                            });

                            deletIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    Dialog<Integer> dialog = new Dialog<Integer>();
                                    dialog.setContentText("¿Estas Seguro de eliminar al docente: "+  maestro.getNombre());
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
                                        boolean seElimnio= mediador.eliminar(maestro);
                                        if (seElimnio){
                                            paginasProperty.setValue(Paginas.LIST);
                                        }
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
        busquedaSearch.textProperty().addListener((observableValue, s, t1) -> {
           aplicarFiltros();
        });

        toggleFiltroNormal.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioMenuItem rb = (RadioMenuItem) toggleFiltroNormal.getSelectedToggle();
            filtroSeleccionado = rb == null ? Filtros.NOMBRE :  Filtros.valueOf(rb.getUserData().toString().toUpperCase());
        });
    }

    private void aplicarFiltros(){
        filteredList.setPredicate(maestro->{
            String busqueda = busquedaSearch.getText().toLowerCase();
            return busqueda.isEmpty() || switch (filtroSeleccionado){
                case RFC ->  maestro.getRfc().toLowerCase().contains(busqueda);
                case NOMBRE -> maestro.getNombre().toLowerCase().contains(busqueda);
                case APELLIDOS -> maestro.getApPat().toLowerCase().contains(busqueda);
            };
        });
    }

    @Override
    protected void cleanData() {
       mediador.loadBD();
    }

    enum Filtros{
        RFC,
        NOMBRE,
        APELLIDOS
    }
}

