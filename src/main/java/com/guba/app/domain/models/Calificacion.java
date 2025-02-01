package com.guba.app.domain.models;

import com.guba.app.domain.dto.CalificacionDTO;
import javafx.beans.property.*;

import java.util.Objects;

public class Calificacion {
    private IntegerProperty idCalificacion;
    private IntegerProperty idAlumno;
    private IntegerProperty idDocente;
    private IntegerProperty idPerido;
    private IntegerProperty idMateria;
    private ObjectProperty<Materia> materia;
    private ObjectProperty<Estudiante> estudiante;
    private ObjectProperty<Periodo> perido;
    private ObjectProperty <Maestro> maestro;
    private ObjectProperty<Grupo> grupo;
    private ObjectProperty<Float> p1U1;
    private ObjectProperty<Float> p2U1;
    private ObjectProperty<Float> p3U1;
    private ObjectProperty<Float> p4U1;
    private ObjectProperty<Float> p1U2;
    private ObjectProperty<Float> p2U2;
    private ObjectProperty<Float> p3U2;
    private ObjectProperty<Float> p4U2;
    private ObjectProperty<Float>p1U3;
    private ObjectProperty<Float> p2U3;
    private ObjectProperty<Float> p3U3;
    private ObjectProperty<Float> p4U3;
    private ObjectProperty<Float> p1U4;
    private ObjectProperty<Float> p2U4;
    private ObjectProperty<Float> p3U4;
    private ObjectProperty<Float> p4U4;
    private ObjectProperty<Float> trabjoFinal;
    private ObjectProperty<Float> promedioU1;
    private ObjectProperty<Float> promedioU2;
    private ObjectProperty<Float> promedioU3;
    private ObjectProperty<Float> promedioU4;
    private ObjectProperty<Float> promedioFinal;
    private ObjectProperty<Periodo> periodo;
    private SimpleStringProperty fecha;
    private SimpleStringProperty tipo;
    private SimpleStringProperty modulo;

    public Calificacion() {
        this.idCalificacion = new SimpleIntegerProperty();
        this.idAlumno = new SimpleIntegerProperty();
        this.idDocente = new SimpleIntegerProperty();
        this.materia = new SimpleObjectProperty<>();
        this.estudiante = new SimpleObjectProperty<>();
        this.maestro = new SimpleObjectProperty<>();
        this.perido = new SimpleObjectProperty<>();
        this.grupo = new SimpleObjectProperty<>();
        this.p1U1 = new SimpleObjectProperty<>();
        this.p2U1 = new SimpleObjectProperty<>();
        this.p3U1 = new SimpleObjectProperty<>();
        this.p4U1 = new SimpleObjectProperty<>();
        this.p1U2 = new SimpleObjectProperty<>();
        this.p2U2 = new SimpleObjectProperty<>();
        this.p3U2 = new SimpleObjectProperty<>();
        this.p4U2 = new SimpleObjectProperty<>();
        this.p1U3 = new SimpleObjectProperty<>();
        this.p2U3 = new SimpleObjectProperty<>();
        this.p3U3 = new SimpleObjectProperty<>();
        this.p4U3 = new SimpleObjectProperty<>();
        this.p1U4 = new SimpleObjectProperty<>();
        this.p2U4 = new SimpleObjectProperty<>();
        this.p3U4 = new SimpleObjectProperty<>();
        this.p4U4 = new SimpleObjectProperty<>();
        this.trabjoFinal = new SimpleObjectProperty<>();
        this.promedioU1 = new SimpleObjectProperty<>();
        this.promedioU2 = new SimpleObjectProperty<>();
        this.promedioU3 = new SimpleObjectProperty<>();
        this.promedioU4 = new SimpleObjectProperty<>();
        this.promedioFinal = new SimpleObjectProperty<>();
        this.periodo = new SimpleObjectProperty<>();
        this.fecha = new SimpleStringProperty();
        this.tipo = new SimpleStringProperty();
        this.modulo = new SimpleStringProperty();
        this.idMateria = new SimpleIntegerProperty();
        this.idPerido = new SimpleIntegerProperty();
    }

