package com.example.CSVLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import com.example.Entities.Group;
import com.example.Entities.Student;
import com.example.Entities.Teacher;
import com.example.Entities.Tutor;
import com.example.Entities.User;
import com.example.Logic.CurrentCourse;
import com.example.Logic.GroupJPAManager;
import com.example.Logic.StudentsJPAManager;
import com.example.Logic.TeachersJPAManager;
import com.example.Logic.TutorJPAManager;
import com.example.Logic.UserJPAManager;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

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
 * 
 *
 *         Esta clase se encarga de cargar masivamente alumnos, grupos,
 *         docentes, usuarios y tutores mediante un fichero csv
 * 
 *******************************************************************************/

public class CSVLoader {

	private final static String FORMAT = "ISO-8859-3";
	private final static String DATEFORMAT = "dd/MM/yyyy";
	private final static String ALGORITHM = "SHA-1";
	private final static String ROLTUTOR = "Tutor";
	private final static String ROLPROFESSOR = "Professor";
	private final static String NOTIF = "Dades carregades correctament";

	private int id;
	private String surname, name, group, country, nacionality, phone;
	private String email, password, user, tutoring;
	private Date born, date;
	private StudentsJPAManager studentsJPA;
	private UserJPAManager userJPA;
	private TutorJPAManager tutorJPA;
	private TeachersJPAManager teachersJPA;
	private GroupJPAManager groupsJPA;
	private MessageDigest messageDigest;
	private HexBinaryAdapter hBinaryAdapter;
	private BufferedReader bReader;
	private DateFormat dateFormat;
	private ArrayList<String> lGroups;
	private ArrayList<Student> lStudents;
	private Set<String> linkedHashSet;
	private String line = "";
	private String passwordHash;
	private CurrentCourse course;

	/**
	 * Este es el método main en el que se inician las conexiones con la base de
	 * datos para cada entidad.
	 * 
	 */
	public CSVLoader() {

		course = new CurrentCourse();
		studentsJPA = new StudentsJPAManager();
		userJPA = new UserJPAManager();
		teachersJPA = new TeachersJPAManager();
		groupsJPA = new GroupJPAManager();
		tutorJPA = new TutorJPAManager();
	}

	/**
	 * Este es el método en el que se cargan los alumnos en la base de datos
	 * además de los grupos.
	 * 
	 * @param file
	 *            Este parámetro es el fichero que se descarga el programa a
	 *            partir del fichero elejido por el usuario para posteriormente
	 *            cargarlo en la base de datos
	 * 
	 */

	public void loadStudents(File file) throws IOException, SQLException, ParseException {

		bReader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), FORMAT));
		dateFormat = new SimpleDateFormat(DATEFORMAT);

		// Skip first line
		bReader.skip(bReader.readLine().indexOf(""));

		lGroups = new ArrayList<String>();
		lStudents = new ArrayList<Student>();

		String linea = "";

		while ((linea = bReader.readLine()) != null) {

			StringTokenizer str = new StringTokenizer(linea, ",\"\"");

			id = Integer.parseInt(str.nextToken());
			surname = str.nextToken();
			name = str.nextToken();
			group = str.nextToken();
			date = dateFormat.parse(str.nextToken());
			born = new java.sql.Date(date.getTime());
			country = str.nextToken();
			nacionality = str.nextToken();
			phone = str.nextToken();
			lGroups.add(group);

			// Value null is a email of parents
			lStudents.add(new Student(name, surname, null, phone, born, course.currentCourse(), group));

		}

		linkedHashSet = new LinkedHashSet<String>();
		linkedHashSet.addAll(lGroups);
		lGroups.clear();
		lGroups.addAll(linkedHashSet);

		// Add all groups in database
		for (int i = 0; i < lGroups.size(); i++) {
			groupsJPA.addGroup(new Group(lGroups.get(i)));
		}
		groupsJPA.closeTransaction();

		// Add all students in databaseF
		for (int i = 0; i < lStudents.size(); i++) {
			studentsJPA.addStudent(lStudents.get(i));
		}
		studentsJPA.closeTransaction();

		bReader.close();
		// Notification is loaded everything correctly
		notif(NOTIF);

	}

	/**
	 * Este es el método en el que se cargan los docentes en la base de datos
	 * además de los tutores y usuarios.
	 * 
	 * @param file
	 *            Este parámetro es el fichero que se descarga el programa a
	 *            partir del fichero elejido por el usuario para posteriormente
	 *            cargarlo en la base de datos
	 * 
	 */
	public void loadTeachers(File file) throws IOException, SQLException, ParseException, NoSuchAlgorithmException {

		messageDigest = MessageDigest.getInstance(ALGORITHM);
		hBinaryAdapter = new HexBinaryAdapter();

		bReader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), FORMAT));

		while ((line = bReader.readLine()) != null) {

			StringTokenizer str = new StringTokenizer(line, ",");

			name = str.nextToken();
			surname = str.nextToken();
			email = str.nextToken();
			user = str.nextToken();
			password = str.nextToken();
			tutoring = str.nextToken();

			teachersJPA.addTeacher(new Teacher(name, surname, email));

			passwordHash = hBinaryAdapter.marshal(messageDigest.digest(password.getBytes())).toLowerCase();

			if (!tutoring.equals("null")) {
				tutorJPA.addTutor(new Tutor(teachersJPA.getIdDocent(email), tutoring));
				userJPA.addUser(new User(teachersJPA.getIdDocent(email), passwordHash, user, ROLTUTOR));
			} else
				userJPA.addUser(new User(teachersJPA.getIdDocent(email), passwordHash, user, ROLPROFESSOR));

		}
		userJPA.closeTransaction();
		bReader.close();
		notif("Dades carregades correctament");

	}

	/**
	 * Este es el método en el que se declaran las notificaciones que el usuario
	 * verá en pantalla
	 * 
	 * @param mensaje
	 *            Este parámetro es la String que se mostrará por pantalla al
	 *            usuario
	 * 
	 * 
	 */

	public static void notif(String mensaje) {
		Notification notif = new Notification(mensaje, null, Notification.Type.HUMANIZED_MESSAGE, true); // Contains
		// Customize it
		notif.show(Page.getCurrent());
		notif.setDelayMsec(500);
		notif.setPosition(Position.TOP_CENTER);
	}
}