package com.guba.app.domain.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Titulo {

    private final StringProperty idTitulacion;
    private final StringProperty idAlumno;
    private final StringProperty numero;
    private final StringProperty registro;
    private final StringProperty libro;
    private final StringProperty foja;
    private final StringProperty folio;
    private final StringProperty acuerdo;
    private final StringProperty tipoExamen;
    private final StringProperty fechaAcuerdo;
    private final StringProperty fechaAplicacion;
    private final StringProperty horaAplicacion;
    private final StringProperty duracion;
    private final StringProperty horaFinalizacion;
    private final StringProperty presidente;
    private final StringProperty secretario;
    private final StringProperty vocal;
    private final StringProperty nombre;
    private final StringProperty acta;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public Titulo() {
        this.idTitulacion = new SimpleStringProperty();
        this.idAlumno = new SimpleStringProperty();
        this.numero = new SimpleStringProperty();
        this.registro = new SimpleStringProperty();
        this.libro = new SimpleStringProperty();
        this.foja = new SimpleStringProperty();
        this.folio = new SimpleStringProperty();
        this.acuerdo = new SimpleStringProperty();
        this.tipoExamen = new SimpleStringProperty();
        this.fechaAcuerdo = new SimpleStringProperty();
        this.fechaAplicacion = new SimpleStringProperty();
        this.horaAplicacion = new SimpleStringProperty();
        this.duracion = new SimpleStringProperty();
        this.horaFinalizacion = new SimpleStringProperty();
        this.presidente = new SimpleStringProperty();
        this.secretario = new SimpleStringProperty();
        this.vocal = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty();
        this.acta = new SimpleStringProperty();
    }

    public Titulo(String idTitulacion, String idAlumno, String numero, String registro, String libro, String foja,
                  String folio, String acuerdo, String tipoExamen, String fechaAcuerdo, String fechaAplicacion,
                  String horaAplicacion, String duracion, String horaFinalizacion, String presidente,
                  String secretario, String vocal, String nombre, String acta) {
        this.idTitulacion = new SimpleStringProperty(idTitulacion);
        this.idAlumno = new SimpleStringProperty(idAlumno);
        this.numero = new SimpleStringProperty(numero);
        this.registro = new SimpleStringProperty(registro);
        this.libro = new SimpleStringProperty(libro);
        this.foja = new SimpleStringProperty(foja);
        this.folio = new SimpleStringProperty(folio);
        this.acuerdo = new SimpleStringProperty(acuerdo);
        this.tipoExamen = new SimpleStringProperty(tipoExamen);
        this.fechaAcuerdo = new SimpleStringProperty(fechaAcuerdo);
        this.fechaAplicacion = new SimpleStringProperty(fechaAplicacion);
        this.horaAplicacion = new SimpleStringProperty(horaAplicacion);
        this.duracion = new SimpleStringProperty(duracion);
        this.horaFinalizacion = new SimpleStringProperty(horaFinalizacion);
        this.presidente = new SimpleStringProperty(presidente);
        this.secretario = new SimpleStringProperty(secretario);
        this.vocal = new SimpleStringProperty(vocal);
        this.nombre = new SimpleStringProperty(nombre);
        this.acta = new SimpleStringProperty(acta);
    }

    public String getActa() {
        return acta.get();
    }

    public void setActa(String acta) {
        this.acta.set(acta);
    }

    public StringProperty actaProperty() {
        return acta;
    }

    public String getIdTitulacion() {
        return idTitulacion.get();
    }

    public void setIdTitulacion(String idTitulacion) {
        this.idTitulacion.set(idTitulacion);
    }

    public StringProperty idTitulacionProperty() {
        return idTitulacion;
    }

    public String getIdAlumno() {
        return idAlumno.get();
    }

    public void setIdAlumno(String idAlumno) {
        this.idAlumno.set(idAlumno);
    }

    public StringProperty idAlumnoProperty() {
        return idAlumno;
    }

    public String getNumero() {
        return numero.get();
    }

    public void setNumero(String numero) {
        this.numero.set(numero);
    }

    public StringProperty numeroProperty() {
        return numero;
    }

    public String getRegistro() {
        return registro.get();
    }

    public void setRegistro(String registro) {
        this.registro.set(registro);
    }

    public StringProperty registroProperty() {
        return registro;
    }

    public String getLibro() {
        return libro.get();
    }

    public void setLibro(String libro) {
        this.libro.set(libro);
    }

    public StringProperty libroProperty() {
        return libro;
    }

    public String getFoja() {
        return foja.get();
    }

    public void setFoja(String foja) {
        this.foja.set(foja);
    }

    public StringProperty fojaProperty() {
        return foja;
    }

    public String getFolio() {
        return folio.get();
    }

    public void setFolio(String folio) {
        this.folio.set(folio);
    }

    public StringProperty folioProperty() {
        return folio;
    }

    public String getAcuerdo() {
        return acuerdo.get();
    }

    public void setAcuerdo(String acuerdo) {
        this.acuerdo.set(acuerdo);
    }

    public StringProperty acuerdoProperty() {
        return acuerdo;
    }

    public String getTipoExamen() {
        return tipoExamen.get();
    }

    public void setTipoExamen(String tipoExamen) {
        this.tipoExamen.set(tipoExamen);
    }

    public StringProperty tipoExamenProperty() {
        return tipoExamen;
    }

    public String getFechaAcuerdo() {
        return fechaAcuerdo.get();
    }

    public void setFechaAcuerdo(String fechaAcuerdo) {
        this.fechaAcuerdo.set(fechaAcuerdo);
    }

    public StringProperty fechaAcuerdoProperty() {
        return fechaAcuerdo;
    }

    public String getFechaAplicacion() {
        return fechaAplicacion.get();
    }

    public void setFechaAplicacion(String fechaAplicacion) {
        this.fechaAplicacion.set(fechaAplicacion);
    }

    public StringProperty fechaAplicacionProperty() {
        return fechaAplicacion;
    }

    public String getHoraAplicacion() {
        return horaAplicacion.get();
    }

    public void setHoraAplicacion(String horaAplicacion) {
        this.horaAplicacion.set(horaAplicacion);
    }

    public StringProperty horaAplicacionProperty() {
        return horaAplicacion;
    }

    public String getDuracion() {
        return duracion.get();
    }

    public void setDuracion(String duracion) {
        this.duracion.set(duracion);
    }

    public StringProperty duracionProperty() {
        return duracion;
    }

    public String getHoraFinalizacion() {
        return horaFinalizacion.get();
    }

    public void setHoraFinalizacion(String horaFinalizacion) {
        this.horaFinalizacion.set(horaFinalizacion);
    }

    public StringProperty horaFinalizacionProperty() {
        return horaFinalizacion;
    }

    public String getPresidente() {
        return presidente.get();
    }

    public void setPresidente(String presidente) {
        this.presidente.set(presidente);
    }

    public StringProperty presidenteProperty() {
        return presidente;
    }

    public String getSecretario() {
        return secretario.get();
    }

    public void setSecretario(String secretario) {
        this.secretario.set(secretario);
    }

    public StringProperty secretarioProperty() {
        return secretario;
    }

    public String getVocal() {
        return vocal.get();
    }

    public void setVocal(String vocal) {
        this.vocal.set(vocal);
    }

    public StringProperty vocalProperty() {
        return vocal;
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Titulo titulo = (Titulo) o;
        return Objects.equals(idTitulacion.get(), titulo.idTitulacion.get()) &&
                Objects.equals(idAlumno.get(), titulo.idAlumno.get()) &&
                Objects.equals(numero.get(), titulo.numero.get()) &&
                Objects.equals(registro.get(), titulo.registro.get()) &&
                Objects.equals(libro.get(), titulo.libro.get()) &&
                Objects.equals(foja.get(), titulo.foja.get()) &&
                Objects.equals(folio.get(), titulo.folio.get()) &&
                Objects.equals(acuerdo.get(), titulo.acuerdo.get()) &&
                Objects.equals(tipoExamen.get(), titulo.tipoExamen.get()) &&
                Objects.equals(fechaAcuerdo.get(), titulo.fechaAcuerdo.get()) &&
                Objects.equals(fechaAplicacion.get(), titulo.fechaAplicacion.get()) &&
                Objects.equals(horaAplicacion.get(), titulo.horaAplicacion.get()) &&
                Objects.equals(duracion.get(), titulo.duracion.get()) &&
                Objects.equals(horaFinalizacion.get(), titulo.horaFinalizacion.get()) &&
                Objects.equals(presidente.get(), titulo.presidente.get()) &&
                Objects.equals(secretario.get(), titulo.secretario.get()) &&
                Objects.equals(vocal.get(), titulo.vocal.get()) &&
                Objects.equals(nombre.get(), titulo.nombre.get()) &&
                Objects.equals(acta.get(), titulo.acta.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTitulacion.get(), idAlumno.get(), numero.get(), registro.get(), libro.get(), foja.get(),
                folio.get(), acuerdo.get(), tipoExamen.get(), fechaAcuerdo.get(), fechaAplicacion.get(),
                horaAplicacion.get(), duracion.get(), horaFinalizacion.get(), presidente.get(),
                secretario.get(), vocal.get(), nombre.get(), acta.get());
    }


    public String toStringDate(LocalDate date){
        return dateTimeFormatter.format(date);
    }

    public LocalDate toDateString(String fecha){
        try {
            return LocalDate.parse(fecha,dateTimeFormatter);
        }catch (NullPointerException e){
            return null;
        }
    }

    public LocalTime stringToTime(String date){
        try{
            return LocalTime.parse(date, timeFormatter);
        }catch (NullPointerException e){
            return null;
        }
    }

    public String timeToString(LocalTime time){
        return timeFormatter.format(time);
    }

}
