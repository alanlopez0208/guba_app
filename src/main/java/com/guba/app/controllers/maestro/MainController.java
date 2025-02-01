package com.guba.app.controllers.maestro;

import com.guba.app.data.dao.DAOMaestro;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Maestro;

import java.util.*;

public class MainController extends BaseMainController<Maestro> {

    private DAOMaestro daoMaestro = new DAOMaestro();

    @Override
    protected void registrarPaginas() {
        registrarPagina(Paginas.LIST, new ListController(this, this.estadoProperty, this.paginaProperty, dataList));
        registrarPagina(Paginas.ADD, new AddController(this,this.estadoProperty, this.paginaProperty));
        registrarPagina(Paginas.EDIT,new EditController(this, this.estadoProperty, this.paginaProperty));
        registrarPagina(Paginas.DETAILS, new DetailsController(this, this.estadoProperty, this.paginaProperty));
        stack.getChildren().addAll(nodos.values());
        nodos.get(Paginas.LIST).setVisible(true);
    }

    @Override
    protected List<Maestro> fetchData() {
        return daoMaestro.getDocentes();
    }

    @Override
    public boolean actualizar(Maestro maestro) {
        return daoMaestro.updateDocente(maestro);
    }

    @Override
    public boolean eliminar(Maestro maestro) {
        boolean seElimino = daoMaestro.deleteDocente(maestro.getId());
        if (!seElimino) {
            return false;
        }
        dataList.remove(maestro);
        return true;
    }

    @Override
    public boolean guardar(Maestro maestro) {
        boolean seAgrego = daoMaestro.crearDocente(maestro);
        if (!seAgrego) {
            return false;
        }
        dataList.add(maestro);
        return true;
    }

    @Override
    public Maestro ver(Maestro maestro) {
        return daoMaestro.obtenerDocente(maestro.getId());
    }
}
