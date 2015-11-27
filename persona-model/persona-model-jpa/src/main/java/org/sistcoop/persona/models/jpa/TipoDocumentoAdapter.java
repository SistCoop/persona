package org.sistcoop.persona.models.jpa;

import javax.persistence.EntityManager;

import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.enums.TipoPersona;
import org.sistcoop.persona.models.jpa.entities.TipoDocumentoEntity;

/**
 * @author <a href="mailto:carlosthe19916@sistcoop.com">Carlos Feria</a>
 */

public class TipoDocumentoAdapter implements TipoDocumentoModel {

	private static final long serialVersionUID = 1L;

	private TipoDocumentoEntity tipoDocumentoEntity;
	private EntityManager em;

	public TipoDocumentoAdapter(EntityManager em, TipoDocumentoEntity tipoDocumentoEntity) {
		this.em = em;
		this.tipoDocumentoEntity = tipoDocumentoEntity;
	}

	public static TipoDocumentoEntity toTipoDocumentoEntity(TipoDocumentoModel model, EntityManager em) {
		if (model instanceof TipoDocumentoAdapter) {
			return ((TipoDocumentoAdapter) model).getTipoDocumentEntity();
		}
		return em.getReference(TipoDocumentoEntity.class, model.getAbreviatura());
	}

	public TipoDocumentoEntity getTipoDocumentEntity() {
		return tipoDocumentoEntity;
	}

	@Override
	public String getId() {
		return tipoDocumentoEntity.getId();
	}

	@Override
	public String getAbreviatura() {
		return tipoDocumentoEntity.getAbreviatura();
	}

	@Override
	public void setAbreviatura(String abreviatura) {
		tipoDocumentoEntity.setAbreviatura(abreviatura);
	}

	@Override
	public String getDenominacion() {
		return tipoDocumentoEntity.getDenominacion();
	}

	@Override
	public void setDenominacion(String denominacion) {
		tipoDocumentoEntity.setDenominacion(denominacion);
	}

	@Override
	public int getCantidadCaracteres() {
		return tipoDocumentoEntity.getCantidadCaracteres();
	}

	@Override
	public void setCantidadCaracteres(int cantidadCaracteres) {
		tipoDocumentoEntity.setCantidadCaracteres(cantidadCaracteres);
	}

	@Override
	public TipoPersona getTipoPersona() {
		String tipoPersona = tipoDocumentoEntity.getTipoPersona();
		return TipoPersona.valueOf(tipoPersona);
	}

	@Override
	public void setTipoPersona(TipoPersona tipoPersona) {
		tipoDocumentoEntity.setTipoPersona(tipoPersona.toString());
	}

	@Override
	public boolean getEstado() {
		return tipoDocumentoEntity.isEstado();
	}

	@Override
	public void setEstado(boolean estado) {
		tipoDocumentoEntity.setEstado(estado);
	}

	@Override
	public void commit() {
		em.merge(tipoDocumentoEntity);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getAbreviatura() == null) ? 0 : getAbreviatura().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TipoDocumentoModel))
			return false;
		TipoDocumentoModel other = (TipoDocumentoModel) obj;
		if (getAbreviatura() == null) {
			if (other.getAbreviatura() != null)
				return false;
		} else if (!getAbreviatura().equals(other.getAbreviatura()))
			return false;
		return true;
	}

}
