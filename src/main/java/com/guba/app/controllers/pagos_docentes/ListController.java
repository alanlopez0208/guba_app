package com.guba.app.controllers.pagos_docentes;

import com.dlsc.gemsfx.YearMonthPicker;
import com.guba.app.data.dao.DAOPagoDocentes;
import com.guba.app.utils.BaseController;
import com.guba.app.utils.Paginas;
import com.guba.app.domain.models.*;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
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
import java.util.function.Consumer;

public class ListController extends BaseController<PagoDocente> implements Initializable {

    @FXML
    private Label label;
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
    private Filtros filtros = Filtros.NOMBRE;
    private List<PagoDocente> pagoList = new ArrayList<>();
    private ObservableList<PagoDocente> listaFiltros = FXCollections.observableArrayList();
    private DAOPagoDocentes daoPagoDocentes = new DAOPagoDocentes();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarPagoAlumnos();
        setCellColumns();
        //setFiltro();
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
        //getLista().setAll(pagoList);
    }


    private void cargarPagoAlumnos(){
        Task<List<PagoDocente>> task = new Task<List<PagoDocente>>() {
            @Override
            protected List<PagoDocente> call() throws Exception {
                return daoPagoDocentes.getPagos();
            }
        };
        task.setOnSucceeded(event -> {
            try {
                label.setVisible(false);
                pagoList = task.get();
                listaFiltros.setAll(pagoList);
                //getLista().setAll(listaFiltros);
                //tableView.setItems(getLista());
                tableView.setVisible(true);
                tableView.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Error al carrgar los pagos docentes: " +task.getException());
            }
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
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
                                                daoPagoDocentes.deletePago(pagoDocente.getIdPago());
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

    /*
    private void setFiltro(){
        yearmMonthPicker.getEditor().setText(null);
        busquedaSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (t1.isEmpty()){
                    getLista().setAll(listaFiltros);
                    return;
                }
                System.out.println(filtros);
                switch (filtros){

                    case NOMBRE -> {
                        System.out.println("SE VA A BUSCAR POR EL NOMBRE PA");
                        List<PagoDocente> filtro = listaFiltros.stream()
                                .filter(pagoDocente -> pagoDocente.getMaestro().getNombre().toLowerCase().contains(t1.toLowerCase()))
                                .toList();

                        getLista().setAll(filtro);
                    }
                    case RFC -> {
                        List<PagoDocente> filtro = listaFiltros.stream()
                                .filter(pagoDocente -> pagoDocente.getMaestro().getRfc().toLowerCase().contains(t1.toLowerCase()))
                                .toList();
                        getLista().setAll(filtro);
                    }
                    case CANTIDAD -> {
                        List<PagoDocente> filtro = listaFiltros.stream()
                                .filter(pagoDocente -> pagoDocente.getCantidad().toLowerCase().contains(t1.toLowerCase()))
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
                    filtros = Filtros.RFC;
                }
                case 2->{
                    filtros = Filtros.NOMBRE;
                }
                case 3->{
                    filtros = Filtros.CANTIDAD;
                }
            }
        });


        yearmMonthPicker.valueProperty().addListener((observableValue, yearMonth, t1) -> {
            if (t1 == null){
                return;
            }
            listaFiltros.setAll(pagoList.stream()
                    .filter(pa->{
                        YearMonth yearMonth1 = YearMonth.from(pa.getDate());
                        return t1.equals(yearMonth1);
                    })
                    .toList());

            getLista().setAll(listaFiltros);

        });
    }*/

    @Override
    protected void cleanData() {

    }
}
enum Filtros{
    NOMBRE,
    RFC,
    CANTIDAD
}

