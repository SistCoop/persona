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

import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;
import org.sistcoop.persona.representations.idm.search.SearchResultsRepresentation;

@Path("/personas/naturales")
@Consumes(MediaType.APPLICATION_JSON)
public interface PersonasNaturalesResource {

    @Path("/{persona}")
    public PersonaNaturalResource persona(@PathParam("persona") String persona);

    @POST
    public Response create(PersonaNaturalRepresentation representation);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsRepresentation<PersonaNaturalRepresentation> search(
            @QueryParam("documento") String documento, @QueryParam("numero") String numero);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsRepresentation<PersonaNaturalRepresentation> search(
            @QueryParam("filterText") @DefaultValue(value = "") String filterText,
            @QueryParam("page") @DefaultValue(value = "1") int page,
            @QueryParam("pageSize") @DefaultValue(value = "10") int pageSize);

}