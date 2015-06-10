package org.sistcoop.persona.services.resources.admin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.sistcoop.persona.admin.client.Roles;
import org.sistcoop.persona.admin.client.resource.TipoDocumentoResource;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.enums.TipoPersona;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.models.utils.RepresentationToModel;
import org.sistcoop.persona.representations.idm.TipoDocumentoRepresentation;

@Stateless
@SecurityDomain("keycloak")
public class TipoDocumentoResourceImpl implements TipoDocumentoResource {

	@Inject
	private TipoDocumentoProvider tipoDocumentoProvider;

	@Inject
	private RepresentationToModel representationToModel;

	@Context
	private UriInfo uriInfo;

	@RolesAllowed(Roles.ver_documentos)
	@Override		
	public List<TipoDocumentoRepresentation> findAll(String tipoPersona, Boolean estado) {
		List<TipoDocumentoModel> list = null;
		
		if(tipoPersona != null) {
			TipoPersona personType = TipoPersona.valueOf(tipoPersona.toUpperCase());
			if(estado != null){				
				list = tipoDocumentoProvider.getTiposDocumento(personType, estado);
			} else {
				list = tipoDocumentoProvider.getTiposDocumento(personType);
			}
		} else {
			if (estado != null) {
				list = tipoDocumentoProvider.getTiposDocumento(estado);
			} else {
				list = tipoDocumentoProvider.getTiposDocumento();
			}
		}

		List<TipoDocumentoRepresentation> result = new ArrayList<>();
		for (TipoDocumentoModel model : list) {
			result.add(ModelToRepresentation.toRepresentation(model));
		}		

		return result;
	}
	
	@RolesAllowed(Roles.ver_documentos)
	@Override
	public TipoDocumentoRepresentation findById(String id) {
		TipoDocumentoModel model = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(id);
		TipoDocumentoRepresentation rep = ModelToRepresentation.toRepresentation(model);
		return rep;
	}

	@RolesAllowed(Roles.administrar_documentos)
	@Override
	public Response create(TipoDocumentoRepresentation tipoDocumentoRepresentation) {
		TipoDocumentoModel tipoDocumentoModel = representationToModel.createTipoDocumento(tipoDocumentoRepresentation, tipoDocumentoProvider);
		return Response.created(uriInfo.getAbsolutePathBuilder().path(tipoDocumentoModel.getAbreviatura()).build()).header("Access-Control-Expose-Headers", "Location").build();
	}

	@RolesAllowed(Roles.administrar_documentos)
	@Override
	public void update(String id, TipoDocumentoRepresentation tipoDocumentoRepresentation) {
		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(id);
		tipoDocumentoModel.setDenominacion(tipoDocumentoRepresentation.getDenominacion());
		tipoDocumentoModel.setTipoPersona(TipoPersona.valueOf(tipoDocumentoRepresentation.getTipoPersona().toUpperCase()));
		tipoDocumentoModel.setCantidadCaracteres(tipoDocumentoRepresentation.getCantidadCaracteres());
		tipoDocumentoModel.setEstado(tipoDocumentoRepresentation.getEstado());
		tipoDocumentoModel.commit();
	}

	@RolesAllowed(Roles.eliminar_documentos)
	@Override
	public void delete(String id) {
		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(id);
		boolean removed = tipoDocumentoProvider.removeTipoDocumento(tipoDocumentoModel);
		if (!removed)
			throw new InternalServerErrorException("No se pudo eliminar el elemento");
	}

}
