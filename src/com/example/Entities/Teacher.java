package com.example.Entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.sql.rowset.serial.SerialArray;


/**
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
 * 		Java POJO que representa un profesor
 */
@Entity
@Table(name = "docent")
public class Teacher implements Serializable {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String nom;
	private String cognoms;
	private String email;
	
	public Teacher() {
		
	}
	
	/**
	 * Constructor completo de la clase Teacher
	 * 
	 * @params id identificador del profesor
	 * @params nom nombre del profesor
	 * @params cognom apellido del profesor
	 * @params email del profesor
	 * 	 * 
	 */
	public Teacher(int id, String nom,String cognoms, String email) {
		super();
		this.id = id;
		this.nom = nom;
		this.cognoms = cognoms;
		this.email = email;
	}
	
	public Teacher(String nom,String cognoms, String email) {
		super();
		this.cognoms = cognoms;
		this.nom = nom;
		this.email = email;
	}
	
	
	
	public String getCognoms() {
		return cognoms;
	}


	public void setCognoms(String cognoms) {
		cognoms = cognoms;
	}

	// Getters y Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}


	@Override
	public String toString() {
		return "Docent [id=" + id + ", nom=" + nom + ", Cognoms=" + cognoms + ", email=" + email + "]";
	}
	

}
