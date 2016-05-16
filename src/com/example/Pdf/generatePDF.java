package com.example.Pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.view.TeacherView.TeacherViewWarningJava;
import com.example.Entities.Student;
import com.example.Entities.Teacher;
import com.example.Logic.EntityManagerUtil;
import com.example.Logic.UserJPAManager;
import com.example.Logic.WarningJPAManager;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class generatePDF extends WarningJPAManager {

	private String timewarning;
	WarningJPAManager jpa = new WarningJPAManager();

	/**
	 * Main method.
	 * 
	 * @param args
	 *            no arguments needed
	 * @throws DocumentException
	 * @throws IOException
	 */

	public generatePDF() {

	}

	public String[] generate(String[] query) throws DocumentException, IOException {

		UserJPAManager MA = new UserJPAManager();
		// OBTENER ALUMNO
		Student al = MA.ObtenerAlumno(query[0], query[1]);

		// OBTENER TUTOR
		int tutorname = MA.getIdTutor(al.getGrup());
		// OBTENER PERSONA QUE REALIZA EL PARTE
		String nametutor = MA.getNomTutor(tutorname);

		java.util.Date date = new java.util.Date();
		Timestamp data1 = new Timestamp(date.getTime());
		String dateparsed = String.valueOf(data1);

		String fechaparte = dateparsed.substring(0, 10);
		String horaparte = dateparsed.substring(11, 19);

		if (query[13].length() > 2 ) {
			fechaparte = query[13];
			horaparte = query[14];
		}
		Document document = new Document();
		Paragraph preface = new Paragraph();

		File currDir = new File(".");
		String path2 = currDir.getCanonicalPath();
		Image img = Image.getInstance(String.format(path2 + "/git/ga2/WebContent/PDFContent/icons/logo1.jpg"));
		img.setWidthPercentage(50);
		// X - Y
		img.setAbsolutePosition(45, 770);
		img.scaleToFit(25, 25);

		// P1
		String nomCognom = (query[0].concat(" "+query[1])).replaceFirst(" ", "").replaceAll(" ", "_");

		String user = jpa.currentUser();

		String path = path2 + "/git/ga2/WebContent/PDFContent/pdftmp/amonestacio(" +horaparte.substring(0, 5)+ ")(" + nomCognom
				+ ").pdf";
		
		

		
		PdfWriter.getInstance(document, new FileOutputStream(getPath(nomCognom)));

		PdfWriter.getInstance(document, new FileOutputStream(path));
		Paragraph paragraph1 = new Paragraph("Generalitat de Catalunya\nDepartament d'Ensenyament", BLACK_BOLD);
		paragraph1.setIndentationLeft(40);
		paragraph1.setSpacingBefore(11);

		// P2
		Paragraph paragraph2 = new Paragraph("Institut Puig Castellar\nSanta Coloma de Gramenet", BLACK_BOLD);
		paragraph2.setSpacingBefore(-25);
		paragraph2.setIndentationLeft(205);

		// P3
		Paragraph paragraph3 = new Paragraph("Anselm de Riu, 10\nTelèfon 93 391 61 11", BLACK_BOLD);
		paragraph3.setSpacingBefore(-25);
		paragraph3.setIndentationLeft(380);

		// Header
		Paragraph headerFalta = new Paragraph("NOTIFICACIÓ DE FALTA", HEADER);
		headerFalta.setIndentationLeft(180);

		// Prueba
		String nomAlumne = query[0] + " " + query[1];
		// CAMP ALUMNE
		Paragraph alumne = new Paragraph("ALUMNE: ", CAPS);
		alumne.setIndentationLeft(25);
		alumne.setSpacingBefore(10);
		Paragraph campAlumne = new Paragraph(nomAlumne, DADES);
		campAlumne.setIndentationLeft(75);
		campAlumne.setSpacingBefore(-12);

		// Prueba
		String grupAlumne = query[2];
		// CAMP ALUMNE
		Paragraph grup = new Paragraph("GRUP: ", CAPS);
		grup.setIndentationLeft(220);
		grup.setSpacingBefore(-13);
		Paragraph campGrup = new Paragraph(grupAlumne, DADES);
		campGrup.setIndentationLeft(255);
		campGrup.setSpacingBefore(-13);

		// Prueba
//		if(query[14] != "null"){
//			
//			String dataAlumne = query[14];
//
//			
//		}
		// CAMP ALUMNE
		String dataAlumne = fechaparte;

		Paragraph data = new Paragraph("DATA: ", CAPS);
		data.setIndentationLeft(340);
		data.setSpacingBefore(-13);
		Paragraph campData = new Paragraph(dataAlumne, DADES);
		campData.setIndentationLeft(380);
		campData.setSpacingBefore(-12);

		// Prueba

		String materiaAlumne;

		if (query[5] == null) {
			materiaAlumne = " ";
		} else {
			materiaAlumne = query[5];
		}

		// CAMP ALUMNE
		Paragraph materia = new Paragraph("MATÈRIA: ", CAPS);
		materia.setIndentationLeft(25);
		Paragraph campMateria = new Paragraph(materiaAlumne, DADES);
		campMateria.setIndentationLeft(75);
		campMateria.setSpacingBefore(-12);

		// Prueba
//		if(query[15] != "null" || query[15] != ""){
//			
//			timewarning = query[15].substring(11, 16);
//
//		}
		
		// CAMP ALUMNE
		Paragraph hora = new Paragraph("HORA: ", CAPS);
		hora.setIndentationLeft(195);
		hora.setSpacingBefore(-13);
		Paragraph campHora = new Paragraph(horaparte, DADES);
		campHora.setIndentationLeft(230);
		campHora.setSpacingBefore(-12);

		// Prueba
		String cirAlumne = query[4];
		// CAMP CIRCUMSTANCIA
		Paragraph circumstancia = new Paragraph("CIRCUMSTÀNCIA(aula,pati,etc.): ", CAPS);
		circumstancia.setIndentationLeft(280);
		circumstancia.setSpacingBefore(-13);
		Paragraph campCirc = new Paragraph(cirAlumne, DADES);
		campCirc.setIndentationLeft(430);
		campCirc.setSpacingBefore(-12);

		//
		// ma = new UserJPAManager();
		// int id =
		// Integer.parseInt(getUI().getCurrent().getSession().getAttribute("id").toString());
		// // TODO Auto-generated method stub
		// wellcome.setValue("Benvingut "+ma.getNomTutor(id));

		// Prueba
		String nomProfessor = MA.currentTeacher();

		if (query[12] != null) {
			nomProfessor = query[12];
		}

		if (query[12] == "null") {
			nomProfessor = MA.currentTeacher();
		}

		// CAMP PROFESSOR
		Paragraph professor = new Paragraph("PROFESSOR: ", CAPS);
		professor.setIndentationLeft(25);
		Paragraph campProfe = new Paragraph(nomProfessor, DADES);
		campProfe.setIndentationLeft(90);
		campProfe.setSpacingBefore(-12);

		// Prueba
		String tutorAlumne = nametutor;
		// CAMP ALUMNE
		Paragraph tutor = new Paragraph("TUTOR: ", CAPS);
		tutor.setIndentationLeft(220);
		tutor.setSpacingBefore(-13);
		Paragraph campTutor = new Paragraph(tutorAlumne, DADES);
		campTutor.setIndentationLeft(260);
		campTutor.setSpacingBefore(-12);

		String grauPart = null;
		// Prueba
		if (query[7].equals("true")) {
			grauPart = "[ ]EXPULSAT DE CLASSE\n[X]AMONESTAT";
		} else {
			grauPart = "[X]EXPULSAT DE CLASSE\n[ ]AMONESTAT";

		}
		// CAMP 2part
		Paragraph gravetat = new Paragraph(
				"En compliment de la Normativa de Règim Intern vigent, l'esmentat alumne ha estat: ", DADES);
		gravetat.setIndentationLeft(25);

		Paragraph campGrav = new Paragraph(grauPart, DADES);
		campGrav.setIndentationLeft(330);
		campGrav.setSpacingBefore(-12);

		String[] motivos = { "Faltar al respecte al professor", "Faltar al respecte a algún company",
				"Acumulació d'amonestacions lleus", "Causar dany o desperfecte material",
				"Negar-se a realitzar treballs encomanats", "Molestar o distreure de forma reiterada als companys" };

		String queryCon = query[9].concat(query[11]).trim().replace("][", ",");

		String subQuery = queryCon.substring(1, queryCon.length() - 1).trim();

		// System.out.println("Subquery: " + subQuery);
		String[] motiusUsuari = subQuery.split(",");

		// Faltar al respecte al professor, Acumulació d'amonestacions
		// lleus,Causar dany o desperfecte material, Negar-se a realitzar
		// treballs encomanats

		// Bloc1Left
		String motiu1 = null;
		String motiu3 = null;
		String motiu5 = null;
		// Bloc2Right
		String motiu2 = null;
		String motiu4 = null;
		String motiu6 = null;

		List llistaMotius = new ArrayList<>();

		for (int i = 0; i < motiusUsuari.length; i++) {
			motiusUsuari[i] = motiusUsuari[i].trim();

		}

		for (int i = 0; i < motivos.length; i++) {

			// System.out.println(motivos[i].trim());
			boolean marcat = false;

			for (int j = 0; j < motiusUsuari.length; j++) {

				marcat = marcat || motivos[i].equals(motiusUsuari[j]);

			}
			if (marcat) {
				System.out.println("[X]" + motivos[i].trim());
				llistaMotius.add("[X]" + motivos[i].trim());

			} else {

				System.out.println("[ ]" + motivos[i].trim());
				llistaMotius.add("[  ]" + motivos[i].trim());

			}
		}

		// System.out.println("Lista de motivos");
		// System.out.println(llistaMotius.get(0));
		// System.out.println(llistaMotius.get(1));
		// System.out.println(llistaMotius.get(2));
		// System.out.println(llistaMotius.get(3));
		// System.out.println(llistaMotius.get(4));
		// System.out.println(llistaMotius.get(5));

		if (llistaMotius.contains("[X]Faltar al respecte al professor")) {

			motiu1 = "[X]Faltar al respecte al professor";

		} else {

			motiu1 = "[ ]Faltar al respecte al professor";

		}

		if (llistaMotius.contains("[X]Faltar al respecte a algún company")) {

			motiu3 = "[X]Faltar al respecte a algún company";

		} else {

			motiu3 = "[ ]Faltar al respecte a algún company";

		}

		if (llistaMotius.contains("[X]Acumulació d'amonestacions lleus")) {

			motiu5 = "[X]Acumulació d'amonestacions lleus";

		} else {

			motiu5 = "[ ]Acumulació d'amonestacions lleus";

		}

		// Bloc2

		if (llistaMotius.contains("[X]Causar dany o desperfecte material")) {

			motiu2 = "[X]Causar dany o desperfecte material";

		} else {

			motiu2 = "[ ]Causar dany o desperfecte material";

		}

		if (llistaMotius.contains("[X]Negar-se a realitzar treballs encomanats")) {

			motiu4 = "[X]Negar-se a realitzar treballs encomanats";

		} else {

			motiu4 = "[ ]Negar-se a realitzar treballs encomanats";

		}

		if (llistaMotius.contains("[X]Molestar o distreure de forma reiterada als companys")) {

			motiu6 = "[X]Molestar o distreure de forma reiterada als companys";

		} else {

			motiu6 = "[ ]Molestar o distreure de forma reiterada als companys";

		}

		String motiu10 = null;
		// System.out.println("Altres motius"+query[10]);
		if (query[10].equals("")) {

			motiu10 = "[  ]Altres motius:";

		} else {
			motiu10 = "[X]Altres motius:";

		}

		// CAMP 2part
		Paragraph motius = new Paragraph("Pel següent motiu: ", DADES);
		motius.setIndentationLeft(25);

		Paragraph motiuNum1 = new Paragraph(motiu1, DADES);
		motiuNum1.setIndentationLeft(95);
		motiuNum1.setSpacingBefore(-12);

		Paragraph motiuNum2 = new Paragraph(motiu2, DADES);
		motiuNum2.setIndentationLeft(235);
		motiuNum2.setSpacingBefore(-12);

		Paragraph motiuNum3 = new Paragraph(motiu3, DADES);
		motiuNum3.setIndentationLeft(95);

		Paragraph motiuNum4 = new Paragraph(motiu4, DADES);
		motiuNum4.setIndentationLeft(235);
		motiuNum4.setSpacingBefore(-12);

		Paragraph motiuNum5 = new Paragraph(motiu5, DADES);
		motiuNum5.setIndentationLeft(95);

		Paragraph motiuNum6 = new Paragraph(motiu6, DADES);
		motiuNum6.setIndentationLeft(235);
		motiuNum6.setSpacingBefore(-12);

		Paragraph motiuNum10 = new Paragraph(motiu10, DADES);
		motiuNum10.setIndentationLeft(95);

		Paragraph campMotiu10 = new Paragraph(query[10] + " ", DADES);
		campMotiu10.setIndentationLeft(160);
		campMotiu10.setSpacingBefore(-12);
		campMotiu10.setSpacingAfter(10);

		String lineCamp = "     __________________________________________________________________________________________________________";
		Paragraph line = new Paragraph(lineCamp, DADES);
		line.setSpacingAfter(5);

		Paragraph signProfe = new Paragraph("Signatura del professor\nque impossa l'amonestació", SUBLINE);
		signProfe.setIndentationLeft(25);

		Paragraph signPares = new Paragraph("Signatura dels pares", SUBLINE);
		signPares.setIndentationLeft(210);
		signPares.setSpacingBefore(-19);

		Paragraph signAlumne = new Paragraph("Signatura de l'alumne", SUBLINE);
		signAlumne.setIndentationLeft(410);
		signAlumne.setSpacingBefore(-9);
		signAlumne.setSpacingAfter(35);

		Paragraph avis = new Paragraph("(A retornar signat al Professor el dia següent.)", SUBLINE);
		avis.setIndentationLeft(410);

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(80 / 2f);
		// table.setWidths(new int[]{1, 1, 1});
		PdfPCell cell;
		String caracterGreu = null;
		String caracterLleu = null;

		if (query[3].equals("Lleu")) {
			caracterGreu = "    [ ]GREU";
			caracterLleu = "    [X]LLEU";
		} else if (query[3].equals("Greu")) {
			caracterGreu = "    [X]GREU";
			caracterLleu = "    [ ]LLEU";
		}

		cell = new PdfPCell(new Phrase("CARÀCTER DE LA FALTA", DADES));
		cell.setColspan(1);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("" + caracterGreu + "" + caracterLleu, DADES));
		cell.setRowspan(2);
		table.addCell(cell);

		// DOC

		document.open();
		document.add(img);
		document.add(paragraph1);
		document.add(paragraph2);
		document.add(paragraph3);
		document.add(headerFalta);
		document.add(alumne);
		document.add(campAlumne);
		document.add(grup);
		document.add(campGrup);
		document.add(data);
		document.add(campData);
		document.add(materia);
		document.add(campMateria);
		document.add(hora);
		document.add(campHora);
		document.add(circumstancia);
		document.add(campCirc);
		document.add(professor);
		document.add(campProfe);
		document.add(tutor);
		document.add(campTutor);
		document.add(gravetat);
		document.add(campGrav);
		document.add(motius);
		document.add(motiuNum1);
		document.add(motiuNum2);
		document.add(motiuNum3);
		document.add(motiuNum4);
		document.add(motiuNum5);
		document.add(motiuNum6);

		document.add(motiuNum10);
		document.add(campMotiu10);
		document.add(table);
		document.add(line);
		document.add(signProfe);
		document.add(signPares);
		document.add(signAlumne);
		document.add(avis);

		document.add(preface);
		document.close();

		String[] re = {path,fechaparte,horaparte};
		
		return re;
	}



	/**
	 * Creates a PDF with information about the movies
	 * 
	 * @param filename
	 *            the name of the PDF file that will be created.
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static final Font BLACK_BOLD = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	public static final Font HEADER = new Font(FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
	public static final Font CAPS = new Font(FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
	public static final Font DADES = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
	public static final Font SUBLINE = new Font(FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);

	/**
	 * Creates our first table
	 * 
	 * @return our first table
	 * @throws IOException
	 * @throws DocumentException
	 */

	public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
		Image img = Image.getInstance(path);
		PdfPCell cell = new PdfPCell(img, true);
		return cell;
	}

	public String getPath(String nom) throws IOException {
		File currDir = new File(".");
		String path2 = currDir.getCanonicalPath();

		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		// get current date time with Date()
		Date date = new Date();


		String user = jpa.currentUser();

		return path2 + "/git/ga2/WebContent/PDFContent/pdftmp/amonestacio("+ dateFormat.format(date)+")(" + nom
				+").pdf";

	}

	public String getPath2(String nomCognom, String fecha) throws IOException {
		File currDir = new File(".");
		String path2 = currDir.getCanonicalPath();

		return path2 + "/git/ga2/WebContent/PDFContent/pdftmp/amonestacio(" + fecha + ")(" + nomCognom + ").pdf";

	}
}