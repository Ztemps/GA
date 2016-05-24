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

	private DateFormat dateFormat;
	private boolean checkTutor = false;
	private boolean checkPares = false;
	private boolean checkTelegram = false;
	private ResourceBundle rb = ResourceBundle.getBundle("GA");

	/**
	 * 
	 */
	public WarningJPAManager() {
		// TODO Auto-generated constructor stub
		entman = new EntityManagerUtil();
		em = entman.getEntityManager();
		sendTel = new SendTelegram();
	}

	public void introducirParte(String[] query)
			throws MalformedURLException, DocumentException, IOException, ParseException {

		
		dateFormat = new SimpleDateFormat(DATEFORMAT);
		Date date;

		try {
			if (query[7].equals("true")) {
				System.out.println("query7" + query[7]);
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
		Student al = ObtenerAlumno(query[0], query[1]);
		// OBTENER TUTOR
		int tutor = getIdTutor(al.getGrup());
		// OBTENER PERSONA QUE REALIZA EL PARTE
		user = getIdCurrentDocent(currentUser());

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

		if (checkPares) {
			try {

				if (al.getEmail().contains("@") || al.getEmail() != null) {
					sendMail = new sendMail(al.getEmail(),
							"El seu fill " + query[0] + " " + query[1] + " a sigut amonestat ", query[12]);
				}
			} catch (NullPointerException e) {

			}

		}

		String fecha = query[14] + " " + query[15];
		addWarning(new Warning(user.getId(), dateFormat.parse(fecha), query[2], al.getId(), query[3], query[4],
				query[5], tutor, amonestat2, expulsat, "15/16", querycon, query[10]));

	}

	public User getIdCurrentDocent(String currentUser) {

		Query query = em.createNativeQuery("SELECT id_docent FROM usuari where usuari  LIKE #currentUser", User.class);
		query.setParameter("currentUser", currentUser);
		//
		User user = (User) query.getSingleResult();

		return user;
	}

	public int getIdTutor(String grup) {

		Query query = em.createNativeQuery("SELECT docent FROM tutor where grup LIKE #grup", Tutor.class);
		query.setParameter("grup", grup);
		//
		Tutor tutor = (Tutor) query.getSingleResult();

		return tutor.getDocent();
	}

	public void addWarning(Warning amonestacio) {

		em.getTransaction().begin();
		em.persist(amonestacio);
		em.getTransaction().commit();
		em.close();
	};

	public Date getCurrentTimeStamp() {

		java.util.Date date = new java.util.Date();
		Timestamp data = new Timestamp(date.getTime());

		return data;

	}

	public Student ObtenerAlumno(String name, String surname) {

		Query query = em.createNativeQuery("SELECT id FROM alumne where nom LIKE #name AND cognoms LIKE #surname",
				Student.class);

		query.setParameter("name", name);
		query.setParameter("surname", surname);

		Student alumne = (Student) query.getSingleResult();

		return alumne;

	}

	public String currentUser() {

		return String.valueOf(GaUI.getCurrent().getSession().getAttribute("user"));
	}

}
