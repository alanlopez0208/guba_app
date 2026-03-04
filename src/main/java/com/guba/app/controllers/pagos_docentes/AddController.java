package com.guba.app.controllers.pagos_docentes;

import com.guba.app.data.dao.DAOMaestro;
import com.guba.app.domain.models.PagoDocente;
import com.guba.app.utils.*;
import com.guba.app.domain.models.Maestro;
import com.guba.app.presentation.dialogs.DialogConfirmacion;
import com.guba.app.presentation.utils.ComboCell;
import javafx.beans.binding.Bindings;
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


public class AddController extends BaseController<PagoDocente> implements Initializable, Loadable<PagoDocente> {

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
    private ComboBox<Maestro> comboMaestros;
    @FXML
    private DatePicker dateFeha;
    private PagoDocente pagoDocente;
    private DAOMaestro daoMaestro = new DAOMaestro();


    public AddController(Mediador<PagoDocente> mediador, ObjectProperty<Estado> estadoProperty, ObjectProperty<Paginas> paginasProperty) {
        super("/pago_docentes/Add", mediador, estadoProperty, paginasProperty);
        // Inicializar objeto para evitar NPE si el usuario interactúa con los campos antes de loadData
        pagoDocente = new PagoDocente();

        txtCantidad.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1){
                    containerMoney.getStyleClass().add("select");
                }else{
                    containerMoney.getStyleClass().remove("select");
                }
            }
        });

        // Configurar TextFormatter para aceptar solo números y punto decimal (máx 2 decimales)
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            // Permitir cadena vacía
            if (newText.isEmpty()) {
                return change;
            }
            // Rechazar comas y cualquier otro caracter distinto de dígito y punto
            if (newText.contains(",")) {
                return null;
            }
            // Validar formato: dígitos opcionales, opcional punto y hasta 2 decimales
            if (newText.matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        txtCantidad.setTextFormatter(textFormatter);

        // Formatear a 2 decimales al perder el foco
        txtCantidad.focusedProperty().addListener((obs, oldV, newV) -> {
            if (!newV) { // perdió foco
                String text = txtCantidad.getText();
                if (text != null && !text.isBlank()) {
                    try {
                        // Usar BigDecimal para precisión y formateo
                        BigDecimal value = new BigDecimal(text);
                        value = value.setScale(2, RoundingMode.HALF_UP);
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                        DecimalFormat df = new DecimalFormat("#0.00", symbols);
                        txtCantidad.setText(df.format(value));
                        pagoDocente.setCantidad(df.format(value));
                    } catch (NumberFormatException ex) {
                        // Si no es un número válido, limpiar o dejar como está
                        txtCantidad.setText("");
                        pagoDocente.setCantidad("");
                    }
                } else {
                    pagoDocente.setCantidad("");
                }
            }
        });

        comboMaestros.setCellFactory(new Callback<ListView<Maestro>, ListCell<Maestro>>() {
            @Override
            public ListCell<Maestro> call(ListView<Maestro> carreraListView) {
                return new ComboCell<Maestro>();
            }
        });
        txtCantidad.textProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null) {
                pagoDocente.setCantidad(t1);
            }
        });
        txtConcepto.textProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null) {
                pagoDocente.setConcepto(t1);
            }
        });
        txtFactura.textProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null) {
                pagoDocente.setFactura(t1);
            }
        });
        comboMaestros.setButtonCell(new ComboCell<>());
        comboMaestros.setButtonCell(new ComboCell<>());
        backButton.setOnAction(this::regresarAPanel);
        btnGuardar.setOnAction(this::guardar);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    @FXML
    private void regresarAPanel(ActionEvent actionEvent) {
        pagoDocente = null;
        paginasProperty.set(Paginas.LIST);
    }

    @FXML
    private void guardar(ActionEvent event){
        if (mostrarConfirmacion()){
            // Validar cantidad: no vacía y coincide con patrón numérico con punto
            String cantidadText = txtCantidad.getText();
            if (cantidadText == null || cantidadText.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("La cantidad es obligatoria");
                alert.showAndWait();
                return;
            }
            // Asegurar que no contenga comas
            if (cantidadText.contains(",")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("La cantidad no puede contener comas, use punto decimal (ej. 3000.50)");
                alert.showAndWait();
                return;
            }

            // Intentar parsear y formatear de nuevo antes de guardar
            try {
                BigDecimal value = new BigDecimal(cantidadText.replaceAll("\\s+", ""));
                value = value.setScale(2, RoundingMode.HALF_UP);
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat df = new DecimalFormat("#0.00", symbols);
                String formatted = df.format(value);
                pagoDocente.setCantidad(formatted);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("La cantidad no es un número válido. Use el formato 3000.50");
                alert.showAndWait();
                return;
            }

            pagoDocente.setMaestro(comboMaestros.getValue());
            pagoDocente.setDate(dateFeha.getValue());
            boolean seAgrego = mediador.guardar(pagoDocente);
            if (seAgrego){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Se agrego Correctamente");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error al guardar contecta con soporte");
                alert.showAndWait();
            }
            cleanData();
            paginasProperty.set(Paginas.LIST);
        }
    }

    private void loadMaestrosAsync() {
        Utils.loadAsync(()->{
            comboMaestros.setDisable(true);
            return daoMaestro.getDocentes();
        },maestros -> {
            comboMaestros.getItems().setAll(maestros);
            comboMaestros.setDisable(false);
        } );
    }


    @Override
    public void loadData(PagoDocente data) {
        pagoDocente = data;
        loadMaestrosAsync();
        // Si viene un pago con cantidad, formatearlo en la vista
        if (pagoDocente != null && pagoDocente.getCantidad() != null && !pagoDocente.getCantidad().isBlank()) {
            txtCantidad.setText(pagoDocente.getCantidad());
        }
    }

    private boolean mostrarConfirmacion() {
        DialogConfirmacion dialogConfirmacion = new DialogConfirmacion("¿Estás seguro de añadir el pago: " +"?");
        return dialogConfirmacion.showAndWait().orElse(0) == 1;
    }

    @Override
    protected void cleanData() {
        pagoDocente = null;
        dateFeha.setValue(null);
        comboMaestros.setValue(null);
        txtCantidad.setText(null);
        txtConcepto.setText(null);
        txtFactura.setText(null);
        dateFeha.setValue(null);
    }
}
