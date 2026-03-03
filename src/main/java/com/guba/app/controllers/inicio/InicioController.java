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
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class InicioController implements Initializable {

    @FXML private Text txtResumen;
    @FXML private Text txtTotalEstudiantes;
    @FXML private Text txtTotalMaestros;
    @FXML private Text txtTotalCarreras;
    @FXML private Text txtTotalIngresos;

    @FXML private PieChart pieChartCarreras;
    @FXML private PieChart pieChartGenero;
    @FXML private BarChart<String, Number> barChartPagosMes;
    @FXML private LineChart<String, Number> lineChartIngresos;
    @FXML private BarChart<String, Number> barChartEstudiantesSemestre;
    @FXML private BarChart<String, Number> barChartPagosDocentes;

    private final DAOAlumno daoAlumno = new DAOAlumno();
    private final DAOMaestro daoMaestro = new DAOMaestro();
    private final DAOCarreras daoCarreras = new DAOCarreras();
    private final DAOPagoAlumnos daoPagoAlumnos = new DAOPagoAlumnos();
    private final DAOPagoDocentes daoPagoDocentes = new DAOPagoDocentes();

    private final DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarDatos();
    }

    private void cargarDatos() {
        try {
            List<Estudiante> estudiantes = daoAlumno.getEstudiantes();
            List<Maestro> maestros = daoMaestro.getDocentes();
            List<Carrera> carreras = daoCarreras.getAllCarreras();
            List<PagoAlumno> pagosAlumnos = daoPagoAlumnos.getPagos();
            List<PagoDocente> pagosDocentes = daoPagoDocentes.getPagos();

            actualizarEstadisticas(estudiantes, maestros, carreras, pagosAlumnos);

            cargarPieChartCarreras(estudiantes, carreras);
            cargarPieChartGenero(estudiantes);
            cargarBarChartPagosMes(pagosAlumnos);
            cargarLineChartIngresos(pagosAlumnos);
            cargarBarChartEstudiantesSemestre(estudiantes);
            cargarBarChartPagosDocentes(pagosDocentes);

        } catch (Exception e) {
            e.printStackTrace();
            txtResumen.setText("Error al cargar los datos del dashboard");
        }
    }

    private void actualizarEstadisticas(List<Estudiante> estudiantes, List<Maestro> maestros,
                                         List<Carrera> carreras, List<PagoAlumno> pagosAlumnos) {
        txtTotalEstudiantes.setText(String.valueOf(estudiantes.size()));
        txtTotalMaestros.setText(String.valueOf(maestros.size()));
        txtTotalCarreras.setText(String.valueOf(carreras.size()));

        double totalIngresos = pagosAlumnos.stream()
                .mapToDouble(p -> parseDoubleSeguro(p.getCantidad()))
                .sum();
        txtTotalIngresos.setText(currencyFormat.format(totalIngresos));

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

    private void cargarBarChartPagosMes(List<PagoAlumno> pagosAlumnos) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Pagos Alumnos");

        Map<Month, Double> pagosPorMes = agruparPagosPorMes(pagosAlumnos);

        // Ordenar por mes
        List<Month> mesesOrdenados = Arrays.asList(Month.values());
        for (Month mes : mesesOrdenados) {
            String nombreMes = mes.getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
            double cantidad = pagosPorMes.getOrDefault(mes, 0.0);
            series.getData().add(new XYChart.Data<>(nombreMes, cantidad));
        }

        barChartPagosMes.getData().clear();
        barChartPagosMes.getData().add(series);
    }

    private void cargarLineChartIngresos(List<PagoAlumno> pagosAlumnos) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ingresos Mensuales");

        Map<Month, Double> pagosPorMes = agruparPagosPorMes(pagosAlumnos);

        double acumulado = 0;
        for (Month mes : Month.values()) {
            String nombreMes = mes.getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
            acumulado += pagosPorMes.getOrDefault(mes, 0.0);
            series.getData().add(new XYChart.Data<>(nombreMes, acumulado));
        }

        lineChartIngresos.getData().clear();
        lineChartIngresos.getData().add(series);
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

    private void cargarBarChartPagosDocentes(List<PagoDocente> pagosDocentes) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Pagos Docentes");

        Map<Month, Double> pagosPorMes = new EnumMap<>(Month.class);

        for (PagoDocente pago : pagosDocentes) {
            try {
                LocalDate fecha = pago.getDate();
                if (fecha != null) {
                    Month mes = fecha.getMonth();
                    double cantidad = parseDoubleSeguro(pago.getCantidad());
                    pagosPorMes.merge(mes, cantidad, Double::sum);
                }
            } catch (Exception e) {
                // Ignorar pagos con fecha inválida
            }
        }

        for (Month mes : Month.values()) {
            String nombreMes = mes.getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
            double cantidad = pagosPorMes.getOrDefault(mes, 0.0);
            series.getData().add(new XYChart.Data<>(nombreMes, cantidad));
        }

        barChartPagosDocentes.getData().clear();
        barChartPagosDocentes.getData().add(series);
    }

    // Métodos auxiliares
    private Map<Month, Double> agruparPagosPorMes(List<PagoAlumno> pagos) {
        Map<Month, Double> pagosPorMes = new EnumMap<>(Month.class);

        for (PagoAlumno pago : pagos) {
            try {
                LocalDate fecha = pago.getDate();
                if (fecha != null) {
                    Month mes = fecha.getMonth();
                    double cantidad = parseDoubleSeguro(pago.getCantidad());
                    pagosPorMes.merge(mes, cantidad, Double::sum);
                }
            } catch (Exception e) {
                // Ignorar pagos con fecha inválida
            }
        }

        return pagosPorMes;
    }

    private double parseDoubleSeguro(String valor) {
        if (valor == null || valor.isEmpty()) {
            return 0.0;
        }
        try {
            // Remover símbolos de moneda y comas
            String valorLimpio = valor.replace("$", "").replace(",", "").trim();
            return Double.parseDouble(valorLimpio);
        } catch (NumberFormatException e) {
            return 0.0;
        }
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
