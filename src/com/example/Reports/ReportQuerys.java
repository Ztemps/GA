package com.example.Reports;

import java.io.FileWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.Logic.EntityManagerUtil;

public class ReportQuerys {
	public Query queryNom = null;
	public Query queryCognom = null;
	public Query queryDate = null;
	EntityManagerUtil emu = new EntityManagerUtil();
	private EntityManager em = emu.getEntityManager();

	public ReportQuerys() {

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
		queryCognom = em.createNativeQuery("SELECT cognoms FROM alumne");
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

	public void closeTransaction() {
		em.close();

	}

}
