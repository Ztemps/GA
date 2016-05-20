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
//

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "grup")
public class Group implements Serializable {
	@Id
	private String id;
	
	private int max_alumnes;

	public Group(String string) {
		this.id = string;
		this.max_alumnes = 35;

	}
	
	public Group() {
		
	}

	public Group(String id, int max_alumnes) {
		super();
		this.id = id;
		this.max_alumnes = max_alumnes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMax_alumnes() {
		return max_alumnes;
	}

	public void setMax_alumnes(int max_alumnes) {
		this.max_alumnes = max_alumnes;
	}

}
