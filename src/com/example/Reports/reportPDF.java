package com.example.Reports;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Header;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class reportPDF {

	public static final Font BLACK_BOLD = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	public static final Font HEADER = new Font(FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
	public static final Font CAPS = new Font(FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
	public static final Font DADES = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
	public static final Font SUBLINE = new Font(FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);
	
	public static final String RESULT = "/home/javi/Escritorio/trimestral.pdf";
	
	 public static void main(String[] args)
		        throws IOException, DocumentException {
		        new reportPDF().createPdf(RESULT);
		    }
	
	public void createPdf(String filename)
	        throws IOException, DocumentException {
 

    	// step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        // step 3
        document.open();

    	Paragraph headerTrimestre = new Paragraph("1r Trimestre", BLACK_BOLD);

    	Paragraph headerCurs = new Paragraph("2015/2016", BLACK_BOLD);
    	headerCurs.setIndentationLeft(250);

    	
    	Paragraph curs = new Paragraph("1rA", BLACK_BOLD);
    	
		document.add(headerCurs);
		document.add(headerTrimestre);
		document.add(curs);

		for (int i = 0; i<10;i++){
			
			
		}

		document.close();
        
    }
}
