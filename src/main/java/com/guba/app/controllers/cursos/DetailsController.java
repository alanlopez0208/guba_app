package com.guba.app.controllers.cursos;

import com.dlsc.gemsfx.CalendarPicker;
import com.guba.app.utils.*;
import com.guba.app.data.dao.DAOAseor;
import com.guba.app.data.dao.DAOParticipante;
import com.guba.app.domain.models.Curso;
import com.guba.app.domain.models.Participante;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailsController extends BaseController<Curso> implements Loadable<Curso> {

    @FXML
    private Button backButton;
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
    private ListView<Participante> listviewParticipanetes;
    private Curso curso;
    private DAOAseor daoAseor = new DAOAseor();
    private DAOParticipante daoParticipante = new DAOParticipante();


    public DetailsController( Mediador<Curso> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super("/cursos/Details", mediador, estadoProperty, paginasProperty);
        setListView();
        setComboBox();
        backButton.setOnAction(this::backPanel);
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void backPanel(ActionEvent event) {
       paginasProperty.set(Paginas.LIST);
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
        loadAsesorAsync(curso.getIdCurso());
        loadParticipantes(curso.getIdCurso());
    }

    private void setListView(){
        listviewParticipanetes.setSelectionModel(null);
        listviewParticipanetes.setCellFactory(new Callback<>() {
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

    }

    private void setComboBox(){
        comboModalidades.getItems().setAll("Presencial","Virtual","Mixta");
    }


    public void loadAsesorAsync(int idcurso){
        Utils.loadAsync(() -> daoAseor.obtenerAsesorPorCurso(idcurso), asesor -> {
            this.impartidorNombreField.setText(asesor.getNombre());
            this.impartdiorLugarField.setText(asesor.getLugar());
            this.impartidorPuestoFlied.setText(asesor.getPuesto());
        });
    }


    public void loadParticipantes(int idcurso){
        Utils.loadAsync(() -> daoParticipante.obtenerParticipantesPorCurso(idcurso), participantes -> {
            curso.getParticipantes().setAll(participantes);
            listviewParticipanetes.getItems().setAll(curso.getParticipantes());
        });
    }

    @Override
    protected void cleanData() {
        this.curso = null;
        listviewParticipanetes.getItems().clear();
    }
}
