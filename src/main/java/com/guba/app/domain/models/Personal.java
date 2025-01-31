package com.guba.app.domain.models;

import java.util.Objects;

import javafx.beans.property.SimpleStringProperty;

public class Personal {
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
    private SimpleStringProperty password;
    private SimpleStringProperty foto;
    private SimpleStringProperty matricula;

    public Personal() {
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
        this.password = new SimpleStringProperty();
        this.foto = new SimpleStringProperty();
        this.matricula = new SimpleStringProperty();
    }

    public Personal(SimpleStringProperty id, SimpleStringProperty rfc, SimpleStringProperty curp, SimpleStringProperty apPat, SimpleStringProperty nombre, SimpleStringProperty apMat, SimpleStringProperty genero, SimpleStringProperty correoPer, SimpleStringProperty correoIns, SimpleStringProperty domicilio, SimpleStringProperty celular, SimpleStringProperty estado, SimpleStringProperty municipio, SimpleStringProperty password, SimpleStringProperty foto, SimpleStringProperty matricula) {
        this.id = id;
        this.rfc = rfc;
        this.curp = curp;
        this.apPat = apPat;
        this.nombre = nombre;
        this.apMat = apMat;
        this.genero = genero;
        this.correoPer = correoPer;
        this.correoIns = correoIns;
        this.domicilio = domicilio;
        this.celular = celular;
        this.estado = estado;
        this.municipio = municipio;
        this.password = password;
        this.foto = foto;
        this.matricula = matricula;
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

    public String getMatricula() {
        return matricula.get();
    }

    public SimpleStringProperty matriculaProperty() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula.set(matricula);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Personal personal)) return false;
        return Objects.equals(getId(), personal.getId()) && Objects.equals(getRfc(), personal.getRfc()) && Objects.equals(getCurp(), personal.getCurp()) && Objects.equals(getApPat(), personal.getApPat()) && Objects.equals(getNombre(), personal.getNombre()) && Objects.equals(getApMat(), personal.getApMat()) && Objects.equals(getGenero(), personal.getGenero()) && Objects.equals(getCorreoPer(), personal.getCorreoPer()) && Objects.equals(getCorreoIns(), personal.getCorreoIns()) && Objects.equals(getDomicilio(), personal.getDomicilio()) && Objects.equals(getCelular(), personal.getCelular()) && Objects.equals(getEstado(), personal.getEstado()) && Objects.equals(getMunicipio(), personal.getMunicipio()) && Objects.equals(getPassword(), personal.getPassword()) && Objects.equals(getFoto(), personal.getFoto()) && Objects.equals(getMatricula(), personal.getMatricula());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRfc(), getCurp(), getApPat(), getNombre(), getApMat(), getGenero(), getCorreoPer(), getCorreoIns(), getDomicilio(), getCelular(), getEstado(), getMunicipio(), getPassword(), getFoto(), getMatricula());
    }
}
