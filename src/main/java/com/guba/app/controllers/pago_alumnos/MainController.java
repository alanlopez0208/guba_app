package com.guba.app.controllers.pago_alumnos;


import com.guba.app.dao.DAOPagoAlumnos;
import com.guba.app.conexion.Config;
import com.guba.app.controllers.BaseController;
import com.guba.app.controllers.Loadable;
import com.guba.app.controllers.Mediador;
import com.guba.app.controllers.Paginas;
import com.guba.app.models.PagoAlumno;
import com.guba.app.presentation.utils.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Mediador<PagoAlumno>, Initializable {

    @FXML
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

    @Override
    public void changePane(Paginas pagina) {
        paginas.values().forEach(p->p.setVisible(false));
        paginas.get(pagina).setVisible(true);
    }

    @Override
    public void loadData(Paginas pagina, PagoAlumno c) {
        Loadable<PagoAlumno> controller = (Loadable<PagoAlumno>) controladores.get(pagina);
        if (controller != null) {
            controller.loadData(c);
        }
        changePane(pagina);
    }

    @Override
    public List<PagoAlumno> loadData() {
        return null;
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

}
