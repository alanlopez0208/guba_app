package com.guba.app.utils;

import com.guba.app.data.local.database.conexion.Config;
import javafx.concurrent.Task;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class Utils {
    public static <T> void  loadAsync(Callable<T> callable, Consumer<T> consumer){
        Task<T> task = new Task<T>() {
            @Override
            protected T call() throws Exception {
                return callable.call();
            }
        };
        task.setOnSucceeded(event -> {
            try {
                consumer.accept(task.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        task.setOnFailed(event -> {
            System.out.println("Ocurrio un error: "+ task.getException().getMessage());
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public static String guardarFoto(String ruta, BufferedImage bufferedImage) {
        try {
            File outputFile = new File(ruta);
            ImageIO.write(bufferedImage, "jpg", outputFile);
            return  outputFile.getAbsolutePath();
        } catch (IOException ex) {
            return null;
        }
    }

    public static LocalDate parseLocalDate(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d/M/yyyy").withLocale(Locale.getDefault());
            return LocalDate.parse(input, fmt);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}
