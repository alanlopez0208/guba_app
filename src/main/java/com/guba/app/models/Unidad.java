package com.guba.app.models;

import javafx.beans.property.*;

import java.util.Objects;

public class Unidad {
    private final IntegerProperty idUnidad;
    private final IntegerProperty idCurso;
    private final StringProperty nombre;
    private final IntegerProperty hti;
    private final IntegerProperty hcba;

    public Unidad(){
        this.idUnidad = new SimpleIntegerProperty();
        this.idCurso = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.hti = new SimpleIntegerProperty();
        this.hcba = new SimpleIntegerProperty();
    }

    public Unidad(int idUnidad, int idCurso, String nombre, int hti, int hcba) {
        this.idUnidad = new SimpleIntegerProperty(idUnidad);
        this.idCurso = new SimpleIntegerProperty(idCurso);
        this.nombre = new SimpleStringProperty(nombre);
        this.hti = new SimpleIntegerProperty(hti);
        this.hcba = new SimpleIntegerProperty(hcba);
    }

    public int getIdUnidad() {
        return idUnidad.get();
    }

    public IntegerProperty idUnidadProperty() {
        return idUnidad;
    }

    public void setIdUnidad(int idUnidad) {
        this.idUnidad.set(idUnidad);
    }

    public int getIdCurso() {
        return idCurso.get();
    }

    public IntegerProperty idCursoProperty() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso.set(idCurso);
    }

    public String getNombre() {
        return nombre.get();
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public int getHti() {
        return hti.get();
    }

    public IntegerProperty htiProperty() {
        return hti;
    }

    public void setHti(int hti) {
        this.hti.set(hti);
    }

    public int getHcba() {
        return hcba.get();
    }

    public IntegerProperty hcbaProperty() {
        return hcba;
    }

    public void setHcba(int hcba) {
        this.hcba.set(hcba);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unidad unidad)) return false;
        return Objects.equals(getIdUnidad(), unidad.getIdUnidad()) && Objects.equals(getIdCurso(), unidad.getIdCurso()) && Objects.equals(getNombre(), unidad.getNombre()) && Objects.equals(getHti(), unidad.getHti()) && Objects.equals(getHcba(), unidad.getHcba());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdUnidad(), getIdCurso(), getNombre(), getHti(), getHcba());
    }

    @Override
    public String toString() {
        return "Unidad{" +
                "idUnidad=" + idUnidad +
                ", idCurso=" + idCurso +
                ", nombre=" + nombre +
                ", hti=" + hti +
                ", hcba=" + hcba +
                '}';
    }
}
