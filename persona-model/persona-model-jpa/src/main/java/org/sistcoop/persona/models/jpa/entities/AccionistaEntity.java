package org.sistcoop.persona.models.jpa.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "ACCIONISTA")
public class AccionistaEntity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private PersonaNaturalEntity personaNatural;
	private PersonaJuridicaEntity personaJuridica;
	private BigDecimal porcentajeParticipacion;

	private Timestamp optlk;

	public AccionistaEntity() {
		// TODO Auto-generated constructor stub
	}
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@NotNull
	@NaturalId
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey)
	public PersonaNaturalEntity getPersonaNatural() {
		return personaNatural;
	}

	public void setPersonaNatural(PersonaNaturalEntity personaNatural) {
		this.personaNatural = personaNatural;
	}

	@NotNull
	@NaturalId
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey)
	public PersonaJuridicaEntity getPersonaJuridica() {
		return personaJuridica;
	}

	public void setPersonaJuridica(PersonaJuridicaEntity personaJuridica) {
		this.personaJuridica = personaJuridica;
	}

	@NotNull
	@Min(value = 1)
	@Max(value = 100)
	@Digits(integer = 3, fraction = 2)
	@Column(name = "PORCENTAJE_PARTICIPACION")
	public BigDecimal getPorcentajeParticipacion() {
		return porcentajeParticipacion;
	}

	public void setPorcentajeParticipacion(BigDecimal porcentajeParticipacion) {
		this.porcentajeParticipacion = porcentajeParticipacion;
	}

	@Version
	public Timestamp getOptlk() {
		return optlk;
	}

	public void setOptlk(Timestamp optlk) {
		this.optlk = optlk;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((personaJuridica == null) ? 0 : personaJuridica.hashCode());
		result = prime * result + ((personaNatural == null) ? 0 : personaNatural.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AccionistaEntity))
			return false;
		AccionistaEntity other = (AccionistaEntity) obj;
		if (personaJuridica == null) {
			if (other.personaJuridica != null)
				return false;
		} else if (!personaJuridica.equals(other.personaJuridica))
			return false;
		if (personaNatural == null) {
			if (other.personaNatural != null)
				return false;
		} else if (!personaNatural.equals(other.personaNatural))
			return false;
		return true;
	}
}
