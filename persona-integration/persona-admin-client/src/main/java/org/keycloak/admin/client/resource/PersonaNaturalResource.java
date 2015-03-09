package org.keycloak.admin.client.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.keycloak.representations.idm.PersonaNaturalRepresentation;

public interface PersonaNaturalResource {

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public PersonaNaturalRepresentation findById(@PathParam("id") Long id);

	@GET
	@Path("/buscar")
	@Produces(MediaType.APPLICATION_JSON)
	public PersonaNaturalRepresentation findByTipoNumeroDocumento(@QueryParam("tipoDocumento") String tipoDocumento, @QueryParam("numeroDocumento") String numeroDocumento);
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(PersonaNaturalRepresentation personaNaturalRepresentation);

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void update(@PathParam("id") Long id, PersonaNaturalRepresentation rep);

	@DELETE
	@Path("/{id}")
	public void remove(@PathParam("id") Long id);

}