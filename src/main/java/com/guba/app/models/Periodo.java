package com.guba.app.models;

import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

public class Periodo {

    private SimpleStringProperty id;
    private SimpleStringProperty nombre;
    private SimpleStringProperty inicio;
    private SimpleStringProperty fin;
    private SimpleStringProperty vacaciones;

    public Periodo(){
        this.id = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty();
        this.inicio = new SimpleStringProperty();
        this.fin = new SimpleStringProperty();
        this.vacaciones = new SimpleStringProperty();
    }

    public Periodo(SimpleStringProperty id, SimpleStringProperty nombre, SimpleStringProperty inicio, SimpleStringProperty fin, SimpleStringProperty vacaciones) {
        this.id = id;
        this.nombre = nombre;
        this.inicio = inicio;
        this.fin = fin;
        this.vacaciones = vacaciones;
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

    public String getInicio() {
        return inicio.get();
    }

    public SimpleStringProperty inicioProperty() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio.set(inicio);
    }

    public String getFin() {
        return fin.get();
    }

    public SimpleStringProperty finProperty() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin.set(fin);
    }

    public String getVacaciones() {
        return vacaciones.get();
    }

    public SimpleStringProperty vacacionesProperty() {
        return vacaciones;
    }

    public void setVacaciones(String vacaciones) {
        this.vacaciones.set(vacaciones);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Periodo periodo)) return false;
        return Objects.equals(getId(), periodo.getId()) && Objects.equals(getNombre(), periodo.getNombre()) && Objects.equals(getInicio(), periodo.getInicio()) && Objects.equals(getFin(), periodo.getFin()) && Objects.equals(getVacaciones(), periodo.getVacaciones());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNombre(), getInicio(), getFin(), getVacaciones());
    }
}
