package com.guba.app.controllers.grupos;


import com.guba.app.data.dao.DAOGrupo;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Grupo;

import java.util.*;

public class MainController extends BaseMainController<Grupo> {

    private DAOGrupo daoGrupo = new DAOGrupo();

    @Override
    protected void registrarPaginas() {
        registrarPagina(Paginas.LIST, new ListController(this, this.estadoProperty, this.paginaProperty, dataList));
        registrarPagina(Paginas.DETAILS, new DetailsController(this, this.estadoProperty, this.paginaProperty));
        stack.getChildren().addAll(nodos.values());
        nodos.get(Paginas.LIST).setVisible(true);
    }

    @Override
    protected List<Grupo> fetchData() {
        return daoGrupo.getGrupos();
    }

    @Override
    public boolean actualizar(Grupo grupo) {
        return daoGrupo.actualizarGrupo(grupo);
    }

    @Override
    public boolean eliminar(Grupo grupo) {
        boolean eliminado = daoGrupo.eliminarGrupo(grupo.getId());
        if (!eliminado) {
            return false;
        }
        dataList.remove(grupo);
        return true;
    }

    @Override
    public boolean guardar(Grupo grupo) {
        var optional = daoGrupo.agregarGrupo(grupo);
        if (optional.isEmpty()){
            return false;
        }
        grupo.setId(optional.get().toString());
        dataList.add(grupo);
        return true;
    }

    @Override
    public Grupo ver(Grupo grupo) {
        return null;
    }

}
