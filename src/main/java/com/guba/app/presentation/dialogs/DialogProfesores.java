package com.guba.app.presentation.dialogs;


import com.guba.app.dao.DAOMaestro;
import com.guba.app.models.Maestro;
import com.guba.app.presentation.utils.ComboCell;
import com.guba.app.presentation.utils.Constants;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class DialogProfesores extends Dialog<Maestro> {

    private ComboBox<Maestro> profesoresComboBox;
    private DAOMaestro daoMaestro = new DAOMaestro();

    public DialogProfesores(){
        inicializarUI();
        setUpButtons();
    }

    private void inicializarUI(){
        this.getDialogPane().getScene().getStylesheets().add(getClass().getResource(Constants.URL_PRESENTATION+"/styles.css").toExternalForm());
        VBox contendor = new VBox();
        contendor.setSpacing(25);
        contendor.setFillWidth(false);

        profesoresComboBox = new ComboBox<>();

        profesoresComboBox.setButtonCell(new ComboCell<>());
        profesoresComboBox.getStyleClass().add("combo");
        profesoresComboBox.getStyleClass().add("combo");
        profesoresComboBox.minWidth(500);
        profesoresComboBox.setButtonCell(new ComboCell<Maestro>());
        profesoresComboBox.setCellFactory(carreraListView -> new ComboCell<Maestro>());


        contendor.getChildren().addAll(wrapContainer("Asigne un profesor a la materia", profesoresComboBox));
        getDialogPane().setContent(contendor);

        loadProfesoresAsync(maestros -> {
            profesoresComboBox.getItems().setAll(maestros);
        });
    }

    private void setUpButtons(){
        ButtonType savePhotoButtonType = new ButtonType("Guardar Grupo", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(savePhotoButtonType, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType == savePhotoButtonType) {
                if (!profesoresComboBox.getSelectionModel().isEmpty()){
                   return profesoresComboBox.getSelectionModel().getSelectedItem();
                }
                return null;
            }
            return null;
        });
    }


    private void loadProfesoresAsync(Consumer<List<Maestro>> listCallback) {
        Task<List<Maestro>> task = new Task<List<Maestro>>() {
            @Override
            protected List<Maestro> call() throws Exception {
                return daoMaestro.getDocentes();
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
    }


    private VBox wrapContainer(String titulo, Node node){
        VBox wraper = new VBox();
        wraper.setSpacing(10);
        wraper.setFillWidth(false);
        wraper.getChildren().addAll(new Label(titulo),node);
        return wraper;
    }


}
