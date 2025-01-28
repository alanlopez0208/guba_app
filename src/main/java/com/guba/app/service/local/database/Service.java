package com.guba.app.service.local.database;

import com.guba.app.dao.*;
import com.guba.app.models.Estudiante;
import com.guba.app.models.Grupo;
import com.guba.app.models.Periodo;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;

public class Service {

    public static Service service;

    private DAOAlumno daoAlumno;
    private DAOPeriodo daoPeriodo;
    private DAOGrupo daoCurso;

    private DAOGrupoMateria daoGrupoMateria;
    private ObservableList<Estudiante> estudiantes;
    private ObservableList<Grupo> grupos;

    public Service(){
        daoAlumno = new DAOAlumno();
        daoPeriodo = new DAOPeriodo();
        daoCurso = new DAOGrupo();
        daoGrupoMateria = new DAOGrupoMateria();
        estudiantes =  FXCollections.observableArrayList();
        grupos = FXCollections.observableArrayList();
    }


    public static Service getService(){
        if (service == null){
            service = new Service();
        }
        return service;
    }


    public List<Estudiante> cargarEstudiantes(){
        List<Estudiante> estudianteList = daoAlumno.getEstudiantes();
        estudiantes.setAll(estudianteList);
        return estudianteList;
    }


    public boolean actualizarAlumno(Estudiante estudiante){
        return daoAlumno.updateAlumno(estudiante);
    }

    public boolean eliminarAlumno(Estudiante estudiante){
        boolean seElimino= daoAlumno.deleteAlumno(estudiante.getMatricula());
        if (!seElimino){
            return false;
        }
        estudiantes.remove(estudiante);
        return true;
    }

    public boolean agregarAlumno(Estudiante estudiante, Integer integer){
        var seAgrego = daoAlumno.crearAlumno(estudiante);

        if (seAgrego.isEmpty()){
            return false;
        }
        estudiante.setId(integer.toString());
        estudiantes.add(estudiante);
        return true;
    }

    public List<Grupo> cargarGrupos(){
        List<Grupo> grupoList = daoCurso.getGrupos();
        grupos.setAll(grupoList);
        return grupoList;
    }

    public boolean agregarGrupo(Grupo grupo){
        Optional<Integer> integer = daoCurso.agregarGrupo(grupo);
        if (integer.isEmpty()){
            return false;
        }
        grupo.setId(integer.get().toString());
        grupos.add(grupo);
        daoGrupoMateria.agregarGrupoMaterias(grupo);
        return true;
    }

    public boolean actualizarGrupo(Grupo grupo){
        return daoCurso.actualizarGrupo(grupo);
    }

    public boolean eliminarCurso(Grupo grupo){
        boolean seElimino = daoCurso.eliminarGrupo(grupo.getId());
        if (!seElimino){
            return false;
        }
        grupos.remove(grupo);
        daoGrupoMateria.eliminimarByIdGrupo(grupo.getId());
        return true;
    }


    public Optional<Periodo> crearPeriodo(Periodo periodo){
        var actualizado = daoPeriodo.crearPeriodo(periodo);
        if (actualizado.isEmpty()){
            return Optional.empty();
        }
        cargarEstudiantes();
        cargarGrupos();
        return Optional.of(periodo);
    }

    public ObservableList<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public ObservableList<Grupo> getGrupos() {
        System.out.println(grupos);
        return grupos;
    }

}
