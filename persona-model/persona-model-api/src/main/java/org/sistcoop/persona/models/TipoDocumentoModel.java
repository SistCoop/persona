package org.sistcoop.persona.models;

import org.sistcoop.persona.models.enums.TipoPersona;

public interface TipoDocumentoModel extends Model {

	String getId();

	String getAbreviatura();

	void setAbreviatura(String abreviatura);

	String getDenominacion();

	void setDenominacion(String denominacion);

	int getCantidadCaracteres();

	void setCantidadCaracteres(int cantidadCaracteres);

	TipoPersona getTipoPersona();

	void setTipoPersona(TipoPersona tipoPersona);

	boolean getEstado();

	void setEstado(boolean estado);

}