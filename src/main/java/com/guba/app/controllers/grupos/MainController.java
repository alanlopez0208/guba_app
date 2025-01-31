package com.guba.app.controllers.grupos;


import com.guba.app.data.dao.DAOGrupo;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Grupo;

import java.util.*;

public class MainController extends BaseMainController<Grupo> {

    private DAOGrupo daoGrupo = new DAOGrupo();

    @Override
    protected void registrarPaginas() {
        registrarPagina(Paginas.LIST, "/grupos/List");
        registrarPagina(Paginas.DETAILS, "/grupos/Details");
        stack.getChildren().addAll(paginas.values());
        paginas.get(Paginas.LIST).setVisible(true);
    }

    @Override
    protected List<Grupo> fetchData() {
        return daoGrupo.getGrupos();
    }

    @Override
    public boolean actualizar(Grupo grupo) {
        return false;
    }

    @Override
    public boolean eliminar(Grupo grupo) {
        return false;
    }

    @Override
    public boolean guardar(Grupo grupo) {
        return false;
    }

    @Override
    public Grupo ver(Grupo grupo) {
        return null;
    }

}
