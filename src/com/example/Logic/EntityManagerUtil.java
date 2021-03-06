
package com.example.Logic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

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
 * 
 * 	Esta clase se encarga de la persistencia a la base de datos mediante al entity manager factory(jpa)
 * 
 *******************************************************************************/
public class EntityManagerUtil {

	private static final String PERSISTENCE = "GestioAmonestacions";
	public static EntityManagerFactory emf = null;

	/**
	 * Este es el método en el que se crea la persistencia.
	 * @return 
	 * 
	 * 
	 */
	public EntityManagerUtil() {
		emf = Persistence.createEntityManagerFactory(PERSISTENCE);
	}

	/**
	 * Este es el método en el que se abre la conexión con la base de datos.
	 * @return emf Devuelve el entity manager factory
	 * 		
	 */
	public EntityManagerFactory getEntityManagerFactory() {
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory(PERSISTENCE);
		}
		return emf;
	}

	/**
	 * Este es el método en el que se obtiene la conexion ya establecida.
	 * 
	 */
	
	public EntityManager getEntityManager() {
		return getEntityManagerFactory().createEntityManager();
	}
	/**
	 * Este es el método en el que se cierra la conexión.
	 * 
	 */
	public static void close() {
		emf.close();
	}

}