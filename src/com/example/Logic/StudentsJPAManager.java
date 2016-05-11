package com.example.Logic;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.persistence.EntityManager;

import com.example.Entities.Student;
import com.example.Entities.Warning;

public class StudentsJPAManager {
	private Student alumne; 
	private EntityManagerUtil entman = new EntityManagerUtil();
	private EntityManager em = entman.getEntityManager();

	public void addStudent(Student alumne) {

		em.getTransaction().begin();
		em.persist(alumne);
		em.getTransaction().commit();
	};
	
	public void updateStudent(Student alumne) {

		em.getTransaction().begin();
		em.merge(alumne);
		em.getTransaction().commit();
	}
	
	public void removeStudent(Student alumne) {

		em.getTransaction().begin();
		em.remove(alumne);
		em.getTransaction().commit();
	}
	
	
	public void closeTransaction() {

		em.close();

	};

}
