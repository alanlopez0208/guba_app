package com.guba.app.data.local.database.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    public static Connection getConnection() {
        Connection connection = null;
        String rutaBD = Config.getConif().obtenerConfiguracion("01 RUTA BD");
        String url = "jdbc:sqlite:" + rutaBD;
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Conexión a la base de datos SQLite establecida.");
        } catch (SQLException e) {
            System.out.println("Error al intentar conectar con la base de datos: " + e.getMessage());
        }
        return connection;
    }
}
