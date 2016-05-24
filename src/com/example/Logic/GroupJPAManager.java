
package com.example.Logic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.Entities.Group;
import com.example.Entities.Student;
import com.example.Entities.Teacher;
import com.example.Entities.Tutor;

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
 *         datos de la tabla grup mediante JPA
 * 
 *******************************************************************************/
public class GroupJPAManager {
	private Group group;
	private EntityManagerUtil entman;
	private EntityManager em;
	private List<Group> listGroups;

	/**
	 * Este método es el constructor del JPA manager en el que se obtiene la
	 * conexión con la base da datos.
	 */
	public GroupJPAManager() {
		entman = new EntityManagerUtil();
		em = entman.getEntityManager();
	}

	/**
	 * Este método se encarga de añadir un grupo en la tabla grup de la base de
	 * datos a través de un objeto de tipo Group.
	 * 
	 * @param grup
	 *            Éste es el objeto Group que se quiere guardar en la base de
	 *            datos
	 */
	public void addGroup(Group grup) {

		em.getTransaction().begin();
		em.persist(grup);
		em.getTransaction().commit();
	};

	/**
	 * Este método se encarga de editar un grupo en la tabla grup de la base de
	 * datos a través de un objeto de tipo Group.
	 * 
	 * @param grup
	 *            Éste es el objeto Group que se quiere editar de la base de
	 *            datos
	 */
	public void updateGroup(Group grup) {

		em.getTransaction().begin();
		em.merge(grup);
		em.getTransaction().commit();
	}

	/**
	 * Este método se encarga de eliminar un grupo en la tabla grup de la base
	 * de datos a través de un objeto de tipo Group.
	 * 
	 * @param grup
	 *            Éste es el objeto Group que se quiere eliminar de la base de
	 *            datos
	 */
	public void removeGroup(Group grup) {

		em.getTransaction().begin();
		em.remove(grup);
		em.getTransaction().commit();
	}

	/**
	 * Este método obtiene la lista de grupos con el nombre de su tutor
	 * 
	 * @return Devuelve la lista de resultados de la consulta
	 */
	public List listGrups() {

		Query query = null;
		try {

			// lista los grupos con el nombre de su tutor
			em.getTransaction().begin();

			query = em.createNativeQuery(

					"SELECT g.id,d.nom FROM Grup g, Tutor t,Docent d WHERE g.id = t.grup and t.docent = d.id");

			em.getTransaction().commit();

		} catch (Exception e) {
			em.getTransaction().rollback();
		}
		return query.getResultList();
	}

	/**
	 * Este método obtiene la lista de grupos ordenados alfabéticamente
	 * 
	 * @return Devuelve la lista de los grupos
	 */
	public List<Group> getGroups() {

		try {

			em.getTransaction().begin();
			listGroups = em.createQuery("Select e From Group e ORDER BY e.id").getResultList();

			em.getTransaction().commit();
			return listGroups;

		} catch (Exception e) {
			em.getTransaction().rollback();
		}

		return listGroups;

	}

	/**
	 * Este método obtiene el grupo en el que está el docente a partir del id.
	 * 
	 * @param docent
	 *            Este es el parámetro que se utiliza para identificar el
	 *            docente del que queremos obtener su grupo
	 * @return Devuelve la lista de los grupos
	 */
	public String getGroupTutor(int docent) {

		Query query = em.createNativeQuery("SELECT docent,grup FROM tutor WHERE docent = #docent", Tutor.class);

		query.setParameter("docent", docent);
		List results = query.getResultList();
		Tutor tutor = null;
		for (int i = 0; i < results.size(); i++) {
			tutor = (Tutor) results.get(i);
		}

		return tutor.getGrup();
	}

	/**
	 * Este método cierra las conexiones con la base de datos.
	 */
	public void closeTransaction() {

		em.close();

	};
}
