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


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

public class EntityManagerUtil {

	public static EntityManagerFactory emf = null;

	//private static Logger LOG = Logger.getLogger(EntityManagerUtil.class);

	public EntityManagerUtil() {
		// Read the persistence.xml once
	//	LOG.debug("Loading persistence.xml to EntityManagerFactory.");
		emf = Persistence.createEntityManagerFactory("GestioAmonestacions");
	}

	public EntityManagerFactory getEntityManagerFactory() {
		if (emf == null) {
			//LOG.debug("EntityManagerFactory is null. Creating fresh EntityManagerFactory.");
			emf = Persistence.createEntityManagerFactory("GestioAmonestacions");
		}
		return emf;
	}

	public EntityManager getEntityManager() {
		return getEntityManagerFactory().createEntityManager();
	}
	
	public static void close(){
		emf.close();
	}

}