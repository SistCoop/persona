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
import org.sistcoop.persona.representations.idm.search.SearchResultsRepresentation;

@Path("/personas/juridicas")
@Consumes(MediaType.APPLICATION_JSON)
public interface PersonasJuridicasResource {

    @Path("{personaJuridica}")
    public PersonaJuridicaResource personaJuridica(@PathParam("personaJuridica") String personaJuridica);

    @POST
    public Response create(PersonaJuridicaRepresentation representation);

    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsRepresentation<PersonaJuridicaRepresentation> search(
            @QueryParam("tipoDocumento") String tipoDocumento,
            @QueryParam("numeroDocumento") String numeroDocumento,
            @QueryParam("filterText") @DefaultValue(value = "") String filterText,
            @QueryParam("page") @DefaultValue(value = "1") int page,
            @QueryParam("pageSize") @DefaultValue(value = "20") int pageSize);

}