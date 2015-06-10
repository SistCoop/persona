package org.sistcoop.persona.models.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.enums.TipoPersona;
import org.sistcoop.persona.models.jpa.entities.TipoDocumentoEntity;

@Named
@Stateless
@Local(TipoDocumentoProvider.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JpaTipoDocumentoProvider implements TipoDocumentoProvider {

	@PersistenceContext
	protected EntityManager em;

	@Override
	public TipoDocumentoModel addTipoDocumento(String abreviatura, String denominacion, int cantidadCaracteres, TipoPersona tipoPersona) {
		TipoDocumentoEntity tipoDocumentoEntity = new TipoDocumentoEntity();
		tipoDocumentoEntity.setAbreviatura(abreviatura);
		tipoDocumentoEntity.setDenominacion(denominacion);
		tipoDocumentoEntity.setCantidadCaracteres(cantidadCaracteres);
		tipoDocumentoEntity.setTipoPersona(tipoPersona);
		tipoDocumentoEntity.setEstado(true);
		em.persist(tipoDocumentoEntity);
		return new TipoDocumentoAdapter(em, tipoDocumentoEntity);
	}

	@Override
	public TipoDocumentoModel getTipoDocumentoByAbreviatura(String abreviatura) {
		TypedQuery<TipoDocumentoEntity> query = em.createQuery("SELECT t FROM TipoDocumentoEntity t WHERE UPPER(t.abreviatura) = UPPER(:abreviatura)", TipoDocumentoEntity.class);
		query.setParameter("abreviatura", abreviatura);
		List<TipoDocumentoEntity> results = query.getResultList();
		if (results.size() == 0)
			return null;
		return new TipoDocumentoAdapter(em, results.get(0));
	}

	@Override
	public List<TipoDocumentoModel> getTiposDocumento() {
		TypedQuery<TipoDocumentoEntity> query = em.createQuery("Select t from TipoDocumentoEntity t", TipoDocumentoEntity.class);
		List<TipoDocumentoEntity> list = query.getResultList();
		List<TipoDocumentoModel> results = new ArrayList<TipoDocumentoModel>();
		for (TipoDocumentoEntity entity : list) {
			if(entity.isEstado()) {
				results.add(new TipoDocumentoAdapter(em, entity));
			}
		}
		return results;
	}

	@Override
	public List<TipoDocumentoModel> getTiposDocumento(TipoPersona tipoPersona) {
		TypedQuery<TipoDocumentoEntity> query = em.createQuery("SELECT t FROM TipoDocumentoEntity t WHERE t.tipoPersona = :tipoPersona", TipoDocumentoEntity.class);
		query.setParameter("tipoPersona", tipoPersona);
		List<TipoDocumentoEntity> list = query.getResultList();
		List<TipoDocumentoModel> results = new ArrayList<TipoDocumentoModel>();
		for (TipoDocumentoEntity entity : list) {
			if(entity.isEstado()) {
				results.add(new TipoDocumentoAdapter(em, entity));
			}
		}
		return results;
	}

	@Override
	public List<TipoDocumentoModel> getTiposDocumento(boolean estado) {
		TypedQuery<TipoDocumentoEntity> query = em.createQuery("Select t from TipoDocumentoEntity t", TipoDocumentoEntity.class);
		List<TipoDocumentoEntity> list = query.getResultList();
		List<TipoDocumentoModel> results = new ArrayList<TipoDocumentoModel>();
		for (TipoDocumentoEntity entity : list) {
			if(entity.isEstado() == estado)
				results.add(new TipoDocumentoAdapter(em, entity));
		}
		return results;
	}

	@Override
	public List<TipoDocumentoModel> getTiposDocumento(TipoPersona tipoPersona, boolean estado) {
		TypedQuery<TipoDocumentoEntity> query = em.createQuery("SELECT t FROM TipoDocumentoEntity t WHERE t.tipoPersona = :tipoPersona", TipoDocumentoEntity.class);
		query.setParameter("tipoPersona", tipoPersona);
		List<TipoDocumentoEntity> list = query.getResultList();
		List<TipoDocumentoModel> results = new ArrayList<TipoDocumentoModel>();
		for (TipoDocumentoEntity entity : list) {
			if(entity.isEstado() == estado)
				results.add(new TipoDocumentoAdapter(em, entity));
		}
		return results;
	}
	
	@Override
	public boolean removeTipoDocumento(TipoDocumentoModel tipoDocumentoModel) {		
		TipoDocumentoEntity tipoDocumentoEntity = em.find(TipoDocumentoEntity.class, tipoDocumentoModel.getAbreviatura());
		if (tipoDocumentoEntity == null) return false;
        em.remove(tipoDocumentoEntity);
        return true;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

}
