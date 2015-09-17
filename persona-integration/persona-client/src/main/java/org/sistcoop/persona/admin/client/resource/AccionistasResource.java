package org.sistcoop.persona.admin.client.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.sistcoop.persona.representations.idm.AccionistaRepresentation;

@Consumes(MediaType.APPLICATION_JSON)
public interface AccionistasResource {

    @Path("{accionista}")
    public AccionistaResource accionista(@PathParam("accionista") String accionista);

    @POST
    public Response create(AccionistaRepresentation rep);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccionistaRepresentation> getAll();

}