package com.guba.app.service.local.poi;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileOutputStream;
import java.io.IOException;

public class WordWriter {
    public static void writeWordFile(String filePath, String content) throws IOException {
        try (XWPFDocument document = new XWPFDocument();
             FileOutputStream fos = new FileOutputStream(filePath)) {

            String[] paragraphs = content.split("\n");
            for (String paragraphText : paragraphs) {
                XWPFParagraph paragraph = document.createParagraph();
                paragraph.createRun().setText(paragraphText);
            }

            document.write(fos);
        }
    }
}
