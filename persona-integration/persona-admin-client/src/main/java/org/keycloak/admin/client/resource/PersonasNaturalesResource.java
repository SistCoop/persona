package org.keycloak.admin.client.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.keycloak.representations.idm.PersonaNaturalRepresentation;

public interface PersonasNaturalesResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<PersonaNaturalRepresentation> listAll(
			@QueryParam("filterText") String filterText, 
			@QueryParam("offset") Integer offset, 
			@QueryParam("limit") Integer limit);

	@GET
	@Path("/count")
	@Produces(MediaType.APPLICATION_JSON)
	public int countAll();

}