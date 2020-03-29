package org.soldo.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.*;

public class StringToPDF {

    private static final int FONTSCALE = 1000;

    private static final float LINE_HEIGHT_FACTOR = 1.05f;

    private float leftMargin;

    private float rightMargin;

    private float topMargin;

    private float belowMargin;

    private float fontSize;

    private PDRectangle rectangle;

    private PDFont font;

    public StringToPDF(float leftMargin, float rightMargin, float topMargin, float belowMargin, float fontSize, PDRectangle rectangle) {
        this.leftMargin = leftMargin;
        this.rightMargin = rightMargin;
        this.topMargin = topMargin;
        this.belowMargin = belowMargin;
        this.fontSize = fontSize;
        this.rectangle = rectangle;
    }

    public PDDocument createPDFFromString(String text, String fontFile) throws Exception {
        PDDocument doc = new PDDocument();
        this.font = PDType0Font.load(doc, new File(fontFile));
        createPDFFromString(doc, text);
        return doc;
    }

    public static void main(String[] args) throws Exception {
        String fontfile = "MSYH.TTF";
        String file = "test";
        StringToPDF stringToPDF = new StringToPDF(40, 40, 40, 40, 10, PDRectangle.A4);
        PDDocument doc = stringToPDF.createPDFFromString(pdfmain.readFile(file), fontfile);
        doc.save("StringToPDF.pdf");
        doc.close();
    }

    private void createPDFFromString(PDDocument doc, String text) throws Exception {
        float fontHeight = font.getBoundingBox().getHeight() / FONTSCALE;
        fontHeight = fontHeight * fontSize * LINE_HEIGHT_FACTOR;

        PDPage page = new PDPage(rectangle);
        PDPageContentStream contentStream = null;
        //y 当前执行长度 当小于belowMargin时另起一页
        float y = -1;
        float maxLineLength = page.getMediaBox().getWidth() - (leftMargin + rightMargin);

        //text 制表符替换 按 \n 区分换行 (此处需根据文本可能出现的特殊符号做特殊处理，否则字符集不识别会报错)
        String[] textLines = text.replaceAll("[\\t]+$", "   ").split("\\n");

        for (String line : textLines) {
            if (y < belowMargin) {
                //另其新页
                page = new PDPage(rectangle);
                doc.addPage(page);
                if (contentStream != null) {
                    contentStream.endText();
                    contentStream.close();
                }
                contentStream = new PDPageContentStream(doc, page);
                contentStream.setFont(font, fontSize);
                contentStream.beginText();
                y = page.getMediaBox().getHeight() - topMargin;
                contentStream.newLineAtOffset(topMargin, y);
            }
            if (contentStream == null) {
                throw new IOException("PDPageContentStream Create Failed");
            }

            float lineWidth = font.getStringWidth(line) / FONTSCALE * fontSize;
            if (lineWidth <= maxLineLength) {
                //一行可以装下内容
                y -= fontHeight;
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -fontHeight);
                continue;
            } else {
                //需要多行空间
                //一行所能装下的字体数目 应向下取整
                int maxLineLengths = (int) Math.floor(maxLineLength / fontSize);
                //所需的字体数目
                int lineWholeLengths = (int) Math.ceil(lineWidth / fontSize);
                //所需行数 应向上取整
                int rows = (int) Math.ceil((float) lineWholeLengths / maxLineLengths);
                if (y - rows * fontHeight < belowMargin) {
                    //部分行需另起1页
                    int wordIndex = 0;
                    //写在当前页的部分 应向下取整
                    int currentRows = (int) Math.floor((y - belowMargin) / fontHeight);
                    for (int i = 0; i < currentRows; i++) {
                        y -= fontHeight;
                        contentStream.showText(line.substring(wordIndex, wordIndex + maxLineLengths - 1));
                        wordIndex += maxLineLengths - 1;
                        contentStream.newLineAtOffset(0, -fontHeight);
                    }
                    //写在下一页部分
                    int nextRows = rows - currentRows;
                    //新起一页
                    page = new PDPage(rectangle);
                    doc.addPage(page);
                    contentStream.endText();
                    contentStream.close();
                    contentStream = new PDPageContentStream(doc, page);
                    contentStream.setFont(font, fontSize);
                    contentStream.beginText();
                    y = page.getMediaBox().getHeight() - topMargin;
                    contentStream.newLineAtOffset(topMargin, y);
                    y -= writeMultiLines(line.substring(wordIndex), contentStream, fontHeight, nextRows, maxLineLengths);

                } else {
                    //无需另起1页
                    y -= writeMultiLines(line, contentStream, fontHeight, rows, maxLineLengths);
                }
            }
        }
        if (contentStream != null) {
            contentStream.endText();
            contentStream.close();
        }
    }

    /***
     * 返回写入多行所占据的长度
     * @param text
     * @param contentStream
     * @param fontHeight
     * @param rows
     * @param maxLineLengths 一行所能装下的字体数目
     * @return
     * @throws IOException
     */
    private float writeMultiLines(String text, PDPageContentStream contentStream, float fontHeight, int rows, int maxLineLengths) throws IOException {
        int wordIndex = 0;
        float multiLength = 0;
        for (int i = 0; i < rows; i++) {
            multiLength += fontHeight;
            if (wordIndex + maxLineLengths <= text.length()) {
                contentStream.showText(text.substring(wordIndex, wordIndex + maxLineLengths - 1));
                wordIndex += maxLineLengths - 1;
                contentStream.newLineAtOffset(0, -fontHeight);
            } else {
                contentStream.showText(text.substring(wordIndex));
                contentStream.newLineAtOffset(0, -fontHeight);
                break;
            }
        }
        return multiLength;
    }

    public float getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(float leftMargin) {
        this.leftMargin = leftMargin;
    }

    public float getRightMargin() {
        return rightMargin;
    }

    public void setRightMargin(float rightMargin) {
        this.rightMargin = rightMargin;
    }

    public float getTopMargin() {
        return topMargin;
    }

    public void setTopMargin(float topMargin) {
        this.topMargin = topMargin;
    }

    public float getBelowMargin() {
        return belowMargin;
    }

    public void setBelowMargin(float belowMargin) {
        this.belowMargin = belowMargin;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public PDRectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(PDRectangle rectangle) {
        this.rectangle = rectangle;
    }

    public PDFont getFont() {
        return font;
    }

    public void setFont(PDFont font) {
        this.font = font;
    }
}
