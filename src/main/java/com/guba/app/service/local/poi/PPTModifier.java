package com.guba.app.service.local.poi;

import java.awt.Desktop;
import java.io.*;

import com.guba.app.conexion.Config;
import com.guba.app.models.Asesor;
import com.guba.app.models.Curso;
import com.guba.app.models.Participante;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

public class PPTModifier {

    private static final String URL_PRESENTACIONES = Config.getConif().obtenerConfiguracion("10 RUTA Cursos") + "\\";
    private static final String URL_ASESOR = URL_PRESENTACIONES + Presentacion.Asesor.getNombre();
    private static final String URL_PARTICIPANTE = URL_PRESENTACIONES + Presentacion.Participante.getNombre();

    public void modificarPpt(Curso curso, String path) {
        try {
            modificarPptAsesor(curso, path);
            modificarPptParticipantes(curso, path);
        } catch (IOException e) {
            System.err.println("Error al modificar las presentaciones: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void modificarPptAsesor(Curso curso, String path) throws IOException {
        String periodo = generarPeriodo(curso);
        String fechaRealizacion = curso.dateToStringLocale(curso.getDateRealizacion());

        try (FileInputStream fileInputStream = new FileInputStream(URL_ASESOR);
             XMLSlideShow xmlSlideShow = new XMLSlideShow(fileInputStream)) {

            XSLFSlide slide = xmlSlideShow.getSlides().get(0);
            remplazarTexto(slide, "name", curso.getAsesor().getNombre());
            remplazarTexto(slide, "curse", curso.getNombre());
            remplazarTexto(slide, "periodo", periodo);
            remplazarTexto(slide, "today", fechaRealizacion);

            String fileName = generarNombreArchivo(curso.getAsesor().getNombre(), "Asesor", fechaRealizacion);
            saveFile(xmlSlideShow, path + fileName);
        }
    }

    private void modificarPptParticipantes(Curso curso, String path) throws IOException {
        String periodo = generarPeriodo(curso);
        String fechaRealizacion = curso.dateToStringLocale(curso.getDateRealizacion());
        Asesor asesor = curso.getAsesor();

        for (Participante participante : curso.getParticipantes()) {
            try (FileInputStream inputStream = new FileInputStream(URL_PARTICIPANTE);
                 XMLSlideShow xmlSlideShow = new XMLSlideShow(inputStream)) {

                XSLFSlide slide = xmlSlideShow.getSlides().get(0);
                remplazarTexto(slide, "boss", asesor.getNombre());
                remplazarTexto(slide, "puest", asesor.getPuesto() != null ? asesor.getPuesto() : " ");
                remplazarTexto(slide, "place", asesor.getLugar() != null ? asesor.getLugar() : " ");
                remplazarTexto(slide, "person", participante.getNombre());
                remplazarTexto(slide, "name", curso.getNombre());
                remplazarTexto(slide, "hours", generarHoras(curso));
                remplazarTexto(slide, "periodo", periodo);
                remplazarTexto(slide, "today", fechaRealizacion);
                remplazarTexto(slide, "covid", curso.getModalidad());

                String fileName = generarNombreArchivo(participante.getNombre(), "Part", fechaRealizacion);
                saveFile(xmlSlideShow, path + fileName);
            }
        }
    }

    private String generarPeriodo(Curso curso) {
        String fechaInicio = curso.dateToStringLocale(curso.getDateInicio());
        if (curso.getDateFin() == null) {
            return ", el día " + fechaInicio;
        } else {
            String fechaFinal = curso.dateToStringLocale(curso.getDateFin());
            return ", el periodo comprendido del " + fechaInicio + " al " + fechaFinal;
        }
    }

    private String generarHoras(Curso curso){
        int duracionHoras = curso.getDuracionHoras();
        if (duracionHoras == 0){
            return "";
        }
        return " con una duracion de " + duracionHoras + (duracionHoras > 1 ? "horas" : "hora");
    }

    private String generarNombreArchivo(String nombre, String tipo, String fechaRealizacion) {
        return nombre.split(" ")[0] + "_" + tipo + "_Diploma_" + fechaRealizacion.replace(" ", "").replace("de", "_") + ".pptx";
    }

    private void remplazarTexto(XSLFSlide slide, String placeholder, String newValue) {
        for (XSLFShape shape : slide.getShapes()) {
            if (shape instanceof XSLFTextShape) {
                XSLFTextShape textShape = (XSLFTextShape) shape;
                textShape.getTextParagraphs().forEach(paragraph ->
                        paragraph.getTextRuns().forEach(run -> {
                            String text = run.getRawText();
                            if (text != null && text.contains(placeholder)) {
                                run.setText(text.replace(placeholder, newValue));
                            }
                        })
                );
            }
        }
    }

    private void saveFile(XMLSlideShow xmlSlideShow, String path) {
        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            xmlSlideShow.write(outputStream);
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo en: " + path);
            throw new RuntimeException(e);
        }
    }

    private void openModifiedDocument(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                System.err.println("El archivo no existe: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Error al abrir el archivo: " + e.getMessage());
        }
    }
}