    public Calificacion(CalificacionDTO daoCalificiaciones) {
        this();
        this.setIdCalificacion(daoCalificiaciones.idCalificacion());
        this.setIdAlumno(daoCalificiaciones.idAlumno());
        this.setIdDocente(daoCalificiaciones.idDocente());
        this.setP1U1(daoCalificiaciones.P1U1());
        this.setP2U1(daoCalificiaciones.P2U1());
        this.setP3U1(daoCalificiaciones.P3U1());
        this.setP4U1(daoCalificiaciones.P4U1());
        this.setP1U2(daoCalificiaciones.P1U2());
        this.setP2U2(daoCalificiaciones.P2U2());
        this.setP3U2(daoCalificiaciones.P3U2());
        this.setP4U2(daoCalificiaciones.P4U2());
        this.setP1U3(daoCalificiaciones.P1U3());
        this.setP2U3(daoCalificiaciones.P2U3());
        this.setP3U3(daoCalificiaciones.P3U3());
        this.setP4U3(daoCalificiaciones.P4U3());
        this.setP1U4(daoCalificiaciones.P1U4());
        this.setP2U4(daoCalificiaciones.P2U4());
        this.setP3U4(daoCalificiaciones.P3U4());
        this.setP4U4(daoCalificiaciones.P4U4());
        this.setFecha(daoCalificiaciones.Fecha());
        this.setModulo(daoCalificiaciones.Folio());
        this.setTipo(daoCalificiaciones.Tipo());
        this.setIdMateria(daoCalificiaciones.idMateria());
        this.setIdPerido(daoCalificiaciones.idPeriodo());
    }

    public int getIdCalificacion() {
        return idCalificacion.get();
    }

    public IntegerProperty idCalificacionProperty() {
        return idCalificacion;
    }

    public void setIdCalificacion(int idCalificacion) {
        this.idCalificacion.set(idCalificacion);
    }

    public int getIdAlumno() {
        return idAlumno.get();
    }

