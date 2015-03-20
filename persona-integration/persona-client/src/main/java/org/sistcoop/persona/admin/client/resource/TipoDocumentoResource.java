package org.sistcoop.persona.admin.client.resource;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.sistcoop.persona.representations.idm.TipoDocumentoRepresentation;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/tiposDocumento")
public interface TipoDocumentoResource {

	@GET
	@Path("/{id}")
	public TipoDocumentoRepresentation findById(
			@PathParam("id") 
			@NotNull 
			@Size(min = 1, max = 20) String id);

	@POST
	public Response create(
			@NotNull 
			@Valid TipoDocumentoRepresentation tipoDocumentoRepresentation);

	@PUT
	@Path("/{id}")
	public void update(@PathParam("id") String id, 
			@NotNull 
			@Valid TipoDocumentoRepresentation tipoDocumentoRepresentation);

	@DELETE
	@Path("/{id}")
	public void delete(
			@PathParam("id") 
			@NotNull 
			@Size(min = 1, max = 20) String id);

}