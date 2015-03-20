package org.sistcoop.persona.representations.idm;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "accionista")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class AccionistaRepresentation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private PersonaNaturalRepresentation personaNatural;	
	private BigDecimal porcentajeParticipacion;

	@XmlAttribute
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlAttribute
	public BigDecimal getPorcentajeParticipacion() {
		return porcentajeParticipacion;
	}

	public void setPorcentajeParticipacion(BigDecimal porcentajeParticipacion) {
		this.porcentajeParticipacion = porcentajeParticipacion;
	}

	@XmlElement
	public PersonaNaturalRepresentation getPersonaNatural() {
		return personaNatural;
	}

	public void setPersonaNatural(PersonaNaturalRepresentation personaNatural) {
		this.personaNatural = personaNatural;
	}	

}
