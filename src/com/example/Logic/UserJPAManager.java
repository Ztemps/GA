/*******************************************************************************
 * Gestió d'Amonestacions v1.0
 *
 * Esta obra está sujeta a la licencia Reconocimiento-NoComercial-SinObraDerivada 4.0 Internacional de Creative Commons. 
 * Para ver una copia de esta licencia, visite http://creativecommons.org/licenses/by-nc-nd/4.0/.
 *  
 * @author Francisco Javier Casado Moreno - fcasasdo@elpuig.xeill.net 
 * @author Daniel Pérez Palacino - dperez@elpuig.xeill.net 
 * @author Gerard Enrique Paulino Decena - gpaulino@elpuig.xeill.net 
 * @author Xavier Murcia Gámez - xmurica@elpuig.xeill.net 
 *******************************************************************************/
package com.example.Logic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.example.Entities.Group;
import com.example.Entities.Student;
import com.example.Entities.Teacher;
import com.example.Entities.Tutor;
import com.example.Entities.User;
import com.example.Entities.Warning;
import com.example.Pdf.generatePDF;
import com.example.ga.GaUI;
import com.itextpdf.text.DocumentException;

public class UserJPAManager {
	User user;
	private List<User> list_users;
	private List<Group> list_grups;
	boolean amonestat2 = false;
	boolean expulsat = false;
	boolean gravetat = false;

	private EntityManagerUtil entman;
	private EntityManager em;

	public UserJPAManager() {
		entman = new EntityManagerUtil();
		em = entman.getEntityManager();
	}

	public void addUser(User user) {
		em.getTransaction().begin();
		em.persist(user);
		em.getTransaction().commit();
	}

	public void updateUser(User user) {
		em.getTransaction().begin();
		User usuari = (User) em.find(User.class, user.getId());
		usuari.setPassword(user.getPassword());
		em.merge(usuari);
		em.getTransaction().commit();

	}

	public void addWarning(Warning amonestacio) {
		em.getTransaction().begin();
		em.persist(amonestacio);
		em.getTransaction().commit();
	};


	public List<User> listUsers() {

		try {

			em.getTransaction().begin();
			list_users = em.createQuery("Select e From User e").getResultList();

			em.getTransaction().commit();
			return list_users;

		} catch (Exception e) {
			em.getTransaction().rollback();
		}
		return list_users;

	}

	// ITERATOR LIST USERS

	public List<Group> listTeachers() {
		// TODO Auto-generated method stub

		try {

			em.getTransaction().begin();
			list_grups = em.createQuery("Select e From Docent e").getResultList();

			em.getTransaction().commit();
			return list_grups;

		} catch (Exception e) {
			em.getTransaction().rollback();
		}

		return null;
	}

	public Student ObtenerAlumno(String name, String surname) {

		Query query = em.createNativeQuery("SELECT id FROM alumne where nom LIKE #name AND cognoms LIKE #surname",
				Student.class);

		query.setParameter("name", name);
		query.setParameter("surname", surname);

		Student alumne = (Student) query.getSingleResult();

		return alumne;

	}

	public Date getCurrentTimeStamp() {

		java.util.Date date = new java.util.Date();
		Timestamp data = new Timestamp(date.getTime());

		return data;

	}

	public String getCurrentRol(String currentUser) {
		Object obj = null;
		System.out.println(currentUser);

		Query query = em.createQuery("SELECT rol FROM usuari WHERE usuari LIKE #currentUser", User.class);

		query.setParameter("currentUser", "Manolo");

		try {
			obj = query.getSingleResult();

		} catch (Exception e) {

		}

		// EntityManagerUtil.close();

		return obj.toString();

	}

	public User getIdCurrentDocent(String currentUser) {

		Query query = em.createNativeQuery("SELECT id_docent FROM usuari where usuari  LIKE #currentUser", User.class);
		query.setParameter("currentUser", currentUser);
		//
		User user = (User) query.getSingleResult();

		// EntityManagerUtil.close();

		return user;
	}

	public int getIdTutor(String grup) {

		Query query = em.createNativeQuery("SELECT docent FROM tutor where grup LIKE #grup", Tutor.class);
		query.setParameter("grup", grup);
		//
		Tutor tutor = (Tutor) query.getSingleResult();

		return tutor.getDocent();
	}

	public String getNomTutor(int id) {

		Query query = em.createNativeQuery("SELECT * FROM docent where id = #id", Teacher.class);
		query.setParameter("id", id);
		//
		Teacher docent = (Teacher) query.getSingleResult();

		System.out.println(docent.toString());

		return docent.getNom() + " " + docent.getCognoms();
	}

	public String getNomTutorHeader(int id) {

		Query query = em.createNativeQuery("SELECT * FROM docent where id = #id", Teacher.class);
		query.setParameter("id", id);
		//
		Teacher docent = (Teacher) query.getSingleResult();

		System.out.println(docent.toString());

		return docent.getNom();
	}

	public String currentUser() {

		return String.valueOf(GaUI.getCurrent().getSession().getAttribute("user"));
	}

	public String currentTeacher() {

		return getNomTutor(Integer.parseInt(String.valueOf(GaUI.getCurrent().getSession().getAttribute("id"))));

	}

	public String currentTeacherName() {
		int id = 0;
		try {
			id = Integer.parseInt(String.valueOf(GaUI.getCurrent().getSession().getAttribute("id")));
		} catch (NullPointerException | NumberFormatException e) {

			return null;
		}

		return getNomTutorHeader(id);

	}

	public void addStudent(Student al) {
		// TODO Auto-generated method stub
		em.getTransaction().begin();
		em.persist(al);
		em.getTransaction().commit();

	}

	public void addDocent(Teacher docent) {
		// TODO Auto-generated method stub
		em.getTransaction().begin();
		em.persist(docent);
		em.getTransaction().commit();
	}

	public void addTutor(Tutor tutor) {
		// TODO Auto-generated method stub
		em.getTransaction().begin();
		em.persist(tutor);
		em.getTransaction().commit();
	}

	public void closeTransaction() {

		em.close();

	}

	public void removeUser(User user) {
		// TODO Auto-generated method stub
		em.getTransaction().begin();
		User ss = (User) em.find(User.class, user.getId());
		em.remove(ss);
		em.getTransaction().commit();
	};


}
