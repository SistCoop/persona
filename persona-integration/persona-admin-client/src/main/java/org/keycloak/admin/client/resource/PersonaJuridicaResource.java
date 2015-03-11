package org.keycloak.admin.client.resource;

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
import org.keycloak.representations.idm.AccionistaRepresentation;
import org.keycloak.representations.idm.PersonaJuridicaRepresentation;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PersonaJuridicaResource {

	@GET
	@Path("/{id}")
	public PersonaJuridicaRepresentation findById(
			@PathParam("id") 
			@NotNull 
			@Min(value = 1) Long id);

	@GET
	@Path("/buscar")
	public PersonaJuridicaRepresentation findByTipoNumeroDocumento(
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
			@Valid PersonaJuridicaRepresentation personaJuridicaRepresentation);

	@PUT
	@Path("/{id}")
	public void update(
			@PathParam("id") 
			@NotNull 
			@Min(value = 1) Long id,
			
			@NotNull
			@Valid PersonaJuridicaRepresentation rep);

	@DELETE
	@Path("/{id}")
	public void remove(
			@PathParam("id") 
			@NotNull 
			@Min(value = 1) Long id);

	/**
	 * ACCIONISTA
	 */
	@GET
	@Path("/{id}/accionistas/{idAccionista}")
	public AccionistaRepresentation findAccionistaById(
			@PathParam("id") 
			@NotNull 
			@Min(value = 1) Long id,
			
			@PathParam("idAccionista") 			
			@NotNull 
			@Min(value = 1) Long idAccionista);

	@GET
	@Path("/{id}/accionistas")
	public List<AccionistaRepresentation> findAllAccionistas(
			@PathParam("id") 
			@NotNull 
			@Min(value = 1) Long id);

	@POST
	@Path("/{id}/accionistas")
	public Response addAccionista(
			@PathParam("id") 
			@NotNull 
			@Min(value = 1) Long id,
			
			@NotNull
			@Valid AccionistaRepresentation accionistaRepresentation);

	@PUT
	@Path("/{id}/accionistas/{idAccionista}")
	public void updateAccionista(
			@PathParam("id") 
			@NotNull 
			@Min(value = 1) Long id,
			
			@PathParam("idAccionista") 			
			@NotNull 
			@Min(value = 1) Long idAccionista,
			
			@NotNull
			@Valid AccionistaRepresentation accionistaRepresentation);

	@DELETE
	@Path("/{id}/accionistas/{idAccionista}")
	public void removeAccionista(
			@PathParam("id") 
			@NotNull 
			@Min(value = 1) Long id,
			
			@PathParam("idAccionista") 			
			@NotNull 
			@Min(value = 1) Long idAccionista);

}