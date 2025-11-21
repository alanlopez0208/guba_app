package com.guba.app.data.local.jasper;

import com.guba.app.data.local.database.conexion.Config;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Jasper {


    public void generateDocument(String pathJasper, Collection<?> beanCollection) {
        Map parameters = new HashMap();

        try {
            parameters.put("URL_IMAGE", new FileInputStream(Config.getConif().obtenerConfiguracion("07 RUTA Logo")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(beanCollection);
        parameters.put("DATA_SORUCE", beanColDataSource);
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    pathJasper, parameters, new JREmptyDataSource());

            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setVisible(true);
            viewer.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
