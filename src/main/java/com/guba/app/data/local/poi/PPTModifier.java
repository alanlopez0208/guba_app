package com.guba.app.data.local.poi;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import com.guba.app.data.local.database.conexion.Config;
import com.guba.app.domain.models.Asesor;
import com.guba.app.domain.models.Curso;
import com.guba.app.domain.models.Participante;
import com.guba.app.domain.models.Tema;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.TableCell;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.*;

import javax.imageio.ImageIO;

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

            for (int i = 0; i < slide.getShapes().size(); i++) {
                System.out.println(i+"->"+slide.getShapes().get(i).getShapeName());
            }
            remplazarTexto(slide, 5, xslfTextRuns -> {
                XSLFTextRun textRun = xslfTextRuns.addNewTextRun();
                textRun.setText(String.format("A: %s", curso.getAsesor().getNombre()));
                textRun.setFontSize(28.0);
                textRun.setBold(true);
                textRun.setUnderlined(true);
                textRun.setFontFamily("Times New Roman");
            });
            remplazarTexto(slide,6,xslfTextRuns -> {
                XSLFTextRun textRun = xslfTextRuns.addNewTextRun();
                textRun.setText(String.format("Por haber moderado el curso “%s” %s", curso.getNombre(), generarPeridoFechas(curso.getDateInicio(), curso.getDateFin())));
                textRun.setFontSize(18.0);
                textRun.setFontFamily("Times New Roman");

            });
            remplazarTexto(slide,7,xslfTextRuns -> {
                XSLFTextRun textRun = xslfTextRuns.addNewTextRun();
                textRun.setText(String.format("Victoria de Durango, Dgo., a %s", fechaRealizacion));
                textRun.setFontSize(18.0);
                textRun.setFontFamily("Times New Roman");
            });
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
                remplazarTexto(slide, 5, xslfTextRuns -> {
                    XSLFTextRun textRun = xslfTextRuns.addNewTextRun();
                    textRun.setBold(true);
                    String lugar = curso.getAsesor().getLugar();
                    textRun.setText("\n"+ (lugar != null ? lugar : " "));
                    textRun.setFontFamily("Times New Roman");
                });
                remplazarTexto(slide, 1, xslfTextRuns -> {
                    XSLFTextRun textRun = xslfTextRuns.addNewTextRun();
                    textRun.setText("A: ");
                    textRun.setFontSize(24.0);
                    textRun.setFontFamily("Tw Cen MT");

                    XSLFTextRun textRunName = xslfTextRuns.addNewTextRun();
                    textRunName.setText(participante.getNombre());
                    textRunName.setFontSize(18.0);
                    textRunName.setBold(true);
                    textRunName.setFontFamily("Tw Cen MT");

                });
                remplazarTexto(slide,6, xslfTextRuns -> {
                    XSLFTextRun textRun = xslfTextRuns.addNewTextRun();
                    textRun.setText(String.format("Por haber acreditado el Curso “%s”. \n" +
                            "Modalidad: %s,%s%s",curso.getNombre(), curso.getModalidad().toLowerCase() ,generarHoras(curso), periodo));
                    textRun.setFontSize(12.0);
                    textRun.setFontFamily("Times New Roman");
                });

                remplazarTexto(slide,7, xslfTextRuns -> {
                    XSLFTextRun textRun = xslfTextRuns.addNewTextRun();
                    textRun.setText(String.format("Victoria de Durango, Dgo., a %s", fechaRealizacion));
                    textRun.setFontSize(12.0);
                    textRun.setBold(true);
                    textRun.setFontFamily("Times New Roman");
                });
                remplazarTexto(slide,11, xslfTextRuns -> {
                    XSLFTextRun textName = xslfTextRuns.addNewTextRun();
                    textName.setText(asesor.getNombre()+ " ");
                    textName.setFontSize(13.0);
                    textName.setBold(true);
                    textName.setFontFamily("Goudy Old Style");

                    xslfTextRuns.addLineBreak();
                    XSLFTextRun textPuest = xslfTextRuns.addNewTextRun();
                    textPuest.setText(asesor.getPuesto() != null ? asesor.getPuesto() : "");
                    textPuest.setFontSize(10.5);
                    textPuest.setFontFamily("Times New Roman");

                    xslfTextRuns.addLineBreak();
                    XSLFTextRun texLugar = xslfTextRuns.addNewTextRun();
                    texLugar.setText(asesor.getLugar() != null ? asesor.getLugar() : " ");
                    texLugar.setFontSize(9.0);
                    texLugar.setFontFamily("Times New Roman");
                });
                insertImage(xmlSlideShow, slide, curso.getImage());

                XSLFSlide slide2 = xmlSlideShow.getSlides().get(1);
                createContentTable((XSLFTable) slide2.getShapes().get(5), curso);
                createContentSummary((XSLFTextShape) slide2.getShapes().get(1),curso.getDuracionHoras(),generarPeridoFechas(curso.getDateInicio(), curso.getDateFin()));
                String fileName = generarNombreArchivo(participante.getNombre(), "Part", fechaRealizacion);
                saveFile(xmlSlideShow, path + fileName);
            }
        }
    }


    private String generarPeriodo(Curso curso) {
        return Optional.ofNullable(curso.getDateFin()).map(dateFin -> {
            return ", impartido durante el periodo comprendido" + generarPeridoFechas(curso.getDateInicio(), curso.getDateFin());
        }).orElse(String.format(", %s", generarPeridoFechas(curso.getDateInicio(), curso.getDateFin())));
    }


    private String generarPeridoFechas(LocalDate inicio, LocalDate fin) {
        String mesInicio = inicio.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("es","MX"));
        int diaInicio = inicio.getDayOfMonth();
        if (fin == null) {
            return "el día "+ diaInicio +" de " + mesInicio+ " de "+inicio.getYear() + ".";
        } else {
            DateTimeFormatter formatterLocale = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy ", new Locale.Builder().setLanguage("es").setRegion("MX").build());
            String fechaFinal = fin.format(formatterLocale);
            return " del "+ diaInicio + " de " + mesInicio + " al " + fechaFinal +".";
        }
    }

    private String generarHoras(Curso curso){
        int duracionHoras = curso.getDuracionHoras();
        if (duracionHoras == 0){
            return "";
        }
        return " con una duración de " + duracionHoras + (duracionHoras > 1 ? " horas" : " hora");
    }

    private String generarNombreArchivo(String nombre, String tipo, String fechaRealizacion) {
        return nombre.split(" ")[0] + "_" + tipo + "_Diploma_" + fechaRealizacion.replace(" ", "").replace("de", "_") + ".pptx";
    }

    private void remplazarTexto(XSLFSlide slide, int shape, Consumer<XSLFTextParagraph> paragraphConsumer ) {
        XSLFTextShape textShape = (XSLFTextShape) slide.getShapes().get(shape);
        var paragraph = textShape.getTextParagraphs().get(0);
        paragraphConsumer.accept(paragraph);
    }


    private void insertImage(XMLSlideShow slideShow, XSLFSlide slide, String path) throws IOException {
        if (path == null) return;
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(path));
        var idx  = slideShow.addPicture(bytes, PictureData.PictureType.PNG);

        XSLFPictureShape pic = slide.createPicture(idx);

        BufferedImage img = ImageIO.read(new FileInputStream(path));
        double anchoOriginal = img.getWidth();
        double alturaOrginal = img.getHeight();
        double alturaDeseada = 80;

        double anchoDeseado = (anchoOriginal / alturaOrginal) * alturaDeseada;
        pic.setAnchor(new Rectangle(10,7,(int) anchoDeseado,(int) alturaDeseada));
    }

    private void createContentTable(XSLFTable table,Curso curso) {
        if (curso.getTemas().isEmpty()) {
            return;
        }

        for (int i = 0; i < curso.getTemas().size(); i++) {
            Tema tema = curso.getTemas().get(i);
            addRow(table,tema.getNombre(), tema.getDuracionHoras()+" hs.", Integer.toString(curso.getDuracionHoras()) +" hs. ");
        }
        table.mergeCells(1,table.getRows().size()-1,2,2);
    }


    private void addRow(XSLFTable table,String... topics){
        var row = table.addRow();
        for (String topic : topics) {
            addCell(row, topic);
        }
    }

    private void addCell(XSLFTableRow row, String text) {
        var cell = row.addCell();

        setBorderCell(cell, TableCell.BorderEdge.top);
        setBorderCell(cell, TableCell.BorderEdge.bottom);
        setBorderCell(cell, TableCell.BorderEdge.left);
        setBorderCell(cell, TableCell.BorderEdge.right);

        cell.setFillColor(Color.WHITE);

        var parh = cell.addNewTextParagraph();
        parh.setTextAlign(TextParagraph.TextAlign.CENTER);
        XSLFTextRun textRun = parh.addNewTextRun();
        textRun.setText(text);
        textRun.setFontSize(9.0);
        textRun.setFontColor(Color.BLACK);
        textRun.setBold(true);
        textRun.setFontFamily("Times New Roman");
    }


    private void setBorderCell(XSLFTableCell cell , TableCell.BorderEdge borderEdge){
        cell.setBorderWidth(borderEdge, 1);
        cell.setBorderColor(borderEdge, Color.BLACK);
    }


    private void createContentSummary(XSLFTextShape textShape, int duracionHoras, String periodo) {
        var parh = textShape.getTextParagraphs().get(0);
        parh.setTextAlign(TextParagraph.TextAlign.LEFT);
        XSLFTextRun textRun = parh.addNewTextRun();
        textRun.setText(String.format("Núm de Folio:\nDuración: %s hs. \nPeriodo: %s"
                ,duracionHoras, periodo
                ));
        textRun.setFontSize(10.0);
        textRun.setFontColor(Color.BLACK);
        textRun.setFontFamily("Times New Roman");
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
