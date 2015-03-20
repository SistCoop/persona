package org.sistcoop.persona.representations.idm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tipoEmpresa")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TipoEmpresaRepresentation {

	String denominacion;

	public TipoEmpresaRepresentation(String denominacion) {
		this.denominacion = denominacion;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

}
