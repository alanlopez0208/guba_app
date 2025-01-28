package com.guba.app.controllers.materias;


import com.guba.app.dao.DAOMaterias;
import com.guba.app.conexion.Config;
import com.guba.app.controllers.BaseController;
import com.guba.app.controllers.Loadable;
import com.guba.app.controllers.Mediador;
import com.guba.app.controllers.Paginas;
import com.guba.app.models.Materia;
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

public class MainController implements Mediador<Materia>, Initializable {

    @FXML
    private StackPane stack;
    private Map<Paginas, Node> paginas;
    private Map<Paginas, BaseController<Materia>> controladores;
    private ObservableList<Materia> observableList;
    private Config config;
    private DAOMaterias daoMaterias;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paginas = new HashMap<>();
        controladores = new HashMap<>();
        observableList = FXCollections.observableArrayList();
        registrarPagina(Paginas.LIST, "/materias/List");
        registrarPagina(Paginas.ADD, "/materias/Add");
        registrarPagina(Paginas.EDIT, "/materias/Edit");
        registrarPagina(Paginas.DETAILS, "/materias/Details");
        stack.getChildren().addAll(paginas.values());
        paginas.get(Paginas.LIST).setVisible(true);
    }

    @Override
    public void changePane(Paginas pagina) {
        paginas.values().forEach(p->p.setVisible(false));
        paginas.get(pagina).setVisible(true);
    }

    @Override
    public void loadData(Paginas pagina, Materia c) {
        Loadable<Materia> controller = (Loadable<Materia>) controladores.get(pagina);
        if (controller != null) {
            controller.loadData(c);
        }
        changePane(pagina);
    }

    @Override
    public List<Materia> loadData() {
        return null;
    }


    private void registrarPagina(Paginas pagina, String rutaVista) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.URL_MODULES+rutaVista+".fxml"));
            Parent parent =  loader.load();
            parent.setVisible(false);
            BaseController<Materia> controller= loader.getController();
            controller.setMediador(this);
            controller.setLista(observableList);
            controladores.put(pagina, controller);
            paginas.put(pagina, parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
