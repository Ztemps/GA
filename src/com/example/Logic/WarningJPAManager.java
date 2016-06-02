/*******************************************************************************
 * 
 * Gestió d'Amonestacions v1.0
 *
 * Esta obra está sujeta a la licencia Reconocimiento-NoComercial-SinObraDerivada 4.0 Internacional de Creative Commons. 
 * Para ver una copia de esta licencia, visite http://creativecommons.org/licenses/by-nc-nd/4.0/.
 *  
 * @author Francisco Javier Casado Moreno - fcasado@elpuig.xeill.net 
 * @author Daniel Pérez Palacino - dperez@elpuig.xeill.net 
 * @author Gerard Enrique Paulino Decena - gpaulino@elpuig.xeill.net 
 * @author Xavier Murcia Gámez - xmurcia@elpuig.xeill.net 
 * 
 *    Esta clase se encarga de gestionar las operacions con la base de
 *         datos referentes a las amonestaciones mediante JPA.
 *         
 *******************************************************************************/
package com.example.Logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.postgresql.jdbc2.optional.SimpleDataSource;

import com.example.Entities.Student;
import com.example.Entities.Teacher;
import com.example.Entities.Tutor;
import com.example.Entities.User;
import com.example.Entities.Warning;
import com.example.Pdf.generatePDF;
import com.example.SendMail.sendMail;
import com.example.SendTelegram.SendTelegram;
import com.example.ga.GaUI;
import com.itextpdf.text.DocumentException;

import java_cup.parse_action;

public class WarningJPAManager {
	final static String DATEFORMAT = "yyyy-MM-dd HH:mm";
	boolean amonestat2 = false;
	private User user;
	boolean expulsat = false;
	boolean gravetat = false;
	private EntityManagerUtil entman;
	private EntityManager em;
	private SendTelegram sendTel;
	private sendMail sendMail;
	private TeachersJPAManager teacherJPA;
	private TutorJPAManager tutorJPA;
	private StudentsJPAManager studentJPA;
	private DateFormat dateFormat;
	private boolean checkTutor = false;
	private boolean checkPares = false;
	private boolean checkTelegram = false;
	private ResourceBundle rb = ResourceBundle.getBundle("GA");

	/**
	 * Este método es el constructor WarningJPAManager que controla los accesos
	 * a la base de datos mediante JPA. Principalmente esta clase se usa para
	 * gestionar las amonestaciones.
	 */
	public WarningJPAManager() {
		// TODO Auto-generated constructor stub
		entman = new EntityManagerUtil();
		em = entman.getEntityManager();
		sendTel = new SendTelegram();
	}

	/**
	 * Este método introduce una amonestación en la base de datos a partir de un
	 * Array de Strings
	 * 
	 * @param query
	 *            Este es el parámetro que se utiliza para obtener toda la
	 *            información referente a la amonestación que se va a introducir
	 *            en la base de datos.
	 */
	public void introducirParte(String[] query)
			throws MalformedURLException, DocumentException, IOException, ParseException {

		teacherJPA = new TeachersJPAManager();
		studentJPA = new StudentsJPAManager();
		tutorJPA = new TutorJPAManager();
		dateFormat = new SimpleDateFormat(DATEFORMAT);
		Date date;

		try {
			if (query[7].equals("true")) {
				amonestat2 = true;
				expulsat = false;
			} else {
				amonestat2 = false;
				expulsat = true;
			}

		} catch (NullPointerException e) {

		}

		String queryCon = query[9].concat(query[11]).trim().replace("][", ", ");

		String querycon = queryCon.substring(1, queryCon.length() - 1).trim();

		// OBTENER ALUMNO
		Student al = studentJPA.ObtenerAlumno(query[0], query[1]);
		// OBTENER TUTOR
		int tutor = tutorJPA.getIdTutor(al.getGrup());
		// OBTENER PERSONA QUE REALIZA EL PARTE
		user = teacherJPA.getIdCurrentDocent(currentUser());

		date = new Date();

		FileReader reader;
		String linea = null;

		File f = new File(rb.getString("file_settings"));

		try {
			if (!f.exists()) {
				f.createNewFile();
			}

			BufferedReader br = new BufferedReader(new FileReader(f));
			if (br.readLine() == null) {

			} else {

				reader = new FileReader(f);
				BufferedReader flux = new BufferedReader(reader);

				while ((linea = flux.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(linea, ",");
					while (st.hasMoreTokens()) {

						String aux1 = st.nextToken();
						String aux2 = st.nextToken();
						String aux3 = st.nextToken();
						String aux4 = st.nextToken();
						String aux5 = st.nextToken();
						String aux6 = st.nextToken();

						if (st.nextToken().equals("true")) {
							checkTutor = true;
						} else {
							checkTutor = false;
						}

						if (st.nextToken().equals("true")) {
							checkPares = true;
						} else {
							checkPares = false;
						}

						if (st.nextToken().equals("true")) {
							checkTelegram = true;
						} else {
							checkTelegram = false;

						}

					}
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String fecha = query[14].substring(6, 10)+"-"+query[14].substring(3, 5)+"-"+query[14].substring(0, 2)+ " " + query[15];
		
		addWarning(new Warning(user.getId(), dateFormat.parse(fecha), query[2], al.getId(), query[3], query[4],
				query[5], tutor, amonestat2, expulsat, "15/16", querycon, query[10]));

		try{
			
			if (al.getEmail().contains("@")) {
				sendMail = new sendMail(al.getEmail(), "El seu fill " + query[0] + " " + query[1] + " a sigut amonestat ",
						query[12]);
			}
			
		}catch(NullPointerException e){
			
		}
		
	}

	/**
	 * Este método introduce una amonestación en la base de datos.
	 * 
	 * @param warning
	 *            Este es el parámetro que se utiliza para introducir el objecto
	 *            Warning a la base de datos.
	 */
	public void addWarning(Warning warning) {

		em.getTransaction().begin();
		em.persist(warning);
		em.getTransaction().commit();
		em.close();
	};

	/**
	 * Este método devuelve el nombre del usuario actual
	 *
	 * @return Objeto de tipo string
	 * 
	 */

	public String currentUser() {

		return String.valueOf(GaUI.getCurrent().getSession().getAttribute("user"));
	}

	public void closeTransaction() {
		em.close();
	}
}
