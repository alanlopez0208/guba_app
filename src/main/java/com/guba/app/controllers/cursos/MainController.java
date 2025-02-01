package com.guba.app.controllers.cursos;

import com.guba.app.data.dao.DAOCurso;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Curso;

import java.util.*;

public class MainController extends BaseMainController<Curso> {


    private DAOCurso daoCurso = new DAOCurso();

    @Override
    protected void registrarPaginas() {
        registrarPagina(Paginas.LIST, new ListController(this, this.estadoProperty, this.paginaProperty, dataList));
        registrarPagina(Paginas.ADD, new AddController(this, this.estadoProperty, this.paginaProperty));
        registrarPagina(Paginas.DETAILS, new DetailsController(this, this.estadoProperty, this.paginaProperty));
        stack.getChildren().addAll(nodos.values());
        nodos.get(Paginas.LIST).setVisible(true);
    }

    @Override
    protected List<Curso> fetchData() {
        return daoCurso.obtenerTodosLosCursos();
    }


    @Override
    public boolean actualizar(Curso curso) {
        return daoCurso.actualizarCurso(curso);
    }

    @Override
    public boolean eliminar(Curso curso) {
        boolean seElimino = daoCurso.eliminarCurso(curso.getIdCurso());

        if (!seElimino){
            return false;
        }
        dataList.remove(curso);
        return true;
    }

    @Override
    public boolean guardar(Curso curso) {
        var optional = daoCurso.insertarCurso(curso);
        if (optional.isPresent()) {
            curso.setIdCurso(optional.get());
            dataList.add(curso);
            return true;
        }
        return false;
    }

    @Override
    public Curso ver(Curso curso) {
        return null;
    }
}
