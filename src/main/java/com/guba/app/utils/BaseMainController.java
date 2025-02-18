package com.guba.app.utils;

import com.guba.app.presentation.utils.Constants;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public abstract class BaseMainController<T> implements Mediador<T>, Initializable, IPane {

    @FXML
    protected StackPane stack;
    protected final ObjectProperty<Estado> estadoProperty = new SimpleObjectProperty<>(Estado.INICIAL);
    protected final ObjectProperty<Paginas> paginaProperty = new SimpleObjectProperty<>();
    protected ObservableList<T> dataList = FXCollections.observableArrayList();
    protected Map<Paginas,BaseController<T>> nodos = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registrarPaginas();
        paginaProperty.setValue(Paginas.LIST);
        paginaProperty.addListener((observableValue, oldValue, newValue) -> {
            nodos.get(oldValue).setVisible(false);
            nodos.get(newValue).setVisible(true);
        });
    }
    protected void registrarPagina(Paginas pagina, BaseController<T> controller) {
        controller.setVisible(false);
        nodos.put(pagina, controller);
    }
    
    protected abstract void registrarPaginas();

    protected abstract List<T> fetchData();

    @Override
    public void loadContent(Paginas nueva, T data) {
        BaseController<T> controller = nodos.get(nueva);
        if (controller instanceof Loadable) {
            ((Loadable<T>) controller).loadData(data);
            paginaProperty.setValue(nueva);
        } else {
            throw new IllegalStateException("El controlador no implementa la interfaz Loadable");
        }
    }

    @Override
    public void loadBD() {
        Utils.loadAsync(() -> {
            estadoProperty.setValue(Estado.CARGANDO);
            System.out.println("CARGANDOOO");
            return fetchData();

        }, data -> {
            estadoProperty.setValue(Estado.CARGADO);
            System.out.println("CARGADOOO");
            dataList.setAll(data);
        });
    }

    @Override
    public void openPane(){
        paginaProperty.setValue(Paginas.LIST);
        nodos.values().forEach(BaseController::cleanData);
    }
}