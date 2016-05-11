package com.example.Entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vaadin.ui.Image;

@Entity
@Table(name = "alumne")
//@SequenceGenerator(name="seq", initialValue=1, allocationSize=500)
public class Student implements Serializable {
	 //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name="nom")
	private String nom;
	@Column(name="cognoms")
	private String cognoms;
	@Column(name="email_pares")
	private String email;
	@Column(name="telefon_pares")
	private String telefon;
	private Date data_naixement;
	private String curs;
	@Column(name="grup")
	private String grup;
	@Column(name="foto")
	private Image foto;

	
	public Student(){}

		
	public Student(int id, String nom, String cognom, String email, String telefon, String curs,Date data_naixement, String grup) {
		super();
		this.id=id;
		this.nom = nom;
		this.cognoms = cognom;
		this.email = email;
		this.telefon = telefon;
		this.curs = curs;
		this.grup = grup;
		this.data_naixement=data_naixement;
	}
	
	public Student(int id, String nom, String cognom, String email, String telefon, String curs, String grup) {
		super();
		this.id=id;
		this.nom = nom;
		this.cognoms = cognom;
		this.email = email;
		this.telefon = telefon;
		this.curs = curs;
		this.grup = grup;
	}
	
	public Student(String nom, String cognom, String email, String telefon, Date data_naixement, String curs, String grup,Image iconURL) {
		super();
		this.nom = nom;
		this.cognoms = cognom;
		this.email = email;
		this.telefon = telefon;
		this.data_naixement = data_naixement;
		this.curs = curs;
		this.grup = grup;
		this.foto= iconURL;
	}
	
	
	
	
	

	public Student(String nom, String cognom, String email, String telf, Date fecha, String curs, String grup) {
		// TODO Auto-generated constructor stub
		this.nom = nom;
		this.cognoms = cognom;
		this.email = email;
		this.telefon = telf;
		this.data_naixement = fecha;
		this.curs = curs;
		this.grup = grup;
	}


	public Image getFoto() {
		return foto;
	}

	public void setFoto(Image foto) {
		this.foto = foto;
	}

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
	public String getTelefon() {
		return telefon;
	}
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	public Date getData_naixement() {
		return data_naixement;
	}
	public void setData_naixement(Date data_naixement) {
		this.data_naixement = data_naixement;
	}
	public String getCurs() {
		return curs;
	}
	public String getCognoms() {
		return cognoms;
	}


	public void setCognoms(String cognoms) {
		this.cognoms = cognoms;
	}


	public void setCurs(String curs) {
		this.curs = curs;
	}
	public String getGrup() {
		return grup;
	}
	public void setGrup(String grup) {
		this.grup = grup;
	}


	@Override
	public String toString() {
		return "Alumne [id=" + id + ", nom=" + nom + ", email=" + email + ", telefon=" + telefon + ", data_naixement="
				+ data_naixement + ", curs=" + curs + ", grup=" + grup + "]";
	}
	
	
	
	
	
}
