package com.guba.app.utils;


public interface Mediador<T> {
    boolean actualizar(T t);
    boolean eliminar(T t);
    boolean guardar(T t);
    T ver(T t);
    void loadContent(Paginas nueva, T data);
    void loadBD();
}
