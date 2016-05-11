package com.example.Logic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.Entities.Group;
import com.example.Entities.Student;
import com.example.Entities.Tutor;

public class GroupJPAManager {
	Group group;
	private EntityManager em = EntityManagerUtil.getEntityManager();

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
