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
package com.example.Reports;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.Logic.EntityManagerUtil;

public class ReportQuerys {
	public Query queryNom = null;
	public Query queryCognom = null;
	public Query queryDate = null;
	public Query queryIds = null;
	public Query queryWarnings = null;
	public Query queryExpulsion = null;
	
	private EntityManager em;

	public ReportQuerys() {

		EntityManagerUtil emu = new EntityManagerUtil();
		em = emu.getEntityManager();
	}
	
	public List getIdAlumnes(String grup) {
		em.getTransaction().begin();
		// Curso que escoja el admin esto será una variable
		queryIds = em.createNativeQuery("SELECT id FROM alumne  WHERE grup LIKE '" + grup + "'");
		List idQuery = queryIds.getResultList();
		em.getTransaction().commit();
		return idQuery;
	}
	

	public List getNomAlumnes(String grup) {

		em.getTransaction().begin();
		// Curso que escoja el admin esto será una variable
		queryNom = em.createNativeQuery("SELECT nom FROM alumne WHERE grup LIKE '" + grup + "'");
		List nomsQuery = queryNom.getResultList();
		em.getTransaction().commit();
		return nomsQuery;
	}

	public List getCognomsAlumnes(String grup) {

		em.getTransaction().begin();
		// Curso que escoja el admin esto será una variable
		queryCognom = em.createNativeQuery("SELECT cognoms FROM alumne WHERE grup LIKE '" + grup + "'");
		List cognomsQuery = queryCognom.getResultList();
		em.getTransaction().commit();
		return cognomsQuery;

	}
	
	
	public String getDateCurs() {

		em.getTransaction().begin();
		// Curso que escoja el admin esto será una variable
		queryDate = em.createNativeQuery("SELECT curs FROM alumne LIMIT 1");
		String dateCurs = queryDate.getSingleResult().toString();
		em.getTransaction().commit();
		return dateCurs;

	}
	
	public String getExpulsionCurs(int id, Date semana1, Date semana2) {

		em.getTransaction().begin();
		// Curso que escoja el admin esto será una variable
		queryExpulsion = em.createNativeQuery("SELECT count(expulsat) FROM amonestacio WHERE expulsat=true AND alumne = '" + id + "' "
				+ "AND data BETWEEN '" + semana1 + "' AND '" + semana2 + "'");
		String expulsions = queryExpulsion.getSingleResult().toString();;
		em.getTransaction().commit();
		
	
		
		return expulsions;

	}
	
	public String getWarningCurs(int id , Date semana1, Date semana2) {

		em.getTransaction().begin();
		// Curso que escoja el admin esto será una variable
		queryWarnings = em.createNativeQuery("SELECT count(amonestat) FROM amonestacio WHERE amonestat=true AND alumne = '" + id + "'"
				+ "AND data BETWEEN '" + semana1 + "' AND '" + semana2 + "'");
		String amonestacions = queryWarnings.getSingleResult().toString();
		em.getTransaction().commit();
		return amonestacions;

	}
	
	

	public void closeTransaction() {
		em.close();

	}

	

}