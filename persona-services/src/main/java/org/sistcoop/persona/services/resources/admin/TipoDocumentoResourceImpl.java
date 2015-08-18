package org.sistcoop.persona.services.resources.admin;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PathParam;

import org.sistcoop.persona.admin.client.resource.TipoDocumentoResource;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.representations.idm.TipoDocumentoRepresentation;
import org.sistcoop.persona.services.managers.TipoDocumentoManager;

@Stateless
public class TipoDocumentoResourceImpl implements TipoDocumentoResource {

    @PathParam("documento")
    private String documento;

    @Inject
    private TipoDocumentoProvider tipoDocumentoProvider;

    @Inject
    private TipoDocumentoManager tipoDocumentoManager;

    private TipoDocumentoModel getTipoDocumentoModel() {
        return tipoDocumentoProvider.findByAbreviatura(documento);
    }

    @Override
    public TipoDocumentoRepresentation documento() {
        TipoDocumentoRepresentation rep = ModelToRepresentation.toRepresentation(getTipoDocumentoModel());
        if (rep != null) {
            return rep;
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public void update(TipoDocumentoRepresentation representation) {
        tipoDocumentoManager.update(getTipoDocumentoModel(), representation);
    }

    @Override
    public void enable() {
        tipoDocumentoManager.enable(getTipoDocumentoModel());
    }

    @Override
    public void disable() {
        tipoDocumentoManager.disable(getTipoDocumentoModel());
    }

    @Override
    public void remove() {
        boolean result = tipoDocumentoProvider.remove(getTipoDocumentoModel());
        if (!result) {
            throw new InternalServerErrorException();
        }
    }

}
