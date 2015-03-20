package org.sistcoop.persona.models;

import java.math.BigDecimal;

public interface AccionistaModel extends Model {

	Long getId();

	PersonaNaturalModel getPersonaNatural();

	void setPersonaNatural(PersonaNaturalModel personaNatural);

	PersonaJuridicaModel getPersonaJuridica();

	void setPersonaJuridica(PersonaJuridicaModel personaJuridica);

	BigDecimal getPorcentajeParticipacion();

	void setPorcentajeParticipacion(BigDecimal porcentajeParticipacion);

}