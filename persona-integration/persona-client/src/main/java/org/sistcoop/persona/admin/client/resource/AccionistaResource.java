package org.sistcoop.persona.admin.client.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.sistcoop.persona.representations.idm.AccionistaRepresentation;

/**
 * @author carlosthe19916@gmail.com
 */

public interface AccionistaResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public AccionistaRepresentation toRepresentation();

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(AccionistaRepresentation rep);

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response remove();

}