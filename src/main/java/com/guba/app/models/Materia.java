package com.guba.app.models;

import com.guba.app.presentation.utils.ComboBoxCell;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;


public class Materia implements ComboBoxCell {

    private SimpleStringProperty id;
    private SimpleStringProperty idMateria;
    private SimpleStringProperty nombre;
    private SimpleStringProperty hcba;
    private SimpleStringProperty hti;
    private SimpleStringProperty creditos;
    private SimpleStringProperty carrera;
    private SimpleStringProperty semestre;
    private ObjectProperty<Carrera> carreraModelo;
    private SimpleStringProperty clave;
    private SimpleStringProperty modalidad;

    public Materia() {
        this.id = new SimpleStringProperty();
        this.idMateria = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty();
        this.hcba = new SimpleStringProperty();
        this.hti = new SimpleStringProperty();
        this.creditos = new SimpleStringProperty();
        this.carrera = new SimpleStringProperty();
        this.semestre = new SimpleStringProperty();
        this.carreraModelo = new SimpleObjectProperty<>();
        this.modalidad = new SimpleStringProperty();
        this.clave = new SimpleStringProperty();
    }

    public Materia(SimpleStringProperty id, SimpleStringProperty idMateria, SimpleStringProperty nombre, SimpleStringProperty hcba, SimpleStringProperty hti, SimpleStringProperty creditos, SimpleStringProperty carrera, SimpleStringProperty semestre, ObjectProperty<Carrera> carreraModelo, SimpleStringProperty modalidad) {
        this.id = id;
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.hcba = hcba;
        this.hti = hti;
        this.creditos = creditos;
        this.carrera = carrera;
        this.semestre = semestre;
        this.carreraModelo = carreraModelo;
        this.modalidad = modalidad;
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

    public String getIdMateria() {
        return idMateria.get();
    }

    public SimpleStringProperty idMateriaProperty() {
        return idMateria;
    }

    public void setIdMateria(String idMateria) {
        this.idMateria.set(idMateria);
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

    public String getHcba() {
        return hcba.get();
    }

    public SimpleStringProperty hcbaProperty() {
        return hcba;
    }

    public void setHcba(String hcba) {
        this.hcba.set(hcba);
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

    public String getCreditos() {
        return creditos.get();
    }

    public SimpleStringProperty creditosProperty() {
        return creditos;
    }

    public void setCreditos(String creditos) {
        this.creditos.set(creditos);
    }

    public String getCarrera() {
        return carrera.get();
    }

    public SimpleStringProperty carreraProperty() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera.set(carrera);
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

    public Carrera getCarreraModelo() {
        return carreraModelo.get();
    }

    public ObjectProperty<Carrera> carreraModeloProperty() {
        return carreraModelo;
    }

    public void setCarreraModelo(Carrera carreraModelo) {
        this.carreraModelo.set(carreraModelo);
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

    public String getClave() {
        return clave.get();
    }

    public SimpleStringProperty claveProperty() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave.set(clave);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Materia materia)) return false;
        return Objects.equals(getId(), materia.getId()) && Objects.equals(getIdMateria(), materia.getIdMateria()) && Objects.equals(getNombre(), materia.getNombre()) && Objects.equals(getHcba(), materia.getHcba()) && Objects.equals(getHti(), materia.getHti()) && Objects.equals(getCreditos(), materia.getCreditos()) && Objects.equals(getCarrera(), materia.getCarrera()) && Objects.equals(getSemestre(), materia.getSemestre()) && Objects.equals(getCarreraModelo(), materia.getCarreraModelo()) && Objects.equals(getClave(), materia.getClave()) && Objects.equals(getModalidad(), materia.getModalidad());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getIdMateria(), getNombre(), getHcba(), getHti(), getCreditos(), getCarrera(), getSemestre(), getCarreraModelo(), getClave(), getModalidad());
    }

    @Override
    public String toComboCell() {
        return this.getNombre();
    }
}
