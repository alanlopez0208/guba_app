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
    protected Map<Paginas, Node> paginas = new HashMap<>();
    protected Map<Paginas, BaseController<T>> controladores = new HashMap<>();
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

    protected void registrarPagina(Paginas pagina, String rutaVista) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.URL_MODULES+ rutaVista +".fxml"));
            Parent parent = loader.load();
            parent.setVisible(false);
            BaseController<T> controller = loader.getController();
            controller.setMediador(this);
            controller.setEstadoProperty(estadoProperty);
            controller.setPaginasProperty(paginaProperty);
            controladores.put(pagina, controller);
            paginas.put(pagina, parent);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
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
            return fetchData();
        }, data -> {
            estadoProperty.setValue(Estado.CARGADO);
            dataList.setAll(data);
        });
    }

    @Override
    public void openPane(){
        paginaProperty.setValue(Paginas.LIST);
        nodos.values().forEach(BaseController::cleanData);
    }
}