package com.guba.app.data.local.database.conexion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {
    
    private static final String CONFIG_FILE_PATH = "C:\\Guba\\Configuraciones.txt";

    private Map<String, String> configuraciones = new HashMap<>();

    private static Config config;

    private Config() {
        cargarConfiguraciones();
    }

    private void cargarConfiguraciones() {
        try (FileReader archivo = new FileReader(CONFIG_FILE_PATH);
             BufferedReader lector = new BufferedReader(archivo)) {
             
            String linea;
            while ((linea = lector.readLine()) != null) {
                int idx = linea.indexOf("=");
                if (idx != -1) {
                    String clave = linea.substring(0, idx).trim();
                    String valor = linea.substring(idx + 1).trim();
                    configuraciones.put(clave, valor);
                } else {
                    System.out.println("Formato de línea incorrecto: " + linea);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al intentar leer el archivo de configuración: " + e.getMessage());
        }
    }


    public static Config getConif(){
        if (config == null){
            config = new Config();
        }
        return config;
    }

    public String obtenerConfiguracion(String clave) {
        return configuraciones.get(clave);
    }

    /*public static void main(String[] args) {
        Config config = new Config();
        String rutaBD = config.obtenerConfiguracion("01 RUTA BD");
        String rutaCV = config.obtenerConfiguracion("02 RUTA CV");
        String rutaImagenes = config.obtenerConfiguracion("03 RUTA IMAGENES");
        String rutaPlantillas = config.obtenerConfiguracion("04 RUTA PLANTILLAS");
        
        System.out.println("Ruta de la base de datos obtenida: " + rutaBD);
        System.out.println("Ruta del CV obtenida: " + rutaCV);
        System.out.println("Ruta de las imágenes obtenida: " + rutaImagenes);
        System.out.println("Ruta de las plantillas obtenida: " + rutaPlantillas);
    }*/
}
