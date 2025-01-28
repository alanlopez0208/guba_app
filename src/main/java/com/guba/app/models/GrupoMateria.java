
package com.guba.app.models;


import com.guba.app.presentation.utils.ComboBoxCell;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

public class GrupoMateria implements ComboBoxCell {
    private SimpleStringProperty idGrupoMateria;
    private SimpleStringProperty idGrupo;
    private ObjectProperty<Materia> materia;
    private ObjectProperty<Maestro> maestro;
    private SimpleIntegerProperty cursada;
    private SimpleIntegerProperty prioridad;
    private ObjectProperty<Periodo> periodo;

    public GrupoMateria() {
        this.idGrupoMateria = new SimpleStringProperty();
        this.idGrupo = new SimpleStringProperty();
        this.materia = new SimpleObjectProperty<>();
        this.maestro = new SimpleObjectProperty<>();
        this.cursada = new SimpleIntegerProperty();
        this.prioridad = new SimpleIntegerProperty();
    }

    public String getIdGrupoMateria() {
        return idGrupoMateria.get();
    }

    public SimpleStringProperty idGrupoMateriaProperty() {
        return idGrupoMateria;
    }

    public void setIdGrupoMateria(String idGrupoMateria) {
        this.idGrupoMateria.set(idGrupoMateria);
    }

    public String getIdGrupo() {
        return idGrupo.get();
    }

    public SimpleStringProperty idGrupoProperty() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo.set(idGrupo);
    }

    public Materia getMateria() {
        return materia.get();
    }

    public ObjectProperty<Materia> materiaProperty() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia.set(materia);
    }

    public Maestro getMaestro() {
        return maestro.get();
    }

    public ObjectProperty<Maestro> maestroProperty() {
        return maestro;
    }

    public void setMaestro(Maestro maestro) {
        this.maestro.set(maestro);
    }

    public int getCursada() {
        return cursada.get();
    }

    public SimpleIntegerProperty cursadaProperty() {
        return cursada;
    }

    public void setCursada(int cursada) {
        this.cursada.set(cursada);
    }


    public int getPrioridad() {
        return prioridad.get();
    }

    public SimpleIntegerProperty prioridadProperty() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad.set(prioridad);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GrupoMateria that)) return false;
        return Objects.equals(getIdGrupoMateria(), that.getIdGrupoMateria()) && Objects.equals(getIdGrupo(), that.getIdGrupo()) && Objects.equals(getMateria(), that.getMateria()) && Objects.equals(getMaestro(), that.getMaestro()) && Objects.equals(getCursada(), that.getCursada()) && Objects.equals(getPrioridad(), that.getPrioridad());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdGrupoMateria(), getIdGrupo(), getMateria(), getMaestro(), getCursada(), getPrioridad());
    }

    @Override
    public String toComboCell() {
        return this.getMateria().getNombre();
    }
}
