package com.guba.app.domain.models;


import com.guba.app.domain.dto.AlumnoDto;
import com.guba.app.presentation.utils.ComboBoxCell;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

import java.util.Objects;

public class Estudiante implements ComboBoxCell {
    private SimpleStringProperty  id;
    private SimpleStringProperty  matricula;
    private SimpleStringProperty  nombre;
    private SimpleStringProperty  apPaterno;
    private SimpleStringProperty  apMaterno;
    private SimpleStringProperty  emailPersonal;
    private SimpleStringProperty  emailInstitucional;
    private SimpleStringProperty  generacion;
    private SimpleStringProperty  numCelular;
    private SimpleStringProperty  estado;
    private SimpleStringProperty  municipio;
    private SimpleStringProperty  escProcedencia;
    private SimpleStringProperty  grado;
    private SimpleStringProperty  grupo;
    private SimpleStringProperty  Status;
    private SimpleStringProperty  password;
    private SimpleStringProperty  passwordTemporal;
    private SimpleStringProperty foto;
    private SimpleStringProperty  sexo;
    private SimpleStringProperty  semestre;
    private ObjectProperty<Carrera> carrera;
    private SimpleStringProperty  direccion;
    private SimpleStringProperty  nacimiento;

    private ObjectProperty<Image> fotoPerfil;


    public Estudiante(){
        this.id = new SimpleStringProperty();
        this.matricula = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty();
        this.apPaterno = new SimpleStringProperty();
        this.apMaterno = new SimpleStringProperty();
        this.emailPersonal = new SimpleStringProperty();
        this.emailInstitucional = new SimpleStringProperty();
        this.generacion = new SimpleStringProperty();
        this.numCelular=  new SimpleStringProperty();
        this.estado = new SimpleStringProperty();
        this.municipio = new SimpleStringProperty();
        this.escProcedencia = new SimpleStringProperty();
        this.grado = new SimpleStringProperty();
        this.grupo = new SimpleStringProperty();
        this.Status = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.passwordTemporal = new SimpleStringProperty();
        this.foto = new SimpleStringProperty();
        this.semestre = new SimpleStringProperty();
        this.sexo = new SimpleStringProperty();
        this.direccion = new SimpleStringProperty();
        this.nacimiento = new SimpleStringProperty();
        this.fotoPerfil = new SimpleObjectProperty<>();
        this.carrera = new SimpleObjectProperty<>();
    }

    public Estudiante(AlumnoDto alumnoDto){
        this();
        this.setId(alumnoDto.IdAlumno());
        this.setMatricula(alumnoDto.Matricula());
        this.setPassword(alumnoDto.Password());
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

    public String getMatricula() {
        return matricula.get();
    }

    public SimpleStringProperty matriculaProperty() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula.set(matricula);
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

    public String getApPaterno() {
        return apPaterno.get();
    }

    public SimpleStringProperty apPaternoProperty() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno.set(apPaterno);
    }

    public String getApMaterno() {
        return apMaterno.get();
    }

    public SimpleStringProperty apMaternoProperty() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno.set(apMaterno);
    }

    public String getEmailPersonal() {
        return emailPersonal.get();
    }

    public SimpleStringProperty emailPersonalProperty() {
        return emailPersonal;
    }

    public void setEmailPersonal(String emailPersonal) {
        this.emailPersonal.set(emailPersonal);
    }

    public String getEmailInstitucional() {
        return emailInstitucional.get();
    }

    public SimpleStringProperty emailInstitucionalProperty() {
        return emailInstitucional;
    }

    public void setEmailInstitucional(String emailInstitucional) {
        this.emailInstitucional.set(emailInstitucional);
    }

    public String getGeneracion() {
        return generacion.get();
    }

    public SimpleStringProperty generacionProperty() {
        return generacion;
    }

    public void setGeneracion(String generacion) {
        this.generacion.set(generacion);
    }

    public String getNumCelular() {
        return numCelular.get();
    }

    public SimpleStringProperty numCelularProperty() {
        return numCelular;
    }

    public void setNumCelular(String numCelular) {
        this.numCelular.set(numCelular);
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

    public String getEscProcedencia() {
        return escProcedencia.get();
    }

    public SimpleStringProperty escProcedenciaProperty() {
        return escProcedencia;
    }

    public void setEscProcedencia(String escProcedencia) {
        this.escProcedencia.set(escProcedencia);
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

    public String getGrupo() {
        return grupo.get();
    }

    public SimpleStringProperty grupoProperty() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo.set(grupo);
    }

    public String getStatus() {
        return Status.get();
    }

    public SimpleStringProperty statusProperty() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status.set(status);
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

    public String getPasswordTemporal() {
        return passwordTemporal.get();
    }

    public SimpleStringProperty passwordTemporalProperty() {
        return passwordTemporal;
    }

    public void setPasswordTemporal(String passwordTemporal) {
        this.passwordTemporal.set(passwordTemporal);
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

    public String getSexo() {
        return sexo.get();
    }

    public SimpleStringProperty sexoProperty() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo.set(sexo);
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

    public Carrera getCarrera() {
        return carrera.get();
    }

    public ObjectProperty<Carrera> carreraProperty() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera.set(carrera);
    }

    public String getDireccion() {
        return direccion.get();
    }

    public SimpleStringProperty direccionProperty() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion.set(direccion);
    }

    public String getNacimiento() {
        return nacimiento.get();
    }

    public SimpleStringProperty nacimientoProperty() {
        return nacimiento;
    }

    public void setNacimiento(String nacimiento) {
        this.nacimiento.set(nacimiento);
    }


    public Image getFotoPerfil() {
        return fotoPerfil.get();
    }

    public ObjectProperty<Image> fotoPerfilProperty() {
        return fotoPerfil;
    }

    public void setFotoPerfil(Image fotoPerfil) {
        this.fotoPerfil.set(fotoPerfil);
    }


    @Override
    public String toString() {
        return this.getNombre() + " " + this.getApPaterno() + " " + this.getApMaterno();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Estudiante that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getMatricula(), that.getMatricula()) && Objects.equals(getNombre(), that.getNombre()) && Objects.equals(getApPaterno(), that.getApPaterno()) && Objects.equals(getApMaterno(), that.getApMaterno());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMatricula(), getNombre(), getApPaterno(), getApMaterno());
    }

    @Override
    public String toComboCell() {
        return this.getNombre() +" " + this.getApPaterno() + " " + this.getApMaterno();
    }
}
