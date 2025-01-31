package com.guba.app.controllers.cursos;

import com.dlsc.gemsfx.CalendarPicker;
import com.guba.app.utils.*;
import com.guba.app.data.dao.DAOCurso;
import com.guba.app.domain.models.Curso;
import com.guba.app.presentation.dialogs.DialogParticipantes;
import com.guba.app.domain.models.Participante;
import com.guba.app.data.local.poi.PPTModifier;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;


import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddController extends BaseController<Curso> implements Loadable<Curso> {
    @FXML
    private VBox panel;
    @FXML
    private Button backButton;
    @FXML
    private Button btnGuardad;
    @FXML
    private Button btnAgregarAlumnos;
    @FXML
    private TextField nombreField;
    @FXML
    private ComboBox<String> comboModalidades;
    @FXML
    private CalendarPicker daeFechaInico;
    @FXML
    private CalendarPicker fechaFin;
    @FXML
    private TextField duracionField;
    @FXML
    private TextField impartidorNombreField;
    @FXML
    private TextField impartidorPuestoFlied;
    @FXML
    private TextField impartdiorLugarField;
    @FXML
    private ListView<Participante> listParticipantes;
    @FXML
    private ObservableList<Participante> participanteObservableList = FXCollections.observableArrayList();
    private Curso curso;
    private PPTModifier pptModifier = new PPTModifier();
    private DirectoryChooser directoryChooser = new DirectoryChooser();

    public AddController( Mediador<Curso> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super("/cursos/Add", mediador, estadoProperty, paginasProperty);
        setListView();
        setComboBox();
        duracionField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")){
                return change;
            }
            return null;
        }));
        backButton.setOnAction(this::backPanel);
        btnGuardad.setOnAction(this::guardarCurso);
        btnAgregarAlumnos.setOnAction(this::agregarAlumnos);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void loadData(Curso data) {
        curso = data;
        this.nombreField.textProperty().bindBidirectional(curso.nombreProperty());
        this.comboModalidades.valueProperty().bindBidirectional(curso.modalidadProperty());
        this.duracionField.textProperty().bindBidirectional(curso.duracionHorasProperty(), new NumberStringConverter());
        this.impartidorNombreField.textProperty().bindBidirectional(curso.getAsesor().nombreProperty());
        this.impartdiorLugarField.textProperty().bindBidirectional(curso.getAsesor().lugarProperty());
        this.impartidorPuestoFlied.textProperty().bindBidirectional(curso.getAsesor().puestoProperty());
        this.fechaFin.valueProperty().bindBidirectional(curso.dateFinProperty());
        this.daeFechaInico.valueProperty().bindBidirectional(curso.dateInicioProperty());

    }

    private void backPanel(ActionEvent actionEvent){
        cleanData();
        paginasProperty.set(Paginas.LIST);
    }

    @FXML
    private void guardarCurso(ActionEvent actionEvent){
        if (!validad()){
            return;
        }
        File file = directoryChooser.showDialog(new Stage());
        if (file == null || !file.exists()){
            return;
        }
        curso.getParticipantes().setAll(participanteObservableList);
        curso.setDateRealizacion(LocalDate.now());


        boolean guardar = mediador.guardar(curso);
        if (guardar){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Curso Guardado");
            alert.showAndWait();
            pptModifier.modificarPpt(curso, file.getPath()+"\\");
            paginasProperty.set(Paginas.LIST);
        }
    }

    @FXML
    private void agregarAlumnos(ActionEvent event){
        DialogParticipantes dialogAlumnos = new DialogParticipantes();
        dialogAlumnos.showAndWait().ifPresent(participantes -> {
            participanteObservableList.setAll(participantes);
        });
    }



    private boolean validad(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (daeFechaInico.getValue() == null){
            alert.setContentText("Debes de Ingresar la fecha en la cual se Inicio el Curso");
            alert.show();
            return false;
        }
        if (comboModalidades.valueProperty() == null){
            alert.setContentText("Debes de Ingresar la modalidad");
            alert.show();
            return false;
        }
        if (curso.getAsesor().getNombre() == null){
            alert.setContentText("Debes de Ingresar el nombre del Asesor");
            alert.show();
            return false;
        }
        return true;
    }


    private void setListView(){
        listParticipantes.setSelectionModel(null);
        listParticipantes.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Participante> call(ListView<Participante> asesorListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Participante item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty){
                            setText(null);
                            setGraphic(null);
                        }else{
                            Participante participante = item;
                            VBox vBox = new VBox();

                            Label nombre = new Label();
                            nombre.getStyleClass().add("textoNegritas");
                            nombre.setText(participante.getNombre().toUpperCase());

                            Button deleteButton = new Button();
                            deleteButton.setText("Eliminar Alumno");
                            deleteButton.getStyleClass().add("btnBorrar");
                            deleteButton.setOnAction(event -> {
                               participanteObservableList.remove(participante);
                            });
                            vBox.setAlignment(Pos.CENTER);
                            vBox.getChildren().addAll(nombre,deleteButton);
                            vBox.getStyleClass().add("panelShadow");
                            vBox.setSpacing(15);
                            setGraphic(vBox);
                        }
                    }
                };
            }
        });
        listParticipantes.setItems(participanteObservableList);

    }

    private void setComboBox(){
        comboModalidades.getItems().setAll("Presencial","Virtual","Mixta");
    }

    private void setUpWindow(){
        System.out.println(panel.getScene());
    }

    @Override
    protected void cleanData() {
        participanteObservableList.clear();
        curso = null;
        daeFechaInico.setValue(null);
        fechaFin.setValue(null);
    }
}
