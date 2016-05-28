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
 *         datos de la tabla tutor mediante JPA
 * 
 *******************************************************************************/
public class TutorJPAManager {

	private User user;
	private List<User> list_users;
	private List<Group> list_grups;
	boolean amonestat2 = false;
	boolean expulsat = false;
	boolean gravetat = false;

	private EntityManagerUtil entman;
	private EntityManager em;

	/**
	 * Este método es el constructor del JPA manager en el que se obtiene la
	 * conexión con la base da datos.
	 */
	public TutorJPAManager() {
		// TODO Auto-generated constructor stub
		entman = new EntityManagerUtil();
		em = entman.getEntityManager();
	}

	/**
	 * Este método se encarga de obtener el id del docent que es tutor a partir
	 * del grupo.
	 * 
	 * @param grup
	 *            Es el grupo del que se quiere obtener el id del tutor
	 * @return Devuelve el id del docent que es tutor
	 */
	public int getIdTutor(String grup) {

		Query query = em.createNativeQuery("SELECT docent FROM tutor where grup LIKE #grup", Tutor.class);
		query.setParameter("grup", grup);
		//
		Tutor tutor = (Tutor) query.getSingleResult();

		return tutor.getDocent();
	}
	

	/**
	 * Este método se encarga de obtener el nombre y apellidos del docente que
	 * es tutor mediante el id del docente.
	 * 
	 * @param id
	 *            Este parámetro es el id del docente del cual se buscará el
	 *            nombre y apellidos
	 * @return Devuelve el nombre y apellidos separados por un espacio
	 */
	public String getNomTutor(int id) {

		Query query = em.createNativeQuery("SELECT * FROM docent where id = #id", Teacher.class);
		query.setParameter("id", id);
		//
		Teacher docent = (Teacher) query.getSingleResult();

		System.out.println(docent.toString());

		return docent.getNom() + " " + docent.getCognoms();
	}

	/**
	 * Este método se encarga de obtener el nombre del docente que es tutor
	 * mediante el id del docente.
	 * 
	 * @param id
	 *            Este parámetro es el id del docente del cual se buscará el
	 *            nombre
	 * @return Devuelve el nombre del docente
	 */
	public String getNomTutorHeader(int id) {

		Query query = em.createNativeQuery("SELECT * FROM docent where id = #id", Teacher.class);
		query.setParameter("id", id);
		//
		Teacher docent = (Teacher) query.getSingleResult();

		System.out.println(docent.toString());

		return docent.getNom();
	}

	/**
	 * Este método se encarga de añadir un tutor en la tabla tutor de la base de
	 * datos a través de un objeto de tipo Tutor.
	 * 
	 * @param tutor
	 *            Éste es el objeto Tutor que se quiere guardar en la base de
	 *            datos
	 */
	public void addTutor(Tutor tutor) {
		// TODO Auto-generated method stub
		em.getTransaction().begin();
		em.persist(tutor);
		em.getTransaction().commit();
	}

	/**
	 * Este método cierra las conexiones con la base de datos.
	 */
	public void closeTransaction() {

		em.close();

	}

}
