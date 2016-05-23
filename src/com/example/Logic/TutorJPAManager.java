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

	private User user;
	private List<User> list_users;
	private List<Group> list_grups;
	boolean amonestat2 = false;
	boolean expulsat = false;
	boolean gravetat = false;

	private EntityManagerUtil entman;
	private EntityManager em;

	public TutorJPAManager() {
		// TODO Auto-generated constructor stub
		entman = new EntityManagerUtil();
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

}
