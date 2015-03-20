package org.sistcoop.persona.representations.idm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tipoPersona")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TipoPersonaRepresentation {

	String denominacion;

	public TipoPersonaRepresentation(String denominacion) {
		this.denominacion = denominacion;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

}
