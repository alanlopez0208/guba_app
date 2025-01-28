package com.guba.app.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

public class Grupo {
    private SimpleStringProperty id;
    private SimpleStringProperty nombre;
    private SimpleStringProperty semestre;
    private ObjectProperty<Carrera> carrera;


    public Grupo() {
        this.id = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty();
        this.semestre = new SimpleStringProperty();
        this.carrera = new SimpleObjectProperty<>();
    }

    public Grupo(SimpleStringProperty id, SimpleStringProperty nombre, SimpleStringProperty semestre) {
        this.id = id;
        this.nombre = nombre;
        this.semestre = semestre;
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
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

    public String getSemestre() {
        return semestre.get();
    }

    public SimpleStringProperty semestreProperty() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre.set(semestre);
    }

    public Carrera getCarrera() {
        return carrera.get();
    }

    public ObjectProperty<Carrera> carreraProperty() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera.set(carrera);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grupo grupo)) return false;
        return Objects.equals(getId(), grupo.getId()) && Objects.equals(getNombre(), grupo.getNombre()) && Objects.equals(getSemestre(), grupo.getSemestre()) && Objects.equals(getCarrera(), grupo.getCarrera());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNombre(), getSemestre(), getCarrera());
    }
}
