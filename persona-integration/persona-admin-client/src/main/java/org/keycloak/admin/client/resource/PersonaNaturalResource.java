package org.keycloak.admin.client.resource;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.keycloak.representations.idm.PersonaNaturalRepresentation;

public interface PersonaNaturalResource {

	@GET
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public PersonaNaturalRepresentation findById(@PathParam("id") Long id);

	@GET
	@Path("/buscar")
	@Produces({ "application/xml", "application/json" })
	public PersonaNaturalRepresentation findByTipoNumeroDocumento(@QueryParam("tipoDocumento") String tipoDocumento, @QueryParam("numeroDocumento") String numeroDocumento);

	@GET
	@Produces({ "application/xml", "application/json" })
	public List<PersonaNaturalRepresentation> listAll(@QueryParam("filterText") String filterText, @QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit);

	@GET
	@Path("/count")
	@Produces({ "application/xml", "application/json" })
	public int countAll();

	@POST
	@Produces({ "application/xml", "application/json" })
	public Response create(PersonaNaturalRepresentation personaNaturalRepresentation);

	@PUT
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public void update(@PathParam("id") Long id, PersonaNaturalRepresentation rep);

	@DELETE
	@Path("/{id}")
	public void remove(@PathParam("id") Long id);

}