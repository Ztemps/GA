
package com.example.Pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.example.Entities.Student;
import com.example.Logic.StudentsJPAManager;
import com.example.Logic.TeachersJPAManager;
import com.example.Logic.TutorJPAManager;
import com.example.Logic.UserJPAManager;
import com.example.Logic.WarningJPAManager;
import com.itextpdf.text.BadElementException;
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

/*******************************************************************************
 * 
 * Gestió d'Amonestacions v1.0
 *
 * Esta obra está sujeta a la licencia
 * Reconocimiento-NoComercial-SinObraDerivada 4.0 Internacional de Creative
 * Commons. Para ver una copia de esta licencia, visite
 * http://creativecommons.org/licenses/by-nc-nd/4.0/.
 * 
 * @author Francisco Javier Casado Moreno - fcasado@elpuig.xeill.net
 * @author Daniel Pérez Palacino - dperez@elpuig.xeill.net
 * @author Gerard Enrique Paulino Decena - gpaulino@elpuig.xeill.net
 * @author Xavier Murcia Gámez - xmurcia@elpuig.xeill.net
 * 
 *******************************************************************************/

/**
 * Esta clase consiste en la creación de un PDF,el cual se genera a partir de
 * los campos seleccionados a la hora de introducir un parte.
 */
public class generatePDF extends WarningJPAManager {

	private WarningJPAManager warningJPA;
	private UserJPAManager userJPA;
	private TutorJPAManager tutorJPA;
	private TeachersJPAManager teacherJPA;
	private StudentsJPAManager studentJPA;
	private File currentDirectory;
	private String path2;
	private ResourceBundle rb = ResourceBundle.getBundle("GA");

