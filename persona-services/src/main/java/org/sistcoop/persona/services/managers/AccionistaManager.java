package org.sistcoop.persona.services.managers;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.sistcoop.persona.models.AccionistaModel;
import org.sistcoop.persona.representations.idm.AccionistaRepresentation;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AccionistaManager {

    public void update(AccionistaModel model, AccionistaRepresentation rep) {
        model.setPorcentajeParticipacion(rep.getPorcentajeParticipacion());
        model.commit();
    }

}