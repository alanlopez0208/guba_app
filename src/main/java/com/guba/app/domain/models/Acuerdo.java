package com.guba.app.domain.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Acuerdo {
    private SimpleStringProperty numero;
    private SimpleStringProperty fecha;
    private SimpleStringProperty cc;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public Acuerdo(){
        this.numero = new SimpleStringProperty();
        this.fecha = new SimpleStringProperty();
        this.cc = new SimpleStringProperty();
        this.date = new SimpleObjectProperty<>();
    }

    public Acuerdo(SimpleStringProperty numero, SimpleStringProperty fecha, SimpleStringProperty cc, ObjectProperty<LocalDate> date) {
        this.numero = numero;
        this.fecha = fecha;
        this.cc = cc;
        this.date = date;
    }

    private ObjectProperty<LocalDate> date;


    public String getNumero() {
        return numero.get();
    }

    public SimpleStringProperty numeroProperty() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero.set(numero);
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

    public String getCc() {
        return cc.get();
    }

    public SimpleStringProperty ccProperty() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc.set(cc);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public String toStringDate(){
        return dateTimeFormatter.format(this.getDate());
    }

    public LocalDate toDateString(){
        return LocalDate.parse(this.getFecha(),dateTimeFormatter);
    }
}
