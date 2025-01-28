package com.guba.app.presentation.dialogs;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.concurrent.atomic.AtomicReference;


public class DialogCamara extends Dialog<BufferedImage> {
    private HBox root;
    private VBox cameraSettingsPanel;
    private BorderPane cameraPanel;
    private ImageView imageView;
    private ImageView resultImageView;
    private Webcam webcam;
    private boolean stopCamera;
    private BufferedImage bufferedImage;
    private ObjectProperty<Image> imageViewObjectProperty = new SimpleObjectProperty<>();

    public DialogCamara() {
        initializeUI();
        initializeEvents();
        startCamera();
    }

    private void initializeUI() {
        root = new HBox(30);
        root.setAlignment(Pos.CENTER);

        cameraSettingsPanel = new VBox(30);
        cameraSettingsPanel.setAlignment(Pos.CENTER);

        imageView = new ImageView();
        resultImageView = new ImageView();
        resultImageView.setFitWidth(300);
        resultImageView.setFitHeight(300);

        setupCameraPanel();
        setupButtons();

        root.getChildren().addAll(resultImageView, cameraSettingsPanel);
        getDialogPane().setContent(root);
    }

    private void setupCameraPanel() {
        cameraPanel = new BorderPane();
        cameraPanel.setPrefSize(300, 300);

        imageView.prefWidth(300);
        imageView.prefHeight(300);
        imageView.setFitHeight(300);
        imageView.setFitWidth(300);
        cameraPanel.setCenter(imageView);

        ComboBox<Webcam> webcamComboBox = new ComboBox<>();
        webcamComboBox.getItems().addAll(Webcam.getWebcams());
        webcamComboBox.getSelectionModel().select(Webcam.getDefault());
        cameraSettingsPanel.getChildren().addAll(webcamComboBox, cameraPanel);
    }

    private void setupButtons() {
        Button takePhotoButton = new Button("Tomar Foto");
        takePhotoButton.setOnAction(event -> takePhoto());

        HBox buttonContainer = new HBox(takePhotoButton);
        buttonContainer.setAlignment(Pos.CENTER);

        cameraSettingsPanel.getChildren().add(buttonContainer);

        ButtonType savePhotoButtonType = new ButtonType("Guardar Foto", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(savePhotoButtonType, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if (buttonType == savePhotoButtonType) {
                return bufferedImage;
            }
            return null;
        });
    }

    private void initializeEvents() {
        setOnCloseRequest(event -> closeCamera());
    }

    private void startCamera() {
        stopCamera = false;
        Task<Void> webcamTask = new Task<>() {
            @Override
            protected Void call() {
                webcam = Webcam.getDefault();
                webcam.setViewSize(WebcamResolution.VGA.getSize());
                webcam.open();
                return null;
            }
        };

        webcamTask.setOnSucceeded(event -> startCameraStream());
        Thread webcamThread = new Thread(webcamTask);
        webcamThread.setDaemon(true);
        webcamThread.start();
    }

    private void startCameraStream() {
        Task<Void> cameraStreamTask = new Task<>() {
            @Override
            protected Void call() {
                final AtomicReference<WritableImage> ref = new AtomicReference<>();
                while (!stopCamera) {
                    ref.set(SwingFXUtils.toFXImage(webcam.getImage(), null));
                    Platform.runLater(() -> imageViewObjectProperty.set(ref.get()));
                }
                return null;
            }
        };

        Thread cameraStreamThread = new Thread(cameraStreamTask);
        cameraStreamThread.setDaemon(true);
        cameraStreamThread.start();
        imageView.imageProperty().bind(imageViewObjectProperty);
    }

    private void takePhoto() {
        Platform.runLater(() -> {
            this.bufferedImage = webcam.getImage();
            WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight()));
            resultImageView.setImage(writableImage);
        });
    }

    private void closeCamera() {
        stopCamera = true;
        if (webcam != null) {
            webcam.close();
        }
    }
}
