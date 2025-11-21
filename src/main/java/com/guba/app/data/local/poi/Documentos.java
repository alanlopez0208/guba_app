package com.guba.app.data.local.poi;

public enum Documentos {
    Boleta("Boleta"),
    Certificado("Certificado"),
    Constancia("Constancia"),
    Diploma("Diploma"),
    Kardex("Kardex"),
    Acta("Acta");

    private String nombre;

    private Documentos(String nombre) {
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
