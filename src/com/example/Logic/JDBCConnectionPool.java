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

import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

public class JDBCConnectionPool {

	private static final String DRIVER = "org.postgresql.Driver";
	private static final String DATABASE = "jdbc:postgresql://localhost:5432/GAdb";
	private static final String USER = "postgres";
	private static final String PASSWORD = "postgres";

	private SimpleJDBCConnectionPool connectionPool;

	public JDBCConnectionPool() {

		try {
			connectionPool = new SimpleJDBCConnectionPool(DRIVER, DATABASE, USER, PASSWORD, 2, 5);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public SimpleJDBCConnectionPool GetConnection() {

		return connectionPool;
	}

	public void close() {
		connectionPool.destroy();
	}

}
