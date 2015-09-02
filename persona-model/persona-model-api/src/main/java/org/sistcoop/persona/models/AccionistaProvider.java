package org.sistcoop.persona.models;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;

import org.sistcoop.persona.provider.Provider;

@Local
public interface AccionistaProvider extends Provider {

    AccionistaModel findById(String id);

    AccionistaModel create(PersonaJuridicaModel personaJuridicaModel,
            PersonaNaturalModel personaNaturalModel, BigDecimal porcentaje);

    boolean remove(AccionistaModel accionistaModel);

    List<AccionistaModel> getAll(PersonaJuridicaModel personaJuridicaModel);

}
