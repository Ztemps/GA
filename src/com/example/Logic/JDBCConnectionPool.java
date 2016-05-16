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
