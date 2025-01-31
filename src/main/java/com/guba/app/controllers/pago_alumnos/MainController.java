package com.guba.app.controllers.pago_alumnos;


import com.guba.app.data.dao.DAOPagoAlumnos;
import com.guba.app.utils.*;
import com.guba.app.domain.models.PagoAlumno;

import java.util.*;

public class MainController extends BaseMainController<PagoAlumno> {

    private DAOPagoAlumnos pagoAlumnos = new DAOPagoAlumnos();


    @Override
    protected void registrarPaginas() {
        registrarPagina(Paginas.LIST, "/pago_alumnos/List");
        registrarPagina(Paginas.ADD, "/pago_alumnos/Add");
        registrarPagina(Paginas.EDIT, "/pago_alumnos/Edit");
        registrarPagina(Paginas.DETAILS, "/pago_alumnos/Details");
        stack.getChildren().addAll(paginas.values());
        paginas.get(Paginas.LIST).setVisible(true);
    }

    @Override
    protected List<PagoAlumno> fetchData() {
        return pagoAlumnos.getPagos();
    }

    @Override
    public boolean actualizar(PagoAlumno pagoAlumno) {
        return false;
    }

    @Override
    public boolean eliminar(PagoAlumno pagoAlumno) {
        return false;
    }

    @Override
    public boolean guardar(PagoAlumno pagoAlumno) {
        return false;
    }

    @Override
    public PagoAlumno ver(PagoAlumno pagoAlumno) {
        return null;
    }

    /*@FXML
    private StackPane stack;
    private Map<Paginas, Node> paginas;
    private Map<Paginas, BaseController<PagoAlumno>> controladores;
    private ObservableList<PagoAlumno> observableList;
    private Config config;
    private DAOPagoAlumnos daoPagoAlumnos = new DAOPagoAlumnos();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paginas = new HashMap<>();
        controladores = new HashMap<>();
        observableList = FXCollections.observableArrayList();
        registrarPagina(Paginas.LIST, "/pago_alumnos/List");
        registrarPagina(Paginas.ADD, "/pago_alumnos/Add");
        registrarPagina(Paginas.EDIT, "/pago_alumnos/Edit");
        registrarPagina(Paginas.DETAILS, "/pago_alumnos/Details");
        stack.getChildren().addAll(paginas.values());
        paginas.get(Paginas.LIST).setVisible(true);
    }


    private void registrarPagina(Paginas pagina, String rutaVista) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.URL_MODULES+rutaVista+".fxml"));
            Parent parent =  loader.load();
            parent.setVisible(false);
            BaseController<PagoAlumno> controller= loader.getController();
            controller.setMediador(this);
            controller.setLista(observableList);
            controladores.put(pagina, controller);
            paginas.put(pagina, parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean actualizar(PagoAlumno pagoAlumno) {
        return false;
    }

    @Override
    public boolean eliminar(PagoAlumno pagoAlumno) {
        return false;
    }

    @Override
    public Optional<PagoAlumno> guardar(PagoAlumno pagoAlumno) {
        return Optional.empty();
    }

    @Override
    public PagoAlumno ver(PagoAlumno pagoAlumno) {
        return null;
    }

    @Override
    public void loadContent(Paginas nueva, PagoAlumno data) {

    }

    @Override
    public void loadBD() {

    }*/
}
