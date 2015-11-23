package org.sistcoop.persona.admin.client.resource;

import java.util.List;

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
import org.sistcoop.persona.representations.idm.search.SearchCriteriaRepresentation;
import org.sistcoop.persona.representations.idm.search.SearchResultsRepresentation;

/**
 * @author carlosthe19916@gmail.com
 */

@Path("tipoDocumentos")
@Consumes(MediaType.APPLICATION_JSON)
public interface TiposDocumentoResource {

    @Path("{tipoDocumento}")
    public TipoDocumentoResource tipoDocumento(@PathParam("tipoDocumento") String tipoDocumento);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(TipoDocumentoRepresentation rep);

    @POST
    @Path("findByAbreviatura")
    @Produces(MediaType.APPLICATION_JSON)
    public TipoDocumentoRepresentation findByAbreviatura(TipoDocumentoRepresentation rep);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TipoDocumentoRepresentation> getAll(@QueryParam("tipoPersona") String tipoPersona,
            @QueryParam("estado") @DefaultValue(value = "true") Boolean estado);

    /**
     * Este endpoint provee una forma de buscar direccionesRegionales. Los
     * criterios de busqueda estan definidos por los parametros enviados.
     * 
     * @summary Search for DireccionesRegionales
     * @param criteria
     *            Criterio de busqueda.
     * @statuscode 200 Si la busqueda fue realizada satisfactoriamente.
     * @return Los resultados de la busqueda (una pagina de
     *         direccionesRegionales).
     */
    @POST
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsRepresentation<TipoDocumentoRepresentation> search(
            SearchCriteriaRepresentation criteria);

}