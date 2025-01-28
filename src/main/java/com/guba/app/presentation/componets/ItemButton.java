package com.guba.app.presentation.componets;

import com.guba.app.presentation.utils.Constants;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.transitions.JFXFillTransition;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class ItemButton extends VBox {

    private final Color COLOR_INICIAL;
    private final Color COLOR_SELECCIONADO;
    @FXML
    public Label label;
    @FXML
    public JFXButton boton;
    private String textButton;
    private boolean selected;
    private FontIcon icono;
    private ObjectProperty<ItemButton> simpleObjectProperty;
    private OnItemPressed onItemPressed;

    private int index;


    public ItemButton(ObjectProperty simpleObjectProperty){
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.URL_COMPONENTS+"/ItemButton.fxml"));
        try {
            loader.setRoot(this);
            loader.setController(ItemButton.this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        COLOR_INICIAL = Color.WHITE;
        COLOR_SELECCIONADO = Color.web("0x894957");
        selected = false;
        this.simpleObjectProperty = simpleObjectProperty;
        initElements();
    }

    private void initElements() {
        boton.setBackground(new Background(new BackgroundFill(COLOR_INICIAL, CornerRadii.EMPTY, null)));

        Timeline fontIn = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(label.textFillProperty(), COLOR_INICIAL, Interpolator.EASE_OUT)
                ),
                new KeyFrame(
                        Duration.millis(500),
                        new KeyValue(label.textFillProperty(), COLOR_SELECCIONADO, Interpolator.EASE_OUT)
                )
        );

        Timeline fontOut = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(label.textFillProperty(), COLOR_SELECCIONADO, Interpolator.EASE_OUT)
                ),
                new KeyFrame(
                        Duration.millis(500),
                        new KeyValue(label.textFillProperty(), label.getTextFill(), Interpolator.EASE_OUT)
                )
        );


        boton.setOnMouseEntered(event -> {
            if (selected){
                return;
            }
            playHoverEffect(boton, true);
            playHoverEffect(icono, false);
            fontIn.playFromStart();
        });

        boton.setOnMouseExited(event -> {
            if (selected){
                return;
            }
            playHoverEffect(boton, false);
            playHoverEffect(icono, true);
            fontOut.playFromStart();
        });

        boton.setOnAction(event -> {
            if (selected){
                return;
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    simpleObjectProperty.setValue(ItemButton.this);
                }
            });
        });

        simpleObjectProperty.addListener((observableValue, oldValue, newValue) -> {
            selected = newValue.equals(this);
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    if (selected) {
                        Platform.runLater(() -> {
                            boton.setBackground(new Background(new BackgroundFill(COLOR_SELECCIONADO, CornerRadii.EMPTY, null)));
                            icono.setFill(COLOR_INICIAL);
                            label.setFont(new Font("Montserrat bold", 12));
                        });
                    } else {
                        if (oldValue.equals(this)){
                            return null;
                        }
                        Platform.runLater(() -> {
                            boton.setBackground(new Background(new BackgroundFill(COLOR_INICIAL, CornerRadii.EMPTY, null)));
                            icono.setFill(COLOR_SELECCIONADO);
                            label.setFont(new Font("Montserrat", 12));
                            label.setTextFill(Color.BLACK);
                        });
                    }

                    return null;
                }
            };
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });

    }

    private void playFillTransition(Region region, Color fromColor, Color toColor, Duration duration){
        JFXFillTransition fillTransition = new JFXFillTransition();
        fillTransition.setDuration(duration);
        fillTransition.setRegion(region);
        fillTransition.setFromValue(fromColor);
        fillTransition.setToValue(toColor);
        fillTransition.playFromStart();
    }

    private void playFillTransition(Shape shape, Color fromColor, Color toColor, Duration duration){
        FillTransition fillTransition = new FillTransition();
        fillTransition.setShape(shape);
        fillTransition.setDuration(duration);
        fillTransition.setFromValue(fromColor);
        fillTransition.setToValue(toColor);
        fillTransition.playFromStart();
    }

    private void playHoverEffect(Region region, boolean isHovering){
        Color currentColor = (Color) region.getBackground().getFills().get(0).getFill();
        Color targetColor = isHovering ? COLOR_SELECCIONADO : COLOR_INICIAL;
        playFillTransition(region, currentColor, targetColor, Duration.millis(300));
    }

    private void playHoverEffect(Shape shape, boolean isHovering){
        Color currentColor = (Color) shape.getFill();
        Color targetColor = isHovering ? COLOR_SELECCIONADO : COLOR_INICIAL;
        playFillTransition(shape, currentColor, targetColor, Duration.millis(300));
    }



    public String getTextButton() {
        return textButton;
    }

    public void setTextButton(String textButton) {
        this.textButton = textButton;
        label.setText(textButton);
    }

    public FontIcon getIcono() {
        return icono;
    }

    public void setIcono(FontIcon icono) {
        this.icono = icono;
        icono.setFill(COLOR_SELECCIONADO);
        boton.setGraphic(icono);
    }


    public OnItemPressed getOnItemPressed() {
        return onItemPressed;
    }

    public void setOnItemPressed(OnItemPressed onItemPressed) {
        this.onItemPressed = onItemPressed;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;

    }
}
