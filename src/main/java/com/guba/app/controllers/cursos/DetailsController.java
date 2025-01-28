package com.guba.app.controllers.cursos;

import com.dlsc.gemsfx.CalendarPicker;
import com.guba.app.controllers.BaseController;
import com.guba.app.controllers.Loadable;
import com.guba.app.controllers.Paginas;
import com.guba.app.dao.DAOAseor;
import com.guba.app.dao.DAOParticipante;
import com.guba.app.models.Asesor;
import com.guba.app.models.Curso;
import com.guba.app.models.Participante;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class DetailsController extends BaseController<Curso> implements Initializable, Loadable<Curso> {
    @FXML
    private TextField nombreField;
    @FXML
    private ComboBox<String> comboModalidades;
    @FXML
    private CalendarPicker daeFechaInico;
    @FXML
    private CalendarPicker fechaFin;
    @FXML
    private TextField duracionField;
    @FXML
    private TextField impartidorNombreField;
    @FXML
    private TextField impartidorPuestoFlied;
    @FXML
    private TextField impartdiorLugarField;
    @FXML
    private ListView<Participante> listParticipantes;
    @FXML
    private ObservableList<Participante> participanteObservableList = FXCollections.observableArrayList();
    private Curso curso;
    private DAOAseor daoAseor = new DAOAseor();
    private DAOParticipante daoParticipante = new DAOParticipante();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setListView();
        setComboBox();
    }


    @FXML
    public void backPanel(ActionEvent event) {
        mediador.changePane(Paginas.LIST);
    }

    @Override
    public void loadData(Curso data) {
        this.curso = data;
        this.nombreField.textProperty().bind(curso.nombreProperty());
        this.comboModalidades.valueProperty().bind(curso.modalidadProperty());
        this.duracionField.setText(String.valueOf(curso.getDuracionHoras()));
        this.daeFechaInico.setValue(curso.stringToDateBd(curso.getFechaInicio()));
        this.fechaFin.setValue(curso.stringToDateBd(curso.getFechaFin()));
        //this.comboModalidades.setValue(curso.getModalidad());
        this.listParticipantes.setItems(curso.getParticipantes());
        loadAsesorAsync(curso.getIdCurso());
        loadParticipantes(curso.getIdCurso());
    }



    private void setListView(){
        listParticipantes.setSelectionModel(null);
        listParticipantes.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Participante> call(ListView<Participante> asesorListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Participante item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty){
                            setText(null);
                            setGraphic(null);
                        }else{

                            Participante participante = item;
                            VBox vBox = new VBox();

                            Label nombre = new Label();
                            nombre.getStyleClass().add("textoNegritas");
                            nombre.setText(participante.getNombre().toUpperCase());
                            vBox.setAlignment(Pos.CENTER);
                            vBox.getChildren().addAll(nombre);
                            vBox.getStyleClass().add("panelShadow");
                            vBox.setSpacing(15);
                            setGraphic(vBox);
                        }
                    }
                };
            }
        });
        listParticipantes.setItems(participanteObservableList);

    }

    private void setComboBox(){
        comboModalidades.getItems().setAll("Presencial","Virtual","Mixta");
    }


    public void loadAsesorAsync(int idcurso){
        Task<Asesor> asesorTask = new Task<Asesor>() {
            @Override
            protected Asesor call() throws Exception {
                return daoAseor.obtenerAsesorPorCurso(idcurso);
            }
        };

        asesorTask.setOnSucceeded(event -> {
            try {
                Asesor asesor = asesorTask.get();
                this.impartidorNombreField.setText(asesor.getNombre());
                this.impartdiorLugarField.setText(asesor.getLugar());
                this.impartidorPuestoFlied.setText(asesor.getPuesto());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        asesorTask.setOnFailed(event -> {
            System.out.println("Error al obtener al impartidor: "+ asesorTask.getException());
        });

        new Thread(asesorTask).start();
    }


    public void loadParticipantes(int idcurso){
        Task<List<Participante>> particpanteTask = new Task<List<Participante>>() {
            @Override
            protected List<Participante> call() throws Exception {
                return daoParticipante.obtenerParticipantesPorCurso(idcurso);
            }
        };

        particpanteTask.setOnSucceeded(event -> {
            try {
                curso.getParticipantes().setAll(particpanteTask.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        new Thread(particpanteTask).start();
    }

}
