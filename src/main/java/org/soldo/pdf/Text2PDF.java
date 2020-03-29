package org.soldo.pdf;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.*;

/**
 * *  This  will  take  a  text  file  and  ouput  a  pdf  with  that  text.
 * *
 * *  @author  Ben  Litchfield
 */
public class Text2PDF {
    /**
     * The scaling factor for font units to PDF units
     */
    private static final int FONTSCALE = 1000;

    /**
     * The  default  font  size
     */
    private static final int DEFAULT_FONT_SIZE = 10;

    /**
     * The  line  height  as  a  factor  of  the  font  size
     */
    private static final float LINE_HEIGHT_FACTOR = 1.05f;

    private int fontSize = DEFAULT_FONT_SIZE;
    private PDRectangle mediaBox = PDRectangle.LETTER;
    private boolean landscape = false;
    private PDFont font = null;

    /**
     * Create  a  PDF  document  with  some  text.
     *
     * @param text The  stream  of  text  data.
     * @return The  document  with  the  text  in  it.
     * @throws IOException If  there  is  an  error  writing  the  data.
     */
    public PDDocument createPDFFromText(Reader text) throws IOException {
        PDDocument doc = new PDDocument();
        createPDFFromText(doc, text);
        return doc;
    }

    /**
     * Create  a  PDF  document  with  some  text.
     *
     * @param doc  The  document.
     * @param text The  stream  of  text  data.
     * @throws IOException If  there  is  an  error  writing  the  data.
     */
    public void createPDFFromText(PDDocument doc, Reader text) throws IOException {
        try {

            final int margin = 40;
            float height = font.getBoundingBox().getHeight() / FONTSCALE;
            PDRectangle actualMediaBox = mediaBox;
//            if (landscape) {
//                actualMediaBox = new PDRectangle(mediaBox.getHeight(), mediaBox.getWidth());
//            }

            //calculate  font  height  and  increase  by  a  factor.
            height = height * fontSize * LINE_HEIGHT_FACTOR;
            BufferedReader data = new BufferedReader(text);
            String nextLine;
            PDPage page = new PDPage(actualMediaBox);
            PDPageContentStream contentStream = null;
            float y = -1;
            float maxStringLength = page.getMediaBox().getWidth() - 2 * margin;

            //  There  is  a  special  case  of  creating  a  PDF  document  from  an  empty  string.
            boolean textIsEmpty = true;

            while ((nextLine = data.readLine()) != null) {

                //  The  input  text  is  nonEmpty.  New  pages  will  be  created  and  added
                //  to  the  PDF  document  as  they  are  needed,  depending  on  the  length  of
                //  the  text.
                textIsEmpty = false;

                String[] lineWords = nextLine.replaceAll("[\\n\\r]+$", "").split("  ");
                int lineIndex = 0;
                while (lineIndex < lineWords.length) {
                    StringBuilder nextLineToDraw = new StringBuilder();
                    float lengthIfUsingNextWord = 0;
                    boolean ff = false;
                    do {
                        String word1, word2 = "";
                        int indexFF = lineWords[lineIndex].indexOf('\f');
                        if (indexFF == -1) {
                            word1 = lineWords[lineIndex];
                        } else {
                            ff = true;
                            word1 = lineWords[lineIndex].substring(0, indexFF);
                            if (indexFF < lineWords[lineIndex].length()) {
                                word2 = lineWords[lineIndex].substring(indexFF + 1);
                            }
                        }
                        //  word1  is  the  part  before  ff,  word2  after
                        //  both  can  be  empty
                        //  word1  can  also  be  empty  without  ff,  if  a  line  has  many  spaces
                        if (word1.length() > 0 || !ff) {
                            nextLineToDraw.append(word1);
                            nextLineToDraw.append("  ");
                        }
                        if (!ff || word2.length() == 0) {
                            lineIndex++;
                        } else {
                            lineWords[lineIndex] = word2;
                        }
                        if (ff) {
                            break;
                        }
                        if (lineIndex < lineWords.length) {
                            //  need  cut  off  at  \f  in  next  word  to  avoid  IllegalArgumentException
                            String nextWord = lineWords[lineIndex];
                            indexFF = nextWord.indexOf('\f');
                            if (indexFF != -1) {
                                nextWord = nextWord.substring(0, indexFF);
                            }

                            String lineWithNextWord = nextLineToDraw.toString() + "  " + nextWord;
                            lengthIfUsingNextWord =
                                    (font.getStringWidth(lineWithNextWord) / FONTSCALE) * fontSize;
                        }
                    }
                    while (lineIndex < lineWords.length && lengthIfUsingNextWord < maxStringLength);

                    if (y < margin) {
                        //  We  have  crossed  the  end-of-page  boundary  and  need  to  extend  the
                        //  document  by  another  page.
                        page = new PDPage(actualMediaBox);
                        doc.addPage(page);
                        if (contentStream != null) {
                            contentStream.endText();
                            contentStream.close();
                        }
                        contentStream = new PDPageContentStream(doc, page);
                        contentStream.setFont(font, fontSize);
                        contentStream.beginText();
                        y = page.getMediaBox().getHeight() - margin + height;
                        contentStream.newLineAtOffset(margin, y);
                    }

                    if (contentStream == null) {
                        throw new IOException("Error:Expected  non-null  content  stream.");
                    }
                    contentStream.newLineAtOffset(0, -height);
                    y -= height;
                    contentStream.showText(nextLineToDraw.toString());
                    if (ff) {
                        page = new PDPage(actualMediaBox);
                        doc.addPage(page);
                        contentStream.endText();
                        contentStream.close();
                        contentStream = new PDPageContentStream(doc, page);
                        contentStream.setFont(font, fontSize);
                        contentStream.beginText();
                        y = page.getMediaBox().getHeight() - margin + height;
                        contentStream.newLineAtOffset(margin, y);
                    }
                }
            }

            //  If  the  input  text  was  the  empty  string,  then  the  above  while  loop  will  have  short-circuited
            //  and  we  will  not  have  added  any  PDPages  to  the  document.
            //  So  in  order  to  make  the  resultant  PDF  document  readable  by  Adobe  Reader  etc,  we'll  add  an  empty  page.
            if (textIsEmpty) {
                doc.addPage(page);
            }

            if (contentStream != null) {
                contentStream.endText();
                contentStream.close();
            }
        } catch (IOException io) {
            if (doc != null) {
                doc.close();
            }
            throw io;
        }
    }

