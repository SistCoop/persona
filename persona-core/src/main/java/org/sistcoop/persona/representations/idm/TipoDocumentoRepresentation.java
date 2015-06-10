package org.sistcoop.persona.representations.idm;

import java.io.Serializable;

public class TipoDocumentoRepresentation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String abreviatura;
	private String denominacion;
	private int cantidadCaracteres;
	private String tipoPersona;
	private Boolean estado;

	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public int getCantidadCaracteres() {
		return cantidadCaracteres;
	}

	public void setCantidadCaracteres(int cantidadCaracteres) {
		this.cantidadCaracteres = cantidadCaracteres;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

}
