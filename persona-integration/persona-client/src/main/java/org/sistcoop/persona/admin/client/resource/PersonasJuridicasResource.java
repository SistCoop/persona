package org.sistcoop.persona.admin.client.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.sistcoop.persona.representations.idm.PersonaJuridicaRepresentation;
import org.sistcoop.producto.representations.idm.search.SearchResultsRepresentation;

@Path("/personas/juridicas")
@Consumes(MediaType.APPLICATION_JSON)
public interface PersonasJuridicasResource {

    @Path("/{persona}")
    public PersonaJuridicaResource persona(@PathParam("persona") String persona);

    @POST
    public Response create(PersonaJuridicaRepresentation representation);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsRepresentation<PersonaJuridicaRepresentation> search(
            @QueryParam("documento") String documento, @QueryParam("numero") String numero);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsRepresentation<PersonaJuridicaRepresentation> search(
            @QueryParam("filterText") @DefaultValue(value = "") String filterText,
            @QueryParam("page") @DefaultValue(value = "1") int page,
            @QueryParam("pageSize") @DefaultValue(value = "10") int pageSize);

}