    /**
     * This  will  create  a  PDF  document  with  some  text  in  it.
     * <br>
     * see  usage()  for  commandline
     *
     * @param args Command  line  arguments.
     * @throws IOException If  there  is  an  error  with  the  PDF.
     */
    public static void main(String[] args) throws IOException {

        String fileName = "Text2PDF.pdf";
        String fontfile = "ChineseGBK.ttf";
        String textFile = "test";
        TextToPDF app = new TextToPDF();

        PDDocument doc = new PDDocument();
        try {
            PDFont font = PDType0Font.load(doc, new File(fontfile));
            app.setFont(font);
            app.setMediaBox(createRectangle("A4"));
            app.createPDFFromText(doc, new FileReader(textFile));
            doc.save(fileName);
        } finally {
            doc.close();
        }
    }

    private static PDRectangle createRectangle(String paperSize) {
        if ("letter".equalsIgnoreCase(paperSize)) {
            return PDRectangle.LETTER;
        } else if ("legal".equalsIgnoreCase(paperSize)) {
            return PDRectangle.LEGAL;
        } else if ("A0".equalsIgnoreCase(paperSize)) {
            return PDRectangle.A0;
        } else if ("A1".equalsIgnoreCase(paperSize)) {
            return PDRectangle.A1;
        } else if ("A2".equalsIgnoreCase(paperSize)) {
            return PDRectangle.A2;
        } else if ("A3".equalsIgnoreCase(paperSize)) {
            return PDRectangle.A3;
        } else if ("A4".equalsIgnoreCase(paperSize)) {
            return PDRectangle.A4;
        } else if ("A5".equalsIgnoreCase(paperSize)) {
            return PDRectangle.A5;
        } else if ("A6".equalsIgnoreCase(paperSize)) {
            return PDRectangle.A6;
        } else {
            return null;
        }
    }

    /**
     * @return Returns  the  font.
     */
    public PDFont getFont() {
        return font;
    }

    /**
     * @param aFont The  font  to  set.
     */
    public void setFont(PDFont aFont) {
        this.font = aFont;
    }

    /**
     * @return Returns  the  fontSize.
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * @param aFontSize The  fontSize  to  set.
     */
    public void setFontSize(int aFontSize) {
        this.fontSize = aFontSize;
    }

    /**
     * Sets  page  size  of  produced  PDF.
     *
     * @return returns  the  page  size  (media  box)
     */
    public PDRectangle getMediaBox() {
        return mediaBox;
    }

    /**
     * Sets  page  size  of  produced  PDF.
     *
     * @param mediaBox
     */
    public void setMediaBox(PDRectangle mediaBox) {
        this.mediaBox = mediaBox;
    }

    /**
     * Tells  the  paper  orientation.
     *
     * @return true  for  landscape  orientation
     */
    public boolean isLandscape() {
        return landscape;
    }

    /**
     * Sets  paper  orientation.
     *
     * @param landscape
     */
    public void setLandscape(boolean landscape) {
        this.landscape = landscape;
    }
}