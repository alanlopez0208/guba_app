package com.guba.app.utils;

public enum Modulo {
    INICIO("/inicio/Inicio"),
    ESTUDIANTES("/estudiantes/EstudiantesMain"),
    CURSOS("/cursos/Main");
    //MAESTROS("/maestros/Main"),
    //GRUPOS("/grupos/Main"),
    //MATERIAS("/materias/Main"),
    //CARRERAS("/carreras/Main"),
    //PERSONAL("/personal/Main"),
    //PAGO_ALUMNOS("/pago_alumnos/Main"),
    //PAGO_DOCENTES("/pago_docentes/Main"),
    //WEB("/web/Web"),
    //CONFIGURACION("/configuracion/Configuracion");

    private final String path;

    Modulo(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
