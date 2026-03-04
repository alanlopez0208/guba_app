package com.guba.app.controllers.inicio;

import com.guba.app.data.dao.*;
import com.guba.app.domain.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class InicioController implements Initializable {

    // Componentes de texto para estadísticas
    @FXML private Text txtResumen;
    @FXML private Text txtTotalEstudiantes;
    @FXML private Text txtTotalMaestros;
    @FXML private Text txtTotalCarreras;
    @FXML private Text txtTotalGrupos;

    // Charts
    @FXML private PieChart pieChartCarreras;
    @FXML private PieChart pieChartGenero;
    @FXML private BarChart<String, Number> barChartEstudiantesSemestre;
    @FXML private BarChart<String, Number> barChartEstudiantesGeneracion;

    // DAOs
    private final DAOAlumno daoAlumno = new DAOAlumno();
    private final DAOMaestro daoMaestro = new DAOMaestro();
    private final DAOCarreras daoCarreras = new DAOCarreras();
    private final DAOGrupo daoGrupo = new DAOGrupo();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarDatos();
    }

    private void cargarDatos() {
        try {
            // Obtener datos de la base de datos
            List<Estudiante> estudiantes = daoAlumno.getEstudiantes();
            List<Maestro> maestros = daoMaestro.getDocentes();
            List<Carrera> carreras = daoCarreras.getAllCarreras();
            List<Grupo> grupos = daoGrupo.getGrupos();

            // Actualizar estadísticas generales
            actualizarEstadisticas(estudiantes, maestros, carreras, grupos);

            // Cargar gráficos
            cargarPieChartCarreras(estudiantes, carreras);
            cargarPieChartGenero(estudiantes);
            cargarBarChartEstudiantesSemestre(estudiantes);
            cargarBarChartEstudiantesGeneracion(estudiantes);

        } catch (Exception e) {
            e.printStackTrace();
            txtResumen.setText("Error al cargar los datos del dashboard");
        }
    }

    private void actualizarEstadisticas(List<Estudiante> estudiantes, List<Maestro> maestros,
                                         List<Carrera> carreras, List<Grupo> grupos) {
        txtTotalEstudiantes.setText(String.valueOf(estudiantes.size()));
        txtTotalMaestros.setText(String.valueOf(maestros.size()));
        txtTotalCarreras.setText(String.valueOf(carreras.size()));
        txtTotalGrupos.setText(String.valueOf(grupos.size()));

        txtResumen.setText("Resumen actualizado - " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private void cargarPieChartCarreras(List<Estudiante> estudiantes, List<Carrera> carreras) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Agrupar estudiantes por carrera
        Map<String, Long> estudiantesPorCarrera = estudiantes.stream()
                .filter(e -> e.getCarrera() != null && e.getCarrera().getNombre() != null)
                .collect(Collectors.groupingBy(
                        e -> e.getCarrera().getNombre(),
                        Collectors.counting()
                ));

        estudiantesPorCarrera.forEach((carrera, cantidad) ->
                pieChartData.add(new PieChart.Data(carrera + " (" + cantidad + ")", cantidad))
        );

        if (pieChartData.isEmpty()) {
            pieChartData.add(new PieChart.Data("Sin datos", 1));
        }

        pieChartCarreras.setData(pieChartData);
        pieChartCarreras.setLabelsVisible(true);
    }

    private void cargarPieChartGenero(List<Estudiante> estudiantes) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        Map<String, Long> estudiantesPorGenero = estudiantes.stream()
                .filter(e -> e.getSexo() != null && !e.getSexo().isEmpty())
                .collect(Collectors.groupingBy(
                        e -> normalizarGenero(e.getSexo()),
                        Collectors.counting()
                ));

        estudiantesPorGenero.forEach((genero, cantidad) ->
                pieChartData.add(new PieChart.Data(genero + " (" + cantidad + ")", cantidad))
        );

        if (pieChartData.isEmpty()) {
            pieChartData.add(new PieChart.Data("Sin datos", 1));
        }

        pieChartGenero.setData(pieChartData);
        pieChartGenero.setLabelsVisible(true);
    }

    private void cargarBarChartEstudiantesSemestre(List<Estudiante> estudiantes) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Estudiantes por Semestre");

        Map<String, Long> estudiantesPorSemestre = estudiantes.stream()
                .filter(e -> e.getSemestre() != null && !e.getSemestre().isEmpty())
                .collect(Collectors.groupingBy(
                        Estudiante::getSemestre,
                        Collectors.counting()
                ));

        // Ordenar semestres del 1 al 10
        for (int i = 1; i <= 10; i++) {
            String semestre = String.valueOf(i);
            long cantidad = estudiantesPorSemestre.getOrDefault(semestre, 0L);
            series.getData().add(new XYChart.Data<>("Sem " + semestre, cantidad));
        }

        barChartEstudiantesSemestre.getData().clear();
        barChartEstudiantesSemestre.getData().add(series);
    }

    private void cargarBarChartEstudiantesGeneracion(List<Estudiante> estudiantes) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Estudiantes por Generación");

        Map<String, Long> estudiantesPorGeneracion = estudiantes.stream()
                .filter(e -> e.getGeneracion() != null && !e.getGeneracion().isEmpty())
                .collect(Collectors.groupingBy(
                        Estudiante::getGeneracion,
                        Collectors.counting()
                ));

        // Ordenar por generación (de más reciente a más antigua)
        estudiantesPorGeneracion.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByKey().reversed())
                .limit(8) // Mostrar solo las últimas 8 generaciones
                .forEach(entry ->
                    series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()))
                );

        if (series.getData().isEmpty()) {
            series.getData().add(new XYChart.Data<>("Sin datos", 0));
        }

        barChartEstudiantesGeneracion.getData().clear();
        barChartEstudiantesGeneracion.getData().add(series);
    }

    private String normalizarGenero(String genero) {
        if (genero == null) return "No especificado";
        String generoLower = genero.toLowerCase().trim();
        if (generoLower.startsWith("m") || generoLower.equals("masculino") || generoLower.equals("hombre")) {
            return "Masculino";
        } else if (generoLower.startsWith("f") || generoLower.equals("femenino") || generoLower.equals("mujer")) {
            return "Femenino";
        }
        return "Otro";
    }
}
