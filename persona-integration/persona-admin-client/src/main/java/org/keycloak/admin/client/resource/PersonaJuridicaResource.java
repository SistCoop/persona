package org.keycloak.admin.client.resource;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.keycloak.representations.idm.AccionistaRepresentation;
import org.keycloak.representations.idm.PersonaJuridicaRepresentation;

public interface PersonaJuridicaResource {

	@GET
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public PersonaJuridicaRepresentation findById(@PathParam("id") Long id);

	@GET
	@Path("/buscar")
	@Produces({ "application/xml", "application/json" })
	public PersonaJuridicaRepresentation findByTipoNumeroDocumento(@QueryParam("tipoDocumento") String tipoDocumento, @QueryParam("numeroDocumento") String numeroDocumento);
	
	@POST
	@Produces({ "application/xml", "application/json" })
	public Response create(PersonaJuridicaRepresentation personaJuridicaRepresentation);

	@PUT
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public void update(@PathParam("id") Long id, PersonaJuridicaRepresentation rep);

	@DELETE
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public void remove(@PathParam("id") Long id);

	/**
	 * ACCIONISTA
	 */
	@GET
	@Path("/{id}/accionistas/{idAccionista}")
	@Produces({ "application/xml", "application/json" })
	public AccionistaRepresentation findAccionistaById(@PathParam("id") Long id, @PathParam("idAccionista") Long idAccionista);

	@GET
	@Path("/{id}/accionistas")
	@Produces({ "application/xml", "application/json" })
	public List<AccionistaRepresentation> findAllAccionistas(@PathParam("id") Long id);

	@POST
	@Path("/{id}/accionistas")
	@Produces({ "application/xml", "application/json" })
	public Response addAccionista(@PathParam("id") Long id, AccionistaRepresentation accionistaRepresentation);

	@PUT
	@Path("/{id}/accionistas/{idAccionista}")
	@Produces({ "application/xml", "application/json" })
	public void updateAccionista(@PathParam("id") Long id, @PathParam("idAccionista") Long idAccionista, AccionistaRepresentation accionistaRepresentation);

	@DELETE
	@Path("/{id}/accionistas/{idAccionista}")
	@Produces({ "application/xml", "application/json" })
	public void removeAccionista(@PathParam("id") Long id, @PathParam("idAccionista") Long idAccionista);

}