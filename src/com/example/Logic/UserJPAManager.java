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

	private EntityManagerUtil entman = new EntityManagerUtil();
	private EntityManager em;

	public UserJPAManager(){
		em = entman.getEntityManager();
	}

	public void addUser(User user) {
		// EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(user);
		em.getTransaction().commit();
	}
	

	public void updateUser(Integer id, String password) {
		em.getTransaction().begin();
		User user = (User) em.find(User.class, id );
		user.setPassword(password);
		em.merge(user);
		em.getTransaction().commit();
		em.close();
	}

	

	public void addWarning(Warning amonestacio) {

		// if(em.isOpen()){
		// em.close();
		// }else{
		em.getTransaction().begin();
		em.persist(amonestacio);
		em.getTransaction().commit();
		// }
	};

	// public List<Doctor> listDoctors() {
	//
	// try {
	//
	//// em.getTransaction().begin();
	//// list_doctors = em.createQuery("Select e From Doctor
	// e").getResultList();
	////
	//// em.getTransaction().commit();
	//// return list_doctors;
	//
	// } catch (Exception e) {
	// em.getTransaction().rollback();
	// }
	//
	// //return list_doctors;
	// }

	/* Method to READ all Autors */
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

		return getNomTutor(
				Integer.parseInt(String.valueOf(GaUI.getCurrent().getSession().getAttribute("id"))));

	}
	
	public String currentTeacherName() {

		return getNomTutorHeader(
				Integer.parseInt(String.valueOf(GaUI.getCurrent().getSession().getAttribute("id"))));

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

	/**
	 * for (Iterator<User> iterator = list_users.iterator();
	 * iterator.hasNext();) { // // User usuario = (User) iterator.next(); //
	 * System.out.println(usuario.toString()); // }
	 **/

}
