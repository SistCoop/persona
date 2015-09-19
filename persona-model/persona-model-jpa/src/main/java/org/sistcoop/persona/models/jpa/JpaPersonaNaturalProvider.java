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

import org.sistcoop.persona.models.ModelDuplicateException;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.PersonaNaturalProvider;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.enums.Sexo;
import org.sistcoop.persona.models.jpa.entities.AccionistaEntity;
import org.sistcoop.persona.models.jpa.entities.PersonaJuridicaEntity;
import org.sistcoop.persona.models.jpa.entities.PersonaNaturalEntity;
import org.sistcoop.persona.models.jpa.entities.TipoDocumentoEntity;
import org.sistcoop.persona.models.search.SearchCriteriaModel;
import org.sistcoop.persona.models.search.SearchResultsModel;

/**
 * @author <a href="mailto:carlosthe19916@sistcoop.com">Carlos Feria</a>
 */

@Named
@Stateless
@Local(PersonaNaturalProvider.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JpaPersonaNaturalProvider extends AbstractHibernateStorage implements PersonaNaturalProvider {

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
    public PersonaNaturalModel create(String codigoPais, TipoDocumentoModel tipoDocumentoModel,
            String numeroDocumento, String apellidoPaterno, String apellidoMaterno, String nombres,
            Date fechaNacimiento, Sexo sexo) {
        if (findByTipoNumeroDocumento(tipoDocumentoModel, numeroDocumento) != null) {
            throw new ModelDuplicateException(
                    "PersonaNaturalEntity tipoDocumento y numeroDocumento debe ser unico, se encontro otra entidad con tipoDocumento="
                            + tipoDocumentoModel + "y numeroDocumento=" + numeroDocumento);
        }

        TipoDocumentoEntity tipoDocumentoEntity = em.find(TipoDocumentoEntity.class,
                tipoDocumentoModel.getAbreviatura());

        PersonaNaturalEntity personaNaturalEntity = new PersonaNaturalEntity();
        personaNaturalEntity.setCodigoPais(codigoPais);
        personaNaturalEntity.setTipoDocumento(tipoDocumentoEntity);
        personaNaturalEntity.setNumeroDocumento(numeroDocumento);
        personaNaturalEntity.setApellidoPaterno(apellidoPaterno);
        personaNaturalEntity.setApellidoMaterno(apellidoMaterno);
        personaNaturalEntity.setNombres(nombres);
        personaNaturalEntity.setFechaNacimiento(fechaNacimiento);
        personaNaturalEntity.setSexo(sexo);
        em.persist(personaNaturalEntity);
        return new PersonaNaturalAdapter(em, personaNaturalEntity);
    }

    @Override
    public boolean remove(PersonaNaturalModel personaNaturalModel) {
        TypedQuery<AccionistaEntity> query1 = em.createNamedQuery("AccionistaEntity.findByIdPersonaNatural",
                AccionistaEntity.class);
        query1.setParameter("idPersonaNatural", personaNaturalModel.getId());
        query1.setMaxResults(1);
        if (!query1.getResultList().isEmpty()) {
            return false;
        }

        TypedQuery<PersonaJuridicaEntity> query2 = em
                .createNamedQuery("PersonaJuridicaEntity.FindByIdPersonaNaturalRepresentanteLegal",
                        PersonaJuridicaEntity.class);
        query2.setParameter("idPersonaNaturalRepresentanteLegal", personaNaturalModel.getId());
        query2.setMaxResults(1);
        if (!query2.getResultList().isEmpty()) {
            return false;
        }

        PersonaNaturalEntity personaNaturalEntity = em.find(PersonaNaturalEntity.class,
                personaNaturalModel.getId());
        if (personaNaturalEntity == null) {
            return false;
        }
        em.remove(personaNaturalEntity);
        return true;
    }

    @Override
    public PersonaNaturalModel findById(String id) {
        PersonaNaturalEntity personaNaturalEntity = this.em.find(PersonaNaturalEntity.class, id);
        return personaNaturalEntity != null ? new PersonaNaturalAdapter(em, personaNaturalEntity) : null;
    }

    @Override
    public PersonaNaturalModel findByTipoNumeroDocumento(TipoDocumentoModel tipoDocumento,
            String numeroDocumento) {
        TypedQuery<PersonaNaturalEntity> query = em.createNamedQuery(
                "PersonaNaturalEntity.findByTipoNumeroDocumento", PersonaNaturalEntity.class);
        query.setParameter("tipoDocumento", tipoDocumento.getAbreviatura());
        query.setParameter("numeroDocumento", numeroDocumento);
        List<PersonaNaturalEntity> results = query.getResultList();
        if (results.isEmpty()) {
            return null;
        } else if (results.size() > 1) {
            throw new IllegalStateException("Mas de una PersonaNaturalEntity con tipoDocumento="
                    + tipoDocumento + " y numeroDocumento=" + numeroDocumento + ", results=" + results);
        } else {
            return new PersonaNaturalAdapter(em, results.get(0));
        }
    }

    @Override
    public SearchResultsModel<PersonaNaturalModel> search() {
        TypedQuery<PersonaNaturalEntity> query = em.createNamedQuery("PersonaNaturalEntity.findAll",
                PersonaNaturalEntity.class);

        List<PersonaNaturalEntity> entities = query.getResultList();
        List<PersonaNaturalModel> models = new ArrayList<PersonaNaturalModel>();
        for (PersonaNaturalEntity personaNaturalEntity : entities) {
            models.add(new PersonaNaturalAdapter(em, personaNaturalEntity));
        }

        SearchResultsModel<PersonaNaturalModel> result = new SearchResultsModel<>();
        result.setModels(models);
        result.setTotalSize(models.size());
        return result;
    }

    @Override
    public SearchResultsModel<PersonaNaturalModel> search(SearchCriteriaModel criteria) {
        SearchResultsModel<PersonaNaturalEntity> entityResult = find(criteria, PersonaNaturalEntity.class);

        SearchResultsModel<PersonaNaturalModel> modelResult = new SearchResultsModel<>();
        List<PersonaNaturalModel> list = new ArrayList<>();
        for (PersonaNaturalEntity entity : entityResult.getModels()) {
            list.add(new PersonaNaturalAdapter(em, entity));
        }
        modelResult.setTotalSize(entityResult.getTotalSize());
        modelResult.setModels(list);
        return modelResult;
    }

    @Override
    public SearchResultsModel<PersonaNaturalModel> search(SearchCriteriaModel criteria, String filterText) {
        SearchResultsModel<PersonaNaturalEntity> entityResult = findFullText(criteria,
                PersonaNaturalEntity.class, filterText, "numeroDocumento", "apellidoPaterno",
                "apellidoMaterno", "nombres");

        SearchResultsModel<PersonaNaturalModel> modelResult = new SearchResultsModel<>();
        List<PersonaNaturalModel> list = new ArrayList<>();
        for (PersonaNaturalEntity entity : entityResult.getModels()) {
            list.add(new PersonaNaturalAdapter(em, entity));
        }
        modelResult.setTotalSize(entityResult.getTotalSize());
        modelResult.setModels(list);
        return modelResult;
    }

}
