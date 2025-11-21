package com.guba.app.data.local.poi;

import com.guba.app.data.local.database.conexion.Config;
import com.guba.app.data.dao.*;
import com.guba.app.domain.models.*;
import com.tenpisoft.n2w.ValueConverters;
import javafx.scene.control.Alert;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xwpf.usermodel.*;
import java.awt.Desktop;
import java.io.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;

public class WordModifier {

    private final String  FOLDER_PATH = Config.getConif().obtenerConfiguracion("06 RUTA Documentos");

    public void modifyDocument(Documentos documento, Estudiante estudiante, String fecha) {
        String filePath = FOLDER_PATH + "\\" + documento.getNombre() + ".docx";
        try {
            FileInputStream fis = new FileInputStream(filePath);
            XWPFDocument document = new XWPFDocument(fis);
            switch (documento) {
                case Boleta -> rellenarBoleta(estudiante, fecha);
                case Certificado -> rellenarCertificado(estudiante, fecha);
                case Constancia -> rellenarConstancia(estudiante, fecha);
                case Kardex -> rellenarKardek(estudiante, fecha);
                case Diploma-> rellenarDiploma(estudiante, fecha);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void replaceInDocument(XWPFDocument document, String placeholder, String newValue) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replaceText(paragraph, placeholder, newValue);
            XmlCursor cursor = paragraph.getCTP().newCursor();
            cursor.selectPath("declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' .//*/w:txbxContent/w:p/w:r");

            List<XmlObject> ctrsintxtbx = new ArrayList<XmlObject>();
            while (cursor.hasNextSelection()) {
                cursor.toNextSelection();
                XmlObject obj = cursor.getObject();
                ctrsintxtbx.add(obj);
            }
            for (XmlObject obj : ctrsintxtbx) {
                try {
                    CTR ctr = CTR.Factory.parse(obj.xmlText());
                    //CTR ctr = CTR.Factory.parse(obj.newInputStream());
                    XWPFRun bufferrun = new XWPFRun(ctr, (IRunBody) paragraph);
                    String text = bufferrun.getText(0);
                    if (text != null && text.contains(placeholder)) {
                        text = text.replace(placeholder, newValue);
                        bufferrun.setText(text, 0);
                    }
                    obj.set(bufferrun.getCTR());
                } catch (XmlException ex) {
                    Logger.getLogger(WordModifier.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private static void replaceText(XWPFParagraph paragraph, String placeholder, String newValue) {
        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.getText(0);
            if (text != null && text.contains(placeholder)) {
                text = text.replace(placeholder, newValue);
                run.setText(text, 0);
            }
        }
    }

    private void rellenarBoleta( Estudiante modelo, String fecha) {
        DAOCalificiaciones opCalificaciones = new DAOCalificiaciones();
        List<Calificacion> calificaciones = opCalificaciones.obtenerCalificaciones(modelo.getId());
        DAOPeriodo opPeriodo = new DAOPeriodo();
        int totalMaterias = (int) calificaciones.stream().count();
        double promedio = calificaciones.stream().mapToDouble((cal) -> {
            cal.establecerPromedioFinal();
            return cal.getPromedioFinal() != null ? cal.getPromedioFinal() : 0.0;
        }).average().orElse(0.0);
        String promedioFormato = String.format("%.1f", promedio);
        int totalCredtios = calificaciones.stream().mapToInt((cal) -> {
            return Integer.valueOf(cal.getMateria().getCreditos());
        }).sum();
        String[] fechaArray = fecha.split("de");
        Collections.sort(calificaciones, (cal1, cal2) -> {
            Integer materia1 = Integer.parseInt(cal1.getMateria().getClave().substring(3));
            Integer materia2 = Integer.parseInt(cal2.getMateria().getClave().substring(3));
            return Integer.compare(materia1,materia2);
        });
        DAOAcuerdo opAcuerdo = new DAOAcuerdo();
        Acuerdo acuerdo = opAcuerdo.getAcuerdoByCarrera(modelo.getCarrera().getIdCarrera());
        try {
            String filePath = FOLDER_PATH + "\\" + Documentos.Boleta.getNombre() + ".docx";
            XWPFDocument d = new XWPFDocument(new FileInputStream(filePath));
            replaceInDocument(d, "name", modelo.toComboCell());
            replaceInDocument(d, "career", modelo.getCarrera().getNombre() + ". " + modelo.getCarrera().getModalidad());
            replaceInDocument(d, "ciclo-escolar", opPeriodo.getUltimoPeriodo().getNombre());
            replaceInDocument(d, "classmate", Integer.toString(totalMaterias));
            replaceInDocument(d, "average", promedioFormato);
            replaceInDocument(d, "credtis", Integer.toString(totalCredtios));
            replaceInDocument(d, "date", fecha);
            replaceInDocument(d, "dd", fechaArray[0]);
            replaceInDocument(d, "MM", fechaArray[1]);
            replaceInDocument(d, "yyyy", fechaArray[2]);
            replaceInDocument(d, "articulenumber", acuerdo.getNumero());
            replaceInDocument(d, "artdat", acuerdo.getFecha());
            replaceInDocument(d, "articulecc", acuerdo.getCc());
            calificaciones.forEach((cal) -> {
                rellenarFilaCalificacion(d, cal, "Calibri Light", "15", "325", "2", "188", "196", "217", "2", "2");
            });
            String generatedFolderPath = Config.getConif().obtenerConfiguracion("06 RUTA Documentos") + "\\Generados";
            new File(generatedFolderPath).mkdirs();
            String modifiedFilePath = generatedFolderPath + "\\" + modelo.getMatricula() + "_" + Documentos.Boleta.getNombre()+ ".docx";
            FileOutputStream fos = new FileOutputStream(modifiedFilePath);
            d.write(fos);
            fos.close();
            d.close();
            openModifiedDocument(modifiedFilePath);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void rellenarConstancia(Estudiante modelo, String fecha) {
        DAOPeriodo opPeriodo = new DAOPeriodo();
        String nombreCarrera = Arrays.stream(modelo.getCarrera().getNombre().split(" ")).map((palabra) -> {
            return palabra.substring(0, 1).toUpperCase() + palabra.substring(1).toLowerCase();
        }).collect(Collectors.joining(" "));
        try {
            String filePath = FOLDER_PATH + "\\" + Documentos.Constancia.getNombre() + ".docx";
            XWPFDocument d = new XWPFDocument(new FileInputStream(filePath));
            replaceInDocument(d, "name", modelo.toString());
            replaceInDocument(d, "career", "\"" + nombreCarrera + "\"");
            replaceInDocument(d, "modality", modelo.getCarrera().getModalidad());
            replaceInDocument(d, "tuition", modelo.getMatricula());
            replaceInDocument(d, "ciclo-inicio", opPeriodo.getUltimoPeriodo().getInicio());
            replaceInDocument(d, "date", fecha);
            String generatedFolderPath = Config.getConif().obtenerConfiguracion("06 RUTA Documentos") + "\\Generados";
            new File(generatedFolderPath).mkdirs();
            String modifiedFilePath = generatedFolderPath + "\\" + modelo.getMatricula() + "_" + "acta" + ".docx";
            FileOutputStream fos = new FileOutputStream(modifiedFilePath);
            d.write(fos);
            fos.close();
            d.close();
            openModifiedDocument(modifiedFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void rellenarCalificacionCertificado(XWPFTableRow row, Calificacion cal, String fontFamily, String... array) {
        XWPFTableCell celda4 = row.getCell(3);
        celda4.setWidth(array[3]);
        String resultado = String.format("%.1f", cal.getPromedioFinal() != null ? cal.getPromedioFinal() : 0.0);
        celda4.setText(resultado);

        XWPFTableCell celda5 = row.getCell(4);
        celda5.setWidth(array[4]);
        celda5.setText(numeroAString(resultado));

        XWPFTableCell celda6 = row.getCell(5);
        celda6.setWidth(array[5]);
        celda6.setText(cal.getFecha());

        XWPFTableCell celda7 = row.getCell(6);
        celda7.setWidth(array[6]);
        celda7.setText(cal.getModulo());

        XWPFTableCell celda8 = row.getCell(7);
        celda8.setWidth(array[7]);
        celda8.setText(cal.getTipo());

        for (int i = 3; i <= 7; i++) {
            XWPFTableCell celda = row.getCell(i);

            CTTcPr tcPr = celda.getCTTc().addNewTcPr();
            CTTcBorders borders = tcPr.addNewTcBorders();

            borders.addNewTop().setVal(STBorder.SINGLE);
            borders.addNewBottom().setVal(STBorder.SINGLE);
            borders.addNewLeft().setVal(STBorder.SINGLE);
            borders.addNewRight().setVal(STBorder.SINGLE);

            borders.getTop().setSz(BigInteger.valueOf(10));
            borders.getBottom().setSz(BigInteger.valueOf(10));
            borders.getLeft().setSz(BigInteger.valueOf(10));
            borders.getRight().setSz(BigInteger.valueOf(10));

            celda.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

            XWPFParagraph parrafo = celda.addParagraph();
            parrafo.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = parrafo.createRun();
            run.setFontFamily(fontFamily);

            run.setFontSize(10);
            run.setText(celda.getText());
            celda.removeParagraph(0);
        }

    }

    private void rellenarKardek( Estudiante modelo, String fecha) {
        DAOCalificiaciones opCalificaciones = new DAOCalificiaciones();
        DAOAcuerdo opAcuerdo = new DAOAcuerdo();
        Acuerdo acuerdo = opAcuerdo.getAcuerdoByCarrera(modelo.getCarrera().getIdCarrera());
        List<Calificacion> calificaciones = opCalificaciones.obtenerCalificaciones(modelo.getId());
        double promedio = calificaciones.stream().mapToDouble((Calificacion cal) -> {
            cal.establecerPromedioFinal();
            return cal.getPromedioFinal() != null ? cal.getPromedioFinal() : 0.0;
        }).average().orElse(0.0);
        String promedioFinal = String.format("%.1f", promedio);
        int totalCredtios = calificaciones.stream().mapToInt((cal) -> {
            return Integer.valueOf(cal.getMateria().getCreditos());
        }).sum();


        Collections.sort(calificaciones, (cal1, cal2) -> {
            Integer materia1 = Integer.parseInt(cal1.getMateria().getClave().substring(3));
            Integer materia2 = Integer.parseInt(cal2.getMateria().getClave().substring(3));
            return Integer.compare(materia1,materia2);
        });

        String genero = modelo.getSexo().equals("Hombre") ? "el alumno" : "la alumna";
        String[] fechaArray = fecha.split("de");

        LocalDate fechaNac = LocalDate.parse(modelo.getNacimiento(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String fechaSalidaNac = fechaNac.format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale.of("es", "ES")));


        try {
            String filePath = Config.getConif().obtenerConfiguracion("06 RUTA Documentos") + "\\" + Documentos.Kardex.getNombre() + ".docx";
            System.out.println(filePath);
            XWPFDocument document = new XWPFDocument(new FileInputStream(filePath));
            replaceInDocument(document, "peter", promedioFinal);
            replaceInDocument(document, "name", modelo.toString().toUpperCase());
            replaceInDocument(document, "date", fecha);
            replaceInDocument(document, "birth", fechaSalidaNac);
            replaceInDocument(document, "addres", modelo.getDireccion());
            replaceInDocument(document, "tuition", modelo.getMatricula().toLowerCase());
            replaceInDocument(document, "career", modelo.getCarrera().getNombre().toUpperCase() + ". " + modelo.getCarrera().getModalidad());
            replaceInDocument(document, "totalAsignaturas", Integer.toString(calificaciones.size()));
            replaceInDocument(document, "origin", modelo.getEscProcedencia());
            replaceInDocument(document, "phone", modelo.getNumCelular());
            replaceInDocument(document, "credits", Integer.toString(totalCredtios));
            replaceInDocument(document, "dd", fechaArray[0].trim());
            replaceInDocument(document, "MM", fechaArray[1].trim());
            replaceInDocument(document, "yyyy", fechaArray[2].trim());
            replaceInDocument(document, "articulenumber", acuerdo.getNumero());
            replaceInDocument(document, "artdat", acuerdo.getFecha());
            replaceInDocument(document, "articulecc", acuerdo.getCc());

            calificaciones.forEach((cal) -> {
                rellenarFilaCalificacion(document, cal, "Calibri Light", "15", "325", "2", "188", "196", "217", "2", "2");
            });


            try {
                String generatedFolderPath = Config.getConif().obtenerConfiguracion("06 RUTA Documentos") + "\\Generados";
                new File(generatedFolderPath).mkdirs();

                String modifiedFilePath = generatedFolderPath + "\\" + modelo.getMatricula() + "_" + Documentos.Kardex.getNombre() + ".docx";
                FileOutputStream fos = null;
                fos = new FileOutputStream(modifiedFilePath);
                document.write(fos);
                fos.close();
                document.close();
                openModifiedDocument(modifiedFilePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String crearTitulo(Estudiante modelo, Titulo titulo) {
        String rutaBD = Config.getConif().obtenerConfiguracion("06 RUTA Documentos");
        String folderPath = rutaBD;
        String filePath = folderPath + "\\Acta de Examen\\" + "acta" + ".docx";
        Acuerdo acuerdo = new DAOAcuerdo().getAcuerdoByCarrera(modelo.getCarrera().getIdCarrera());
        try {

            FileInputStream fis = new FileInputStream(filePath);
            XWPFDocument document = new XWPFDocument(fis);
            String genero = modelo.getSexo().equals("Hombre") ? "de el" : "de la";
            String carrera = Arrays.stream(modelo.getCarrera().toString().split(" ")).map(palabra -> palabra.substring(0, 1).toUpperCase() + palabra.substring(1).toLowerCase()).collect(Collectors.joining(" "));

            replaceInDocument(document, "acdate", acuerdo.getFecha());
            replaceInDocument(document,"accordance", acuerdo.getNumero());
            replaceInDocument(document,"accordance", acuerdo.getCc());
            replaceInDocument(document, "tezt", titulo.getTipoExamen());
            replaceInDocument(document, "hoursI", titulo.getHoraAplicacion());
            replaceInDocument(document, "date", titulo.getFechaAplicacion());
            replaceInDocument(document, "tesispresident", titulo.getPresidente());
            replaceInDocument(document, "tesissecretario", titulo.getSecretario());
            replaceInDocument(document, "tesisvocal", titulo.getVocal());
            replaceInDocument(document, "gener", genero);
            replaceInDocument(document, "name", modelo.toComboCell());
            replaceInDocument(document, "career", carrera);
            replaceInDocument(document, "thesis", titulo.getNombre());
            replaceInDocument(document, "duration", titulo.getDuracion());
            replaceInDocument(document, "fhoras", titulo.getHoraFinalizacion());
            replaceInDocument(document, "record", titulo.getActa());
            replaceInDocument(document, "book", titulo.getLibro());
            replaceInDocument(document, "sheet", titulo.getFoja());
            replaceInDocument(document, "articulenumber", acuerdo.getNumero());
            replaceInDocument(document, "artdat", acuerdo.getFecha());
            replaceInDocument(document, "articulecc", acuerdo.getCc());

            String generatedFolderPath = folderPath + "\\Generados";
            new File(generatedFolderPath).mkdirs();

            String modifiedFilePath = generatedFolderPath + "\\" + modelo.getMatricula() + "_" + "acta" + ".docx";
            FileOutputStream fos = new FileOutputStream(modifiedFilePath);
            document.write(fos);
            fos.close();
            document.close();

            System.out.println("Documento modificado guardado en: " + modifiedFilePath);

            openModifiedDocument(modifiedFilePath);
            return modifiedFilePath;
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return null;
    }

    private void rellenarCertificado(Estudiante modelo, String fecha) {
        DAOCalificiaciones opCalificaciones = new DAOCalificiaciones();
        DAOMaterias daoMaterias = new DAOMaterias();
        List<Calificacion> calificacions = opCalificaciones.obtenerCalificaciones(modelo.getId());
        List<Calificacion> califcacionesOrdenadas = calificacions.stream()
                .sorted(new Comparator<Calificacion>() {
                    @Override
                    public int compare(Calificacion o1, Calificacion o2) {
                        Integer materia1 = Integer.parseInt(o1.getMateria().getClave().substring(3));
                        Integer materia2 = Integer.parseInt(o2.getMateria().getClave().substring(3));

                        return Integer.compare(materia1,materia2);
                    }
                }).filter(c->{
                    Materia materia = daoMaterias.getMateria(c.getMateria().getId());
                    boolean esValido = materia.getCarreraModelo().getIdCarrera().equals(modelo.getCarrera().getIdCarrera());
                    return esValido;
                }).toList();

        double promedio = califcacionesOrdenadas.stream().mapToDouble((Calificacion cal) -> {
            cal.establecerPromedioFinal();
            return cal.getPromedioFinal() != null ? cal.getPromedioFinal() : 0.0;
        }).average().orElse(0.0);
        String promedioFinal = String.format("%.1f", promedio);

        String genero = modelo.getSexo().equals("Hombre") ? "el alumno" : "la alumna";
        String[] fechaArray = fecha.split("de");
        DAOAcuerdo opAcuerdo = new DAOAcuerdo();
        Acuerdo acuerdo = opAcuerdo.getAcuerdoByCarrera(modelo.getCarrera().getIdCarrera());
        ValueConverters converter1 = ValueConverters.SPANISH_INTEGER;
        String yeartoWord = converter1.asWords(Integer.valueOf(fechaArray[2].trim()));

        try {
            String filePath = Config.getConif().obtenerConfiguracion("09 RUTA CERTIFICADOS") + "\\" + modelo.getCarrera().getIdClave().replace("/", "") + ".docx";
            XWPFDocument d = new XWPFDocument(new FileInputStream(filePath));

            replaceInDocument(d, "name", modelo.toString().toUpperCase());
            replaceInDocument(d, "date", fecha);
            replaceInDocument(d, "tuition", modelo.getMatricula());
            replaceInDocument(d, "gener", genero);
            replaceInDocument(d, "career", modelo.getCarrera().getNombre().toUpperCase());
            replaceInDocument(d, "totalAsignaturas", modelo.getCarrera().getTotalAsignaturas());
            replaceInDocument(d, "averaje", promedioFinal);
            replaceInDocument(d, "letter", numeroAString(promedioFinal));
            replaceInDocument(d, "credits", modelo.getCarrera().getCreditos());
            replaceInDocument(d, "dd", fechaArray[0].trim());
            replaceInDocument(d, "MM", fechaArray[1].trim());
            replaceInDocument(d, "yyyy",yeartoWord);
            replaceInDocument(d, "articulenumber", acuerdo.getNumero());
            replaceInDocument(d, "artdat", acuerdo.getFecha());
            replaceInDocument(d, "articulecc", acuerdo.getCc());

            XWPFTable tabla = d.getTables().get(0);

            for (int i = 0; i < califcacionesOrdenadas.size(); i++) {
                XWPFTableRow xwpfTable = tabla.getRow(i+2);
                rellenarCalificacionCertificado(xwpfTable, califcacionesOrdenadas.get(i) , "Calibri", "188", "325", "225", "125", "175", "225", "275", "225");
            }

            String generatedFolderPath = Config.getConif().obtenerConfiguracion("06 RUTA Documentos") + "\\Generados";
            new File(generatedFolderPath).mkdirs();

            String modifiedFilePath = generatedFolderPath + "\\" + modelo.getMatricula() + "_" + "CERTIFICADO" + ".docx";
            FileOutputStream fos = new FileOutputStream(modifiedFilePath);
            d.write(fos);
            fos.close();
            d.close();
            openModifiedDocument(modifiedFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void rellenarDiploma(Estudiante estudiante, String fecha) {
        DateTimeFormatter formatterEspañol = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale.Builder().setLanguage("es").setRegion("MX").build());
        LocalDate localDate = LocalDate.parse(fecha, formatterEspañol);

        ValueConverters converter1 = ValueConverters.SPANISH_INTEGER;
        String anio = converter1.asWords(localDate.getYear());
        String dia = converter1.asWords(localDate.getDayOfMonth());
        String mes = localDate.getMonth().getDisplayName(TextStyle.FULL, new Locale.Builder().setLanguage("es").setRegion("MX").build());
        String especialidad = estudiante.getCarrera().getNombre().split(" ")[0];
        Titulo titulo = new DAOTitulo().getTitulacionByIdAlumno(estudiante.getId());
        String nombreCompleto = (estudiante.getNombre() + " "+ estudiante.getApPaterno()+" " + estudiante.getApMaterno()).toUpperCase();
        String carrera = estudiante.getCarrera().getNombre().toUpperCase();

        if (titulo == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Debes primero haber creado el titulo para crear el diploma");
            alert.showAndWait();
            return;
        }

        try {
            String rutaBD = Config.getConif().obtenerConfiguracion("06 RUTA Documentos");
            String folderPath = rutaBD;
            String filePath = folderPath + "\\" + Documentos.Diploma +  ".docx";
            FileInputStream fis = new FileInputStream(filePath);
            XWPFDocument document = new XWPFDocument(fis);
            replaceInDocument(document, "name", nombreCompleto);
            replaceInDocument(document, "degree",carrera);
            replaceInDocument(document, "speciality", especialidad);
            replaceInDocument(document, "date", fecha);
            replaceInDocument(document, "day",dia);
            replaceInDocument(document, "month", mes);
            replaceInDocument(document, "year", anio);
            replaceInDocument(document, "sheet", titulo.getFoja());
            replaceInDocument(document, "book", titulo.getLibro());
            replaceInDocument(document, "dodo", titulo.getFolio());
            String generatedFolderPath = folderPath + "\\Generados";
            new File(generatedFolderPath).mkdirs();
            String modifiedFilePath = generatedFolderPath + "\\" + estudiante.getMatricula() + "_" + "acta" + ".docx";
            FileOutputStream fos = new FileOutputStream(modifiedFilePath);
            document.write(fos);
            fos.close();
            document.close();
            System.out.println("Documento modificado guardado en: " + modifiedFilePath);
            openModifiedDocument(modifiedFilePath);
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }


    private void rellenarFilaCalificacion(XWPFDocument document, Calificacion cal, String fontFamily, String... array) {
        cal.establecerPromedioFinal();
        XWPFTable tabla = document.getTables().get(0);

        XWPFTableRow filaCalificacion = tabla.createRow();

        XWPFTableCell celda1 = filaCalificacion.getCell(0);
        celda1.setWidth(array[0]);
        celda1.setText(cal.getMateria().getClave());

        XWPFTableCell celda2 = filaCalificacion.getCell(1);
        celda2.setWidth(array[1]);
        celda2.setText(cal.getMateria().getNombre());

        XWPFTableCell celda3 = filaCalificacion.getCell(2);
        celda3.setWidth(array[2]);
        celda3.setText(cal.getMateria().getCreditos());

        XWPFTableCell celda4 = filaCalificacion.getCell(3);
        celda4.setWidth(array[3]);

        String resultado = String.format("%.1f", cal.getPromedioFinal() != null ? cal.getPromedioFinal() : 0.0);

        celda4.setText(resultado);

        XWPFTableCell celda5 = filaCalificacion.getCell(4);
        celda5.setWidth(array[4]);
        celda5.setText(numeroAString(resultado));

        XWPFTableCell celda6 = filaCalificacion.getCell(5);
        celda6.setWidth(array[5]);
        celda6.setText(cal.getFecha());


        XWPFTableCell celda7 = filaCalificacion.getCell(6);
        celda7.setText(cal.getModulo());
        celda7.setWidth(array[6]);

        XWPFTableCell celda8 = filaCalificacion.createCell();
        celda8.setWidth(array[7]);
        celda8.setText(cal.getTipo());

        for (int i = 0; i < 8; i++) {
            XWPFTableCell celda = filaCalificacion.getCell(i);

            CTTcPr tcPr = celda.getCTTc().addNewTcPr();
            CTTcBorders borders = tcPr.addNewTcBorders();

            borders.addNewTop().setVal(STBorder.SINGLE);
            borders.addNewBottom().setVal(STBorder.SINGLE);
            borders.addNewLeft().setVal(STBorder.SINGLE);
            borders.addNewRight().setVal(STBorder.SINGLE);

            borders.getTop().setSz(BigInteger.valueOf(10));
            borders.getBottom().setSz(BigInteger.valueOf(10));
            borders.getLeft().setSz(BigInteger.valueOf(10));
            borders.getRight().setSz(BigInteger.valueOf(10));

            celda.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

            XWPFParagraph parrafo = celda.addParagraph();
            parrafo.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = parrafo.createRun();
            run.setFontFamily(fontFamily);

            run.setFontSize(10);
            run.setText(celda.getText());
            celda.removeParagraph(0);
        }

    }


    private String numeroAString(String resultado) {
        double calficacion = Double.parseDouble(resultado);
        String[] letrasNumero = {
            "cero", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve", "diez"
        };
        int parteEntera = (int) calficacion;
        int parteDecimal = (int) ((calficacion - parteEntera) * 10);

        return "(" + StringUtils.capitalize(letrasNumero[parteEntera]) + " punto " + letrasNumero[parteDecimal] + ")";
    }


    public void openModifiedDocument(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                System.out.println("El archivo no existe: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
