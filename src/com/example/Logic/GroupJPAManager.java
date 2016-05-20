/*******************************************************************************
 * Gestió d'Amonestacions v1.0
 *
 * Esta obra está sujeta a la licencia Reconocimiento-NoComercial-SinObraDerivada 4.0 Internacional de Creative Commons. 
 * Para ver una copia de esta licencia, visite http://creativecommons.org/licenses/by-nc-nd/4.0/.
 *  
 * @author Francisco Javier Casado Moreno - fcasasdo@elpuig.xeill.net 
 * @author Daniel Pérez Palacino - dperez@elpuig.xeill.net 
 * @author Gerard Enrique Paulino Decena - gpaulino@elpuig.xeill.net 
 * @author Xavier Murcia Gámez - xmurica@elpuig.xeill.net 
 *******************************************************************************/
package com.example.Logic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.Entities.Group;
import com.example.Entities.Student;
import com.example.Entities.Teacher;
import com.example.Entities.Tutor;

public class GroupJPAManager {
	private Group group;
	private EntityManagerUtil entman = new EntityManagerUtil();
	private EntityManager em = entman.getEntityManager();
	private List<Group> listGroups;

	public void addGroup(Group grup) {

		em.getTransaction().begin();
		em.persist(grup);
		em.getTransaction().commit();
	};

	public void updateGroup(Group grup) {

		em.getTransaction().begin();
		em.merge(grup);
		em.getTransaction().commit();
	}

	public void removeGroup(Group grup) {

		em.getTransaction().begin();
		em.remove(grup);
		em.getTransaction().commit();
	}
	
	public List listGrups() {

		Query query = null;
		try {

			// lista los grupos con el nombre de su tutor
			em.getTransaction().begin();
			// list_grups = em.createQuery("SELECT g.id,d.nom FROM grup g, tutor
			// t,docent d WHERE g.id = t.grup and t.docent =
			// d.id").getResultList();

			query = em.createNativeQuery(

					"SELECT g.id,d.nom FROM Grup g, Tutor t,Docent d WHERE g.id = t.grup and t.docent = d.id");

			// System.out.println("objecto:"+ query.getSingleResult());
			em.getTransaction().commit();
			// return list_grups;

		} catch (Exception e) {
			em.getTransaction().rollback();
		}
		// return list_grups;
		return query.getResultList();
	}
	
	
public List<Group> getGroups(){
		
		
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
	
	
	
	public String getGroupTutor(int docent) {

		Query query = em.createNativeQuery("SELECT docent,grup FROM tutor WHERE docent = #docent", Tutor.class);

		query.setParameter("docent", docent);
		//
		// Tutor tutor = (Tutor) query.getSingleResult();

		List results = query.getResultList();
		Tutor tutor = null;
		for (int i = 0; i < results.size(); i++) {
			tutor = (Tutor) results.get(i);
		}

		return tutor.getGrup();
	}
	
	

	public void closeTransaction() {

		em.close();

	};
}
