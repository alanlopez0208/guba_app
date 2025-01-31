package com.guba.app.controllers.materias;


import com.guba.app.data.dao.DAOMaterias;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Materia;

import java.util.*;

public class MainController extends BaseMainController<Materia> {

    private DAOMaterias daoMaterias = new DAOMaterias();

    @Override
    protected void registrarPaginas() {
        registrarPagina(Paginas.LIST, "/materias/List");
        registrarPagina(Paginas.ADD, "/materias/Add");
        registrarPagina(Paginas.EDIT, "/materias/Edit");
        registrarPagina(Paginas.DETAILS, "/materias/Details");
        stack.getChildren().addAll(paginas.values());
        paginas.get(Paginas.LIST).setVisible(true);
    }

    @Override
    protected List<Materia> fetchData() {
        return daoMaterias.getMaterias();
    }

    @Override
    public boolean actualizar(Materia materia) {
        return false;
    }

    @Override
    public boolean eliminar(Materia materia) {
        return false;
    }

    @Override
    public boolean guardar(Materia materia) {
        return false;
    }

    @Override
    public Materia ver(Materia materia) {
        return null;
    }

}
