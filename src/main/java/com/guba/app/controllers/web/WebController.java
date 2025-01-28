package com.guba.app.controllers.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guba.app.dao.DAOAlumno;
import com.guba.app.dao.DAOCalificiaciones;
import com.guba.app.dao.DAOMaestro;
import com.guba.app.dto.CalificacionDTO;
import com.guba.app.models.Calificacion;
import com.guba.app.service.remote.Converter;
import com.guba.app.service.remote.FTPUploader;
import com.guba.app.service.remote.Http;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.swing.text.PlainDocument;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WebController {
    @FXML
    private StackPane containerDownload;
    @FXML
    private FontIcon iconDownload;
    @FXML
    private JFXButton btnDownload;
    @FXML
    private StackPane containerUpload;
    @FXML
    private FontIcon iconUpload;
    @FXML
    private JFXButton btnUpload;

    private Http http = new Http();
    private FTPUploader ftpUploader = new FTPUploader();
    private Converter converter = new Converter();
    private DAOCalificiaciones daoCalificiaciones = new DAOCalificiaciones();



    @FXML
    private void getInfo(ActionEvent event){
        ProgressIndicator indicator = createViewLoading();
        containerDownload.getChildren().add(indicator);
        iconDownload.setOpacity(0.5);
        btnDownload.setDisable(true);

        relizarTarea(() -> {

            String body = http.request("SELECT * FROM Calificaciones");
            return converter.convertir(body, new TypeReference<List<CalificacionDTO>>() {})
                    .stream()
                    .map(Calificacion::new)
                    .toList();
        }, calificacions -> {
            Platform.runLater(() -> {
                containerDownload.getChildren().remove(indicator);
                iconDownload.setOpacity(1);
                btnDownload.setDisable(false);
            });
            System.out.println(calificacions.size());
            daoCalificiaciones.insertOrReplace(calificacions);
        });
    }

    @FXML
    private void uploadInfo(){
        ProgressIndicator indicator = createViewLoading();
        containerUpload.getChildren().add(indicator);
        iconUpload.setOpacity(0.5);
        btnUpload.setDisable(true);
        relizarTarea(() -> {
            ftpUploader.uploadFile();
            return null;
        }, unused -> {
            containerUpload.getChildren().remove(indicator);
            iconUpload.setOpacity(0.5);
            btnUpload.setDisable(true);
        });
    }

    public <T> void relizarTarea(Supplier<T> supplier, Consumer<T> consumer){
        Task<T> task = new Task<T>() {
            @Override
            protected T call() throws Exception {
                return  supplier.get();
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(event -> {
            try {
                consumer.accept(task.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        task.setOnFailed(event -> {
            System.out.println("Error al obtener los datos: " + task.getException());
        });
    }

    private ProgressIndicator createViewLoading(){
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        progressIndicator.prefHeight(1);
        progressIndicator.prefWidth(1);
        progressIndicator.maxHeight(1);
        return progressIndicator;
    }
}
