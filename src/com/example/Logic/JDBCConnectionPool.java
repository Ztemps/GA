package com.example.Logic;

import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

public class JDBCConnectionPool {
	
	public static SimpleJDBCConnectionPool connectionPool;
	
	
	public SimpleJDBCConnectionPool GetConnection(){
		
		try {
			connectionPool = new SimpleJDBCConnectionPool("org.postgresql.Driver",
			        "jdbc:postgresql://localhost:5432/GAdb", 
			        "postgres", "postgres");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return connectionPool;
	}
	


}
