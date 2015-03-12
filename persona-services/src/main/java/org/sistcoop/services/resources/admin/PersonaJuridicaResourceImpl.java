package org.sistcoop.services.resources.admin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sistcoop.admin.client.resource.PersonaJuridicaResource;
import org.sistcoop.models.AccionistaModel;
import org.sistcoop.models.AccionistaProvider;
import org.sistcoop.models.PersonaJuridicaModel;
import org.sistcoop.models.PersonaJuridicaProvider;
import org.sistcoop.models.PersonaNaturalModel;
import org.sistcoop.models.PersonaNaturalProvider;
import org.sistcoop.models.TipoDocumentoModel;
import org.sistcoop.models.TipoDocumentoProvider;
import org.sistcoop.models.enums.TipoEmpresa;
import org.sistcoop.models.utils.ModelToRepresentation;
import org.sistcoop.models.utils.RepresentationToModel;
import org.sistcoop.representations.idm.AccionistaRepresentation;
import org.sistcoop.representations.idm.PersonaJuridicaRepresentation;

public class PersonaJuridicaResourceImpl implements PersonaJuridicaResource {

	@Inject
	protected PersonaJuridicaProvider personaJuridicaProvider;

	@Inject
	protected PersonaNaturalProvider personaNaturalProvider;

	@Inject
	protected TipoDocumentoProvider tipoDocumentoProvider;

	@Inject
	protected AccionistaProvider accionistaProvider;

	@Inject
	protected RepresentationToModel representationToModel;

	@Context
	protected UriInfo uriInfo;

	@Override
	public PersonaJuridicaRepresentation findById(Long id) {
		PersonaJuridicaModel model = personaJuridicaProvider.getPersonaJuridicaById(id);
		PersonaJuridicaRepresentation rep = ModelToRepresentation.toRepresentation(model);
		return rep;
	}

	@Override
	public PersonaJuridicaRepresentation findByTipoNumeroDocumento(String tipoDocumento, String numeroDocumento) {
		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(tipoDocumento);
		PersonaJuridicaModel model = personaJuridicaProvider.getPersonaJuridicaByTipoNumeroDoc(tipoDocumentoModel, numeroDocumento);
		PersonaJuridicaRepresentation rep = ModelToRepresentation.toRepresentation(model);
		return rep;
	}

	@Override
	public Response create(PersonaJuridicaRepresentation personaJuridicaRepresentation) {
		TipoDocumentoModel representanteTipoDocumentoModel = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(personaJuridicaRepresentation.getRepresentanteLegal().getTipoDocumento());
		PersonaNaturalModel representanteModel = personaNaturalProvider.getPersonaNaturalByTipoNumeroDoc(representanteTipoDocumentoModel, personaJuridicaRepresentation.getRepresentanteLegal().getNumeroDocumento());

		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(personaJuridicaRepresentation.getTipoDocumento());
		PersonaJuridicaModel personaJuridicaModel = representationToModel.createPersonaJuridica(personaJuridicaRepresentation, tipoDocumentoModel, representanteModel, personaJuridicaProvider);
		PersonaJuridicaRepresentation result = ModelToRepresentation.toRepresentation(personaJuridicaModel);
		return Response.created(uriInfo.getAbsolutePathBuilder().path(result.getId().toString()).build()).header("Access-Control-Expose-Headers", "Location").entity(result.getId()).build();
	}

	@Override
	public void update(Long id, PersonaJuridicaRepresentation rep) {
		PersonaJuridicaModel model = personaJuridicaProvider.getPersonaJuridicaById(id);
		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(rep.getTipoDocumento());

		model.setCodigoPais(rep.getCodigoPais());
		model.setTipoDocumento(tipoDocumentoModel);
		model.setNumeroDocumento(rep.getNumeroDocumento());
		model.setRazonSocial(rep.getRazonSocial());
		model.setFechaConstitucion(rep.getFechaConstitucion());
		model.setActividadPrincipal(rep.getActividadPrincipal());
		model.setNombreComercial(rep.getNombreComercial());
		model.setFinLucro(rep.isFinLucro());
		model.setTipoEmpresa(TipoEmpresa.valueOf(rep.getTipoEmpresa()));

		model.setUbigeo(rep.getUbigeo());
		model.setDireccion(rep.getDireccion());
		model.setReferencia(rep.getReferencia());
		model.setTelefono(rep.getTelefono());
		model.setCelular(rep.getCelular());
		model.setEmail(rep.getEmail());

		model.commit();
	}

	@Override
	public void remove(Long id) {
		PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.getPersonaJuridicaById(id);
		boolean removed = personaJuridicaProvider.removePersonaJuridica(personaJuridicaModel);
		if (!removed) {
			throw new InternalServerErrorException("No se pudo eliminar el elemento");
		}
	}

	/**
	 * Accionistas
	 */

	@Override
	public List<AccionistaRepresentation> findAllAccionistas(Long id) {
		PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.getPersonaJuridicaById(id);
		List<AccionistaModel> list = personaJuridicaModel.getAccionistas();
		List<AccionistaRepresentation> result = new ArrayList<AccionistaRepresentation>();
		for (AccionistaModel model : list) {
			result.add(ModelToRepresentation.toRepresentation(model));
		}
		return result;
	}

	@Override
	public Response addAccionista(Long id, AccionistaRepresentation accionistaRepresentation) {
		PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.getPersonaJuridicaById(id);
		if (personaJuridicaModel == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(accionistaRepresentation.getPersonaNatural().getTipoDocumento());
		PersonaNaturalModel personaNaturalModel = personaNaturalProvider.getPersonaNaturalByTipoNumeroDoc(tipoDocumentoModel, accionistaRepresentation.getPersonaNatural().getNumeroDocumento());

		AccionistaModel accionistaModel = personaJuridicaModel.addAccionista(personaNaturalModel, accionistaRepresentation.getPorcentajeParticipacion());
		AccionistaRepresentation representation = ModelToRepresentation.toRepresentation(accionistaModel);
		return Response.created(uriInfo.getAbsolutePathBuilder().path(representation.getId().toString()).build()).header("Access-Control-Expose-Headers", "Location").build();
	}

}
