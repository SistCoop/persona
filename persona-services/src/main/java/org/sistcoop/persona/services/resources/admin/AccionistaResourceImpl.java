package org.sistcoop.persona.services.resources.admin;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PathParam;

import org.sistcoop.persona.admin.client.resource.AccionistaResource;
import org.sistcoop.persona.models.AccionistaModel;
import org.sistcoop.persona.models.AccionistaProvider;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.representations.idm.AccionistaRepresentation;
import org.sistcoop.persona.services.managers.AccionistaManager;
import org.sistcoop.persona.services.messages.Messages;

@Stateless
public class AccionistaResourceImpl implements AccionistaResource {

    @PathParam("accionista")
    private String accionista;

    @Inject
    private AccionistaProvider accionistaProvider;

    @Inject
    private AccionistaManager accionistaManager;

    private AccionistaModel getAccionistaModel() {
        return accionistaProvider.findById(accionista);
    }

    @Override
    public AccionistaRepresentation accionista() {
        AccionistaRepresentation rep = ModelToRepresentation.toRepresentation(getAccionistaModel());
        if (rep != null) {
            return rep;
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public void update(AccionistaRepresentation representation) {
        accionistaManager.update(getAccionistaModel(), representation);
    }

    @Override
    public void disable() {
        throw new NotFoundException();
    }

    @Override
    public void remove() {
        boolean result = accionistaProvider.remove(getAccionistaModel());
        if (!result) {
            throw new InternalServerErrorException(Messages.ERROR);
        }
    }

}
