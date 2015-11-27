package org.sistcoop.persona.admin.client.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.sistcoop.persona.representations.idm.AccionistaRepresentation;

/**
 * @author carlosthe19916@gmail.com
 */

@Consumes(MediaType.APPLICATION_JSON)
public interface AccionistasResource {

	@Path("{idAccionista}")
	public AccionistaResource accionista(@PathParam("idAccionista") String idAccionista);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(AccionistaRepresentation rep);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<AccionistaRepresentation> getAll();

}