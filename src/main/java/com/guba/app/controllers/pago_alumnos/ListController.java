package com.guba.app.controllers.pago_alumnos;

import com.dlsc.gemsfx.YearMonthPicker;
import com.guba.app.data.dao.DAOPagoAlumnos;
import com.guba.app.utils.BaseController;
import com.guba.app.utils.Paginas;
import com.guba.app.domain.models.Estudiante;
import com.guba.app.domain.models.PagoAlumno;
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

public class ListController extends BaseController<PagoAlumno> implements Initializable {

    @FXML
    private Label label;
    @FXML
    private TableView<PagoAlumno> tableView;
    @FXML
    TableColumn<PagoAlumno, String> matricula, nombre, fecha,cantidad, concepto,acciones;
    @FXML
    private TextField busquedaSearch;
    @FXML
    private JFXButton btnFiltros;
    @FXML
    private ToggleGroup toggleFiltroNormal;
    @FXML
    private YearMonthPicker yearmMonthPicker;
    private Filtros filtros = Filtros.NOMBRE;
    private List<PagoAlumno> pagoList = new ArrayList<>();
    private ObservableList<PagoAlumno> listaFiltros = FXCollections.observableArrayList();
    private DAOPagoAlumnos daoPagoAlumnos = new DAOPagoAlumnos();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarPagoAlumnos();
        setCellColumns();
        //setFiltro();
    }

    @FXML
    private void openPaneAddAlumno(ActionEvent event){
        mediador.loadContent(Paginas.ADD, new PagoAlumno());
    }

    @FXML
    private void borrarFiltros(ActionEvent event){
        busquedaSearch.setText("");
        yearmMonthPicker.getEditor().setText(null);
        toggleFiltroNormal.selectToggle(null);
        //getLista().setAll(pagoList);
    }

    private void cargarPagoAlumnos(){
        Task<List<PagoAlumno>> task = new Task<List<PagoAlumno>>() {
            @Override
            protected List<PagoAlumno> call() throws Exception {
                return daoPagoAlumnos.getPagos();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Error al carrgar las materias" +task.getException());
            }
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void setCellColumns(){
        matricula.setCellValueFactory(cellData -> {
            PagoAlumno pagoAlumno = cellData.getValue();
            return Bindings.createStringBinding(() -> {
                Estudiante estudiante = pagoAlumno.getAlumno();
                if (estudiante.getMatricula() != null){
                    return estudiante.getMatricula();
                }
                return null;
            }, pagoAlumno.alumnoProperty());
        });
        nombre.setCellValueFactory(cellData -> {
            PagoAlumno pagoAlumno = cellData.getValue();
            return Bindings.createStringBinding(() -> {
                Estudiante estudiante = pagoAlumno.getAlumno();
                if (estudiante.getNombre() != null){
                    return estudiante.getNombre() + " " +  estudiante.getApMaterno() + " " + estudiante.getApMaterno();
                }
                return null;
            }, pagoAlumno.alumnoProperty());
        });
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        concepto.setCellValueFactory(new PropertyValueFactory<>("concepto"));
        acciones.setCellFactory(new Callback<>() {
            @Override
            public TableCell<PagoAlumno, String> call(TableColumn<PagoAlumno, String> estudianteStringTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            PagoAlumno pagoAlumno = getTableView().getItems().get(getIndex());

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
                                    mediador.loadContent(Paginas.DETAILS, pagoAlumno);
                                }
                            });

                            editIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    mediador.loadContent(Paginas.EDIT, pagoAlumno);
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
                                                daoPagoAlumnos.deletePago(pagoAlumno.getIdPago());
                                                //getLista().remove(pagoAlumno);
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
                        List<PagoAlumno> filtro = listaFiltros.stream()
                                .filter(pagoAlumno -> pagoAlumno.getAlumno().getNombre().toLowerCase().contains(t1.toLowerCase()))
                                .toList();

                        getLista().setAll(filtro);
                    }
                    case MATRICULA -> {
                        List<PagoAlumno> filtro = listaFiltros.stream()
                                .filter(pagoAlumno -> pagoAlumno.getAlumno().getMatricula().toLowerCase().contains(t1.toLowerCase()))
                                .toList();
                        getLista().setAll(filtro);
                    }
                    case CANTIDAD -> {
                        List<PagoAlumno> filtro = listaFiltros.stream()
                                .filter(pagoAlumno -> pagoAlumno.getCantidad().toLowerCase().contains(t1.toLowerCase()))
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
                    filtros = Filtros.MATRICULA;
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

            //getLista().setAll(listaFiltros);

        });
    }*/

    @Override
    protected void cleanData() {

    }
}
enum Filtros{
    NOMBRE,
    MATRICULA,
    CANTIDAD
}
