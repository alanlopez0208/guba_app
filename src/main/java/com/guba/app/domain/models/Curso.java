package com.guba.app.domain.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Curso {
    private final IntegerProperty idCurso;
    private final StringProperty nombre;
    private final StringProperty modalidad;
    private final StringProperty fechaFin;
    private final StringProperty fechaRealizacion;
    private final SimpleIntegerProperty duracionHoras;
    private final StringProperty fechaInicio;
    private final ObservableList<Participante> participantes;
    private final ObjectProperty<Asesor> asesor;
    private final ObservableList<Unidad> unidades;
    private DateTimeFormatter formatterLocale = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy ", new Locale.Builder().setLanguage("es").setRegion("MX").build());
    private DateTimeFormatter formatterBd = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private ObjectProperty<LocalDate> dateInicio;
    private ObjectProperty<LocalDate> dateFin;
    private ObjectProperty<LocalDate> dateRealizacion;
    private StringProperty image;
    private ObservableList<Tema> temas ;

    public Curso() {
        this.idCurso = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.modalidad = new SimpleStringProperty();
        this.fechaFin = new SimpleStringProperty();
        this.fechaRealizacion = new SimpleStringProperty();
        this.duracionHoras = new SimpleIntegerProperty();
        this.fechaInicio = new SimpleStringProperty();
        this.participantes = FXCollections.observableArrayList();
        this.asesor = new SimpleObjectProperty<>(new Asesor());
        this.unidades = FXCollections.observableArrayList();
        this.dateFin = new SimpleObjectProperty<>();
        this.dateInicio = new SimpleObjectProperty<>();
        this.dateRealizacion = new SimpleObjectProperty<>();
        this.image = new SimpleStringProperty();
        this.temas = FXCollections.observableArrayList();
    }

    public Curso(int idCurso, String nombre, String modalidad, String fechaFin,
                 String fechaRealizacion, int duracionHoras, String fechaInicio,
                 String image, ObservableList<Tema> temas) {
        this.idCurso = new SimpleIntegerProperty(idCurso);
        this.nombre = new SimpleStringProperty(nombre);
        this.modalidad = new SimpleStringProperty(modalidad);
        this.fechaFin = new SimpleStringProperty(fechaFin);
        this.fechaRealizacion = new SimpleStringProperty(fechaRealizacion);
        this.duracionHoras = new SimpleIntegerProperty(duracionHoras);
        this.fechaInicio = new SimpleStringProperty(fechaInicio);
        this.participantes = FXCollections.observableArrayList();
        this.asesor = new SimpleObjectProperty<>();
        this.unidades = FXCollections.observableArrayList();
        this.image = new SimpleStringProperty(image);
        this.temas = FXCollections.observableArrayList();
    }

    // Getters y Setters
    public int getIdCurso() {
        return idCurso.get();
    }

    public void setIdCurso(int idCurso) {
        this.idCurso.set(idCurso);
    }

    public IntegerProperty idCursoProperty() {
        return idCurso;
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

    public String getModalidad() {
        return modalidad.get();
    }

    public void setModalidad(String modalidad) {
        this.modalidad.set(modalidad);
    }

    public StringProperty modalidadProperty() {
        return modalidad;
    }

    public String getFechaFin() {
        return fechaFin.get();
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin.set(fechaFin);
    }

    public StringProperty fechaFinProperty() {
        return fechaFin;
    }

    public String getFechaRealizacion() {
        return fechaRealizacion.get();
    }

    public void setFechaRealizacion(String fechaRealizacion) {
        this.fechaRealizacion.set(fechaRealizacion);
    }

    public StringProperty fechaRealizacionProperty() {
        return fechaRealizacion;
    }

    public int getDuracionHoras() {
        return duracionHoras.get();
    }

    public SimpleIntegerProperty duracionHorasProperty() {
        return duracionHoras;
    }

    public void setDuracionHoras(int duracionHoras) {
        this.duracionHoras.set(duracionHoras);
    }

    public String getFechaInicio() {
        return fechaInicio.get();
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio.set(fechaInicio);
    }

    public StringProperty fechaInicioProperty() {
        return fechaInicio;
    }

    public ObservableList<Participante> getParticipantes() {
        return participantes;
    }

    public ObservableList<Unidad> getUnidades() {
        return unidades;
    }


    public Asesor getAsesor() {
        return asesor.get();
    }

    public ObjectProperty<Asesor> asesorProperty() {
        return asesor;
    }

    public void setAsesor(Asesor asesor) {
        this.asesor.set(asesor);
    }

    public DateTimeFormatter getFormatterLocale() {
        return formatterLocale;
    }

    public void setFormatterLocale(DateTimeFormatter formatterLocale) {
        this.formatterLocale = formatterLocale;
    }

    public DateTimeFormatter getFormatterBd() {
        return formatterBd;
    }

    public void setFormatterBd(DateTimeFormatter formatterBd) {
        this.formatterBd = formatterBd;
    }

    public LocalDate getDateInicio() {
        return dateInicio.get();
    }

    public ObjectProperty<LocalDate> dateInicioProperty() {
        return dateInicio;
    }

    public void setDateInicio(LocalDate localDate) {
        this.dateInicio.set(localDate);
        this.setFechaInicio(this.dateToStringBD(localDate));
    }

    public LocalDate getDateFin() {
        return dateFin.get();
    }

    public ObjectProperty<LocalDate> dateFinProperty() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin.set(dateFin);
        this.setFechaFin(this.dateToStringBD(dateFin));
    }

    public LocalDate getDateRealizacion() {
        return dateRealizacion.get();
    }

    public ObjectProperty<LocalDate> dateRealizacionProperty() {
        return dateRealizacion;
    }

    public void setDateRealizacion(LocalDate dateRealizacion) {
        this.dateRealizacion.set(dateRealizacion);
        this.setFechaRealizacion(this.dateToStringBD(dateRealizacion));
    }

    public String getImage() {
        return image.get();
    }

    public StringProperty imageProperty() {
        return image;
    }

    public void setImage(String image) {
        this.image.set(image);
    }

    public ObservableList<Tema> getTemas() {
        return temas;
    }

    public void setTemas(ObservableList<Tema> temas) {
        this.temas = temas;
    }

    @Override
    public String toString() {
        return "Curso{" +
                "idCurso=" + idCurso.get() +
                "\n, nombre='" + nombre.get() + '\'' +
                "\n, modalidad='" + modalidad.get() + '\'' +
                "\n, fechaFin='" + fechaFin.get() + '\'' +
                "\n, fechaRealizacion='" + fechaRealizacion.get() + '\'' +
                "\n, duracionHoras='" + duracionHoras.get() + '\'' +
                "\n, fechaInicio='" + fechaInicio.get() + '\'' +
                "\n, participantes=" + participantes +
                "\n, asesor=" + asesor +
                "\n, unidades=" + unidades +
                '}';
    }

    public  String dateToStringLocale(LocalDate localDate){
        return formatterLocale.format(localDate);
    }

    public  String dateToStringBD(LocalDate localDate ){
        return formatterBd.format(localDate);
    }

    public LocalDate stringToDateBd(String date){
        if (date == null) {
            return null;
        }
        return LocalDate.parse(date, formatterBd);
    }

    public LocalDate stringToDateLocale(String date){
        return LocalDate.parse(date, formatterLocale);
    }
}

