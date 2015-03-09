package org.keycloak.admin.client.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.keycloak.representations.idm.AccionistaRepresentation;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface AccionistaResource {

	@GET
	@Path("/{id}")
	public AccionistaRepresentation findById(Long id);

	@DELETE
	@Path("/{id}")
	public void remove(Long id);

}