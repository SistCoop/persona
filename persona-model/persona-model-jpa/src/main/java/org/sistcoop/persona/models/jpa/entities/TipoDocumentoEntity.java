package org.sistcoop.persona.models.jpa.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.sistcoop.persona.models.enums.TipoPersona;

@Entity
@Table(name = "TIPO_DOCUMENTO", indexes = { @Index(columnList = "abreviatura") })
@NamedQueries({ 
	@NamedQuery(name = TipoDocumentoEntity.findAll, query = "Select t from TipoDocumentoEntity t"), 
	@NamedQuery(name = TipoDocumentoEntity.findByAbreviatura, query = "SELECT t FROM TipoDocumentoEntity t WHERE UPPER(t.abreviatura) = UPPER(:abreviatura)"), 
	@NamedQuery(name = TipoDocumentoEntity.findByTipopersona, query = "SELECT t FROM TipoDocumentoEntity t WHERE t.tipoPersona = :tipoPersona") })
public class TipoDocumentoEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String base = "org.sistcoop.models.jpa.entities.TipoDocumentoEntity.";
	public final static String findAll = base + "findAll";
	public final static String findByAbreviatura = base + "findByAbreviatura";
	public final static String findByTipopersona = base + "findByTipopersona";
	
	private String abreviatura;
	private String denominacion;
	private int cantidadCaracteres;
	private TipoPersona tipoPersona;
	private boolean estado;

	private Timestamp optlk;

	@NotNull
	@Size(min = 1, max = 20)
	@NotBlank
	@NotEmpty
	@Id
	@Column(name = "ABREVIATURA")
	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	@NotNull
	@Size(min = 1, max = 60)
	@NotBlank
	@NotEmpty
	@Column(name = "DENOMINACION")
	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	@NotNull
	@Min(value = 1)
	@Max(value = 20)
	@Column(name = "CANTIDAD_CARACTERES")
	public int getCantidadCaracteres() {
		return cantidadCaracteres;
	}

	public void setCantidadCaracteres(int cantidadCaracteres) {
		this.cantidadCaracteres = cantidadCaracteres;
	}

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_PERSONA")
	public TipoPersona getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(TipoPersona tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	@NotNull
	@Type(type = "org.hibernate.type.TrueFalseType")
	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
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
		result = prime * result + ((abreviatura == null) ? 0 : abreviatura.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TipoDocumentoEntity))
			return false;
		TipoDocumentoEntity other = (TipoDocumentoEntity) obj;
		if (abreviatura == null) {
			if (other.abreviatura != null)
				return false;
		} else if (!abreviatura.equals(other.abreviatura))
			return false;
		return true;
	}

}
