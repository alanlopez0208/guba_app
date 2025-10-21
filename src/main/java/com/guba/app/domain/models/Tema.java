package com.guba.app.domain.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Tema {

    private SimpleStringProperty nombre;
    private SimpleStringProperty idCurso;
    private SimpleStringProperty duracionHoras;

    public Tema() {
        nombre = new SimpleStringProperty();
        idCurso = new SimpleStringProperty();
        duracionHoras = new SimpleStringProperty();
    }

    public Tema(SimpleStringProperty nombre, SimpleStringProperty idCurso, SimpleStringProperty duracionHoras) {
        this.nombre = nombre;
        this.idCurso = idCurso;
        this.duracionHoras = duracionHoras;
    }

    public String getNombre() {
        return nombre.get();
    }

    public SimpleStringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getIdCurso() {
        return idCurso.get();
    }

    public SimpleStringProperty idCursoProperty() {
        return idCurso;
    }

    public void setIdCurso(String idCurso) {
        this.idCurso.set(idCurso);
    }

    public String getDuracionHoras() {
        return duracionHoras.get();
    }

    public SimpleStringProperty duracionHorasProperty() {
        return duracionHoras;
    }

    public void setDuracionHoras(String duracionHoras) {
        this.duracionHoras.set(duracionHoras);
    }
}
