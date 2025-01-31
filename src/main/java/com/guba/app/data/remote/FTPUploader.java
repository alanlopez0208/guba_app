package com.guba.app.data.remote;

import com.guba.app.data.local.database.conexion.Config;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class FTPUploader {

    private static final Logger LOGGER = Logger.getLogger(FTPUploader.class.getName());

    // Configuración del servidor FTP
    private String ftpServer = "ftp.gubaescolares.com";
    private String user = "subir@gubaescolares.com";
    private String password = "p_WyU)p}&shG";
    private String remoteDir = "";
    private final String rutaBD = Config.getConif().obtenerConfiguracion("01 RUTA BD");
    private File localFile = new File(rutaBD);
    private boolean useBinary = true;


    public void uploadFile() {
        FTPClient ftpClient = new FTPClient();
        boolean success = false;
        try {

            ftpClient.connect(ftpServer);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                LOGGER.log(Level.SEVERE, "Connection failed. Server reply code: " + replyCode);
                JOptionPane.showMessageDialog(null, "Error al conectar al servidor FTP. Código de respuesta: " + replyCode, "Error de Conexión", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!ftpClient.login(user, password)) {
                LOGGER.log(Level.SEVERE, "Login failed.");
                JOptionPane.showMessageDialog(null, "Error al iniciar sesión en el servidor FTP.", "Error de Inicio de Sesión", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (useBinary) {
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            } else {
                ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
            }

            // Cambiar a modo pasivo
            ftpClient.enterLocalPassiveMode();

            // Cambiar al directorio remoto
            if (!ftpClient.changeWorkingDirectory(remoteDir)) {
                LOGGER.log(Level.SEVERE, "Failed to change working directory to: " + remoteDir);
                JOptionPane.showMessageDialog(null, "No se pudo cambiar al directorio remoto: " + remoteDir, "Error de Directorio", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Subir el archivo
            try (FileInputStream inputStream = new FileInputStream(localFile)) {
                String remoteFileName = localFile.getName();
                boolean done = ftpClient.storeFile(remoteFileName, inputStream);
                if (done) {
                    LOGGER.log(Level.INFO, "File uploaded successfully: " + remoteFileName);
                    success = true;
                } else {
                    LOGGER.log(Level.SEVERE, "File upload failed: " + remoteFileName);
                }
            }

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error occurred during FTP operation.", ex);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Error closing FTP connection.", ex);
            }

            // Mostrar mensaje al usuario sobre el resultado de la operación
            if (success) {
                JOptionPane.showMessageDialog(null, "Archivo subido con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "La subida del archivo falló.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
