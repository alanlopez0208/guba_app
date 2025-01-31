package com.guba.app.controllers.personal;

import com.guba.app.data.dao.DAOPersonal;
import com.guba.app.utils.BaseController;
import com.guba.app.utils.Paginas;
import com.guba.app.domain.models.Personal;
import com.jfoenix.controls.JFXButton;
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

public class ListController extends BaseController<Personal> implements Initializable {

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
    private ToggleGroup toggleCarreras = new ToggleGroup();
    private Filtros filtros = Filtros.NOMBRE;
    private List<Personal> personalList = new ArrayList<>();
    private ObservableList<Personal> listaFiltros = FXCollections.observableArrayList();
    private DAOPersonal daoPerosnal = new DAOPersonal();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setColumns();
        loadAsyncPersonal(personals -> {
            personalList = personals;
            listaFiltros.setAll(personalList);
            //getLista().setAll(listaFiltros);
            //tableView.setItems(getLista());
        });
        //setFiltro();
    }

    @FXML
    private void openPanelAdd() {
        mediador.loadContent(Paginas.ADD, new Personal());
    }

    @FXML
    private void borrarFiltros(ActionEvent event) {
        busquedaSearch.setText("");
        //getLista().setAll(personalList);
        toggleCarreras.selectToggle(null);
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
                                        System.out.println(personal.getId());
                                        boolean seElimnio = daoPerosnal.deletePersonal(personal.getId());
                                        System.out.println(seElimnio);
                                        if (seElimnio) {
                                            //getLista().remove(personal);
                                        }
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

    /*
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

                    case NOMBRE -> {
                        System.out.println("SE VA A BUSCAR POR EL NOMBRE PA");
                        List<Personal> filtro = listaFiltros.stream()
                                .filter(personal -> personal.getNombre().toLowerCase().contains(t1.toLowerCase()))
                                .toList();

                        getLista().setAll(filtro);
                    }
                    case APELLIDOS -> {
                        List<Personal> filtro = listaFiltros.stream()
                                .filter(personal -> personal.getApPat().toLowerCase().contains(t1.toLowerCase()))
                                .toList();
                        getLista().setAll(filtro);
                    }
                }
            }
        });

        toggleFiltroNormal.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioMenuItem rb = (RadioMenuItem) toggleFiltroNormal.getSelectedToggle();
            if(rb == null){
                filtros = Filtros.NOMBRE;
                return;
            }
            int index = Integer.parseInt(rb.getUserData().toString());

            switch (index){
                case 1->{
                    filtros = Filtros.NOMBRE;
                }
                case 2->{
                    filtros = Filtros.APELLIDOS;
                }
            }
        });
    }
*/
    private void loadAsyncPersonal(Consumer<List<Personal>> callBack){
        Task<List<Personal>> task = new Task<List<Personal>>() {
            @Override
            protected List<Personal> call() throws Exception {
                return daoPerosnal.getPersonales();
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

    @Override
    protected void cleanData() {

    }
}

enum Filtros{
    NOMBRE,
    APELLIDOS
}
