package com.guba.app.controllers.pagos_docentes;

import com.dlsc.gemsfx.YearMonthPicker;
import com.guba.app.data.dao.DAOPagoDocentes;
import com.guba.app.utils.BaseController;
import com.guba.app.utils.Estado;
import com.guba.app.utils.Mediador;
import com.guba.app.utils.Paginas;
import com.guba.app.domain.models.*;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ListController extends BaseController<PagoDocente> {

    @FXML
    private Label label;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnBorrarFiltros;
    @FXML
    private JFXButton btnActualizar;
    @FXML
    private TableView<PagoDocente> tableView;
    @FXML
    TableColumn<PagoDocente, String> rfc, nombre, fecha,cantidad, concepto,acciones;
    @FXML
    private TextField busquedaSearch;
    @FXML
    private JFXButton btnFiltros;
    @FXML
    private ToggleGroup toggleFiltroNormal;
    @FXML
    private YearMonthPicker yearmMonthPicker;
    private Filtros filtroSeleccionado = Filtros.NOMBRE;
    private YearMonth yearMonthSelect;
    private FilteredList<PagoDocente> filteredList;


    public ListController(Mediador<PagoDocente> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty, ObservableList<PagoDocente> list) {
        super("/pago_docentes/List", mediador, estadoProperty, paginasProperty);
        cargarPagoDocentes();
        setCellColumns();
        setFiltro();
        btnAgregar.setOnAction(this::openPaneAddAlumno);
        btnBorrarFiltros.setOnAction(this::borrarFiltros);
        btnActualizar.setOnAction(event -> {
            cargarPagoDocentes();
        });
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void openPaneAddAlumno(ActionEvent event){
        mediador.loadContent(Paginas.ADD, new PagoDocente());
    }

    @FXML
    private void borrarFiltros(ActionEvent event){
        busquedaSearch.setText("");
        yearmMonthPicker.getEditor().setText(null);
        toggleFiltroNormal.selectToggle(null);
        filteredList.setPredicate(null);
    }


    private void cargarPagoDocentes(){
        mediador.loadBD();
    }

    private void setCellColumns(){
        rfc.setCellValueFactory(cellData -> {
            PagoDocente pagoDocente = cellData.getValue();
            return Bindings.createStringBinding(() -> {
                Maestro maestro = pagoDocente.getMaestro();
                if (maestro.getRfc() != null){
                    return maestro.getRfc();
                }
                return null;
            }, pagoDocente.maestroProperty());
        });
        nombre.setCellValueFactory(cellData -> {
            PagoDocente pagoDocente = cellData.getValue();
            return Bindings.createStringBinding(() -> {
                Maestro maestro = pagoDocente.getMaestro();
                if (maestro.getNombre() != null){
                    return maestro.getNombre() + " " + maestro.getApPat() + maestro.getApMat();
                }
                return null;
            }, pagoDocente.maestroProperty());
        });
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        concepto.setCellValueFactory(new PropertyValueFactory<>("concepto"));
        acciones.setCellFactory(new Callback<>() {
            @Override
            public TableCell<PagoDocente, String> call(TableColumn<PagoDocente, String> estudianteStringTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            PagoDocente pagoDocente = getTableView().getItems().get(getIndex());

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
                                    mediador.loadContent(Paginas.DETAILS, pagoDocente);
                                }
                            });

                            editIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    mediador.loadContent(Paginas.EDIT, pagoDocente);
                                }
                            });

                            deletIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estas Seguro de eliminar el Pago ?");
                                    Optional<Integer> option = dialogConfirmacion.showAndWait();

                                    option.ifPresent(new Consumer<Integer>() {
                                        @Override
                                        public void accept(Integer integer) {
                                            if (integer.equals(1)){
                                                mediador.eliminar(pagoDocente);
                                                //getLista().remove(pagoDocente);
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
        yearmMonthPicker.getEditor().setText(null);
        busquedaSearch.textProperty().addListener((observableValue, s, t1) -> {
            aplicarFiltro();
        });

        toggleFiltroNormal.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioMenuItem rb = (RadioMenuItem) toggleFiltroNormal.getSelectedToggle();
            filtroSeleccionado = rb == null ? Filtros.NOMBRE : Filtros.valueOf(rb.getUserData().toString());
            aplicarFiltro();
        });


        yearmMonthPicker.valueProperty().addListener((observableValue, yearMonth, t1) -> {
            yearMonthSelect = t1;
            aplicarFiltro();
        });
    }

    private void aplicarFiltro(){
        filteredList.setPredicate(pagoAlumno -> {
            YearMonth fechaPago = YearMonth.from(pagoAlumno.getDate());
            boolean coincideFecha = yearMonthSelect == null || fechaPago.compareTo(yearMonthSelect) >= 0;
            return coincideFecha && compararTexto(pagoAlumno);
        });
    }

    private boolean compararTexto(PagoDocente pagoAlumno){
        String busqueda = busquedaSearch.getText().toLowerCase();
        return busqueda.isEmpty() || switch (filtroSeleccionado){
            case NOMBRE -> pagoAlumno.getMaestro().getNombre().toLowerCase().contains(busqueda);
            case CANTIDAD -> pagoAlumno.getCantidad().toLowerCase().contains(busqueda);
            case RFC -> pagoAlumno.getMaestro().getRfc().toLowerCase().contains(busqueda);
        };
    }

    @Override
    protected void cleanData() {
        cargarPagoDocentes();
    }

    enum Filtros{
        NOMBRE,
        RFC,
        CANTIDAD
    }

}

