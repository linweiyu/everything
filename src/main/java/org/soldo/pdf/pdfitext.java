package org.soldo.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;

public class pdfitext {
    public static final String TEXT

            = "test";

    public static final String DEST

            = "itextpdf.pdf";


    public static void main(String[] args)

            throws DocumentException, IOException {

//        File file = new File(DEST);

//        file.getParentFile().mkdirs();

        new pdfitext().createPdf(DEST);

    }


    public void createPdf(String dest)

            throws DocumentException, IOException {

//        String fontPath = "MSYH.TTF";
        String fontPath = "ChineseGBK.ttf";
        BaseFont baseFont1 = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);


        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream(dest));

        document.open();

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(TEXT), "GBK"));

        String line;

        Paragraph p;

        Font normal = new Font(baseFont1, 12);


        while ((line = br.readLine()) != null) {

            p = new Paragraph(line, normal);

            p.setAlignment(Element.ALIGN_JUSTIFIED);

            document.add(p);

        }

        document.close();
    }
}
