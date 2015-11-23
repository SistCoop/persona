package org.sistcoop.persona.admin.client.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.sistcoop.persona.representations.idm.PersonaJuridicaRepresentation;
import org.sistcoop.persona.representations.idm.search.SearchCriteriaRepresentation;
import org.sistcoop.persona.representations.idm.search.SearchResultsRepresentation;

/**
 * @author carlosthe19916@gmail.com
 */

@Path("/personas/juridicas")
@Consumes(MediaType.APPLICATION_JSON)
public interface PersonasJuridicasResource {

    @Path("{personaJuridica}")
    public PersonaJuridicaResource personaJuridica(@PathParam("personaJuridica") String personaJuridica);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(PersonaJuridicaRepresentation representation);

    @POST
    @Path("findByTipoNumeroDocumento")
    @Produces(MediaType.APPLICATION_JSON)
    public PersonaJuridicaRepresentation findByTipoNumeroDocumento(PersonaJuridicaRepresentation rep);

    
    /*@GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsRepresentation<PersonaJuridicaRepresentation> search(
            @QueryParam("tipoDocumento") String tipoDocumento,
            @QueryParam("numeroDocumento") String numeroDocumento,
            @QueryParam("filterText") @DefaultValue(value = "") String filterText,
            @QueryParam("page") @DefaultValue(value = "1") int page,
            @QueryParam("pageSize") @DefaultValue(value = "20") int pageSize);*/
    
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
    public SearchResultsRepresentation<PersonaJuridicaRepresentation> search(
            SearchCriteriaRepresentation criteria);

}