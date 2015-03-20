package org.sistcoop.persona.admin.client.resource;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;

@Produces(MediaType.APPLICATION_JSON)
@Path("/personasNaturales")
public interface PersonasNaturalesResource {

	@GET	
	public List<PersonaNaturalRepresentation> listAll(
			@QueryParam("filterText")
			@Size(min = 1, max = 100) String filterText, 
			
			@QueryParam("firstResult") 
			@Min(value = 0) Integer firstResult, 
			
			@QueryParam("maxResults") 
			@Min(value = 1) Integer maxResults);

	@GET
	@Path("/count")	
	public long countAll();

}