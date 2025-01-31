package com.guba.app.controllers.carreras;


import com.guba.app.data.dao.DAOCarreras;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Carrera;

import java.util.*;

public class MainController extends BaseMainController<Carrera>{

    private DAOCarreras daoCarreras = new DAOCarreras();

    @Override
    protected void registrarPaginas() {
        registrarPagina(Paginas.LIST, new ListController("/carreras/List", this,this.estadoProperty, this.paginaProperty ));
        registrarPagina(Paginas.ADD, "/carreras/Add");
        registrarPagina(Paginas.EDIT, "/carreras/Edit");
        registrarPagina(Paginas.DETAILS, "/carreras/Details");
        stack.getChildren().addAll(paginas.values());
        paginas.get(Paginas.LIST).setVisible(true);
    }

    @Override
    protected List<Carrera> fetchData() {
        return daoCarreras.getAllCarreras();
    }

    @Override
    public boolean actualizar(Carrera carrera) {
        return daoCarreras.updateCarrera(carrera);
    }

    @Override
    public boolean eliminar(Carrera carrera) {
        boolean eliminar = daoCarreras.eliminarCarrera(carrera.getIdCarrera());
        if (!eliminar){
            return false;
        }

        dataList.remove(carrera);
        return true;
    }

    @Override
    public boolean guardar(Carrera carrera) {
        var optional =  daoCarreras.insertCarrera(carrera);
        if (optional.isPresent()){
            carrera.setIdCarrera(optional.get().toString());
            dataList.add(carrera);
            return true;
        }
        return false;
    }

    @Override
    public Carrera ver(Carrera carrera) {
        return null;
    }
}
