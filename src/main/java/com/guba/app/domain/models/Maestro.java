package com.guba.app.domain.models;

import com.guba.app.presentation.utils.ComboBoxCell;
import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

public class Maestro implements ComboBoxCell {

    private SimpleStringProperty id;
    private SimpleStringProperty rfc;
    private SimpleStringProperty curp;
    private SimpleStringProperty apPat;
    private SimpleStringProperty nombre;
    private SimpleStringProperty apMat;
    private SimpleStringProperty genero;
    private SimpleStringProperty correoPer;
    private SimpleStringProperty correoIns;
    private SimpleStringProperty domicilio;
    private SimpleStringProperty celular;
    private SimpleStringProperty estado;
    private SimpleStringProperty municipio;
    private SimpleStringProperty cv;
    private SimpleStringProperty grado;
    private SimpleStringProperty passwordTemp;
    private SimpleStringProperty password;
    private SimpleStringProperty foto;

    public Maestro(){
        this.id = new SimpleStringProperty();
        this.rfc = new SimpleStringProperty();
        this.curp = new SimpleStringProperty();
        this.apPat = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty();
        this.apMat = new SimpleStringProperty();
        this.genero = new SimpleStringProperty();
        this.correoPer = new SimpleStringProperty();
        this.correoIns = new SimpleStringProperty();
        this.domicilio = new SimpleStringProperty();
        this.celular = new SimpleStringProperty();
        this.estado = new SimpleStringProperty();
        this.municipio = new SimpleStringProperty();
        this.cv = new SimpleStringProperty();
        this.grado = new SimpleStringProperty();
        this.passwordTemp = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.foto = new SimpleStringProperty();

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

    public String getRfc() {
        return rfc.get();
    }

    public SimpleStringProperty rfcProperty() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc.set(rfc);
    }

    public String getCurp() {
        return curp.get();
    }

    public SimpleStringProperty curpProperty() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp.set(curp);
    }

    public String getApPat() {
        return apPat.get();
    }

    public SimpleStringProperty apPatProperty() {
        return apPat;
    }

    public void setApPat(String apPat) {
        this.apPat.set(apPat);
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

    public String getApMat() {
        return apMat.get();
    }

    public SimpleStringProperty apMatProperty() {
        return apMat;
    }

    public void setApMat(String apMat) {
        this.apMat.set(apMat);
    }

    public String getGenero() {
        return genero.get();
    }

    public SimpleStringProperty generoProperty() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero.set(genero);
    }

    public String getCorreoPer() {
        return correoPer.get();
    }

    public SimpleStringProperty correoPerProperty() {
        return correoPer;
    }

    public void setCorreoPer(String correoPer) {
        this.correoPer.set(correoPer);
    }

    public String getCorreoIns() {
        return correoIns.get();
    }

    public SimpleStringProperty correoInsProperty() {
        return correoIns;
    }

    public void setCorreoIns(String correoIns) {
        this.correoIns.set(correoIns);
    }

    public String getDomicilio() {
        return domicilio.get();
    }

    public SimpleStringProperty domicilioProperty() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio.set(domicilio);
    }

    public String getCelular() {
        return celular.get();
    }

    public SimpleStringProperty celularProperty() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular.set(celular);
    }

    public String getEstado() {
        return estado.get();
    }

    public SimpleStringProperty estadoProperty() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    public String getMunicipio() {
        return municipio.get();
    }

    public SimpleStringProperty municipioProperty() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio.set(municipio);
    }

    public String getCv() {
        return cv.get();
    }

    public SimpleStringProperty cvProperty() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv.set(cv);
    }

    public String getGrado() {
        return grado.get();
    }

    public SimpleStringProperty gradoProperty() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado.set(grado);
    }

    public String getPasswordTemp() {
        return passwordTemp.get();
    }

    public SimpleStringProperty passwordTempProperty() {
        return passwordTemp;
    }

    public void setPasswordTemp(String passwordTemp) {
        this.passwordTemp.set(passwordTemp);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getFoto() {
        return foto.get();
    }

    public SimpleStringProperty fotoProperty() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto.set(foto);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Maestro maestro)) return false;
        return Objects.equals(getId(), maestro.getId()) && Objects.equals(getRfc(), maestro.getRfc()) && Objects.equals(getApPat(), maestro.getApPat()) && Objects.equals(getNombre(), maestro.getNombre()) && Objects.equals(getApMat(), maestro.getApMat());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRfc(), getApPat(), getNombre(), getApMat());
    }

    @Override
    public String toComboCell() {
        return this.getNombre() + " " + this.getApPat() +" " + this.getApMat();
    }
}
