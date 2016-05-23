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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

public class EntityManagerUtil {

	private static final String PERSISTENCE = "GestioAmonestacions";
	public static EntityManagerFactory emf = null;

	public EntityManagerUtil() {
		emf = Persistence.createEntityManagerFactory(PERSISTENCE);
	}

	public EntityManagerFactory getEntityManagerFactory() {
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory(PERSISTENCE);
		}
		return emf;
	}

	public EntityManager getEntityManager() {
		return getEntityManagerFactory().createEntityManager();
	}

	public static void close() {
		emf.close();
	}

}