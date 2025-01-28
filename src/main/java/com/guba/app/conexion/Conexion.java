package com.guba.app.conexion;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;


public class Conexion {

    private Connection connection;

    private static Conexion conexion;

    public Conexion(){
        String rutaBD = Config.getConif().obtenerConfiguracion("01 RUTA BD");
        String url = "jdbc:sqlite:" + rutaBD;
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Conexión a la base de datos SQLite establecida.");
        } catch (SQLException e) {
            System.out.println("Error al intentar conectar con la base de datos: " + e.getMessage());
            //JOptionPane.showMessageDialog(null, "No se pudo establecer la conexión a la base de datos SQLite. Error: " + e.getMessage(), "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static Connection getConection(){
        if (conexion == null){
            conexion = new Conexion();
        }
        return conexion.getConnection();
    }

    public Connection getConnection() {
        return connection;
    }
}
