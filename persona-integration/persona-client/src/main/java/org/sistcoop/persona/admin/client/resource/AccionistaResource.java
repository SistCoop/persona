package org.sistcoop.persona.admin.client.resource;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.sistcoop.persona.representations.idm.AccionistaRepresentation;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/accionistas")
public interface AccionistaResource {

	@GET
	@Path("/{id}")
	public AccionistaRepresentation findById(
			@PathParam("id") 
			@NotNull 
			@Min(value = 1) Long id);
	
	@PUT
	@Path("/{id}")
	public void updateAccionista(
			@PathParam("id") 
			@NotNull 
			@Min(value = 1) Long id,					
			
			@NotNull
			@Valid AccionistaRepresentation accionistaRepresentation);	

	@DELETE
	@Path("/{id}")
	public void remove(
			@PathParam("id") 
			@NotNull 
			@Min(value = 1) Long id);

}