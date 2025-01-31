package com.guba.app.controllers.pagos_docentes;


import com.guba.app.utils.*;
import com.guba.app.domain.models.PagoDocente;

import java.util.*;

public class MainController extends BaseMainController<PagoDocente> {
    @Override
    protected void registrarPaginas() {
        registrarPagina(Paginas.LIST, "/pago_docentes/List");
        registrarPagina(Paginas.ADD, "/pago_docentes/Add");
        registrarPagina(Paginas.EDIT, "/pago_docentes/Edit");
        registrarPagina(Paginas.DETAILS, "/pago_docentes/Details");
        stack.getChildren().addAll(paginas.values());
        paginas.get(Paginas.LIST).setVisible(true);
    }

    @Override
    protected List<PagoDocente> fetchData() {
        return List.of();
    }

    @Override
    public boolean actualizar(PagoDocente pagoDocente) {
        return false;
    }

    @Override
    public boolean eliminar(PagoDocente pagoDocente) {
        return false;
    }

    @Override
    public boolean guardar(PagoDocente pagoDocente) {
        return false;
    }

    @Override
    public PagoDocente ver(PagoDocente pagoDocente) {
        return null;
    }
}
