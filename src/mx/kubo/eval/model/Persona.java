package mx.kubo.eval.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Persona implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	@JsonIgnore
	private int coincidences;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCoincidences() {
		return coincidences;
	}

	public void setCoincidences(int coincidences) {
		this.coincidences = coincidences;
	}
	
	
	
	
}
