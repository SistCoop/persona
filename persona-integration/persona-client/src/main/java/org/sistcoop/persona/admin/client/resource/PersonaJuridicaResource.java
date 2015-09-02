package org.sistcoop.persona.admin.client.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.sistcoop.persona.representations.idm.PersonaJuridicaRepresentation;

public interface PersonaJuridicaResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PersonaJuridicaRepresentation personaJuridica();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(PersonaJuridicaRepresentation representation);

    @POST
    @Path("disable")
    public void disable();

    @DELETE
    public void remove();

    @Path("accionistas")
    public AccionistasResource accionistas();

}