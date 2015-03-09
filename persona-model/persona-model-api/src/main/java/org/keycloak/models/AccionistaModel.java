package org.keycloak.models;

import java.math.BigDecimal;

public interface AccionistaModel extends Model {

	public Long getId();

	public PersonaNaturalModel getPersonaNatural();

	public void setPersonaNatural(PersonaNaturalModel personaNatural);

	public PersonaJuridicaModel getPersonaJuridica();

	public void setPersonaJuridica(PersonaJuridicaModel personaJuridica);

	public BigDecimal getPorcentajeParticipacion();

	public void setPorcentajeParticipacion(BigDecimal porcentajeParticipacion);

}