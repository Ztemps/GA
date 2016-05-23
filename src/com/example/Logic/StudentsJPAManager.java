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

import java.io.IOException;
import java.net.MalformedURLException;

import javax.persistence.EntityManager;

import com.example.Entities.Student;
import com.example.Entities.Warning;

public class StudentsJPAManager {
	private Student alumne;
	private EntityManagerUtil entman;
	private EntityManager em;

	/**
	 * 
	 */
	public StudentsJPAManager() {
		// TODO Auto-generated constructor stub

		entman = new EntityManagerUtil();
		em = entman.getEntityManager();
	}

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
