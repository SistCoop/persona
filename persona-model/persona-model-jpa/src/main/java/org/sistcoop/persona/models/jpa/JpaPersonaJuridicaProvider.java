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
import org.sistcoop.persona.models.search.SearchCriteriaModel;
import org.sistcoop.persona.models.search.SearchResultsModel;

@Named
@Stateless
@Local(PersonaJuridicaProvider.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JpaPersonaJuridicaProvider extends AbstractHibernateStorage implements PersonaJuridicaProvider {

    @PersistenceContext
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
    }

    @Override
    public PersonaJuridicaModel create(PersonaNaturalModel representanteLegal, String codigoPais,
            TipoDocumentoModel tipoDocumentoModel, String numeroDocumento, String razonSocial,
            Date fechaConstitucion, TipoEmpresa tipoEmpresa, boolean finLucro) {

        TipoDocumentoEntity tipoDocumentoEntity = em.find(TipoDocumentoEntity.class,
                tipoDocumentoModel.getAbreviatura());
        PersonaNaturalEntity personaNaturalEntity = em.find(PersonaNaturalEntity.class,
                representanteLegal.getId());

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
        PersonaJuridicaEntity personaJuridicaEntity = em.find(PersonaJuridicaEntity.class,
                personaJuridicaModel.getId());
        if (personaJuridicaEntity == null) {
            return false;
        }
        em.remove(personaJuridicaEntity);
        return true;
    }

    @Override
    public PersonaJuridicaModel findById(String id) {
        PersonaJuridicaEntity personaJuridicaEntity = em.find(PersonaJuridicaEntity.class, id);
        return personaJuridicaEntity != null ? new PersonaJuridicaAdapter(em, personaJuridicaEntity) : null;
    }

    @Override
    public PersonaJuridicaModel findByTipoNumeroDocumento(TipoDocumentoModel tipoDocumento,
            String numeroDocumento) {
        TypedQuery<PersonaJuridicaEntity> query = em.createNamedQuery(
                "PersonaJuridicaEntity.findByTipoNumeroDocumento", PersonaJuridicaEntity.class);
        query.setParameter("tipoDocumento", tipoDocumento.getAbreviatura());
        query.setParameter("numeroDocumento", numeroDocumento);
        List<PersonaJuridicaEntity> results = query.getResultList();
        if (results.size() == 0) {
            return null;
        }
        return new PersonaJuridicaAdapter(em, results.get(0));
    }

    @Override
    public SearchResultsModel<PersonaJuridicaModel> search() {
        TypedQuery<PersonaJuridicaEntity> query = em.createNamedQuery("PersonaJuridicaEntity.findAll",
                PersonaJuridicaEntity.class);

        List<PersonaJuridicaEntity> entities = query.getResultList();
        List<PersonaJuridicaModel> models = new ArrayList<PersonaJuridicaModel>();
        for (PersonaJuridicaEntity personaJuridicaEntity : entities) {
            models.add(new PersonaJuridicaAdapter(em, personaJuridicaEntity));
        }

        SearchResultsModel<PersonaJuridicaModel> result = new SearchResultsModel<>();
        result.setModels(models);
        result.setTotalSize(models.size());
        return result;
    }

    @Override
    public SearchResultsModel<PersonaJuridicaModel> search(SearchCriteriaModel criteria) {
        SearchResultsModel<PersonaJuridicaEntity> entityResult = find(criteria, PersonaJuridicaEntity.class);

        SearchResultsModel<PersonaJuridicaModel> modelResult = new SearchResultsModel<>();
        List<PersonaJuridicaModel> list = new ArrayList<>();
        for (PersonaJuridicaEntity entity : entityResult.getModels()) {
            list.add(new PersonaJuridicaAdapter(em, entity));
        }
        modelResult.setTotalSize(entityResult.getTotalSize());
        modelResult.setModels(list);
        return modelResult;
    }

    @Override
    public SearchResultsModel<PersonaJuridicaModel> search(SearchCriteriaModel criteria, String filterText) {
        SearchResultsModel<PersonaJuridicaEntity> entityResult = findFullText(criteria,
                PersonaJuridicaEntity.class, filterText, "numeroDocumento", "razonSocial");

        SearchResultsModel<PersonaJuridicaModel> modelResult = new SearchResultsModel<>();
        List<PersonaJuridicaModel> list = new ArrayList<>();
        for (PersonaJuridicaEntity entity : entityResult.getModels()) {
            list.add(new PersonaJuridicaAdapter(em, entity));
        }
        modelResult.setTotalSize(entityResult.getTotalSize());
        modelResult.setModels(list);
        return modelResult;
    }

}
