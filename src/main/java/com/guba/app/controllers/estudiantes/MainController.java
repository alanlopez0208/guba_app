package com.guba.app.controllers.estudiantes;

import com.guba.app.data.dao.DAOAlumno;
import com.guba.app.domain.models.Estudiante;
import com.guba.app.utils.*;

import java.util.*;

public class MainController extends BaseMainController<Estudiante> {

    private DAOAlumno daoAlumno = new DAOAlumno();

    @Override
    protected void registrarPaginas(){
        registrarPagina(Paginas.LIST, new ListController( this, this.estadoProperty, this.paginaProperty, this.dataList));
        registrarPagina(Paginas.ADD,  new AddController(this , this.estadoProperty, this.paginaProperty));
        registrarPagina(Paginas.EDIT, new EditController(this, this.estadoProperty, this.paginaProperty));
        registrarPagina(Paginas.DETAILS,new DetailsController(this, this.estadoProperty, this.paginaProperty));
        stack.getChildren().addAll(nodos.values());
        nodos.get(Paginas.LIST).setVisible(true);
    }

    @Override
    protected List<Estudiante> fetchData() {
        return daoAlumno.getEstudiantes();
    }


    @Override
    public boolean actualizar(Estudiante estudiante) {
        return daoAlumno.updateAlumno(estudiante);
    }

    @Override
    public boolean eliminar(Estudiante estudiante) {
        boolean eliminar =  daoAlumno.deleteAlumno(estudiante.getId());
        if (eliminar){
            dataList.remove(estudiante);
            return true;
        }
        return false;
    }

    @Override
    public boolean guardar(Estudiante estudiante) {
        var optional = daoAlumno.crearAlumno(estudiante);
        if (optional.isPresent()){
            estudiante.setId(optional.get().toString());
            dataList.add(estudiante);
            return true;
        }
        return false;
    }

    @Override
    public Estudiante ver(Estudiante estudiante) {
        return daoAlumno.getEstudiante(estudiante.getId());
    }
}
