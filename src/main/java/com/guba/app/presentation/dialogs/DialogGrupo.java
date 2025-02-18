package com.guba.app.presentation.dialogs;

import com.guba.app.data.dao.DAOCarreras;
import com.guba.app.domain.models.Carrera;
import com.guba.app.domain.models.Grupo;
import com.guba.app.presentation.utils.ComboCell;
import com.guba.app.presentation.utils.Constants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class DialogGrupo extends Dialog<Grupo> {

        private ComboBox<Carrera> carreraComboBox;
        private ComboBox<String> semestreComboBox;
        private TextField txtNombre;
        private final Grupo grupo;

        private final DAOCarreras daoCarreras = new DAOCarreras();

    public DialogGrupo(Grupo grupo){
            this.grupo = grupo;
            inicializarUI();
            setUpButtons();
    }

    private void inicializarUI(){
        this.getDialogPane().getScene().getStylesheets().add(getClass().getResource(Constants.URL_PRESENTATION+"/styles.css").toExternalForm());
        VBox contendor = new VBox();
        contendor.setSpacing(25);
        contendor.setFillWidth(false);

        carreraComboBox = new ComboBox<>();

        carreraComboBox.setButtonCell(new ComboCell<>());
        carreraComboBox.getStyleClass().add("combo");
        carreraComboBox.getStyleClass().add("combo");
        carreraComboBox.minWidth(500);
        carreraComboBox.setButtonCell(new ComboCell<Carrera>());
        carreraComboBox.setCellFactory(carreraListView -> new ComboCell<Carrera>());

        txtNombre = new TextField();
        txtNombre.getStyleClass().add("textField");
        txtNombre.minWidth(500);
        txtNombre.setPromptText("Nombre del Grupo");
        txtNombre.getStyleClass().add("textField");
        txtNombre.setText(grupo.getNombre());

        semestreComboBox = new ComboBox<>();
        semestreComboBox.getItems().setAll(IntStream.range(1,10).boxed().map(i-> Integer.toString(i)).toList());
        semestreComboBox.setDisable(true);
        semestreComboBox.minWidth(500);
        semestreComboBox.getSelectionModel().select(grupo.getSemestre());

        carreraComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, carrera, t1) -> semestreComboBox.setDisable(false));
        semestreComboBox.getStyleClass().add("combo");


        contendor.getChildren().addAll(wrapContainer("Nombre", txtNombre), wrapContainer("Carrera", carreraComboBox), wrapContainer("Semestre", semestreComboBox));
        getDialogPane().setContent(contendor);

        loadCarrerasAsync(carreras -> {
            carreraComboBox.getItems().setAll(carreras);
            carreraComboBox.getSelectionModel().select(grupo.getCarrera());
        });
    }

    private void setUpButtons(){
        ButtonType savePhotoButtonType = new ButtonType("Guardar Grupo", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(savePhotoButtonType, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType == savePhotoButtonType) {
                if (!carreraComboBox.getSelectionModel().isEmpty() && !txtNombre.getText().isEmpty() && !semestreComboBox.getSelectionModel().isEmpty()){
                    grupo.setNombre(txtNombre.getText());
                    grupo.setCarrera(carreraComboBox.getSelectionModel().getSelectedItem());
                    grupo.setSemestre(semestreComboBox.getSelectionModel().getSelectedItem());
                    return grupo;
                }
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Hay algun campo vacio porfavor intente nuevamente");
                alert.show();
                return null;
            }
                return null;
        });
    }


    private List<Carrera> loadCarrerasAsync(Consumer<List<Carrera>> listCallback) {
        AtomicReference<List<Carrera>> carreras = new AtomicReference<>();
        Task<List<Carrera>> task = new Task<>() {
                @Override
                protected List<Carrera> call() throws Exception {
                    return daoCarreras.getAllCarreras();
                }
            };
        task.setOnSucceeded(event -> {
            try {
                listCallback.accept(task.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Error al obtener los grupos"+ task.getException());
            }
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        return carreras.get();
    }


    private VBox wrapContainer(String titulo, Node node){
        VBox wraper = new VBox();
        wraper.setSpacing(10);
        wraper.setFillWidth(false);
        wraper.getChildren().addAll(new Label(titulo),node);
        return wraper;
    }
}
