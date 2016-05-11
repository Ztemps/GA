package com.example.Logic;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.Entities.Teacher;

public class TeachersJPAManager {
	Teacher teacher; 
	private EntityManager em = EntityManagerUtil.getEntityManager();

	public void addTeacher(Teacher teacher) {

		em.getTransaction().begin();
		em.persist(teacher);
		em.getTransaction().commit();
	};
	
	public void updateTeacher(Teacher teacher) {

		em.getTransaction().begin();
		em.merge(teacher);
		em.getTransaction().commit();
	}
	
	public void removeTeacher(Teacher teacher) {

		em.getTransaction().begin();
		em.remove(teacher);
		em.getTransaction().commit();
	}
	
	
	public int getIdDocent(String email) {
		// return (Docent) em.createQuery("SELECT c FROM Customer c WHERE c.name
		// LIKE :email")
		// .setParameter("email", email)
		// .getSingleResult();

		Query query = em.createNativeQuery("SELECT id FROM docent where email LIKE #email", Teacher.class);
		query.setParameter("email", email);
		Teacher docent = (Teacher) query.getSingleResult();

		// em.getTransaction().commit();
		// em.close();
		// em.flush();
		return docent.getId();
	}
	
	public void closeTransaction() {

		em.close();

	};
}
