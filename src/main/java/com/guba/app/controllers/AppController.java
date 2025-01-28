package com.guba.app.controllers;

import com.guba.app.presentation.componets.Sidebar;
import com.guba.app.presentation.utils.Constants;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class AppController implements Initializable {

    @FXML
    private Sidebar sidebar;
    @FXML
    private BorderPane pantallaPrincipal;

    private Map<Integer, Parent> modulos = new HashMap<>();
    @FXML
    private StackPane stackPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadModules();


        sidebar.setOnItemPressed((index, oldValue) -> {
            Task<Parent> task = new Task<>() {

                @Override
                protected Parent call() throws Exception {
                    return loadModule(Module.values()[index].path);
                }
            };
            task.setOnSucceeded(event -> {
                try {
                    stackPane.getChildren().setAll(task.get());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });

            Thread thread = new Thread(task);
            thread.start();
        });
    }


    private void loadModules() {
        for (int i = 0; i < Module.values().length ; i++) {
            Module modulo = Module.values()[i];
            Parent parent = loadModule(modulo.path);
            modulos.put(i, parent);
        }
    }

    private Parent loadModule(String name){
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.URL_MODULES + name + ".fxml"));
            Parent modulo = loader.load();
            return modulo;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public enum Module {
        INICIO("/inicio/Inicio"),
        ESTUDIANTES("/estudiantes/EstudiantesMain"),
        CURSOS("/cursos/Main"),
        MAESTROS("/maestros/Main"),
        GRUPOS("/grupos/Main"),
        MATERIAS("/materias/Main"),
        CARRERAS("/carreras/Main"),
        PERSONAL("/personal/Main"),
        PAGO_ALUMNOS("/pago_alumnos/Main"),
        PAGO_DOCENTES("/pago_docentes/Main"),
        WEB("/web/Web"),
        CONFIGURACION("/configuracion/Configuracion");

        private final String path;

        Module(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
