package com.guba.app.controllers.configuracion;

import com.guba.app.data.dao.DAOAcuerdo;
import com.guba.app.data.dao.DAOCarreras;
import com.guba.app.data.dao.DAOPeriodo;
import com.guba.app.domain.models.Acuerdo;
import com.guba.app.domain.models.Carrera;
import com.guba.app.domain.models.Periodo;
import com.guba.app.presentation.dialogs.DialogAcuerdo;
import com.guba.app.presentation.dialogs.DialogPeriodo;
import com.guba.app.data.local.database.Service;
import com.guba.app.utils.IPane;
import com.guba.app.utils.Utils;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

import static com.guba.app.utils.Utils.parseLocalDate;

public class ConfiguracionController implements Initializable, IPane {
    @FXML
    private TextField txtNombreCiclo, txtInicioCiclo, txtFinCiclo, txtNumeroAcuerdo, txtCCT;
    @FXML
    private DatePicker dateFecha;
    @FXML
    private JFXButton btnActualizarCiclo, btnActualizarAcuerdo;
    @FXML
    private ListView<Acuerdo> listviewAcuerdos;


    private DAOPeriodo daoPeriodo = new DAOPeriodo();
    private DAOAcuerdo daoAcuerdo = new DAOAcuerdo();
    private DAOCarreras daoCarreras = new DAOCarreras();
    private Periodo periodo;
    private Acuerdo acuerdo;

