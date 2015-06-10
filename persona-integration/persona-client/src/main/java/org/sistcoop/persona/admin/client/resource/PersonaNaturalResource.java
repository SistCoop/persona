package org.sistcoop.persona.admin.client.resource;

import java.util.List;

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
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;

@Path("/personas/naturales")
public interface PersonaNaturalResource {

	@GET	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<PersonaNaturalRepresentation> listAll(
			@QueryParam("filterText")
			@Size(min = 1, max = 100) String filterText, 
			
			@QueryParam("firstResult") 
			@Min(value = 0) Integer firstResult, 
			
			@QueryParam("maxResults") 
			@Min(value = 1) Integer maxResults);

	@GET
	@Path("/count")	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public long countAll();
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public PersonaNaturalRepresentation findById(
			@PathParam("id") 
			@NotNull 
			@Size(min = 1) String id);

	@GET
	@Path("/buscar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
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
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(
			@NotNull
			@Valid PersonaNaturalRepresentation personaNaturalRepresentation);

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(
			@PathParam("id") 
			@NotNull 
			@Size(min = 1) String id, 
			
			@NotNull
			@Valid PersonaNaturalRepresentation personaNaturalRepresentation);

	@DELETE
	@Path("/{id}")	
	public void remove(
			@PathParam("id") 
			@NotNull 
			@Size(min = 1) String id);

	@POST
	@Path("/{id}/foto")
	@Consumes("multipart/form-data")
	public void setFoto(
			@PathParam("id") 
			@NotNull 
			@Size(min = 1) String id, 
			
			@NotNull MultipartFormDataInput input);
	
	@POST
	@Path("/{id}/firma")
	@Consumes("multipart/form-data")
	public void setFirma(
			@PathParam("id") 
			@NotNull 
			@Size(min = 1) String id, 
			
			@NotNull MultipartFormDataInput input);
	
}