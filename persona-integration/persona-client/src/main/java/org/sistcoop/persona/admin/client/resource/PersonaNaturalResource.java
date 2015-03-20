package org.sistcoop.persona.admin.client.resource;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.NotBlank;
import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/personasNaturales")
public interface PersonaNaturalResource {

	@GET
	@Path("/{id}")
	public PersonaNaturalRepresentation findById(
			@PathParam("id") 
			@NotNull 
			@Min(value = 1) Long id);

	@GET
	@Path("/buscar")
	public PersonaNaturalRepresentation findByTipoNumeroDocumento(
			@QueryParam("tipoDocumento")
			@NotNull
			@NotBlank
			@Size(min = 1, max = 20) String tipoDocumento,
			
			@QueryParam("numeroDocumento") 
			@NotNull
			@NotBlank
			@Size(min = 1, max = 20) String numeroDocumento);

	@POST
	public Response create(
			@NotNull
			@Valid PersonaNaturalRepresentation personaNaturalRepresentation);

	@PUT
	@Path("/{id}")
	public void update(
			@PathParam("id") 
			@NotNull 
			@Min(value = 1) Long id, 
			
			@NotNull
			@Valid PersonaNaturalRepresentation personaNaturalRepresentation);

	@DELETE
	@Path("/{id}")
	public void remove(
			@PathParam("id") 
			@NotNull 
			@Min(value = 1) Long id);

}