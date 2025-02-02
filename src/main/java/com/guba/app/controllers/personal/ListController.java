package com.guba.app.controllers.personal;

import com.guba.app.data.dao.DAOPersonal;
import com.guba.app.utils.BaseController;
import com.guba.app.utils.Estado;
import com.guba.app.utils.Mediador;
import com.guba.app.utils.Paginas;
import com.guba.app.domain.models.Personal;
import com.jfoenix.controls.JFXButton;
import javafx.beans.InvalidationListener;
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
import java.util.function.Consumer;

public class ListController extends BaseController<Personal> implements Initializable {

    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnBorrarFiltros;
    @FXML
    private JFXButton btnActualizar;
    @FXML
    private TableView<Personal> tableView;
    @FXML
    TableColumn<Personal, String> rfc, nombre, apPaterno, apMaterno, acciones;
    @FXML
    private TextField busquedaSearch;
    @FXML
    private JFXButton btnFiltros;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private ToggleGroup toggleFiltroNormal;
    private Filtros filtroSeleccioando = Filtros.NOMBRE;
    private FilteredList<Personal> filteredList;

    public ListController(Mediador<Personal> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty, ObservableList<Personal> list) {
        super("/personal/List", mediador, estadoProperty, paginasProperty);
        setColumns();
        loadAsyncPersonal();
        setFiltro();
        btnAgregar.setOnAction(this::openPanelAdd);
        btnBorrarFiltros.setOnAction(this::borrarFiltros);
        btnActualizar.setOnAction(event -> {
            loadAsyncPersonal();
        });
        estadoProperty.addListener((observableValue, oldValue, newValue) -> {
            if (newValue.equals(Estado.CARGANDO)){
                tableView.setVisible(false);
            } else if (newValue.equals(Estado.CARGADO)) {
                filteredList = new FilteredList<>(list, estudiante -> true);
                tableView.setVisible(true);
                tableView.setItems(filteredList);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void openPanelAdd(ActionEvent event) {
        mediador.loadContent(Paginas.ADD, new Personal());
    }

    private void borrarFiltros(ActionEvent event) {
        busquedaSearch.setText("");
        loadAsyncPersonal();
        toggleFiltroNormal.selectToggle(null);
    }

    private void setColumns() {
        rfc.setCellValueFactory(new PropertyValueFactory<>("rfc"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apPaterno.setCellValueFactory(new PropertyValueFactory<>("apPat"));
        apMaterno.setCellValueFactory(new PropertyValueFactory<>("apMat"));
        acciones.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Personal, String> call(TableColumn<Personal, String> estudianteStringTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            Personal personal = getTableView().getItems().get(getIndex());

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
                                    mediador.loadContent(Paginas.DETAILS, personal);
                                }
                            });

                            editIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    mediador.loadContent(Paginas.EDIT, personal);
                                }
                            });

                            deletIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    Dialog<Integer> dialog = new Dialog<Integer>();
                                    dialog.setContentText("¿Estas Seguro de eliminar al personal: " + personal.getNombre());
                                    ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                                    dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
                                    dialog.setResultConverter(buttonType -> {
                                        if (buttonType == okButtonType) {
                                            return 1;
                                        }
                                        return 0;
                                    });

                                    Optional<Integer> option = dialog.showAndWait();

                                    if (option.isPresent() && option.get().equals(1)) {
                                        mediador.eliminar(personal);
                                    }
                                }
                            });
                            HBox hBox = new HBox();
                            hBox.getChildren().addAll(openIcon, editIcon, deletIcon);
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
            filtroSeleccioando =  rb == null ? Filtros.NOMBRE : Filtros.valueOf(rb.getUserData().toString());
            aplicarFiltros();
        });
    }
    private void aplicarFiltros() {
        filteredList.setPredicate(personal -> {
            String filtro = busquedaSearch.getText().toLowerCase();
            return filtro.isEmpty() || switch (filtroSeleccioando){
                case NOMBRE -> personal.getNombre().toLowerCase().contains(filtro);
                case APELLIDOS -> personal.getApPat().toLowerCase().contains(filtro);
            };
        });
    }

    private void loadAsyncPersonal(){
        mediador.loadBD();
    }

    @Override
    protected void cleanData() {
        loadAsyncPersonal();
    }

    enum Filtros{
        NOMBRE,
        APELLIDOS
    }

}
