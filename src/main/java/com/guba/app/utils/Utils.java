package com.guba.app.utils;

import com.guba.app.data.local.database.conexion.Config;
import javafx.concurrent.Task;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class Utils {
    public static <T> void loadAsync(Callable<T> callable, Consumer<T> consumer){
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
}
