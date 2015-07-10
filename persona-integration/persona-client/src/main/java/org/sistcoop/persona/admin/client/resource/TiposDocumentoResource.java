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

import org.sistcoop.persona.representations.idm.TipoDocumentoRepresentation;
import org.sistcoop.producto.representations.idm.search.SearchResultsRepresentation;

@Path("/tipoDocumentos")
@Consumes(MediaType.APPLICATION_JSON)
public interface TiposDocumentoResource {

    @Path("/{documento}")
    public TipoDocumentoResource documento(@PathParam("documento") String documento);

    @POST
    public Response create(TipoDocumentoRepresentation representation);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsRepresentation<TipoDocumentoRepresentation> search(
            @QueryParam("tipoPersona") String tipoPersona,
            @QueryParam("estado") @DefaultValue(value = "true") boolean estado);

}