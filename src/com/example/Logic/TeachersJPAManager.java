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
import com.example.ga.GaUI;

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
	private TutorJPAManager tutorJPA;

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
	 * Este método devuelve el nombre del profesor actual que impone una amonestación
	 * 
	 * @return String Devuelve el nombre del profesor que pone la amonestación.
	 */
	public String currentTeacherName() {
		int id = 0;
		tutorJPA = new TutorJPAManager();
		try {
			id = Integer.parseInt(String.valueOf(GaUI.getCurrent().getSession().getAttribute("id")));
		} catch (NullPointerException | NumberFormatException e) {

			return null;
		}

		return tutorJPA.getNomTutorHeader(id);

	}
	
	/**
	 * Este método devuelve el objecto usuario del profesor actual.
	 * 
	 * @param currentUser Parámetor que recoge una string del usuario actual para poder devolver el objecto
	 * 
	 * @return User Objecto usuario actual
	 */
	
	public User getIdCurrentDocent(String currentUser) {

		Query query = em.createNativeQuery("SELECT id_docent FROM usuari where usuari  LIKE #currentUser", User.class);
		query.setParameter("currentUser", currentUser);
		//
		User user = (User) query.getSingleResult();

		return user;
	}
	
	/**
	 * Este método devuelve una string del nombre del professor actual
	 * 
	 * @return String Devuelve el nombre del profesor actual
	 */
	public String currentTeacher() {

		tutorJPA = new TutorJPAManager();
		
		return tutorJPA.getNomTutor(Integer.parseInt(String.valueOf(GaUI.getCurrent().getSession().getAttribute("id"))));

	}
	
	/**
	 * Este método cierra las conexiones con la base de datos.
	 */
	public void closeTransaction() {

		em.close();

	};
}
