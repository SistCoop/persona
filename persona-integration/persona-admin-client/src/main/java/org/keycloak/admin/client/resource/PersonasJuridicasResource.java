package org.keycloak.admin.client.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.keycloak.representations.idm.PersonaJuridicaRepresentation;

public interface PersonasJuridicasResource {
	
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<PersonaJuridicaRepresentation> findAll(
			@QueryParam("filterText") String filterText, 
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	@GET
	@Path("/count")
	@Produces({ "application/xml", "application/json" })
	public Response countAll();	
	
}