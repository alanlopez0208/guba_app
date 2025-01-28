package com.guba.app.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PagoDocente {

    private SimpleStringProperty idPago;
    private SimpleStringProperty idMaestro;
    private SimpleStringProperty fecha;
    private SimpleStringProperty cantidad;
    private SimpleStringProperty concepto;
    private SimpleStringProperty factura;
    private ObjectProperty<Maestro> maestro;
    private ObjectProperty<LocalDate> date;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PagoDocente() {
        this.idPago = new SimpleStringProperty();
        this.idMaestro = new SimpleStringProperty();
        this.fecha = new SimpleStringProperty();
        this.cantidad = new SimpleStringProperty();
        this.concepto = new SimpleStringProperty();
        this.factura = new SimpleStringProperty();
        this.maestro = new SimpleObjectProperty<>();
        this.date = new SimpleObjectProperty<>();
    }

    public PagoDocente(SimpleStringProperty idPago, SimpleStringProperty idMaestro, SimpleStringProperty fecha, SimpleStringProperty cantidad, SimpleStringProperty concepto, SimpleStringProperty factura, ObjectProperty<Maestro> maestro) {
        this.idPago = idPago;
        this.idMaestro = idMaestro;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.concepto = concepto;
        this.factura = factura;
        this.maestro = maestro;
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

    public String getIdMaestro() {
        return idMaestro.get();
    }

    public SimpleStringProperty idMaestroProperty() {
        return idMaestro;
    }

    public void setIdMaestro(String idMaestro) {
        this.idMaestro.set(idMaestro);
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

    public Maestro getMaestro() {
        return maestro.get();
    }

    public ObjectProperty<Maestro> maestroProperty() {
        return maestro;
    }

    public void setMaestro(Maestro maestro) {
        this.maestro.set(maestro);
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
        if (!(o instanceof PagoDocente that)) return false;
        return Objects.equals(getIdPago(), that.getIdPago()) && Objects.equals(getIdMaestro(), that.getIdMaestro()) && Objects.equals(getFecha(), that.getFecha()) && Objects.equals(getCantidad(), that.getCantidad()) && Objects.equals(getConcepto(), that.getConcepto()) && Objects.equals(getFactura(), that.getFactura()) && Objects.equals(getMaestro(), that.getMaestro());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdPago(), getIdMaestro(), getFecha(), getCantidad(), getConcepto(), getFactura(), getMaestro());
    }

    public String toStringDate(){
        return dateTimeFormatter.format(this.getDate());
    }
}
