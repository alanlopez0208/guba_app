package com.guba.app.models;

import javafx.beans.property.*;

import java.util.Objects;

public class Asesor {
    private final IntegerProperty idDocente;
    private final IntegerProperty idCurso;
    private final StringProperty nombre;
    private final StringProperty puesto;
    private final StringProperty lugar;

    public Asesor(){
        this.idDocente = new SimpleIntegerProperty();
        this.idCurso = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.lugar = new SimpleStringProperty();
        this.puesto = new SimpleStringProperty();
    }

    public Asesor(int idDocente, int idCurso, String nombre,String puesto, String lugar) {
        this.idDocente = new SimpleIntegerProperty(idDocente);
        this.idCurso = new SimpleIntegerProperty(idCurso);
        this.nombre = new SimpleStringProperty(nombre);
        this.lugar = new SimpleStringProperty(lugar);
        this.puesto = new SimpleStringProperty(puesto);
    }

    public int getIdDocente() {
        return idDocente.get();
    }

    public IntegerProperty idDocenteProperty() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente.set(idDocente);
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

    public String getLugar() {
        return lugar.get();
    }

    public StringProperty lugarProperty() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar.set(lugar);
    }

    public String getPuesto() {
        return puesto.get();
    }

    public StringProperty puestoProperty() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto.set(puesto);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Asesor asesor)) return false;
        return Objects.equals(getIdDocente(), asesor.getIdDocente()) && Objects.equals(getIdCurso(), asesor.getIdCurso()) && Objects.equals(getNombre(), asesor.getNombre()) && Objects.equals(getPuesto(), asesor.getPuesto()) && Objects.equals(getLugar(), asesor.getLugar());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdDocente(), getIdCurso(), getNombre(), getPuesto(), getLugar());
    }

    @Override
    public String toString() {
        return "Asesor{" +
                "idDocente=" + idDocente +
                "\n, idCurso=" + idCurso +
                "\n, nombre=" + nombre +
                "\n, puesto=" + puesto +
                "\n,lugar=" + lugar +
                '}';
    }
}
