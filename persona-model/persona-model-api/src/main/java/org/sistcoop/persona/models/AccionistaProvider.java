package org.sistcoop.persona.models;

import java.math.BigDecimal;

import javax.ejb.Local;

import org.sistcoop.persona.models.search.SearchResultsModel;
import org.sistcoop.persona.provider.Provider;

@Local
public interface AccionistaProvider extends Provider {

    AccionistaModel findById(String id);

    AccionistaModel create(PersonaJuridicaModel personaJuridicaModel,
            PersonaNaturalModel personaNaturalModel, BigDecimal porcentaje);

    boolean remove(AccionistaModel accionistaModel);

    SearchResultsModel<AccionistaModel> search(PersonaJuridicaModel personaJuridicaModel);

}
