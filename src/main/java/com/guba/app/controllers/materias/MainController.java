package com.guba.app.controllers.materias;


import com.guba.app.data.dao.DAOMaterias;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Materia;

import java.util.*;

public class MainController extends BaseMainController<Materia> {

    private DAOMaterias daoMaterias = new DAOMaterias();

    @Override
    protected void registrarPaginas() {
        registrarPagina(Paginas.LIST,new ListController(this, this.estadoProperty,this.paginaProperty,dataList));
        registrarPagina(Paginas.ADD, new AddController(this, this.estadoProperty,this.paginaProperty));
        registrarPagina(Paginas.EDIT,new EditController(this, this.estadoProperty, this.paginaProperty) );
        registrarPagina(Paginas.DETAILS, new DetailsController(this, this.estadoProperty, this.paginaProperty));
        stack.getChildren().addAll(nodos.values());
        nodos.get(Paginas.LIST).setVisible(true);
    }

    @Override
    protected List<Materia> fetchData() {
        return daoMaterias.getMaterias();
    }

    @Override
    public boolean actualizar(Materia materia) {
        return daoMaterias.updateMateria(materia);
    }

    @Override
    public boolean eliminar(Materia materia) {
        boolean eliminado = daoMaterias.eliminarMateria(materia.getIdMateria());
        if (!eliminado) {
            return false;
        }
        dataList.remove(materia);
        return true;
    }

    @Override
    public boolean guardar(Materia materia) {
        var optional = daoMaterias.insertMateria(materia);
        if (optional.isEmpty()){
            return false;
        }
        materia.setId(optional.get().toString());
        dataList.add(materia);
        return true;
    }

    @Override
    public Materia ver(Materia materia) {
        return daoMaterias.getMateria(materia.getId());
    }

}
