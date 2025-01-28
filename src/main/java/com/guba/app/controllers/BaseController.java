package com.guba.app.controllers;


import com.guba.app.controllers.carreras.MainController;
import javafx.collections.ObservableList;

public abstract class BaseController<T> implements Cambiador<T> {
    private ObservableList<T> lista;
    protected Mediador<T> mediador;

    @Override
    public void setMediador(Mediador<T> mediador) {
        this.mediador = mediador;
    }

    public ObservableList<T> getLista() {
        return lista;
    }

    public void setLista(ObservableList<T> lista) {
        this.lista = lista;
    }
}
