package com.guba.app.utils;

import javafx.concurrent.Task;

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

}
