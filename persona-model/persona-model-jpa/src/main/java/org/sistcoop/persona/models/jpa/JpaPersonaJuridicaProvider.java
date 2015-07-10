package org.sistcoop.persona.models.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.sistcoop.persona.models.PersonaJuridicaModel;
import org.sistcoop.persona.models.PersonaJuridicaProvider;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.enums.TipoEmpresa;
import org.sistcoop.persona.models.jpa.entities.PersonaJuridicaEntity;
import org.sistcoop.persona.models.jpa.entities.PersonaNaturalEntity;
import org.sistcoop.persona.models.jpa.entities.TipoDocumentoEntity;

@Named
@Stateless
@Local(PersonaJuridicaProvider.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JpaPersonaJuridicaProvider implements PersonaJuridicaProvider {

	@PersistenceContext
	protected EntityManager em;

	@Override
	public PersonaJuridicaModel create(PersonaNaturalModel representanteLegal, String codigoPais, TipoDocumentoModel tipoDocumentoModel, String numeroDocumento, String razonSocial, Date fechaConstitucion, TipoEmpresa tipoEmpresa, boolean finLucro) {
		TipoDocumentoEntity tipoDocumentoEntity = TipoDocumentoAdapter.toTipoDocumentoEntity(tipoDocumentoModel, em);
		PersonaNaturalEntity personaNaturalEntity = PersonaNaturalAdapter.toPersonaNaturalEntity(representanteLegal, em);

		PersonaJuridicaEntity personaJuridicaEntity = new PersonaJuridicaEntity();
		personaJuridicaEntity.setRepresentanteLegal(personaNaturalEntity);
		personaJuridicaEntity.setCodigoPais(codigoPais);
		personaJuridicaEntity.setTipoDocumento(tipoDocumentoEntity);
		personaJuridicaEntity.setNumeroDocumento(numeroDocumento);
		personaJuridicaEntity.setRazonSocial(razonSocial);
		personaJuridicaEntity.setFechaConstitucion(fechaConstitucion);
		personaJuridicaEntity.setTipoEmpresa(tipoEmpresa);
		personaJuridicaEntity.setFinLucro(finLucro);

		em.persist(personaJuridicaEntity);
		return new PersonaJuridicaAdapter(em, personaJuridicaEntity);
	}

	@Override
	public boolean remove(PersonaJuridicaModel personaJuridicaModel) {
		PersonaJuridicaEntity personaJuridicaEntity = em.find(PersonaJuridicaEntity.class, personaJuridicaModel.getId());
		if (personaJuridicaEntity == null) return false;
        em.remove(personaJuridicaEntity);
        return true;
	}

	@Override
	public PersonaJuridicaModel findById(String id) {	
		PersonaJuridicaEntity personaJuridicaEntity = em.find(PersonaJuridicaEntity.class, id);		
		return personaJuridicaEntity != null ? new PersonaJuridicaAdapter(em, personaJuridicaEntity) : null;	
	}

	@Override
	public PersonaJuridicaModel findByTipoNumeroDocumento(TipoDocumentoModel tipoDocumento, String numeroDocumento) {
		TypedQuery<PersonaJuridicaEntity> query = em.createQuery("SELECT p FROM PersonaJuridicaEntity p WHERE p.tipoDocumento.abreviatura = :tipoDocumento AND p.numeroDocumento = :numeroDocumento", PersonaJuridicaEntity.class);
		query.setParameter("tipoDocumento", tipoDocumento.getAbreviatura());
		query.setParameter("numeroDocumento", numeroDocumento);
		List<PersonaJuridicaEntity> results = query.getResultList();
		if (results.size() == 0)
			return null;
		return new PersonaJuridicaAdapter(em, results.get(0));
	}

	@Override
	public List<PersonaJuridicaModel> getPersonasJuridicas() {
		return getPersonasJuridicas(-1, -1);
	}

	@Override
	public long getPersonasJuridicasCount() {
		Object count = em.createQuery("select count(u) from PersonaJuridicaEntity u").getSingleResult();
		return (Long) count;
	}

	@Override
	public List<PersonaJuridicaModel> getPersonasJuridicas(int firstResult, int maxResults) {
		TypedQuery<PersonaJuridicaEntity> query = em.createQuery("SELECT p FROM PersonaJuridicaEntity p", PersonaJuridicaEntity.class);
		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		List<PersonaJuridicaEntity> results = query.getResultList();
		List<PersonaJuridicaModel> users = new ArrayList<PersonaJuridicaModel>();
		for (PersonaJuridicaEntity entity : results)
			users.add(new PersonaJuridicaAdapter(em, entity));
		return users;
	}

	@Override
	public List<PersonaJuridicaModel> searchForNumeroDocumento(String numeroDocumento) {
		return searchForNumeroDocumento(numeroDocumento, -1, -1);
	}

	@Override
	public List<PersonaJuridicaModel> searchForNumeroDocumento(String numeroDocumento, int firstResult, int maxResults) {
		TypedQuery<PersonaJuridicaEntity> query = em.createQuery("SELECT p FROM PersonaJuridicaEntity p WHERE p.numeroDocumento like :numeroDocumento", PersonaJuridicaEntity.class);
		query.setParameter("numeroDocumento", "%" + numeroDocumento + "%");
		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		List<PersonaJuridicaEntity> results = query.getResultList();
		List<PersonaJuridicaModel> users = new ArrayList<PersonaJuridicaModel>();
		for (PersonaJuridicaEntity entity : results)
			users.add(new PersonaJuridicaAdapter(em, entity));
		return users;
	}

	@Override
	public List<PersonaJuridicaModel> searchForFilterText(String filterText) {
		return searchForFilterText(filterText, -1, -1);
	}

	@Override
	public List<PersonaJuridicaModel> searchForFilterText(String filterText, int firstResult, int maxResults) {		
		TypedQuery<PersonaJuridicaEntity> query = em.createQuery("SELECT p FROM PersonaJuridicaEntity p WHERE p.numeroDocumento like :filtertext OR UPPER(p.razonSocial) LIKE :filtertext", PersonaJuridicaEntity.class);
		query.setParameter("filtertext", "%" + filterText.toUpperCase() + "%");
		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		List<PersonaJuridicaEntity> results = query.getResultList();
		List<PersonaJuridicaModel> users = new ArrayList<PersonaJuridicaModel>();
		for (PersonaJuridicaEntity entity : results)
			users.add(new PersonaJuridicaAdapter(em, entity));
		return users;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

}
