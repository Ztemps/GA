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
package com.example.Entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tutor")
public class Tutor implements Serializable{
	
	@Id
	private int docent;
	private String grup;
	
	public Tutor() {
		
	
	}
	

	public Tutor(int docent, String grup) {
		super();
		this.docent = docent;
		this.grup = grup;
	}
	
	
	public int getDocent() {
		return docent;
	}
	public void setDocent(int docent) {
		this.docent = docent;
	}
	public String getGrup() {
		return grup;
	}
	public void setGrup(String grup) {
		this.grup = grup;
	}
	
	@Override
	public String toString() {
		return "Tutor [docent=" + docent + ", grup=" + grup + "]";
	}

}
