package com.guba.app.presentation.componets;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.guba.app.presentation.utils.Constants.URL_COMPONENTS;

public class Sidebar extends VBox {

    @FXML
    VBox itemsContainer;

    private ObjectProperty<ItemButton> itemButtonObjectProperty;
    private OnItemPressed onItemPressed;

    public Sidebar(){
        try {
            URL url = this.getClass().getResource(URL_COMPONENTS+ "/Sidebar.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(Sidebar.this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initElements();
    }

    public void initElements(){
        itemButtonObjectProperty = new SimpleObjectProperty<>();
        createItem("mdi-home", "Inicio");
        createItem("mdi-account", "Estudiantes");
        createItem("mdi-projector-screen","Cursos");
        createItem("mdi-account-box", "Maestros");
        createItem("mdi-account-multiple", "Grupos");
        createItem("mdi-book-open","Materias");
        createItem("mdi-book-open-page-variant", "Carreras");
        createItem("mdi-account-box", "Personal");
        createItem("mdi-cash-usd", "Pago Alumnos");
        createItem("mdi-cash-usd","Pago Maestros");
        createItem("mdi-cloud","Sistema Web");
        createItem("mdi-settings","Configuracion");

        itemButtonObjectProperty.setValue((ItemButton)itemsContainer.getChildren().get(0));
        itemButtonObjectProperty.addListener((observableValue, oldValue, newValue) -> {
            int index = newValue.getIndex();
            int oldIndex = oldValue.getIndex();
            onItemPressed.onItemPressed(index, oldIndex);
        });
    }

    public void setOnItemPressed(OnItemPressed onItemPressed) {
        this.onItemPressed = onItemPressed;
    }


    private void createItem(String iconCode, String label){
        ItemButton it1 = new ItemButton(itemButtonObjectProperty);
        it1.setIcono(new FontIcon(iconCode));
        it1.getIcono().setIconSize(20);
        it1.setTextButton(label);
        it1.setIndex(itemsContainer.getChildren().size());
        itemsContainer.getChildren().add(it1);
    }
}
