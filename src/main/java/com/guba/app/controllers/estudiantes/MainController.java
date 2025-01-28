package com.guba.app.controllers.estudiantes;

import com.guba.app.dao.DAOAlumno;
import com.guba.app.conexion.Config;
import com.guba.app.controllers.*;
import com.guba.app.models.Estudiante;
import com.guba.app.presentation.utils.Constants;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements  Mediador<Estudiante> {

    @FXML
    private BorderPane panel;
    @FXML
    private StackPane stack;
    private Map<Paginas, Node> paginas;
    private Map<Paginas, BaseController<Estudiante>> controladores;
    private ObservableList<Estudiante> observableList;
    private Config config;
    private DAOAlumno daoAlumno;


    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paginas = new HashMap<>();
        controladores = new HashMap<>();
        observableList = FXCollections.observableArrayList();
        daoAlumno = new DAOAlumno();
        Platform.runLater(() -> {
            registrarPagina(Paginas.LIST, "/estudiantes/ListEstudiantes");
            //registrarPagina(Paginas.ADD, "/estudiantes/AddEstudiante");
            //registrarPagina(Paginas.EDIT, "/estudiantes/EditEstudiante");
            //registrarPagina(Paginas.DETAILS, "/estudiantes/DetailsEstudiante");
            stack.getChildren().addAll(paginas.values());
            paginas.get(Paginas.LIST).setVisible(true);
        });
    }

    private void registrarPagina(Paginas pagina, String rutaVista) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.URL_MODULES+rutaVista+".fxml"));
            Parent parent =  loader.load();
            parent.setVisible(false);
            BaseController<Estudiante> controller= loader.getController();
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
    public void loadData(Paginas pagina, Estudiante c) {
        Loadable<Estudiante> controller = (Loadable<Estudiante>) controladores.get(pagina);
        if (controller != null) {
            controller.loadData(c);
        }
       changePane(pagina);
    }


    @Override
    public List<Estudiante> loadData() {
        return daoAlumno.getEstudiantes();
    }

}
