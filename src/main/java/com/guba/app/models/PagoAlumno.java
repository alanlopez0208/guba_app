package com.guba.app.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class PagoAlumno {

    private SimpleStringProperty idPago;
    private SimpleStringProperty idAlumno;
    private SimpleStringProperty fecha;
    private SimpleStringProperty cantidad;
    private SimpleStringProperty concepto;
    private SimpleStringProperty factura;
    private ObjectProperty<Estudiante> alumno;

    private ObjectProperty<LocalDate> date;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PagoAlumno() {
        this.idPago = new SimpleStringProperty();
        this.idAlumno = new SimpleStringProperty();
        this.fecha = new SimpleStringProperty();
        this.cantidad = new SimpleStringProperty();
        this.concepto = new SimpleStringProperty();
        this.factura = new SimpleStringProperty();
        this.alumno = new SimpleObjectProperty<>();
        this.date = new SimpleObjectProperty<>();
    }

    public PagoAlumno(SimpleStringProperty idPago, SimpleStringProperty idMaestro, SimpleStringProperty fecha, SimpleStringProperty cantidad, SimpleStringProperty concepto, SimpleStringProperty factura, ObjectProperty<Estudiante> estudiante) {
        this.idPago = idPago;
        this.idAlumno = idMaestro;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.concepto = concepto;
        this.factura = factura;
        this.alumno = estudiante;
    }

    public String getIdPago() {
        return idPago.get();
    }

    public SimpleStringProperty idPagoProperty() {
        return idPago;
    }

    public void setIdPago(String idPago) {
        this.idPago.set(idPago);
    }

    public String getIdAlumno() {
        return idAlumno.get();
    }

    public SimpleStringProperty idAlumnoProperty() {
        return idAlumno;
    }

    public void setIdAlumno(String idAlumno) {
        this.idAlumno.set(idAlumno);
    }

    public String getFecha() {
        return fecha.get();
    }

    public SimpleStringProperty fechaProperty() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha.set(fecha);
        date.set(LocalDate.parse(fecha,dateTimeFormatter));
    }

    public String getCantidad() {
        return cantidad.get();
    }

    public SimpleStringProperty cantidadProperty() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad.set(cantidad);
    }

    public String getConcepto() {
        return concepto.get();
    }

    public SimpleStringProperty conceptoProperty() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto.set(concepto);
    }

    public String getFactura() {
        return factura.get();
    }

    public SimpleStringProperty facturaProperty() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura.set(factura);
    }

    public Estudiante getAlumno() {
        return alumno.get();
    }

    public ObjectProperty<Estudiante> alumnoProperty() {
        return alumno;
    }

    public void setAlumno(Estudiante alumno) {
        this.alumno.set(alumno);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
        this.setFecha(dateTimeFormatter.format(this.date.get()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PagoAlumno that)) return false;
        return Objects.equals(getIdPago(), that.getIdPago()) && Objects.equals(getIdAlumno(), that.getIdAlumno()) && Objects.equals(getFecha(), that.getFecha()) && Objects.equals(getCantidad(), that.getCantidad()) && Objects.equals(getConcepto(), that.getConcepto()) && Objects.equals(getFactura(), that.getFactura()) && Objects.equals(getAlumno(), that.getAlumno()) && Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdPago(), getIdAlumno(), getFecha(), getCantidad(), getConcepto(), getFactura(), getAlumno(), getDate());
    }

    public String toStringDate(){
        this.setFecha(dateTimeFormatter.format(this.getDate()));
        return dateTimeFormatter.format(this.getDate());
    }
}
