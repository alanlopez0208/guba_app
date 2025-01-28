package com.guba.app.controllers.cursos;

import com.dlsc.gemsfx.YearMonthPicker;
import com.guba.app.controllers.BaseController;
import com.guba.app.controllers.Paginas;
import com.guba.app.dao.DAOCurso;
import com.guba.app.models.Curso;
import com.guba.app.models.Estudiante;
import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.apache.xmlbeans.impl.store.Cur;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ListController extends BaseController<Curso> implements Initializable {

    @FXML
    private TableView<Curso> tableView;
    @FXML
    TableColumn<Curso, String> nombre, fechaInicio , fechaFinalizacion,modalidad,acciones;
    @FXML
    private TextField busquedaSearch;
    @FXML
    private JFXButton btnFiltros;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private ToggleGroup toggleFiltroNormal;
    @FXML
    private YearMonthPicker yearmMonthPicker;
    private List<Curso> cursosList;
    private ObservableList<Curso> listaFiltros = FXCollections.observableArrayList();
    private ToggleGroup toggleCarreras = new ToggleGroup();
    private DAOCurso daoCurso = new DAOCurso();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setColumns();
        loadAsyncCursos(cursos -> {
            getLista().setAll(cursos);
            cursosList = cursos;
            listaFiltros.setAll(cursos);
            getLista().setAll(listaFiltros);
            tableView.setItems(getLista());
        });
        setFilter();

    }

    @FXML
    private void openPanelAdd(){
        mediador.loadData(Paginas.ADD, new Curso());
    }

    @FXML
    private void borrarFiltros(ActionEvent event){
        busquedaSearch.setText("");
        yearmMonthPicker.getEditor().setText(null);
        //toggleFiltroNormal.selectToggle(null);
        getLista().setAll(cursosList);
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
                                    mediador.loadData(Paginas.DETAILS, curso);
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
                                        boolean seElimnio= daoCurso.eliminarCurso(curso.getIdCurso());
                                        System.out.println(seElimnio);
                                        if (seElimnio){
                                            getLista().remove(curso);
                                        }
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
        busquedaSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (t1.isEmpty()){
                    getLista().setAll(listaFiltros);
                    return;
                }
                List<Curso> filtro = listaFiltros.stream()
                        .filter(curso -> curso.getNombre().toLowerCase().contains(t1.toLowerCase()))
                        .toList();

                getLista().setAll(filtro);
            }
        });


        yearmMonthPicker.valueProperty().addListener((observableValue, yearMonth, t1) -> {
            if (t1 == null){
                return;
            }
            listaFiltros.setAll(cursosList.stream()
                            .filter(c->{
                                YearMonth yearMonth1 = YearMonth.from(c.getDateInicio());
                                return t1.equals(yearMonth1);
                            })
                    .toList());

            getLista().setAll(listaFiltros);

        });
    }

    private void loadAsyncCursos(Consumer<List<Curso>> callBack){
        Task<List<Curso>> task = new Task<List<Curso>>() {
            @Override
            protected List<Curso> call() throws Exception {
                return daoCurso.obtenerTodosLosCursos();
            }
        };
        task.setOnSucceeded(event -> {
            try {
               callBack.accept(task.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}


