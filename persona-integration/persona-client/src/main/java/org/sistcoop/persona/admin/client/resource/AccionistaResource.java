package org.sistcoop.persona.admin.client.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.sistcoop.persona.representations.idm.AccionistaRepresentation;

public interface AccionistaResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AccionistaRepresentation accionista();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(AccionistaRepresentation representation);

    @POST
    @Path("disable")
    public void disable();

    @DELETE
    public void remove();

}