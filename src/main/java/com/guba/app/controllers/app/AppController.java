package com.guba.app.controllers.app;

import com.guba.app.presentation.componets.Sidebar;
import com.guba.app.presentation.utils.Constants;
import com.guba.app.utils.BaseMainController;
import com.guba.app.utils.IPane;
import com.guba.app.utils.Modulo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AppController implements Initializable {

    @FXML
    private Sidebar sidebar;
    @FXML
    private StackPane stackPane;
    private Map<Modulo, Parent> modulos = new HashMap<>();
    private Map<Modulo, IPane> moduloControllers = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadModules();
        sidebar.moduleObjectPropertyProperty().addListener((observableValue, module, t1) -> {
            if (module != null){
                modulos.get(module).setVisible(false);
            }
            modulos.get(t1).setVisible(true);
            if (moduloControllers.containsKey(t1)){
                moduloControllers.get(t1).openPane();
            }
        });
        sidebar.moduleObjectPropertyProperty().setValue(Modulo.INICIO);
    }


    private void loadModules() {
        for (Modulo modulo : Modulo.values()) {
            loadModule(modulo);
        }
        stackPane.getChildren().setAll(modulos.values());
    }

    private void loadModule(Modulo modulo){
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.URL_MODULES + modulo.getPath() + ".fxml"));
            Parent nodo = loader.load();
            nodo.setVisible(false);
            modulos.put(modulo, nodo);
            if (loader.getController() instanceof IPane){
                moduloControllers.put(modulo, (IPane) loader.getController());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
