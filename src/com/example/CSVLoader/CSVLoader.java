package com.example.CSVLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import com.example.Entities.Group;
import com.example.Entities.Student;
import com.example.Entities.Teacher;
import com.example.Entities.User;
import com.example.Logic.Cursos;
import com.example.Logic.GroupJPAManager;
import com.example.Logic.StudentsJPAManager;
import com.example.Logic.TeachersJPAManager;
import com.example.Logic.UserJPAManager;
import com.google.gwt.user.client.rpc.core.java.util.Arrays;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;

public class CSVLoader {
	static int option;
	static int id;
	static String cognoms, nom, grup, pais, nacionalitat, telefons;
	static String nom2,cognoms2,email,contrasenya,usuari;
	static java.sql.Date naixement;
	static StudentsJPAManager jpa;
	static UserJPAManager jpa2;
	static TeachersJPAManager jpa3;
	static GroupJPAManager jpaGroups;
	public CSVLoader() {
		jpa = new StudentsJPAManager();
		jpa2= new UserJPAManager();
		jpa3 = new TeachersJPAManager();
		jpaGroups = new GroupJPAManager();
	}

	public static void loaderStudents(File file) throws IOException, SQLException, ParseException {

		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "ISO-8859-3"));
		DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
		Date date;

		// Skip de la primera linea
		br.skip(br.readLine().indexOf(""));

		ArrayList<String> grups = new ArrayList<>();
		ArrayList<Student> students = new ArrayList<Student>();

		String linea = "";
		
		Image iconUser = new Image("/home/javi/Escritorio/no_user.png");

		while ((linea = br.readLine()) != null) {
			
			StringTokenizer str = new StringTokenizer(linea, ",\"\"");

			Image imageurl= new Image("/home/javi/Escritorio/no_user.png");
			id = Integer.parseInt(str.nextToken());
			cognoms = str.nextToken();
			nom = str.nextToken();
			grup = str.nextToken();
			date = dateformat.parse(str.nextToken());
			naixement = new java.sql.Date(date.getTime());
			pais = str.nextToken();
			nacionalitat = str.nextToken();
			telefons = str.nextToken();
			grups.add(grup);
			students.add(new Student(nom,cognoms,null,telefons,naixement,Cursos.ObtenerCursoActual(),grup,imageurl));
		}
		
		
		Set<String> linkedHashSet = new LinkedHashSet<String>();
		linkedHashSet.addAll(grups);
		grups.clear();
		grups.addAll(linkedHashSet);
		
		for (int i = 0; i<grups.size();i++){
			System.out.println(grups.get(i));
			jpaGroups.addGroup(new Group(grups.get(i)));

		}
		jpaGroups.closeTransaction();
System.out.println("====================== ALUMNES ========================");
		for (int i = 0; i<students.size();i++){
			System.out.println(students.get(i).toString());
			jpa.addStudent(students.get(i));

		}
		
		jpa.closeTransaction();
		
		 Notification notif = new Notification(
                 "ATENCIÓ:",
                 "<br>Dades Carregades| Agreements to: Dani Pérez<br/>",
                 Notification.Type.HUMANIZED_MESSAGE,
                 true); // Contains HTML

             // Customize it
		 notif.show(Page.getCurrent());
             notif.setDelayMsec(700);
             notif.setPosition(Position.TOP_CENTER);
		
		
	}
	
	
	public static void loaderTeachers(File file) throws IOException, SQLException, ParseException {

		
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "ISO-8859-3"));

		String linea = "";
		while ((linea = br.readLine()) != null) {

			StringTokenizer str = new StringTokenizer(linea, ",\"\"");

			nom2 = str.nextToken();
			cognoms2 = str.nextToken();
			email = str.nextToken();
			usuari = str.nextToken();
			contrasenya = str.nextToken();
			
			jpa2.addDocent(new Teacher(nom2,cognoms2,email));
			jpa2.addUser(new User(jpa3.getIdDocent(email),contrasenya,usuari,"Professor"));

		}
		jpa2.closeTransaction();
		
	
		
		 Notification notif = new Notification(
                 "ATENCIÓ:",
                 "<br>Dades Carregades| Agreements to: Dani Pérez<br/>",
                 Notification.Type.HUMANIZED_MESSAGE,
                 true); // Contains HTML

             // Customize it
		 notif.show(Page.getCurrent());
             notif.setDelayMsec(20000);
             notif.setPosition(Position.TOP_CENTER);
		
		
	}

}