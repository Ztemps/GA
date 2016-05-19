package com.example.Logic;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.Entities.Group;
import com.example.Entities.Student;
import com.example.Entities.Teacher;
import com.example.Entities.Tutor;
import com.example.Entities.User;
import com.example.Entities.Warning;
import com.example.ga.GaUI;

public class TutorJPAManager {

	User user;
	private List<User> list_users;
	private List<Group> list_grups;
	boolean amonestat2 = false;
	boolean expulsat = false;
	boolean gravetat = false;

	private EntityManagerUtil entman = new EntityManagerUtil();
	private EntityManager em;

	public TutorJPAManager() {
		// TODO Auto-generated constructor stub
		em = entman.getEntityManager();
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

	public void addTutor(Tutor tutor) {
		// TODO Auto-generated method stub
		em.getTransaction().begin();
		em.persist(tutor);
		em.getTransaction().commit();
	}

	public void closeTransaction() {

		em.close();

	}

	/**
	 * for (Iterator<User> iterator = list_users.iterator();
	 * iterator.hasNext();) { // // User usuario = (User) iterator.next(); //
	 * System.out.println(usuario.toString()); // }
	 **/

}
