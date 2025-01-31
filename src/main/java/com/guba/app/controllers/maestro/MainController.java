package com.guba.app.controllers.maestro;

import com.guba.app.data.dao.DAOMaestro;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Maestro;

import java.util.*;

public class MainController extends BaseMainController<Maestro> {


    private DAOMaestro daoMaestro = new DAOMaestro();


    @Override
    protected void registrarPaginas() {
        registrarPagina(Paginas.LIST, "/maestros/List");
        registrarPagina(Paginas.ADD, "/maestros/Add");
        registrarPagina(Paginas.EDIT, "/maestros/Edit");
        registrarPagina(Paginas.DETAILS, "/maestros/Details");
        stack.getChildren().addAll(paginas.values());
        paginas.get(Paginas.LIST).setVisible(true);
    }

    @Override
    protected List<Maestro> fetchData() {
        return daoMaestro.getDocentes();
    }

    @Override
    public boolean actualizar(Maestro maestro) {
        return false;
    }

    @Override
    public boolean eliminar(Maestro maestro) {
        return false;
    }

    @Override
    public boolean guardar(Maestro maestro) {
        return false;
    }

    @Override
    public Maestro ver(Maestro maestro) {
        return null;
    }
}
