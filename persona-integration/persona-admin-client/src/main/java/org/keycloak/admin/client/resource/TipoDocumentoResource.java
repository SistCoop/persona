package org.keycloak.admin.client.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.keycloak.representations.idm.TipoDocumentoRepresentation;

public interface TipoDocumentoResource {

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public TipoDocumentoRepresentation findById(@PathParam("id") String id);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(TipoDocumentoRepresentation tipoDocumentoRepresentation);

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void update(@PathParam("id") String id, TipoDocumentoRepresentation tipoDocumentoRepresentation);

	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") String id);

}