package com.guba.app.controllers.pago_alumnos;

import com.guba.app.data.dao.DAOAlumno;
import com.guba.app.data.dao.DAOPagoAlumnos;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Estudiante;
import com.guba.app.domain.models.PagoAlumno;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.guba.app.presentation.utils.ComboCell;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;


public class AddController extends BaseController<PagoAlumno> implements Loadable<PagoAlumno> {
    @FXML
    private Button backButton;
    @FXML
    private Button btnGuardar;
    @FXML
    private HBox containerMoney;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtConcepto;
    @FXML
    private TextField txtFactura;
    @FXML
    private ComboBox<Estudiante> comboAlumnos;
    @FXML
    private DatePicker dateFeha;
    private PagoAlumno pagoAlumno;

    private DAOAlumno daoAlumno = new DAOAlumno();

    public AddController(Mediador<PagoAlumno> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super("/pago_alumnos/Add", mediador, estadoProperty, paginasProperty);

        // Inicializar para evitar NPE en los listeners
        pagoAlumno = new PagoAlumno();

        txtCantidad.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean focused) {
                if (focused) {
                    containerMoney.getStyleClass().add("select");
                } else {
                    containerMoney.getStyleClass().remove("select");
                }
            }
        });

        // ── TextFormatter: solo dígitos y un punto decimal (máx 2 decimales) ──
        // Rechaza comas, letras y cualquier símbolo que no sea dígito o punto.
        // Ejemplo válido: 3000.50   Ejemplo inválido: 3,000.50
        UnaryOperator<TextFormatter.Change> filtroNumerico = change -> {
            String nuevoTexto = change.getControlNewText();
            if (nuevoTexto.isEmpty()) return change;                  // permitir borrar todo
            if (nuevoTexto.contains(",")) return null;                // rechazar comas
            if (nuevoTexto.matches("\\d*(\\.\\d{0,2})?")) return change; // solo dígitos + punto + 2 dec
            return null;                                              // rechazar cualquier otro carácter
        };
        txtCantidad.setTextFormatter(new TextFormatter<>(filtroNumerico));

        // ── Formatear a 2 decimales al perder el foco ──
        txtCantidad.focusedProperty().addListener((obs, oldFocused, newFocused) -> {
            if (!newFocused) {
                String texto = txtCantidad.getText();
                if (texto != null && !texto.isBlank()) {
                    try {
                        BigDecimal valor = new BigDecimal(texto).setScale(2, RoundingMode.HALF_UP);
                        DecimalFormat df = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.US));
                        String formateado = df.format(valor);
                        txtCantidad.setText(formateado);
                        pagoAlumno.setCantidad(formateado);
                    } catch (NumberFormatException e) {
                        txtCantidad.setText("");
                        pagoAlumno.setCantidad("");
                    }
                } else {
                    pagoAlumno.setCantidad("");
                }
            }
        });

        comboAlumnos.setCellFactory(new Callback<ListView<Estudiante>, ListCell<Estudiante>>() {
            @Override
            public ListCell<Estudiante> call(ListView<Estudiante> carreraListView) {
                return new ComboCell<Estudiante>();
            }
        });
        txtCantidad.textProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null) {
                pagoAlumno.setCantidad(t1);
            }
        });
        txtConcepto.textProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null) {
                pagoAlumno.setConcepto(t1);
            }
        });
        txtFactura.textProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null) {
                pagoAlumno.setFactura(t1);
            }
        });
        comboAlumnos.setButtonCell(new ComboCell<>());
        backButton.setOnAction(this::regresarAPanel);
        btnGuardar.setOnAction(this::guardar);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void regresarAPanel(ActionEvent actionEvent) {
        cleanData();
        paginasProperty.set(Paginas.LIST);
    }

    private void guardar(ActionEvent event) {
        if (mostrarConfirmacion()) {

            // ── Validación de cantidad antes de guardar ──
            String cantidadTexto = txtCantidad.getText();
            if (cantidadTexto == null || cantidadTexto.isBlank()) {
                new Alert(Alert.AlertType.ERROR, "La cantidad es obligatoria.").showAndWait();
                return;
            }
            try {
                BigDecimal valor = new BigDecimal(cantidadTexto).setScale(2, RoundingMode.HALF_UP);
                DecimalFormat df = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.US));
                pagoAlumno.setCantidad(df.format(valor));
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR,
                        "La cantidad no es válida. Use punto decimal, por ejemplo: 3000.50").showAndWait();
                return;
            }

            pagoAlumno.setAlumno(comboAlumnos.getValue());
            pagoAlumno.setDate(dateFeha.getValue());
            boolean seAgrego = mediador.guardar(pagoAlumno);
            if (seAgrego) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Se agrego Correctamente");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error al guardar contacta a soporte");
                alert.showAndWait();
            }
            paginasProperty.set(Paginas.LIST);
            cleanData();
        }
    }

    private void loadAlumnosAsync() {
        Utils.loadAsync(()->{
            comboAlumnos.setDisable(true);
            return daoAlumno.getEstudiantes();
        }, estudiantes -> {
            comboAlumnos.getItems().setAll(estudiantes);
            comboAlumnos.setDisable(false);
        });
    }

    @Override
    public void loadData(PagoAlumno data) {
        pagoAlumno = data;
        loadAlumnosAsync();
    }

    private boolean mostrarConfirmacion() {
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estás seguro de añadir el pago: " +"?");
        return dialogConfirmacion.showAndWait().orElse(0) == 1;
    }

    @Override
    protected void cleanData() {
        pagoAlumno = null;
        txtCantidad.setText(null);
        txtConcepto.setText(null);
        txtFactura.setText(null);
        dateFeha.setValue(null);
    }
}
