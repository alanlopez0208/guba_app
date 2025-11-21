package com.guba.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void init() throws Exception {
        super.init();
        Font.loadFont(App.class.getResource("fonts/Montserrat.ttf").toExternalForm().replace("%20"," "),12);
        Font.loadFont(App.class.getResource("fonts/Montserrat-Bold.ttf").toExternalForm().replace("%20"," "),12);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("presentation/App.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200,750 );
        Image image = new Image(App.class.getResource("assets/img/icono.png").toExternalForm());
        stage.getIcons().add(image);
        stage.setTitle("GUBA");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}