package org.sistcoop.persona.services.resources.admin;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.sistcoop.persona.admin.client.resource.AccionistaResource;
import org.sistcoop.persona.models.AccionistaModel;
import org.sistcoop.persona.models.AccionistaProvider;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.representations.idm.AccionistaRepresentation;
import org.sistcoop.persona.services.ErrorResponse;
import org.sistcoop.persona.services.managers.AccionistaManager;

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
    public AccionistaRepresentation toRepresentation() {
        AccionistaRepresentation rep = ModelToRepresentation.toRepresentation(getAccionistaModel());
        if (rep != null) {
            return rep;
        } else {
            throw new NotFoundException("Accionista no encontrado");
        }
    }

    @Override
    public void update(AccionistaRepresentation rep) {
        accionistaManager.update(getAccionistaModel(), rep);
    }

    @Override
    public void disable() {
        throw new NotFoundException();
    }

    @Override
    public Response remove() {
        AccionistaModel accionistaModel = getAccionistaModel();
        if (accionistaModel == null) {
            throw new NotFoundException("Accionista no encontrado");
        }
        boolean removed = accionistaProvider.remove(accionistaModel);
        if (removed) {
            return Response.noContent().build();
        } else {
            return ErrorResponse.error("Accionista no pudo ser eliminado", Response.Status.BAD_REQUEST);
        }
    }
}
