package com.guba.app.domain.models;

import javafx.beans.property.*;

import java.util.Objects;

public class Participante {
    private final IntegerProperty idParticipante;
    private final IntegerProperty idCurso;
    private final StringProperty nombre;


    public Participante(){
        this.idParticipante = new SimpleIntegerProperty();
        this.idCurso = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
    }

    public Participante(int idParticipante, int idCurso, String nombre, String apellidoPaterno, String apellidoMaterno) {
        this.idParticipante = new SimpleIntegerProperty(idParticipante);
        this.idCurso = new SimpleIntegerProperty(idCurso);
        this.nombre = new SimpleStringProperty(nombre);
    }

    public Participante(Estudiante estudiante){
        this();
        this.setNombre(estudiante.getNombre() + " " + estudiante.getApPaterno() + " " + estudiante.getApMaterno());
    }

    public int getIdParticipante() {
        return idParticipante.get();
    }

    public IntegerProperty idParticipanteProperty() {
        return idParticipante;
    }

    public void setIdParticipante(int idParticipante) {
        this.idParticipante.set(idParticipante);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participante that)) return false;
        return Objects.equals(getIdParticipante(), that.getIdParticipante()) && Objects.equals(getIdCurso(), that.getIdCurso()) && Objects.equals(getNombre(), that.getNombre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdParticipante(), getIdCurso(), getNombre());
    }

    @Override
    public String toString() {
        return "Participante{" +
                "idParticipante=" + idParticipante +
                ", idCurso=" + idCurso +
                ", nombre=" + nombre +
                '}';
    }
}