    public IntegerProperty idAlumnoProperty() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno.set(idAlumno);
    }

    public int getIdDocente() {
        return idDocente.get();
    }

    public IntegerProperty idDocenteProperty() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente.set(idDocente);
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

    public Grupo getGrupo() {
        return grupo.get();
    }

    public ObjectProperty<Grupo> grupoProperty() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo.set(grupo);
    }

    public Float getP1U1() {
        return p1U1.get();
    }

    public ObjectProperty<Float> p1U1Property() {
        return p1U1;
    }

    public void setP1U1(Float p1U1) {
        this.p1U1.set(p1U1);
        setPromedioU1();
    }

    public Float getP2U1() {
        return p2U1.get();
    }

    public ObjectProperty<Float> p2U1Property() {
        return p2U1;
    }

    public void setP2U1(Float p2U1) {
        this.p2U1.set(p2U1);
        setPromedioU1();
    }

    public Float getP3U1() {
        return p3U1.get();
    }

    public ObjectProperty<Float> p3U1Property() {
        return p3U1;
    }

    public void setP3U1(Float p3U1) {
        this.p3U1.set(p3U1);
        setPromedioU1();
    }

    public Float getP4U1() {
        return p4U1.get();

    }

    public ObjectProperty<Float> p4U1Property() {
        return p4U1;
    }

    public void setP4U1(Float p4U1) {
        this.p4U1.set(p4U1);
        setPromedioU1();
    }

    public Float getP1U2() {
        return p1U2.get();
    }

    public ObjectProperty<Float> p1U2Property() {
        return p1U2;
    }

    public void setP1U2(Float p1U2) {
        this.p1U2.set(p1U2);
        setPromedioU2();
    }

    public Float getP2U2() {
        return p2U2.get();
    }

    public ObjectProperty<Float> p2U2Property() {
        return p2U2;
    }

    public void setP2U2(Float p2U2) {
        this.p2U2.set(p2U2);
        setPromedioU2();
    }

    public Float getP3U2() {
        return p3U2.get();

    }

    public ObjectProperty<Float> p3U2Property() {
        return p3U2;
    }

    public void setP3U2(Float p3U2) {
        this.p3U2.set(p3U2);
        setPromedioU2();
    }

    public Float getP4U2() {
        return p4U2.get();
    }

    public ObjectProperty<Float> p4U2Property() {
        return p4U2;
    }

    public void setP4U2(Float p4U2) {
        this.p4U2.set(p4U2);
        setPromedioU2();
    }

    public Float getP1U3() {
        return p1U3.get();
    }

    public ObjectProperty<Float> p1U3Property() {
        return p1U3;
    }

    public void setP1U3(Float p1U3) {
        this.p1U3.set(p1U3);
        setPromedioU3();
    }

    public Float getP2U3() {
        return p2U3.get();
    }

    public ObjectProperty<Float> p2U3Property() {
        return p2U3;
    }

    public void setP2U3(Float p2U3) {
        this.p2U3.set(p2U3);
        setPromedioU3();
    }

    public Float getP3U3() {
        return p3U3.get();
    }

    public ObjectProperty<Float> p3U3Property() {
        return p3U3;
    }

    public void setP3U3(Float p3U3) {
        this.p3U3.set(p3U3);
        setPromedioU3();
    }

    public Float getP4U3() {
        return p4U3.get();
    }

    public ObjectProperty<Float> p4U3Property() {
        return p4U3;
    }

    public void setP4U3(Float p4U3) {
        this.p4U3.set(p4U3);
        setPromedioU3();
    }

    public Float getP1U4() {
        return p1U4.get();
    }

    public ObjectProperty<Float> p1U4Property() {
        return p1U4;
    }

    public void setP1U4(Float p1U4) {
        this.p1U4.set(p1U4);
        setPromedioU4();
    }

    public Float getP2U4() {
        return p2U4.get();
    }

    public ObjectProperty<Float> p2U4Property() {
        return p2U4;
    }

    public void setP2U4(Float p2U4) {
        this.p2U4.set(p2U4);
        setPromedioU4();
    }

    public Float getP3U4() {
        return p3U4.get();
    }

    public ObjectProperty<Float> p3U4Property() {
        return p3U4;
    }

    public void setP3U4(Float p3U4) {
        this.p3U4.set(p3U4);
        setPromedioU4();
    }

    public Float getP4U4() {
        return p4U4.get();
    }

    public ObjectProperty<Float> p4U4Property() {
        return p4U4;
    }

    public void setP4U4(Float p4U4) {
        this.p4U4.set(p4U4);
        setPromedioU4();
    }

    public Float getTrabjoFinal() {
        return trabjoFinal.get();
    }

    public ObjectProperty<Float> trabjoFinalProperty() {
        return trabjoFinal;
    }

    public void setTrabjoFinal(Float trabjoFinal) {
        this.trabjoFinal.set(trabjoFinal);
    }

    public Float getPromedioU1() {
        return promedioU1.get();
    }

    public ObjectProperty<Float> promedioU1Property() {
        return promedioU1;
    }

    public void setPromedioU1() {
        this.promedioU4.set(obtenerPromedioUnidad(
                p1U4.get(),
                p2U4.get(),
                p3U4.get(),
                p4U4.get()
        ));
        establecerPromedioFinal();
    }

    public Float getPromedioU2() {
        return promedioU2.get();
    }

    public ObjectProperty<Float> promedioU2Property() {
        return promedioU2;
    }

    public void setPromedioU2() {
        this.promedioU2.set(obtenerPromedioUnidad(
                p1U2.get(),
                p2U2.get(),
                p3U2.get(),
                p4U2.get()
        ));
        establecerPromedioFinal();
    }

    public Float getPromedioU3() {
        return promedioU3.get();
    }

    public ObjectProperty<Float> promedioU3Property() {
        return promedioU3;
    }

    public void setPromedioU3() {
        this.promedioU3.set(obtenerPromedioUnidad(
                p1U3.get(),
                p2U3.get(),
                p3U3.get(),
                p4U3.get()
        ));
        establecerPromedioFinal();
    }

    public Float getPromedioU4() {
        return promedioU4.get();
    }

    public ObjectProperty<Float> promedioU4Property() {
        return promedioU4;
    }

    public void setPromedioU4() {
        this.promedioU4.set(obtenerPromedioUnidad(
                p1U4.get(),
                p2U4.get(),
                p3U4.get(),
                p4U4.get()
        ));
        establecerPromedioFinal();
    }

    public Float getPromedioFinal() {
        return promedioFinal.get();
    }

    public ObjectProperty<Float> promedioFinalProperty() {
        return promedioFinal;
    }

    public void setPromedioFinal(Float promedioFinal) {
        this.promedioFinal.set(promedioFinal);
    }

    public Periodo getPeriodo() {
        return periodo.get();
    }

    public ObjectProperty<Periodo> periodoProperty() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo.set(periodo);
    }

    public Estudiante getEstudiante() {
        return estudiante.get();
    }

    public ObjectProperty<Estudiante> estudianteProperty() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante.set(estudiante);
    }

    public Periodo getPerido() {
        return perido.get();
    }

    public ObjectProperty<Periodo> peridoProperty() {
        return perido;
    }

    public void setPerido(Periodo perido) {
        this.perido.set(perido);
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

    public String getFecha() {
        return fecha.get();
    }

    public SimpleStringProperty fechaProperty() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha.set(fecha);
    }

    public String getTipo() {
        return tipo.get();
    }

    public SimpleStringProperty tipoProperty() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }

    public String getModulo() {
        return modulo.get();
    }

    public SimpleStringProperty moduloProperty() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo.set(modulo);
    }


    public int getIdPerido() {
        return idPerido.get();
    }

    public IntegerProperty idPeridoProperty() {
        return idPerido;
    }

    public void setIdPerido(int idPerido) {
        this.idPerido.set(idPerido);
    }

    public int getIdMateria() {
        return idMateria.get();
    }

    public IntegerProperty idMateriaProperty() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria.set(idMateria);
    }

    public Float obtenerPromedioUnidad(Float... unidades) {
        float promedio = (float) 0.0;
        int unidadesConPromedio = 0;
        int contador = 0;

        while (contador < unidades.length) {
            if (unidades[contador] != null) {
                promedio += unidades[contador];
                unidadesConPromedio++;
            }
            contador++;
        }
        return unidadesConPromedio == 0 ? null : promedio / unidadesConPromedio;
    }
    public void establecerPromedioFinal() {
        this.promedioU1.set(obtenerPromedioUnidad(
                p1U1.get(),
                p2U1.get(),
                p3U1.get(),
                p4U1.get()
        ));
        this.promedioU2.set(obtenerPromedioUnidad(
                p1U2.get(),
                p2U2.get(),
                p3U2.get(),
                p4U2.get()
        ));
        this.promedioU3.set(obtenerPromedioUnidad(
                p1U3.get(),
                p2U3.get(),
                p3U3.get(),
                p4U3.get()
        ));
        this.promedioU4.set(obtenerPromedioUnidad(
                p1U4.get(),
                p2U4.get(),
                p3U4.get(),
                p4U4.get()
        ));
        if (this.getPromedioU1() != null && this.getTrabjoFinal() != null) {

            this.setPromedioFinal(
                    obtenerPromedioUnidad(this.getPromedioU1(),
                    this.getPromedioU2(),
                    this.getPromedioU3(),
                    this.getPromedioU4(),
                    this.getTrabjoFinal()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Calificacion that)) return false;
        return Objects.equals(getIdCalificacion(), that.getIdCalificacion()) && Objects.equals(getIdAlumno(), that.getIdAlumno()) && Objects.equals(getIdDocente(), that.getIdDocente()) && Objects.equals(getIdPerido(), that.getIdPerido()) && Objects.equals(getIdMateria(), that.getIdMateria()) && Objects.equals(getMateria(), that.getMateria()) && Objects.equals(getEstudiante(), that.getEstudiante()) && Objects.equals(getPerido(), that.getPerido()) && Objects.equals(getMaestro(), that.getMaestro()) && Objects.equals(getGrupo(), that.getGrupo()) && Objects.equals(getP1U1(), that.getP1U1()) && Objects.equals(getP2U1(), that.getP2U1()) && Objects.equals(getP3U1(), that.getP3U1()) && Objects.equals(getP4U1(), that.getP4U1()) && Objects.equals(getP1U2(), that.getP1U2()) && Objects.equals(getP2U2(), that.getP2U2()) && Objects.equals(getP3U2(), that.getP3U2()) && Objects.equals(getP4U2(), that.getP4U2()) && Objects.equals(getP1U3(), that.getP1U3()) && Objects.equals(getP2U3(), that.getP2U3()) && Objects.equals(getP3U3(), that.getP3U3()) && Objects.equals(getP4U3(), that.getP4U3()) && Objects.equals(getP1U4(), that.getP1U4()) && Objects.equals(getP2U4(), that.getP2U4()) && Objects.equals(getP3U4(), that.getP3U4()) && Objects.equals(getP4U4(), that.getP4U4()) && Objects.equals(getTrabjoFinal(), that.getTrabjoFinal()) && Objects.equals(getPromedioU1(), that.getPromedioU1()) && Objects.equals(getPromedioU2(), that.getPromedioU2()) && Objects.equals(getPromedioU3(), that.getPromedioU3()) && Objects.equals(getPromedioU4(), that.getPromedioU4()) && Objects.equals(getPromedioFinal(), that.getPromedioFinal()) && Objects.equals(getPeriodo(), that.getPeriodo()) && Objects.equals(getFecha(), that.getFecha()) && Objects.equals(getTipo(), that.getTipo()) && Objects.equals(getModulo(), that.getModulo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdCalificacion(), getIdAlumno(), getIdDocente(), getIdPerido(), getIdMateria(), getMateria(), getEstudiante(), getPerido(), getMaestro(), getGrupo(), getP1U1(), getP2U1(), getP3U1(), getP4U1(), getP1U2(), getP2U2(), getP3U2(), getP4U2(), getP1U3(), getP2U3(), getP3U3(), getP4U3(), getP1U4(), getP2U4(), getP3U4(), getP4U4(), getTrabjoFinal(), getPromedioU1(), getPromedioU2(), getPromedioU3(), getPromedioU4(), getPromedioFinal(), getPeriodo(), getFecha(), getTipo(), getModulo());
    }

    @Override
    public String toString() {
        return "Calificacion{" +
                "idCalificacion=" + idCalificacion +
                ", idAlumno=" + idAlumno +
                ", idDocente=" + idDocente +
                ", idPerido=" + idPerido +
                ", idMateria=" + idMateria +
                ", materia=" + materia +
                ", estudiante=" + estudiante +
                ", perido=" + perido +
                ", maestro=" + maestro +
                ", grupo=" + grupo +
                ", p1U1=" + p1U1 +
                ", p2U1=" + p2U1 +
                ", p3U1=" + p3U1 +
                ", p4U1=" + p4U1 +
                ", p1U2=" + p1U2 +
                ", p2U2=" + p2U2 +
                ", p3U2=" + p3U2 +
                ", p4U2=" + p4U2 +
                ", p1U3=" + p1U3 +
                ", p2U3=" + p2U3 +
                ", p3U3=" + p3U3 +
                ", p4U3=" + p4U3 +
                ", p1U4=" + p1U4 +
                ", p2U4=" + p2U4 +
                ", p3U4=" + p3U4 +
                ", p4U4=" + p4U4 +
                ", trabjoFinal=" + trabjoFinal +
                ", promedioU1=" + promedioU1 +
                ", promedioU2=" + promedioU2 +
                ", promedioU3=" + promedioU3 +
                ", promedioU4=" + promedioU4 +
                ", promedioFinal=" + promedioFinal +
                ", periodo=" + periodo +
                ", fecha=" + fecha +
                ", tipo=" + tipo +
                ", modulo=" + modulo +
                '}';
    }
}
