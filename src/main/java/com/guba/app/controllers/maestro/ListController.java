package com.guba.app.controllers.maestro;


import com.guba.app.dao.DAOAlumno;
import com.guba.app.dao.DAOMaestro;
import com.guba.app.controllers.BaseController;
import com.guba.app.controllers.Paginas;
import com.guba.app.models.Maestro;
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
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ListController extends BaseController<Maestro> implements Initializable {

    @FXML
    private TableView<Maestro> tableView;
    @FXML
    TableColumn<Maestro, String> rfc, nombre, apPaterno,apMaterno,acciones;
    @FXML
    private TextField busquedaSearch;
    @FXML
    private ToggleGroup toggleFiltroNormal;
    private Filtros filtros = Filtros.NOMBRE;
    private List<Maestro> maestroList = new ArrayList<>();
    private ObservableList<Maestro> listaFiltros = FXCollections.observableArrayList();
    private DAOMaestro daoMaestro = new DAOMaestro();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setColumns();
        loadAsyncMaestros(maestros -> {
            maestroList = maestros;
            listaFiltros.setAll(maestros);
            getLista().setAll(listaFiltros);
            tableView.setItems(getLista());
        });
        setFiltro();
    }

    @FXML
    private void openPanelAdd(){
        mediador.loadData(Paginas.ADD, new Maestro());
    }


    @FXML
    private void borrarFiltros(ActionEvent event){
        busquedaSearch.setText("");
        getLista().setAll(maestroList);
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
                                    mediador.loadData(Paginas.DETAILS, maestro);
                                }
                            });

                            editIcon.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    mediador.loadData(Paginas.EDIT, maestro);
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
                                        boolean seElimnio= daoMaestro.deleteDocente(maestro.getId());
                                        System.out.println(seElimnio);
                                        if (seElimnio){
                                            getLista().remove(maestro);
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
        busquedaSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (t1.isEmpty()){
                    getLista().setAll(listaFiltros);
                    return;
                }
                System.out.println(filtros);
                switch (filtros){

                    case RFC -> {
                        System.out.println("SE VA A BUSCAR POR EL NOMBRE PA");
                        List<Maestro> filtro = listaFiltros.stream()
                                .filter(maestro -> maestro.getRfc().toLowerCase().contains(t1.toLowerCase()))
                                .toList();

                        getLista().setAll(filtro);
                    }
                    case NOMBRE -> {
                        List<Maestro> filtro = listaFiltros.stream()
                                .filter(maestro -> maestro.getNombre().toLowerCase().contains(t1.toLowerCase()))
                                .toList();
                        getLista().setAll(filtro);
                    }
                    case APELLIDOS -> {
                        List<Maestro> filtro = listaFiltros.stream()
                                .filter(maestro -> maestro.getApPat().toLowerCase().contains(t1.toLowerCase()))
                                .toList();
                        getLista().setAll(filtro);
                    }
                }
            }
        });

        toggleFiltroNormal.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioMenuItem rb = (RadioMenuItem) toggleFiltroNormal.getSelectedToggle();
            if (rb == null){
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
                    filtros = Filtros.APELLIDOS;
                }
            }
        });
    }


    private void loadAsyncMaestros(Consumer<List<Maestro>> callBack){
        Task<List<Maestro>> task = new Task<List<Maestro>>() {
            @Override
            protected List<Maestro> call() throws Exception {
                return daoMaestro.getDocentes();
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


enum Filtros{
    RFC,
    NOMBRE,
    APELLIDOS
}