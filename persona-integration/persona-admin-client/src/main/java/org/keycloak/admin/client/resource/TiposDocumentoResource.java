package org.keycloak.admin.client.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.keycloak.representations.idm.TipoDocumentoRepresentation;

public interface TiposDocumentoResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<TipoDocumentoRepresentation> findAll(
			@QueryParam("tipoPersona") String tipoPersona,
			@QueryParam("estado") Boolean estado);

}