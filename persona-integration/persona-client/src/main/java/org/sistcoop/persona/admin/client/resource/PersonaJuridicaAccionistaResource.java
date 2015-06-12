package org.sistcoop.persona.admin.client.resource;

import java.util.List;

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

import org.sistcoop.persona.representations.idm.AccionistaRepresentation;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/personas/juridicas/{personaJuridica}/accionistas")
public interface PersonaJuridicaAccionistaResource {

	@GET
	public List<AccionistaRepresentation> findAllAccionistas(
			@PathParam("personaJuridica") 
			@NotNull 
			@Size(min = 1) String idPersonaJuridica);

	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public Response create(
			@PathParam("personaJuridica") 
			@NotNull 
			@Size(min = 1) String idPersonaJuridica,
			
			@NotNull
			@Valid AccionistaRepresentation accionistaRepresentation);	
	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@PUT
	@Path("/{accionista}")
	public void update(
			@PathParam("personaJuridica") 
			@NotNull 
			@Size(min = 1) String idPersonaJuridica,
			
			@PathParam("accionista") 
			@NotNull 
			@Size(min = 1) String idAccionista,
			
			@NotNull
			@Valid AccionistaRepresentation accionistaRepresentation);	

	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@DELETE
	@Path("/{accionista}")
	public void delete(
			@PathParam("personaJuridica") 
			@NotNull 
			@Size(min = 1) String idPersonaJuridica,
			
			@PathParam("accionista") 
			@NotNull 
			@Size(min = 1) String idAccionista);	
	
}