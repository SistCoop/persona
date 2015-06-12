package org.sistcoop.persona.models.jpa;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import org.sistcoop.persona.models.AccionistaModel;
import org.sistcoop.persona.models.PersonaJuridicaModel;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.jpa.entities.AccionistaEntity;
import org.sistcoop.persona.models.jpa.entities.PersonaJuridicaEntity;
import org.sistcoop.persona.models.jpa.entities.PersonaNaturalEntity;

public class AccionistaAdapter implements AccionistaModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected AccionistaEntity accionistaEntity;
	protected EntityManager em;

	public AccionistaAdapter(EntityManager em, AccionistaEntity accionistaEntity) {
		this.em = em;
		this.accionistaEntity = accionistaEntity;
	}

	public AccionistaEntity getAccionistaEntity() {
		return accionistaEntity;
	}

	@Override
	public String getId() {
		return accionistaEntity.getId();
	}

	@Override
	public PersonaNaturalModel getPersonaNatural() {
		return new PersonaNaturalAdapter(em, accionistaEntity.getPersonaNatural());
	}

	@Override
	public void setPersonaNatural(PersonaNaturalModel personaNaturalModel) {
		PersonaNaturalEntity personaNaturalEntity = PersonaNaturalAdapter.toPersonaNaturalEntity(personaNaturalModel, em);
		accionistaEntity.setPersonaNatural(personaNaturalEntity);
	}

	@Override
	public PersonaJuridicaModel getPersonaJuridica() {
		return new PersonaJuridicaAdapter(em, accionistaEntity.getPersonaJuridica());
	}

	@Override
	public void setPersonaJuridica(PersonaJuridicaModel personaJuridicaModel) {
		PersonaJuridicaEntity personaJuridicaEntity = PersonaJuridicaAdapter.toPersonaJuridicaEntity(personaJuridicaModel, em);
		accionistaEntity.setPersonaJuridica(personaJuridicaEntity);
	}

	@Override
	public BigDecimal getPorcentajeParticipacion() {
		return accionistaEntity.getPorcentajeParticipacion();
	}

	@Override
	public void setPorcentajeParticipacion(BigDecimal porcentajeParticipacion) {
		accionistaEntity.setPorcentajeParticipacion(porcentajeParticipacion);
	}

	public static AccionistaEntity toAccionistaEntity(AccionistaModel model, EntityManager em) {
		if (model instanceof AccionistaAdapter) {
			return ((AccionistaAdapter) model).getAccionistaEntity();
		}
		return em.getReference(AccionistaEntity.class, model.getId());
	}

	@Override
	public void commit() {
		em.merge(accionistaEntity);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getPersonaJuridica() == null) ? 0 : getPersonaJuridica().hashCode());
		result = prime * result + ((getPersonaNatural() == null) ? 0 : getPersonaNatural().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AccionistaModel))
			return false;
		AccionistaModel other = (AccionistaModel) obj;
		if (getPersonaJuridica() == null) {
			if (other.getPersonaJuridica() != null)
				return false;
		} else if (!getPersonaJuridica().equals(other.getPersonaJuridica()))
			return false;
		if (getPersonaNatural() == null) {
			if (other.getPersonaNatural() != null)
				return false;
		} else if (!getPersonaNatural().equals(other.getPersonaNatural()))
			return false;
		return true;
	}

}
