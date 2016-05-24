package com.example.Logic;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.persistence.EntityManager;

import com.example.Entities.Student;
import com.example.Entities.Warning;

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
 *         Esta clase se encarga de gestionar las operacions con la base de
 *         datos de la tabla alumne mediante JPA
 * 
 *******************************************************************************/
public class StudentsJPAManager {
	private Student alumne;
	private EntityManagerUtil entman;
	private EntityManager em;

	/**
	 * Este método es el constructor del JPA manager en el que se obtiene la
	 * conexión con la base da datos.
	 */
	public StudentsJPAManager() {
		// TODO Auto-generated constructor stub

		entman = new EntityManagerUtil();
		em = entman.getEntityManager();
	}

	/**
	 * Este método se encarga de añadir un alumno en la tabla alumne de la base
	 * de datos a través de un objeto de tipo Student.
	 * 
	 * @param alumne
	 *            Éste es el objeto Student que se quiere guardar en la base de
	 *            datos
	 */
	public void addStudent(Student alumne) {

		em.getTransaction().begin();
		em.persist(alumne);
		em.getTransaction().commit();
	};

	/**
	 * Este método se encarga de editar un alumno en la tabla alumne de la base
	 * de datos a través de un objeto de tipo Student.
	 * 
	 * @param alumne
	 *            Éste es el objeto Student que se quiere editar de la base de
	 *            datos
	 */
	public void updateStudent(Student alumne) {

		em.getTransaction().begin();
		em.merge(alumne);
		em.getTransaction().commit();
	}

	/**
	 * Este método se encarga de eliminar un alumno en la tabla alumne de la
	 * base de datos a través de un objeto de tipo Student.
	 * 
	 * @param alumne
	 *            Éste es el objeto Student que se quiere eliminar de la base de
	 *            datos
	 */
	public void removeStudent(Student alumne) {

		em.getTransaction().begin();
		em.remove(alumne);
		em.getTransaction().commit();
	}

	/**
	 * Este método cierra las conexiones con la base de datos.
	 */
	public void closeTransaction() {

		em.close();

	};

}
