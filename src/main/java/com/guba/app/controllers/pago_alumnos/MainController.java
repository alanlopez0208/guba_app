package com.guba.app.controllers.pago_alumnos;


import com.guba.app.data.dao.DAOPagoAlumnos;
import com.guba.app.utils.*;
import com.guba.app.domain.models.PagoAlumno;

import java.util.*;

public class MainController extends BaseMainController<PagoAlumno> {

    private DAOPagoAlumnos pagoAlumnos = new DAOPagoAlumnos();

    @Override
    protected void registrarPaginas() {
        registrarPagina(Paginas.LIST, new ListController(this,this.estadoProperty, this.paginaProperty, dataList));
        registrarPagina(Paginas.ADD, new AddController(this,this.estadoProperty, this.paginaProperty));
        registrarPagina(Paginas.DETAILS,new DetailsController(this, this.estadoProperty, this.paginaProperty));
        registrarPagina(Paginas.EDIT,new EditController(this,this.estadoProperty, this.paginaProperty) );
        stack.getChildren().addAll(nodos.values());
        nodos.get(Paginas.LIST).setVisible(true);
    }

    @Override
    protected List<PagoAlumno> fetchData() {
        return pagoAlumnos.getPagos();
    }

    @Override
    public boolean actualizar(PagoAlumno pagoAlumno) {
        return pagoAlumnos.updatePago(pagoAlumno);
    }

    @Override
    public boolean eliminar(PagoAlumno pagoAlumno) {
        var seElimino = pagoAlumnos.deletePago(pagoAlumno.getIdPago());
        if (seElimino){
            dataList.remove(pagoAlumno);
            return true;
        }
        return false;
    }

    @Override
    public boolean guardar(PagoAlumno pagoAlumno) {
        var optional = pagoAlumnos.crearPago(pagoAlumno);
        if (optional.isPresent()){
            pagoAlumno.setIdPago(optional.get().toString());
            dataList.add(pagoAlumno);
            return true;
        }
        return false;
    }

    @Override
    public PagoAlumno ver(PagoAlumno pagoAlumno) {
        return pagoAlumno;
    }
}
