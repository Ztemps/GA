package com.example.Entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * 		Java POJO que representa una amonestación
 */

@Entity
@Table(name = "amonestacio")
public class Warning implements Serializable {

	
	private static final long serialVersionUID = 1L;
	@Id
	private int docent;
	private Date data;
	private int alumne;

	private String grup;

	private String gravetat;
	String localitzacio;
	private String materia;
	private int tutor;
	private boolean amonestat;
	private boolean expulsat;
	private String curs;
	@Column(name = "motius_selection")
	private String motiu;
	private String altres_motius;

	
	/**
	 * Constructor completo de la clase Warning
	 * 
	 * @params docent identificador del profesor
	 * @params data fecha de la amonestación
	 * @params grup grupo del alumno
	 * @params alumne id del alumno
	 * @params gravetat gravedad de la amonestación (LLEU, GREU...)
	 * @params localitzacio lugar donde se produjo la amonestación
	 * @params materia assignatura en la que se produjo la amonestación
	 * @params tutor identificador del tutor del alumno
	 * @params amonestat indica si el alumno fue amonestado
	 * @params explulsat indica si el alumno fue expulsado
	 * @params curs curso actual del alumno
	 * @params motiu motivo o motivos de la amonestación
	 * @params altre_motius motivos no contemplados en motiu
	 * 
	 */
	public Warning(int docent, Date data, String grup, int alumne, String gravetat, String localitzacio,
			String materia, int tutor, boolean amonestat, boolean expulsat, String curs, String motiu,
			String altres_motius) {
		super();
		this.docent = docent;
		this.data = data;
		this.grup = grup;
		this.alumne = alumne;
		this.gravetat = gravetat;
		this.localitzacio = localitzacio;
		this.materia = materia;
		this.tutor = tutor;
		this.amonestat = amonestat;
		this.expulsat = expulsat;
		this.curs = curs;
		this.motiu = motiu;
		this.altres_motius = altres_motius;
	}
	
	
	// Getters y Setters
	public String getAltres_motius() {
		return altres_motius;
	}

	public void setAltres_motius(String altres_motius) {
		this.altres_motius = altres_motius;
	}

	public String getGravetat() {
		return gravetat;
	}

	public Warning() {

	}

	public String getLocalitzacio() {
		return localitzacio;
	}

	public void setLocalitzacio(String localitzacio) {
		this.localitzacio = localitzacio;
	}


	public int getDocent() {
		return docent;
	}

	public void setDocent(int docent) {
		this.docent = docent;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getGrup() {
		return grup;
	}

	public void setGrup(String grup) {
		this.grup = grup;
	}

	public int getAlumne() {
		return alumne;
	}

	public void setAlumne(int alumne) {
		this.alumne = alumne;
	}

	public String isGravetat() {
		return gravetat;
	}

	public void setGravetat(String gravetat) {
		this.gravetat = gravetat;
	}

	public String getMotiu() {
		return motiu;
	}

	public void setMotiu(String motiu) {
		this.motiu = motiu;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public int getTutor() {
		return tutor;
	}

	public void setTutor(int tutor) {
		this.tutor = tutor;
	}

	public boolean isAmonestat() {
		return amonestat;
	}

	public void setAmonestat(boolean amonestat) {
		this.amonestat = amonestat;
	}

	public boolean isExpulsat() {
		return expulsat;
	}

	public void setExpulsat(boolean expulsat) {
		this.expulsat = expulsat;
	}

	public String getCurs() {
		return curs;
	}

	public void setCurs(String curs) {
		this.curs = curs;
	}

	@Override
	public String toString() {
		return "Amonestacio [docent=" + docent + ", data=" + data + ", grup=" + grup + ", alumne=" + alumne
				+ ", gravetat=" + gravetat + ", motiu=" + motiu + ", materia=" + materia + ", tutor=" + tutor
				+ ", amonestat=" + amonestat + ", expulsat=" + expulsat + ", curs=" + curs + "]";
	}

}
