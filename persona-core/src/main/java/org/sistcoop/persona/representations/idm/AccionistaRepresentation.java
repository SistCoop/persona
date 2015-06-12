package org.sistcoop.persona.representations.idm;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccionistaRepresentation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private PersonaNaturalRepresentation personaNatural;
	private BigDecimal porcentajeParticipacion;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getPorcentajeParticipacion() {
		return porcentajeParticipacion;
	}

	public void setPorcentajeParticipacion(BigDecimal porcentajeParticipacion) {
		this.porcentajeParticipacion = porcentajeParticipacion;
	}

	public PersonaNaturalRepresentation getPersonaNatural() {
		return personaNatural;
	}

	public void setPersonaNatural(PersonaNaturalRepresentation personaNatural) {
		this.personaNatural = personaNatural;
	}

}