    private ObservableList<Acuerdo> acuerdos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAcuerdoAsync();
        loadPeriod();
        setUpViews();
        btnActualizarCiclo.setOnAction(this::handleActualizarCiclo);
    }

    public void handleActualizarCiclo(ActionEvent event) {
        DialogPeriodo dialogPeriodo = new DialogPeriodo();
        dialogPeriodo.showAndWait().ifPresent(p -> {
            Service.getService().crearPeriodo(p).ifPresentOrElse(aBoolean -> {
                periodo.setInicio(p.getInicio());
                periodo.setFin(p.getFin());
                periodo.setNombre(p.getNombre());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Se actualizo el perido de maner correcta");
                alert.show();
            }, () -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error al actualizar porfavor contacte a soporte");
                alert.show();
            });
        });
    }

    public void loadAcuerdoAsync(){
        Utils.loadAsync(()-> daoAcuerdo.getAllAcuerdos(), listAcuerdos->{
            acuerdos.setAll(listAcuerdos);
        });
    }

    public void loadPeriod(){
        Utils.loadAsync(()-> daoPeriodo.getUltimoPeriodo(),p -> {
            periodo = p;
            txtNombreCiclo.textProperty().bindBidirectional(periodo.nombreProperty());
            txtInicioCiclo.textProperty().bindBidirectional(periodo.inicioProperty());
            txtFinCiclo.textProperty().bindBidirectional(periodo.finProperty() );
        });
    }

    private void setUpViews(){
        listviewAcuerdos.setSelectionModel(null);
        listviewAcuerdos.setItems(acuerdos);
        listviewAcuerdos.setCellFactory(new Callback<ListView<Acuerdo>, ListCell<Acuerdo>>() {
            @Override
            public ListCell<Acuerdo> call(ListView<Acuerdo> acuerdoListView) {
                return new ListCell<>(){

                    @Override
                    protected void updateItem(Acuerdo item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty){
                            setText(null);
                            setGraphic(null);
                            return;
                        }else{
                            VBox container = new VBox();

                            Label titulo = new Label("Acuerdo");
                            titulo.getStyleClass().add("subtitulo");


                            Label carreraLabel =  new Label("Carrera:");
                            TextField carreraField = new TextField();
                            carreraField.textProperty().bindBidirectional(item.getCarrera().nombreProperty());
                            carreraField.getStyleClass().add("textField");
                            carreraField.setPrefWidth(300);
                            carreraField.setDisable(true);
                            VBox carreraContainer = createVBox();
                            carreraContainer.getChildren().addAll(carreraLabel,carreraField);

                            Label acuerdoLabel =  new Label("Numero de Acuerdo");
                            TextField acuerdoField = new TextField();
                            acuerdoField.textProperty().bindBidirectional(item.numeroProperty());
                            acuerdoField.setPrefWidth(300);
                            acuerdoField.getStyleClass().add("textField");
                            acuerdoField.setDisable(true);
                            VBox acuerdoVBox = createVBox();
                            acuerdoVBox.getChildren().addAll(acuerdoLabel,acuerdoField);


                            Label fechaLabel =  new Label("Fecha:");
                            DatePicker fechaField = new DatePicker();
                            fechaField.valueProperty().bindBidirectional(item.dateProperty());
                            fechaField.setPrefWidth(300);
                            fechaField.getStyleClass().add("calendario");
                            fechaField.setDisable(true);
                            VBox fechaVBox = createVBox();
                            fechaVBox.getChildren().addAll(fechaLabel,fechaField);


                            Label cctLabel =  new Label("CCT:");
                            TextField cctField = new TextField();
                            cctField.textProperty().bindBidirectional(item.ccProperty());
                            cctField.setPrefWidth(300);
                            cctField.getStyleClass().add("textField");
                            cctField.setDisable(true);
                            VBox ccContainer = createVBox();
                            ccContainer.getChildren().addAll(cctLabel,cctField);

                            HBox hBox = new HBox();
                            JFXButton btnUpdate = new JFXButton("Actualizar");
                            btnUpdate.setOnAction(event -> {
                                handleActualizarAcuerdo(item.getId(),item);
                            });
                            btnUpdate.setFocusTraversable(false);
                            btnUpdate.getStyleClass().add("btnNormal");
                            hBox.getChildren().add(btnUpdate);
                            hBox.setMaxWidth(Double.MAX_VALUE);
                            hBox.setAlignment(Pos.CENTER_RIGHT);


                            Region region = new Region();
                            region.prefHeight(20);

                            container.getChildren().addAll(titulo,region, carreraContainer, acuerdoVBox,fechaVBox,ccContainer,hBox);
                            container.getStyleClass().add("panelShadow");
                            container.setSpacing(10);
                            setGraphic(container);
                        }
                    }

                    private VBox createVBox(){
                        VBox container = new VBox();
                        container.fillWidthProperty().setValue(false);
                        container.maxWidth(Double.MAX_VALUE);
                        container.setSpacing(10.0);

                        return container;
                    }

                    private void handleActualizarAcuerdo(int index,Acuerdo acuerdo) {
                        DialogAcuerdo dialogAcuerdo = DialogAcuerdo.editar(acuerdo);
                        dialogAcuerdo.showAndWait().ifPresent(a -> {
                            boolean seActualizo = daoAcuerdo.actualizarAcuerdo(a);
                            if (seActualizo){
                                acuerdo.setCc(a.getCc());
                                acuerdo.setDate(a.getDate());
                                acuerdo.setNumero(a.getNumero());
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setContentText("Se actualizo el perido de maner correcta");
                                alert.show();
                            }else{
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setContentText("Error al actualizar porfavor contacte a soporte");
                                alert.show();
                            }
                        });
                    }
                };
            }
        });
    }

    public void handleClickAddAcuerdo(ActionEvent event){
        DialogAcuerdo dialogAcuerdo = DialogAcuerdo.crear();
        dialogAcuerdo.showAndWait().ifPresent(a->{
            var seAgrego = daoAcuerdo.insertarAcuerdo(a);
            System.out.println(seAgrego);
            if (seAgrego){
                acuerdos.add(a);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Se agrego el acuerdo de maner correcta");
                alert.show();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error al agregar porfavor contacte a soporte");
                alert.show();
            }
        });
    }

    @Override
    public void openPane() {
        loadPeriod();
        loadAcuerdoAsync();
    }
}
