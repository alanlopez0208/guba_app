package com.guba.app.controllers.cursos;

import com.dlsc.gemsfx.YearMonthPicker;
import com.guba.app.domain.models.Estudiante;
import com.guba.app.utils.BaseController;
import com.guba.app.utils.Estado;
import com.guba.app.utils.Mediador;
import com.guba.app.utils.Paginas;
import com.guba.app.data.dao.DAOCurso;
import com.guba.app.domain.models.Curso;
import com.jfoenix.controls.JFXButton;
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
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ListController extends BaseController<Curso> {

    @FXML
    private Button btnAgregar;
    @FXML
    private TableView<Curso> tableView;
    @FXML
    private TableColumn<Curso, String> nombre, fechaInicio , fechaFinalizacion,modalidad,acciones;
    @FXML
    private TextField busquedaSearch;
    @FXML
    private JFXButton btnFiltros;
    @FXML
    private JFXButton btnActualizar;
    @FXML
    private Button btnBorrarFiltros;
    @FXML
    private YearMonthPicker yearmMonthPicker;

    private ObservableList<Curso> list;
    private FilteredList<Curso> filteredList;
    private YearMonth fechaSeleccionada = null;


    public ListController( Mediador<Curso> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty, ObservableList<Curso> list) {
        super("/cursos/List", mediador, estadoProperty, paginasProperty);
        this.list = list;
        setColumns();
        setFilter();
        loadAsyncCursos();
        estadoProperty.addListener((observableValue, oldValue, newValue) -> {
            if (newValue.equals(Estado.CARGANDO)){
                tableView.setVisible(false);
            } else if (newValue.equals(Estado.CARGADO)) {
                filteredList = new FilteredList<>(list, curso -> true);
                tableView.setVisible(true);
                tableView.setItems(filteredList);
            }
        });
        btnAgregar.setOnAction(this::openPanelAdd);
        btnBorrarFiltros.setOnAction(this::borrarFiltros);
        btnActualizar.setOnAction(this::actualizar);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    private void loadAsyncCursos(){
        mediador.loadBD();
    }

    private void openPanelAdd(ActionEvent actionEvent){
        mediador.loadContent(Paginas.ADD, new Curso());
    }

    private void borrarFiltros(ActionEvent event){
        busquedaSearch.setText("");
        yearmMonthPicker.getEditor().setText(null);
        filteredList.setPredicate(null);
    }

    private void actualizar(ActionEvent event){
        mediador.loadBD();
    }

    private void setColumns(){
        fechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        fechaFinalizacion.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        modalidad.setCellValueFactory(new PropertyValueFactory<>("modalidad"));
        acciones.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Curso, String> call(TableColumn<Curso, String> estudianteStringTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            Curso curso = getTableView().getItems().get(getIndex());

                            Button openIcon = new Button();
                            Button deletIcon = new Button();


                            FontIcon open = new FontIcon("mdi-eye");
                            open.setFill(Color.WHITE);
                            openIcon.setGraphic(open);
                            openIcon.getStyleClass().add("btnVer");


                            FontIcon delete = new FontIcon("mdi-delete");
                            delete.setFill(Color.WHITE);
                            deletIcon.setGraphic(delete);
                            deletIcon.getStyleClass().add("btnBorrar");


                            openIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    mediador.loadContent(Paginas.DETAILS, curso);
                                }
                            });
                            deletIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    Dialog<Integer> dialog = new Dialog<Integer>();
                                    dialog.setContentText("¿Estas Seguro de eliminar?");
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
                                        mediador.eliminar(curso);
                                    }
                                }
                            });
                            HBox hBox = new HBox();
                            hBox.getChildren().addAll(openIcon, deletIcon);
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

    private void setFilter(){
        yearmMonthPicker.getEditor().setText(null);
        busquedaSearch.textProperty().addListener((observableValue, s, t1) -> aplicarFiltros());
        yearmMonthPicker.valueProperty().addListener((observableValue, yearMonth, t1) -> {
            aplicarFiltros();
        });
    }

    private void aplicarFiltros(){
        filteredList.setPredicate(curso->{
           boolean coincideFecha = yearmMonthPicker.getValue() == null || yearmMonthPicker.getValue().equals(YearMonth.from(curso.getDateInicio()));
           boolean coincideTexto = busquedaSearch.getText().isEmpty() || busquedaSearch.getText().equalsIgnoreCase(curso.getNombre());

            System.out.println(coincideFecha && coincideTexto);
            return coincideFecha && coincideTexto;
        });
    }

    @Override
    protected void cleanData() {

    }
}


