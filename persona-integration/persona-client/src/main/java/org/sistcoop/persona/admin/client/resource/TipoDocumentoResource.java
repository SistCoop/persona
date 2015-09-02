package org.sistcoop.persona.admin.client.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.sistcoop.persona.representations.idm.TipoDocumentoRepresentation;

public interface TipoDocumentoResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TipoDocumentoRepresentation tipoDocumento();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(TipoDocumentoRepresentation representation);

    @POST
    @Path("enable")
    public void enable();

    @POST
    @Path("disable")
    public void disable();

    @DELETE
    public void remove();

}