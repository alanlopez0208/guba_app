package com.guba.app.controllers.carreras;


import com.guba.app.dao.DAOCarreras;
import com.guba.app.conexion.Config;
import com.guba.app.controllers.BaseController;
import com.guba.app.controllers.Loadable;
import com.guba.app.controllers.Mediador;
import com.guba.app.controllers.Paginas;
import com.guba.app.models.Carrera;
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

public class MainController implements Mediador<Carrera>, Initializable {

    @FXML
    private StackPane stack;
    private Map<Paginas, Node> paginas;
    private Map<Paginas, BaseController<Carrera>> controladores;
    private ObservableList<Carrera> observableList;
    private Config config;
    private DAOCarreras daoCarreras;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paginas = new HashMap<>();
        controladores = new HashMap<>();
        observableList = FXCollections.observableArrayList();
        registrarPagina(Paginas.LIST, "/carreras/List");
        registrarPagina(Paginas.ADD, "/carreras/Add");
        registrarPagina(Paginas.EDIT, "/carreras/Edit");
        registrarPagina(Paginas.DETAILS, "/carreras/Details");
        stack.getChildren().setAll(paginas.get(Paginas.LIST));
    }

    @Override
    public void changePane(Paginas pagina) {
        paginas.values().forEach(p->{
            p.setVisible(false);
        });
        paginas.get(pagina).setVisible(true);
    }
    @Override
    public void loadData(Paginas pagina, Carrera c) {
        Loadable<Carrera> controller = (Loadable<Carrera>) controladores.get(pagina);
        if (controller != null) {
            controller.loadData(c);
        }
        changePane(pagina);
    }

    @Override
    public List<Carrera> loadData() {
        return null;
    }


    private void registrarPagina(Paginas pagina, String rutaVista) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.URL_MODULES+rutaVista+".fxml"));
            Parent parent =  loader.load();
            parent.setVisible(false);
            BaseController<Carrera> controller= loader.getController();
            controller.setMediador(this);
            controller.setLista(observableList);
            controladores.put(pagina, controller);
            paginas.put(pagina, parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
