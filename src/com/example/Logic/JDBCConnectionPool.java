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

import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

public class JDBCConnectionPool {
	
	private SimpleJDBCConnectionPool connectionPool;
	
	public JDBCConnectionPool(){
		
		try {
			connectionPool = new SimpleJDBCConnectionPool("org.postgresql.Driver",
			        "jdbc:postgresql://localhost:5432/GAdb", 
			        "postgres", "postgres",2,5);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public SimpleJDBCConnectionPool GetConnection(){
		
		return connectionPool;
	}
	
	public void close(){
		connectionPool.destroy();
	}
	


}