	// Definimos las fuentes para el PDF
	public static final Font BLACK_BOLD = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	public static final Font HEADER = new Font(FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
	public static final Font CAPS = new Font(FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
	public static final Font DADES = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
	public static final Font SUBLINE = new Font(FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);

	/**
	 * Constructor de la clase
	 * 
	 * @throws IOException
	 */
	public generatePDF() throws IOException {

		currentDirectory = new File(".");
		path2 = currentDirectory.getCanonicalPath();
		warningJPA = new WarningJPAManager();
		userJPA = new UserJPAManager();
		teacherJPA = new TeachersJPAManager();
		tutorJPA = new TutorJPAManager();
		studentJPA = new StudentsJPAManager();
	}

	/**
	 * Método el cual le hemos de pasar como parametro un array para poder
	 * realizar la correcta creación del parte.
	 * 
	 * @return Nos retorna path, fechaparte, horaparte que serán necesarios para
	 *         poder encontrar el parte generado en nuestro sistema y mostrarlo
	 *         en una ventana.
	 * @param query
	 * @throws DocumentException
	 * @throws IOException
	 */
	public String[] generate(String[] query) throws DocumentException, IOException {

		// Obtenemos el alumno
		Student al = studentJPA.ObtenerAlumno(query[0], query[1]);
		// Obtenemos tutor
		int tutorname = tutorJPA.getIdTutor(al.getGrup());
		// Obtenemos persona que realiza el parte
		String nametutor = tutorJPA.getNomTutor(tutorname);

		String dateWarning = query[13];
		String hourWarning = query[14];

		dateWarning = query[13];

		// Creamos un nuevo documento, que será el nuevo parte(PDF)
		Document document = new Document();
		Paragraph preface = new Paragraph();

		Image img = logoImage();

		String nameLastname = (query[0].concat(" " + query[1])).replaceFirst(" ", "").replaceAll(" ", "_");

		System.out.println("SUBSTRING: " + query[14]);

		String path = rb.getString("path_warning") + "amonestacio(" + hourWarning.substring(0, 5) + ")(" + nameLastname
				+ ").pdf";

		// Definimos la ruta de nuestro documento
		PdfWriter.getInstance(document, new FileOutputStream(path));

		// Creacion del parrafo 1
		Paragraph paragraph1 = new Paragraph("Generalitat de Catalunya\nDepartament d'Ensenyament", BLACK_BOLD);
		paragraph1.setIndentationLeft(40);
		paragraph1.setSpacingBefore(11);

		// Creacion del parrafo 2
		Paragraph paragraph2 = new Paragraph("Institut Puig Castellar\nSanta Coloma de Gramenet", BLACK_BOLD);
		paragraph2.setSpacingBefore(-25);
		paragraph2.setIndentationLeft(205);

		// Creacion del parrafo 3
		Paragraph paragraph3 = new Paragraph("Anselm de Riu, 10\nTelèfon 93 391 61 11", BLACK_BOLD);
		paragraph3.setSpacingBefore(-25);
		paragraph3.setIndentationLeft(380);

		// Creacion de la cabecera
		Paragraph headerWarning = new Paragraph("NOTIFICACIÓ DE FALTA", HEADER);
		headerWarning.setIndentationLeft(180);

		// Obtencion del nombre del alumno
		String studentName = query[0] + " " + query[1];
		Paragraph student = new Paragraph("ALUMNE: ", CAPS);
		student.setIndentationLeft(25);
		student.setSpacingBefore(10);
		Paragraph campStudent = new Paragraph(studentName, DADES);
		campStudent.setIndentationLeft(75);
		campStudent.setSpacingBefore(-12);

		// Obtencion del grupo del alumno
		String groupStudent = query[2];
		// CAMP ALUMNE
		Paragraph group = new Paragraph("GRUP: ", CAPS);
		group.setIndentationLeft(220);
		group.setSpacingBefore(-13);
		Paragraph campGrup = new Paragraph(groupStudent, DADES);
		campGrup.setIndentationLeft(255);
		campGrup.setSpacingBefore(-13);

		// Obtencion de la hora del parte
		String dateStudent = dateWarning;
		Paragraph data = new Paragraph("DATA: ", CAPS);
		data.setIndentationLeft(340);
		data.setSpacingBefore(-13);
		Paragraph campData = new Paragraph(dateStudent, DADES);
		campData.setIndentationLeft(380);
		campData.setSpacingBefore(-12);

		String studentSubject;

		if (query[5] == null) {
			studentSubject = " ";
		} else {
			studentSubject = query[5];
		}

		// Obtencion de la materia del parte
		Paragraph subject = new Paragraph("MATÈRIA: ", CAPS);
		subject.setIndentationLeft(25);
		Paragraph campSubject = new Paragraph(studentSubject, DADES);
		campSubject.setIndentationLeft(75);
		campSubject.setSpacingBefore(-12);

		// Obtencion de la hora del parte
		Paragraph hour = new Paragraph("HORA: ", CAPS);
		hour.setIndentationLeft(195);
		hour.setSpacingBefore(-13);
		Paragraph campHour = new Paragraph(hourWarning, DADES);
		campHour.setIndentationLeft(230);
		campHour.setSpacingBefore(-12);

		// Obtencion de la circumstancia en la que se impone el parte
		String cirAlumne = query[4];
		Paragraph circumstance = new Paragraph("CIRCUMSTÀNCIA(aula,pati,etc.): ", CAPS);
		circumstance.setIndentationLeft(280);
		circumstance.setSpacingBefore(-13);
		Paragraph campCirc = new Paragraph(cirAlumne, DADES);
		campCirc.setIndentationLeft(430);
		campCirc.setSpacingBefore(-12);

		// Obtención del nombre del profesor
		String teacherName = teacherJPA.currentTeacher();

		if (query[12] != null) {
			teacherName = query[12];
		}

		if (query[12] == "null") {
			teacherName = teacherJPA.currentTeacher();
		}

		Paragraph professor = new Paragraph("PROFESSOR: ", CAPS);
		professor.setIndentationLeft(25);
		Paragraph campProfe = new Paragraph(teacherName, DADES);
		campProfe.setIndentationLeft(90);
		campProfe.setSpacingBefore(-12);

		// Obtención del nombre del profesor
		String tutorStudent = nametutor;
		Paragraph tutor = new Paragraph("TUTOR: ", CAPS);
		tutor.setIndentationLeft(220);
		tutor.setSpacingBefore(-13);
		Paragraph campTutor = new Paragraph(tutorStudent, DADES);
		campTutor.setIndentationLeft(260);
		campTutor.setSpacingBefore(-12);

		String warningLevel = null;
		// Prueba
		if (query[7].equals("true")) {
			warningLevel = "[ ]EXPULSAT DE CLASSE\n[X]AMONESTAT";
		} else {
			warningLevel = "[X]EXPULSAT DE CLASSE\n[ ]AMONESTAT";

		}

		// Motius seleccionats per el Profesor/Tutor
		Paragraph level = new Paragraph(
				"En compliment de la Normativa de Règim Intern vigent, l'esmentat alumne ha estat: ", DADES);
		level.setIndentationLeft(25);

		Paragraph campGrav = new Paragraph(warningLevel, DADES);
		campGrav.setIndentationLeft(330);
		campGrav.setSpacingBefore(-12);

		String[] reasons = { "Faltar al respecte al professor", "Faltar al respecte a algún company",
				"Acumulació d'amonestacions lleus", "Causar dany o desperfecte material",
				"Negar-se a realitzar treballs encomanats", "Molestar o distreure de forma reiterada als companys" };

		String queryCon = query[9].concat(query[11]).trim().replace("][", ",");

		String subQuery = queryCon.substring(1, queryCon.length() - 1).trim();

		String[] userReasons = subQuery.split(",");

		// Bloc1Left
		String reason1 = null;
		String reason3 = null;
		String reason5 = null;
		// Bloc2Right
		String reason2 = null;
		String reason4 = null;
		String reason6 = null;

		List reasonsList = new ArrayList<>();

		// Comparación de los campos seleccionados para poder determinar cuales
		// marcar con [X]
		for (int i = 0; i < userReasons.length; i++) {
			userReasons[i] = userReasons[i].trim();

		}

		for (int i = 0; i < reasons.length; i++) {

			// System.out.println(motivos[i].trim());
			boolean marcat = false;

			for (int j = 0; j < userReasons.length; j++) {

				marcat = marcat || reasons[i].equals(userReasons[j]);

			}
			if (marcat) {
				reasonsList.add("[X]" + reasons[i].trim());

			} else {

				reasonsList.add("[  ]" + reasons[i].trim());

			}
		}

		// Bloc 1
		if (reasonsList.contains("[X]Faltar al respecte al professor")) {

			reason1 = "[X]Faltar al respecte al professor";

		} else {

			reason1 = "[ ]Faltar al respecte al professor";

		}

		if (reasonsList.contains("[X]Faltar al respecte a algún company")) {

			reason3 = "[X]Faltar al respecte a algún company";

		} else {

			reason3 = "[ ]Faltar al respecte a algún company";

		}

		if (reasonsList.contains("[X]Acumulació d'amonestacions lleus")) {

			reason5 = "[X]Acumulació d'amonestacions lleus";

		} else {

			reason5 = "[ ]Acumulació d'amonestacions lleus";

		}

		// Bloc2

		if (reasonsList.contains("[X]Causar dany o desperfecte material")) {

			reason2 = "[X]Causar dany o desperfecte material";

		} else {

			reason2 = "[ ]Causar dany o desperfecte material";

		}

		if (reasonsList.contains("[X]Negar-se a realitzar treballs encomanats")) {

			reason4 = "[X]Negar-se a realitzar treballs encomanats";

		} else {

			reason4 = "[ ]Negar-se a realitzar treballs encomanats";

		}

		if (reasonsList.contains("[X]Molestar o distreure de forma reiterada als companys")) {

			reason6 = "[X]Molestar o distreure de forma reiterada als companys";

		} else {

			reason6 = "[ ]Molestar o distreure de forma reiterada als companys";

		}

		// Obtención de otros motivos
		String reason10 = null;

		if (query[10].equals("")) {

			reason10 = "[  ]Altres motius:";

		} else {
			reason10 = "[X]Altres motius:";

		}

		// CAMP 2part
		Paragraph pReasons = new Paragraph("Pel següent motiu: ", DADES);
		pReasons.setIndentationLeft(25);

		Paragraph pReason1 = new Paragraph(reason1, DADES);
		pReason1.setIndentationLeft(95);
		pReason1.setSpacingBefore(-12);

		Paragraph pReason2 = new Paragraph(reason2, DADES);
		pReason2.setIndentationLeft(235);
		pReason2.setSpacingBefore(-12);

		Paragraph pReason3 = new Paragraph(reason3, DADES);
		pReason3.setIndentationLeft(95);

		Paragraph pReason4 = new Paragraph(reason4, DADES);
		pReason4.setIndentationLeft(235);
		pReason4.setSpacingBefore(-12);

		Paragraph pReason5 = new Paragraph(reason5, DADES);
		pReason5.setIndentationLeft(95);

		Paragraph pReason6 = new Paragraph(reason6, DADES);
		pReason6.setIndentationLeft(235);
		pReason6.setSpacingBefore(-12);

		Paragraph pReason10 = new Paragraph(reason10, DADES);
		pReason10.setIndentationLeft(95);

		Paragraph campReason10 = new Paragraph(query[10] + " ", DADES);
		campReason10.setIndentationLeft(160);
		campReason10.setSpacingBefore(-12);
		campReason10.setSpacingAfter(10);

		String lineCamp = "     __________________________________________________________________________________________________________";
		Paragraph line = new Paragraph(lineCamp, DADES);
		line.setSpacingAfter(5);

		Paragraph teacherSign = new Paragraph("Signatura del professor\nque impossa l'amonestació", SUBLINE);
		teacherSign.setIndentationLeft(25);

		Paragraph fathersSign = new Paragraph("Signatura dels pares", SUBLINE);
		fathersSign.setIndentationLeft(210);
		fathersSign.setSpacingBefore(-19);

		Paragraph studentSign = new Paragraph("Signatura de l'alumne", SUBLINE);
		studentSign.setIndentationLeft(410);
		studentSign.setSpacingBefore(-9);
		studentSign.setSpacingAfter(35);

		Paragraph alert = new Paragraph("(A retornar signat al Professor el dia següent.)", SUBLINE);
		alert.setIndentationLeft(410);

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(80 / 2f);
		PdfPCell cell;
		String seriousChar = null;
		String mildChar = null;

		if (query[3].equals("Lleu")) {
			seriousChar = "    [ ]GREU";
			mildChar = "    [X]LLEU";
		} else if (query[3].equals("Greu")) {
			seriousChar = "    [X]GREU";
			mildChar = "    [ ]LLEU";
		}

		cell = new PdfPCell(new Phrase("CARÀCTER DE LA FALTA", DADES));
		cell.setColspan(1);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("" + seriousChar + "" + mildChar, DADES));
		cell.setRowspan(2);
		table.addCell(cell);

		// Añadimos todos los elementos obtenidos al documento

		document.open();
		document.add(img);
		document.add(paragraph1);
		document.add(paragraph2);
		document.add(paragraph3);
		document.add(headerWarning);
		document.add(student);
		document.add(campStudent);
		document.add(group);
		document.add(campGrup);
		document.add(data);
		document.add(campData);
		document.add(subject);
		document.add(campSubject);
		document.add(hour);
		document.add(campHour);
		document.add(circumstance);
		document.add(campCirc);
		document.add(professor);
		document.add(campProfe);
		document.add(tutor);
		document.add(campTutor);
		document.add(level);
		document.add(campGrav);
		document.add(pReasons);
		document.add(pReason1);
		document.add(pReason2);
		document.add(pReason3);
		document.add(pReason4);
		document.add(pReason5);
		document.add(pReason6);

		document.add(pReason10);
		document.add(campReason10);
		document.add(table);
		document.add(line);
		document.add(teacherSign);
		document.add(fathersSign);
		document.add(studentSign);
		document.add(alert);

		document.add(preface);
		document.close();

		String[] re = { path, dateWarning, hourWarning };

		return re;
	}

	/**
	 * Método que selecciona la imagen en la ruta definida i le otorgará unos
	 * valores de de tamaño, posición y escala predeterminados
	 * 
	 * @return Una Imagen
	 * @throws IOException
	 * @throws BadElementException
	 */
	private Image logoImage() throws IOException, BadElementException {
		// TODO Auto-generated method stub

		Image img = Image.getInstance(String.format(rb.getString("logo_pdf")));
		img.setWidthPercentage(50);
		// X - Y
		img.setAbsolutePosition(45, 770);
		img.scaleToFit(25, 25);

		return img;
	}

	/**
	 * Método que crea una celda para la imagen
	 * 
	 * @param path
	 * @return cell
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
		Image img = Image.getInstance(path);
		PdfPCell cell = new PdfPCell(img, true);
		return cell;
	}

	/**
	 * Método que retorna la ruta donde esta alojado el PDF
	 * 
	 * @param nomCognom
	 * @param fecha
	 * @return
	 * @throws IOException
	 */
	public String getPath2(String nomCognom, String fecha) throws IOException {

		return rb.getString("path_warning") + "amonestacio(" + fecha + ")(" + nomCognom + ").pdf";

	}
}