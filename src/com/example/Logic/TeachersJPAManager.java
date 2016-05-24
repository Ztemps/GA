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
 *         datos de la tabla docent mediante JPA
 * 
 *******************************************************************************/
public class TeachersJPAManager {

	private EntityManagerUtil entman;
	private EntityManager em;
	private List<Teacher> listTeachers;

	/**
	 * Este método es el constructor del JPA manager en el que se obtiene la
	 * conexión con la base da datos.
	 */
	public TeachersJPAManager() {
		// TODO Auto-generated constructor stub
		entman = new EntityManagerUtil();
		em = entman.getEntityManager();

	}

	/**
	 * Este método se encarga de añadir un docente en la tabla docent de la base
	 * de datos a través de un objeto de tipo Teacher.
	 * 
	 * @param teacher
	 *            Éste es el objeto Teacher que se quiere guardar en la base de
	 *            datos
	 */
	public void addTeacher(Teacher teacher) {

		em.getTransaction().begin();
		em.persist(teacher);
		em.getTransaction().commit();
	}

	/**
	 * Este método se encarga de editar un docente en la tabla docent de la base
	 * de datos a través de un objeto de tipo Teacher.
	 * 
	 * @param teacher
	 *            Éste es el objeto Teacher que se quiere editar en la base de
	 *            datos
	 */
	public void updateTeacher(Teacher teacher) {

		em.getTransaction().begin();
		em.merge(teacher);
		em.getTransaction().commit();
	}

	/**
	 * Este método se encarga de eliminar un docente en la tabla docent de la
	 * base de datos a través de un objeto de tipo Teacher.
	 * 
	 * @param teacher
	 *            Éste es el objeto Teacher que se quiere eliminar en la base de
	 *            datos
	 */
	public void removeTeacher(Teacher teacher) {

		em.getTransaction().begin();
		em.remove(teacher);
		em.getTransaction().commit();
	}

	/**
	 * Este método se encarga de obtener el id del docente mediante el email.
	 * 
	 * @param email
	 *            Este parámetro es el que se utiliza para buscar el id del
	 *            docente
	 * @return Devuelve el id del docente
	 */
	public int getIdDocent(String email) {

		Query query = em.createNativeQuery("SELECT id FROM docent where email LIKE #email", Teacher.class);
		query.setParameter("email", email);
		Teacher docent = (Teacher) query.getSingleResult();

		return docent.getId();
	}

	/**
	 * Este método se encarga de obtener la lista de todos los docentes de la
	 * base de datos.
	 * 
	 * @return listTeachers Devuelve la lista de docentes
	 */
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

	/**
	 * Este método cierra las conexiones con la base de datos.
	 */
	public void closeTransaction() {

		em.close();

	};
}
