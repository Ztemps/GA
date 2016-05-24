
package com.example.Entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

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
 * 
 * 	Java POJO que representa el historico de las amonestaciones
 * 
 */


//@Entity
//@Table(name = "historic")
public class Historic implements Serializable {
		
	private String curs;
	private String grup;
	private Date data; 
	private int id_alumne;
	private String nom_alumne;
	private int amonestacions;
	
	public Historic(){
		
	}
	
	public Historic(String curs, String grup, Date data, int id_alumne, String nom_alumne, int amonestacions) {
		super();
		this.curs = curs;
		this.grup = grup;
		this.data = data;
		this.id_alumne = id_alumne;
		this.nom_alumne = nom_alumne;
		this.amonestacions = amonestacions;
	}
	
	// Getters i Setters
	public String getCurs() {
		return curs;
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
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public int getId_alumne() {
		return id_alumne;
	}
	public void setId_alumne(int id_alumne) {
		this.id_alumne = id_alumne;
	}
	public String getNom_alumne() {
		return nom_alumne;
	}
	public void setNom_alumne(String nom_alumne) {
		this.nom_alumne = nom_alumne;
	}
	public int getAmonestacions() {
		return amonestacions;
	}
	public void setAmonestacions(int amonestacions) {
		this.amonestacions = amonestacions;
	}

	@Override
	public String toString() {
		return "Historic [curs=" + curs + ", grup=" + grup + ", data=" + data + ", id_alumne=" + id_alumne
				+ ", nom_alumne=" + nom_alumne + ", amonestacions=" + amonestacions + "]";
	}
	
	
	
}
