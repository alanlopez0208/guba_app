package com.guba.app.service.local.poi;

import com.guba.app.conexion.Config;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;

public class FileOpener {

    public ArrayList<String> loadFilesIntoComboBox() {
        String rutaBD = Config.getConif().obtenerConfiguracion("06 RUTA Documentos");
        String folderPath = rutaBD;
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();

        ArrayList<String> documentos = new ArrayList<>();

        if (listOfFiles != null) {
            Arrays.sort(listOfFiles, (file1, file2) -> file1.getName().compareToIgnoreCase(file2.getName()));

            for (File file : listOfFiles) {
                if (file.isFile() && (file.getName().toLowerCase().endsWith(".docx") || file.getName().toLowerCase().endsWith(".pptx"))) {
                    documentos.add(file.getName());
                }
            }

        } else {
            JOptionPane.showMessageDialog(null, "La carpeta no existe o no contiene archivos.");
        }
        return documentos;
    }

}
