package org.sistcoop.persona.services.resources.admin;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.sistcoop.persona.admin.client.resource.TipoDocumentoResource;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.representations.idm.TipoDocumentoRepresentation;
import org.sistcoop.persona.services.ErrorResponse;
import org.sistcoop.persona.services.managers.TipoDocumentoManager;

@Stateless
public class TipoDocumentoResourceImpl implements TipoDocumentoResource {

	@PathParam("idTipoDocumento")
	private String idTipoDocumento;

	@Inject
	private TipoDocumentoProvider tipoDocumentoProvider;

	@Inject
	private TipoDocumentoManager tipoDocumentoManager;

	private TipoDocumentoModel getTipoDocumentoModel() {
		return tipoDocumentoProvider.findById(idTipoDocumento);
	}

	@Override
	public TipoDocumentoRepresentation toRepresentation() {
		TipoDocumentoRepresentation rep = ModelToRepresentation.toRepresentation(getTipoDocumentoModel());
		if (rep != null) {
			return rep;
		} else {
			throw new NotFoundException("Tipo Documento no encontrado");
		}
	}

	@Override
	public void update(TipoDocumentoRepresentation representation) {
		tipoDocumentoManager.update(getTipoDocumentoModel(), representation);
	}

	@Override
	public void enable() {
		tipoDocumentoManager.enable(getTipoDocumentoModel());
	}

	@Override
	public void disable() {
		tipoDocumentoManager.disable(getTipoDocumentoModel());
	}

	@Override
	public Response remove() {
		TipoDocumentoModel tipoDocumento = getTipoDocumentoModel();
		if (tipoDocumento == null) {
			throw new NotFoundException("Tipo Documento no encontrado");
		}
		boolean removed = tipoDocumentoProvider.remove(tipoDocumento);
		if (removed) {
			return Response.noContent().build();
		} else {
			return ErrorResponse.error("Tipo Documento no pudo ser eliminado", Response.Status.BAD_REQUEST);
		}
	}

}
