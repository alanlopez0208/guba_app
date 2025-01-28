package com.guba.app.models;


import com.guba.app.presentation.utils.ComboBoxCell;
import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

public class Carrera implements ComboBoxCell {

    private SimpleStringProperty  idCarrera;
    private SimpleStringProperty  idClave;
    private SimpleStringProperty  nombre;
    private SimpleStringProperty  hbca;
    private SimpleStringProperty  creditos;
    private SimpleStringProperty  totalHoras;
    private SimpleStringProperty  modalidad;
    private SimpleStringProperty  totalAsignaturas;
    private SimpleStringProperty hti;

    public Carrera() {
        this.idCarrera = new SimpleStringProperty();
        this.idClave = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty();
        this.hbca = new SimpleStringProperty();
        this.creditos = new SimpleStringProperty();
        this.totalHoras = new SimpleStringProperty();
        this.modalidad = new SimpleStringProperty();
        this.totalAsignaturas = new SimpleStringProperty();
        this.hti = new SimpleStringProperty();
    }

    public String getIdCarrera() {
        return idCarrera.get();
    }

    public SimpleStringProperty idCarreraProperty() {
        return idCarrera;
    }

    public void setIdCarrera(String idCarrera) {
        this.idCarrera.set(idCarrera);
    }

    public String getIdClave() {
        return idClave.get();
    }

    public SimpleStringProperty idClaveProperty() {
        return idClave;
    }

    public void setIdClave(String idClave) {
        this.idClave.set(idClave);
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

    public String getHbca() {
        return hbca.get();
    }

    public SimpleStringProperty hbcaProperty() {
        return hbca;
    }

    public void setHbca(String hbca) {
        this.hbca.set(hbca);
    }

    public String getCreditos() {
        return creditos.get();
    }

    public SimpleStringProperty creditosProperty() {
        return creditos;
    }

    public void setCreditos(String creditos) {
        this.creditos.set(creditos);
    }

    public String getTotalHoras() {
        return totalHoras.get();
    }

    public SimpleStringProperty totalHorasProperty() {
        return totalHoras;
    }

    public void setTotalHoras(String totalHoras) {
        this.totalHoras.set(totalHoras);
    }

    public String getModalidad() {
        return modalidad.get();
    }

    public SimpleStringProperty modalidadProperty() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad.set(modalidad);
    }

    public String getTotalAsignaturas() {
        return totalAsignaturas.get();
    }

    public SimpleStringProperty totalAsignaturasProperty() {
        return totalAsignaturas;
    }

    public void setTotalAsignaturas(String totalAsignaturas) {
        this.totalAsignaturas.set(totalAsignaturas);
    }

    public String getHti() {
        return hti.get();
    }

    public SimpleStringProperty htiProperty() {
        return hti;
    }

    public void setHti(String hti) {
        this.hti.set(hti);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Carrera carrera)) return false;
        return Objects.equals(getIdCarrera(), carrera.getIdCarrera()) && Objects.equals(getIdClave(), carrera.getIdClave()) && Objects.equals(getNombre(), carrera.getNombre()) && Objects.equals(getHbca(), carrera.getHbca()) && Objects.equals(getCreditos(), carrera.getCreditos()) && Objects.equals(getTotalHoras(), carrera.getTotalHoras()) && Objects.equals(getModalidad(), carrera.getModalidad()) && Objects.equals(getTotalAsignaturas(), carrera.getTotalAsignaturas()) && Objects.equals(getHti(), carrera.getHti());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdCarrera(), getIdClave(), getNombre(), getHbca(), getCreditos(), getTotalHoras(), getModalidad(), getTotalAsignaturas(), getHti());
    }
    @Override
    public String toComboCell() {
        return this.getNombre() +" " + this.getModalidad();
    }


    @Override
    public String toString() {
        return  this.getNombre() + " " + this.getModalidad();
    }
}
