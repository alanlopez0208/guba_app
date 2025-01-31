package com.guba.app.controllers.personal;

import com.guba.app.data.dao.DAOPersonal;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Personal;

import java.util.*;

public class MainController extends BaseMainController<Personal> {

    private DAOPersonal daoPersonal = new DAOPersonal();


    @Override
    protected void registrarPaginas() {
        registrarPagina(Paginas.LIST, "/personal/List");
        registrarPagina(Paginas.ADD, "/personal/Add");
        registrarPagina(Paginas.EDIT, "/personal/Edit");
        registrarPagina(Paginas.DETAILS, "/personal/Details");
        stack.getChildren().addAll(paginas.values());
        paginas.get(Paginas.LIST).setVisible(true);
    }

    @Override
    protected List<Personal> fetchData() {
        return daoPersonal.getPersonales();
    }

    @Override
    public boolean actualizar(Personal personal) {
        return false;
    }

    @Override
    public boolean eliminar(Personal personal) {
        return false;
    }

    @Override
    public boolean guardar(Personal personal) {
        return false;
    }

    @Override
    public Personal ver(Personal personal) {
        return null;
    }
    /*@FXML
    private StackPane stack;
    private Map<Paginas, Node> paginas;
    private Map<Paginas, BaseController<Personal>> controladores;
    private ObservableList<Personal> observableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paginas = new HashMap<>();
        controladores = new HashMap<>();
        observableList = FXCollections.observableArrayList();
        registrarPagina(Paginas.LIST, "/personal/List");
        registrarPagina(Paginas.ADD, "/personal/Add");
        registrarPagina(Paginas.EDIT, "/personal/Edit");
        registrarPagina(Paginas.DETAILS, "/personal/Details");
        stack.getChildren().addAll(paginas.values());
        paginas.get(Paginas.LIST).setVisible(true);
    }

    private void registrarPagina(Paginas pagina, String rutaVista) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.URL_MODULES+rutaVista+".fxml"));
            Parent parent =  loader.load();
            parent.setVisible(false);
            BaseController<Personal> controller= loader.getController();
            controller.setMediador(this);
            controller.setLista(observableList);
            controladores.put(pagina, controller);
            paginas.put(pagina, parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changePane(Paginas pagina) {
        paginas.values().forEach(p->p.setVisible(false));
        paginas.get(pagina).setVisible(true);
    }

    @Override
    public void loadData(Paginas pagina, Personal c) {
        Loadable<Personal> controller = (Loadable<Personal>) controladores.get(pagina);
        if (controller != null) {
            controller.loadData(c);
        }
        changePane(pagina);
    }

    @Override
    public List<Personal> loadData() {
        return null;
    }

    @Override
    public boolean actualizar(Personal personal) {
        return false;
    }

    @Override
    public boolean eliminar(Personal personal) {
        return false;
    }

    @Override
    public Optional<Personal> guardar(Personal personal) {
        return Optional.empty();
    }

    @Override
    public Personal ver(Personal personal) {
        return null;
    }

    @Override
    public void loadContent(Paginas anterior, Paginas nueva, Personal data) {

    }

    @Override
    public void loadBD() {

    }*/
}
