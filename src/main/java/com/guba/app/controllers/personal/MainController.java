package com.guba.app.controllers.personal;

import com.guba.app.data.dao.DAOPersonal;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Personal;

import java.util.*;

public class MainController extends BaseMainController<Personal> {

    private DAOPersonal daoPersonal = new DAOPersonal();


    @Override
    protected void registrarPaginas() {
        registrarPagina(Paginas.LIST, new ListController(this,this.estadoProperty, this.paginaProperty, dataList));
        registrarPagina(Paginas.ADD, new AddController(this,this.estadoProperty, this.paginaProperty));
        registrarPagina(Paginas.EDIT, new EditController(this,this.estadoProperty, this.paginaProperty));
        registrarPagina(Paginas.DETAILS,new DetailsController(this,this.estadoProperty,this.paginaProperty) );
        stack.getChildren().addAll(nodos.values());
        nodos.get(Paginas.LIST).setVisible(true);
    }

    @Override
    protected List<Personal> fetchData() {
        return daoPersonal.getPersonales();
    }

    @Override
    public boolean actualizar(Personal personal) {
        return daoPersonal.updatePersonal(personal);
    }

    @Override
    public boolean eliminar(Personal personal) {
        boolean eliminar =  daoPersonal.deletePersonal(personal.getId());
        if (eliminar){
            dataList.remove(personal);
            return true;
        }
        return false;
    }

    @Override
    public boolean guardar(Personal personal) {
        var optional = daoPersonal.crearPersonal(personal);
        if (optional.isPresent()){
            personal.setId(optional.get().toString());
            dataList.add(personal);
            return true;
        }
        return false;
    }

    @Override
    public Personal ver(Personal personal) {
        return null;
    }
}
