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

import org.keycloak.representations.idm.TipoDocumentoRepresentation;

public interface TipoDocumentoResource {

	@GET
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public TipoDocumentoRepresentation findById(@PathParam("id") String id);

	@GET
	@Produces({ "application/xml", "application/json" })
	public List<TipoDocumentoRepresentation> findAll(@QueryParam("tipoPersona") String tipoPersona);

	@POST
	@Produces({ "application/xml", "application/json" })
	public Response create(TipoDocumentoRepresentation tipoDocumentoRepresentation);

	@PUT
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public void update(@PathParam("id") String id, TipoDocumentoRepresentation tipoDocumentoRepresentation);

	@DELETE
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public void delete(@PathParam("id") String id);

}