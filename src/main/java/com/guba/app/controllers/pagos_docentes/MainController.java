package com.guba.app.controllers.pagos_docentes;


import com.guba.app.data.dao.DAOPagoDocentes;
import com.guba.app.domain.models.PagoAlumno;
import com.guba.app.utils.*;
import com.guba.app.domain.models.PagoDocente;

import java.util.*;

public class MainController extends BaseMainController<PagoDocente> {


    private DAOPagoDocentes daoPagoDocentes = new DAOPagoDocentes();

    @Override
    protected void registrarPaginas() {
        registrarPagina(Paginas.LIST, new ListController(this,this.estadoProperty,this.paginaProperty, dataList));
        registrarPagina(Paginas.ADD, new AddController(this,this.estadoProperty,this.paginaProperty));
        //registrarPagina(Paginas.EDIT, "/pago_docentes/Edit");
        registrarPagina(Paginas.DETAILS, new DetailsController(this,this.estadoProperty,this.paginaProperty));
        stack.getChildren().addAll(nodos.values());
        nodos.get(Paginas.LIST).setVisible(true);
    }


    @Override
    protected List<PagoDocente> fetchData() {
        return daoPagoDocentes.getPagos();
    }

    @Override
    public boolean actualizar(PagoDocente pagoDocente) {
        return daoPagoDocentes.updatePago(pagoDocente);
    }

    @Override
    public boolean eliminar(PagoDocente pagoDocente) {
        var seElimino = daoPagoDocentes.deletePago(pagoDocente.getIdPago());
        if (seElimino){
            dataList.remove(pagoDocente);
            return true;
        }
        return false;
    }

    @Override
    public boolean guardar(PagoDocente pagoDocente) {
        var optional = daoPagoDocentes.crearPago(pagoDocente);
        if (optional.isPresent()){
            pagoDocente.setIdPago(optional.get().toString());
            dataList.add(pagoDocente);
            return true;
        }
        return false;
    }

    @Override
    public PagoDocente ver(PagoDocente pagoDocente) {
        return pagoDocente;
    }
}
