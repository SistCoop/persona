package org.sistcoop.persona.models.jpa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.sistcoop.persona.models.AccionistaModel;
import org.sistcoop.persona.models.AccionistaProvider;
import org.sistcoop.persona.models.PersonaJuridicaModel;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.jpa.entities.AccionistaEntity;
import org.sistcoop.persona.models.jpa.entities.PersonaJuridicaEntity;
import org.sistcoop.persona.models.jpa.entities.PersonaNaturalEntity;
import org.sistcoop.persona.models.search.SearchResultsModel;

@Named
@Stateless
@Local(AccionistaProvider.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JpaAccionistaProvider implements AccionistaProvider {

    @PersistenceContext
    protected EntityManager em;

    @Override
    public void close() {
        // TODO Auto-generated method stub
    }

    @Override
    public AccionistaModel create(PersonaJuridicaModel personaJuridicaModel,
            PersonaNaturalModel personaNaturalModel, BigDecimal porcentaje) {

        PersonaJuridicaEntity personaJuridicaEntity = em.find(PersonaJuridicaEntity.class,
                personaJuridicaModel.getId());
        PersonaNaturalEntity personaNaturalEntity = em.find(PersonaNaturalEntity.class,
                personaNaturalModel.getId());

        AccionistaEntity accionistaEntity = new AccionistaEntity();
        accionistaEntity.setPersonaNatural(personaNaturalEntity);
        accionistaEntity.setPersonaJuridica(personaJuridicaEntity);
        accionistaEntity.setPorcentajeParticipacion(porcentaje);
        em.persist(accionistaEntity);
        return new AccionistaAdapter(em, accionistaEntity);
    }

    @Override
    public AccionistaModel findById(String id) {
        AccionistaEntity accionistaEntity = em.find(AccionistaEntity.class, id);
        return accionistaEntity != null ? new AccionistaAdapter(em, accionistaEntity) : null;
    }

    @Override
    public boolean remove(AccionistaModel accionistaModel) {
        AccionistaEntity accionistaEntity = em.find(AccionistaEntity.class, accionistaModel.getId());
        if (accionistaEntity == null)
            return false;
        em.remove(accionistaEntity);
        return true;
    }

    @Override
    public SearchResultsModel<AccionistaModel> search(PersonaJuridicaModel personaJuridicaModel) {
        PersonaJuridicaEntity personaJuridicaEntity = em.find(PersonaJuridicaEntity.class,
                personaJuridicaModel.getId());

        Set<AccionistaEntity> entities = personaJuridicaEntity.getAccionistas();
        List<AccionistaModel> models = new ArrayList<AccionistaModel>();
        for (AccionistaEntity accionistaEntity : entities) {
            models.add(new AccionistaAdapter(em, accionistaEntity));
        }

        SearchResultsModel<AccionistaModel> result = new SearchResultsModel<>();
        result.setModels(models);
        result.setTotalSize(models.size());
        return result;
    }

}
