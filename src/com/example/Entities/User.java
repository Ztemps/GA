package com.example.Entities;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;

@Entity
@Table(name = "usuari")
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id_docent")
	private int id;
	@Column(name="contrasenya")
	private String password;
	@Column(name="usuari")
	private String username;
	@Column(name="rol")
	private String rol;

	public User() {
		
	}
	public User(int id) {
		super();
		this.id = id;
	}

	public User(int id, String password, String username, String rol) {
		super();
		this.id = id;
		this.password = password;
		this.username = username;
		this.rol = rol;
	}
	
	public User(int id, String username, String rol) {
		super();
		this.id = id;
		this.username = username;
		this.rol = rol;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", password=" + password + ", username=" + username + ", rol=" + rol + "]";
	}
	
	
	


}
