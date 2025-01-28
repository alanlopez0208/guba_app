package com.guba.app.presentation.dialogs;
import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.Optional;


public class DialogLogin extends Dialog<Boolean> {

    private TextField usernameField;
    private PasswordField passwordField;

    public DialogLogin() {
        setTitle("Login");
        setHeaderText("Ingrese su nombre de usuario y contraseña");

        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        usernameField = new TextField();
        usernameField.setPromptText("Usuario");

        passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");

        grid.add(new Label("Usuario:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Contraseña:"), 0, 1);
        grid.add(passwordField, 1, 1);

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return verificarCredenciales(usernameField.getText(), passwordField.getText());
            }
            return false;
        });
    }

    private Boolean verificarCredenciales(String usuario, String contrasena) {
        return "subir@gubaescolares.com".equals(usuario) && "p_WyU)p}&shG".equals(contrasena);
    }

}
