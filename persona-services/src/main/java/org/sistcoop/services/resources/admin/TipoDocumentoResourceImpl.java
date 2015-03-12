package org.sistcoop.services.resources.admin;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sistcoop.admin.client.resource.TipoDocumentoResource;
import org.sistcoop.models.TipoDocumentoModel;
import org.sistcoop.models.TipoDocumentoProvider;
import org.sistcoop.models.enums.TipoPersona;
import org.sistcoop.models.utils.ModelToRepresentation;
import org.sistcoop.models.utils.RepresentationToModel;
import org.sistcoop.representations.idm.TipoDocumentoRepresentation;

public class TipoDocumentoResourceImpl implements TipoDocumentoResource {

	@Inject
	protected TipoDocumentoProvider tipoDocumentoProvider;

	@Inject
	protected RepresentationToModel representationToModel;

	@Context
	protected UriInfo uriInfo;

	@Override
	public TipoDocumentoRepresentation findById(String id) {
		TipoDocumentoModel model = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(id);
		TipoDocumentoRepresentation rep = ModelToRepresentation.toRepresentation(model);
		return rep;
	}

	@Override
	public Response create(TipoDocumentoRepresentation tipoDocumentoRepresentation) {
		TipoDocumentoModel tipoDocumentoModel = representationToModel.createTipoDocumento(tipoDocumentoRepresentation, tipoDocumentoProvider);
		return Response.created(uriInfo.getAbsolutePathBuilder().path(tipoDocumentoModel.getAbreviatura()).build()).header("Access-Control-Expose-Headers", "Location").build();
	}

	@Override
	public void update(String id, TipoDocumentoRepresentation tipoDocumentoRepresentation) {
		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(id);
		tipoDocumentoModel.setDenominacion(tipoDocumentoRepresentation.getDenominacion());
		tipoDocumentoModel.setTipoPersona(TipoPersona.valueOf(tipoDocumentoRepresentation.getTipoPersona().toUpperCase()));
		tipoDocumentoRepresentation.setCantidadCaracteres(tipoDocumentoRepresentation.getCantidadCaracteres());
		tipoDocumentoModel.commit();
	}

	@Override
	public void delete(String id) {
		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(id);
		boolean removed = tipoDocumentoProvider.removeTipoDocumento(tipoDocumentoModel);
		if (!removed)
			throw new InternalServerErrorException("No se pudo eliminar el elemento");
	}

}
