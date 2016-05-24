
package com.example.Logic;

import java.sql.SQLException;
import java.util.ResourceBundle;

import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

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
 *         Esta clase abre la conexión con la base de datos mediante jdbc para
 *         poder crear sql containers.
 *******************************************************************************/
public class JDBCConnectionPool {

	private ResourceBundle rb = ResourceBundle.getBundle("GA");

	private static final String DRIVER = "org.postgresql.Driver";

	private SimpleJDBCConnectionPool connectionPool;

	/**
	 * Este es el constructor de la clase que crea la conexión.
	 */
	public JDBCConnectionPool() {

		try {
			connectionPool = new SimpleJDBCConnectionPool(DRIVER, rb.getString("database"), rb.getString("user"),
					rb.getString("password"), 2, 5);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Este método devuelve la conexión ya creada antes en el constructor.
	 * @return connectionPool Devuelve la conexión creada
	 */
	public SimpleJDBCConnectionPool GetConnection() {

		return connectionPool;
	}
	
	/**
	 * Este método cierra la conexión con la base de datos.
	 * 
	 */
	public void close() {
		connectionPool.destroy();
	}

}
