package org.sistcoop.persona.admin.client.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;
import org.sistcoop.persona.representations.idm.search.SearchCriteriaRepresentation;
import org.sistcoop.persona.representations.idm.search.SearchResultsRepresentation;

/**
 * @author carlosthe19916@gmail.com
 */

@Path("/personas/naturales")
@Consumes(MediaType.APPLICATION_JSON)
public interface PersonasNaturalesResource {

	@Path("{idPersonaNatural}")
	public PersonaNaturalResource personaNatural(@PathParam("idPersonaNatural") String idPersonaNatural);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(PersonaNaturalRepresentation rep);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<PersonaNaturalRepresentation> search(@QueryParam("tipoDocumento") String tipoDocumento,
			@QueryParam("numeroDocumento") String numeroDocumento,
			@QueryParam("apellidoPaterno") String apellidoPaterno,
			@QueryParam("apellidoMaterno") String apellidoMaterno, @QueryParam("nombres") String nombres,
			@QueryParam("first") Integer firstResult, @QueryParam("max") Integer maxResults);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<PersonaNaturalRepresentation> search(@QueryParam("filterText") String filterText,
			@QueryParam("first") Integer firstResult, @QueryParam("max") Integer maxResults);

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
	public SearchResultsRepresentation<PersonaNaturalRepresentation> search(SearchCriteriaRepresentation criteria);

}