package com.example.Logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.Entities.Teacher;
import com.example.Entities.User;

public class TeachersJPAManager {

	private EntityManagerUtil entman = new EntityManagerUtil();
	private EntityManager em = entman.getEntityManager();
	private List<Teacher> listTeachers;

	
	
	


	public void addTeacher(Teacher teacher) {


			em.getTransaction().begin();
			em.persist(teacher);
			em.getTransaction().commit();
		}



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

	public List<Teacher> getNoms() {

		try {

			em.getTransaction().begin();
			listTeachers = em.createQuery("Select e From Teacher e").getResultList();

			em.getTransaction().commit();
			return listTeachers;

		} catch (Exception e) {
			em.getTransaction().rollback();
		}

		return listTeachers;

	}

	public void closeTransaction() {

		em.close();

	};
}
