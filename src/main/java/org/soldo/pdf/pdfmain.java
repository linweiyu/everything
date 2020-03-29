package org.soldo.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.*;

public class pdfmain {

    public static void main(String[] args) throws Exception {
        String filename = "test.pdf";
        String fontfile = "ChineseGBK.ttf";
//        String fontfile2 = "LiberationSans-Regular.ttf";
        String contentFile = "test";

        String content = readFile(contentFile);

        PDDocument doc = new PDDocument();

        PDFont font = PDType0Font.load(doc, new File(fontfile));

        PDPage page = new PDPage();

        PDPageContentStream stream = new PDPageContentStream(doc, page);
        stream.beginText();
        stream.setFont(font, 12);
        stream.newLineAtOffset(100, 700);
        stream.showText("林为昱。，；, . ;");
        stream.showText("hello");
        stream.showText(content);
        stream.endText();
        stream.close();
        doc.addPage(page);
        doc.save(filename);
        doc.close();
    }

    public static void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "gbk"));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
    }

    public static String readFile(String filePath) throws IOException {
        StringBuffer sb = new StringBuffer();
        readToBuffer(sb, filePath);
        return sb.toString();
    }
}
