package com.guba.app.controllers.cursos;

import com.dlsc.gemsfx.CalendarPicker;
import com.guba.app.domain.models.Tema;
import com.guba.app.presentation.dialogs.DialogTema;
import com.guba.app.utils.*;
import com.guba.app.data.dao.DAOCurso;
import com.guba.app.domain.models.Curso;
import com.guba.app.presentation.dialogs.DialogParticipantes;
import com.guba.app.domain.models.Participante;
import com.guba.app.data.local.poi.PPTModifier;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

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
    private Button btnAddImage;
    @FXML
    private Button btnAgregarTemas;
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
    private TextField txtPathImagen;
    @FXML
    private ListView<Participante> listParticipantes;
    @FXML
    private ListView<Tema> listTemas;

    private ObservableList<Participante> participanteObservableList = FXCollections.observableArrayList();

    private Curso curso;
    private PPTModifier pptModifier = new PPTModifier();
    private DirectoryChooser directoryChooser = new DirectoryChooser();

    public AddController( Mediador<Curso> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super("/cursos/Add", mediador, estadoProperty, paginasProperty);
        setListView();
        setComboBox();
        backButton.setOnAction(this::backPanel);
        btnGuardad.setOnAction(this::guardarCurso);
        btnAgregarAlumnos.setOnAction(this::agregarAlumnos);
        btnAddImage.setOnAction(this::addImage);
        btnAgregarTemas.setOnAction(this::addTema);
        fechaFin.valueProperty().addListener((observableValue, localDate, t1) -> {
            if (t1 == null || curso == null) {
                return;
            }
            curso.setDateFin(t1);
        });
        daeFechaInico.valueProperty().addListener((observableValue, localDate, t1) -> {
            if (t1 == null || curso == null) {
                return;
            }
            curso.setDateInicio(t1);
        });
        duracionField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")){
                return change;
            }
            return null;
        }));
        this.nombreField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                curso.setNombre(newVal);
            }
        });

        this.comboModalidades.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                curso.setModalidad(newVal);
            }
        });
        this.duracionField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                try {
                    curso.setDuracionHoras(Integer.parseInt(newVal));
                } catch (NumberFormatException e) {
                    System.err.println("Formato incorrecto en duración: " + newVal);
                }
            }
        });

        this.impartidorNombreField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && curso.getAsesor() != null) {
                curso.getAsesor().setNombre(newVal);
            }
        });

        this.impartdiorLugarField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && curso.getAsesor() != null) {
                curso.getAsesor().setLugar(newVal);
            }
        });

        this.impartidorPuestoFlied.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && curso.getAsesor() != null) {
                curso.getAsesor().setPuesto(newVal);
            }
        });
    }

    private void addTema(ActionEvent actionEvent) {
        DialogTema dialogTema = new DialogTema();
        dialogTema.showAndWait().ifPresent(tema -> {
            curso.getTemas().add(tema);
        });
    }

    private void addImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de imagen", "*.jpg","*.png")
        );
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            curso.setImage(file.getPath());
            txtPathImagen.setText(curso.getImage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void loadData(Curso data) {
        curso = data;
        listTemas.setItems(curso.getTemas());
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


        listTemas.setSelectionModel(null);
        listTemas.setCellFactory(new Callback<ListView<Tema>, ListCell<Tema>>() {
            @Override
            public ListCell<Tema> call(ListView<Tema> temaListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Tema item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty){
                            setText(null);
                            setGraphic(null);
                        }else{
                            Tema tema = item;
                            VBox vBox = new VBox();

                            Label nombre = new Label();
                            nombre.getStyleClass().add("textoNegritas");
                            nombre.setText(tema.getNombre());

                            Label duracion = new Label();
                            duracion.setText(tema.getDuracionHoras()+" hs.");

                            Button deleteButton = new Button();
                            deleteButton.setText("Eliminar Tema");
                            deleteButton.getStyleClass().add("btnBorrar");
                            deleteButton.setOnAction(event -> {
                                curso.getTemas().remove(tema);
                            });
                            vBox.setAlignment(Pos.CENTER);
                            vBox.getChildren().addAll(nombre,duracion,deleteButton);
                            vBox.getStyleClass().add("panelShadow");
                            vBox.setSpacing(10);
                            setGraphic(vBox);
                        }
                    }
                };
            }
        });
    }

    private void setComboBox(){
        comboModalidades.getItems().setAll("Presencial","Virtual","Mixta");
    }

    @Override
    protected void cleanData() {
        curso = null;
        participanteObservableList.clear();
        daeFechaInico.setValue(null);
        fechaFin.setValue(null);
        this.nombreField.setText(null);
        this.comboModalidades.setValue(null);
        this.duracionField.setText(null);
        this.impartidorNombreField.setText(null);
        this.impartdiorLugarField.setText(null);
        this.impartidorPuestoFlied.setText(null);

    }
}
