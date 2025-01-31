package com.guba.app.utils;



import com.guba.app.presentation.utils.Constants;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public abstract class BaseController<T> extends VBox {

    protected Mediador<T> mediador;
    protected ObjectProperty<Paginas> paginasProperty;
    protected ObjectProperty<Estado> estadoProperty;

    public BaseController() {
    }

    public BaseController(String ruta, Mediador<T> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        this.mediador = mediador;
        this.estadoProperty = estadoProperty;
        this.paginasProperty = paginasProperty;
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.URL_MODULES+ ruta +".fxml"));
            loader.setRoot(this);
            loader.setController(BaseController.this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initialize(null,null);
    }

    public void setMediador(Mediador<T> mediador) {
        this.mediador = mediador;
    }

    public void setPaginasProperty(ObjectProperty<Paginas> paginasProperty) {
        this.paginasProperty = paginasProperty;
    }

    public void setEstadoProperty(ObjectProperty<Estado> estadoProperty) {
        this.estadoProperty = estadoProperty;
    }

    protected abstract void cleanData();

    protected void initialize(URL url, ResourceBundle resourceBundle){
        initialize();
    }

    protected void initialize(){

    }
}
