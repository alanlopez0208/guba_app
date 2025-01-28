package com.guba.app.service.local.poi;

public enum Presentacion {
    Asesor("asesor.pptx"),
    Participante("participante.pptx");

    private String nombre;
    Presentacion(String nombre){
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
