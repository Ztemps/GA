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
