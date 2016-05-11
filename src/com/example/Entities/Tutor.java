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
