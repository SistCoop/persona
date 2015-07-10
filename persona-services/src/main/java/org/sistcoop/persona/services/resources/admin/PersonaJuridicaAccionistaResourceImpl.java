package org.sistcoop.persona.services.resources.admin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.sistcoop.persona.Jsend;
import org.sistcoop.persona.admin.client.Roles;
import org.sistcoop.persona.admin.client.resource.AccionistasResource;
import org.sistcoop.persona.models.AccionistaModel;
import org.sistcoop.persona.models.AccionistaProvider;
import org.sistcoop.persona.models.PersonaJuridicaModel;
import org.sistcoop.persona.models.PersonaJuridicaProvider;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.PersonaNaturalProvider;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.models.utils.RepresentationToModel;
import org.sistcoop.persona.representations.idm.AccionistaRepresentation;

@Stateless
@SecurityDomain("keycloak")
public class PersonaJuridicaAccionistaResourceImpl implements AccionistasResource {

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

	@RolesAllowed(Roles.ver_personas)
	@Override
	public List<AccionistaRepresentation> findAllAccionistas(String idPersonaJuridica) {
		PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.getPersonaJuridicaById(idPersonaJuridica);
		List<AccionistaModel> list = personaJuridicaModel.getAccionistas();
		List<AccionistaRepresentation> result = new ArrayList<AccionistaRepresentation>();
		for (AccionistaModel model : list) {
			result.add(ModelToRepresentation.toRepresentation(model));
		}
		return result;
	}

	@RolesAllowed(Roles.administrar_personas)
	@Override
	public Response create(String idPersonaJuridica, AccionistaRepresentation accionistaRepresentation) {
		PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.getPersonaJuridicaById(idPersonaJuridica);
		if (personaJuridicaModel == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.findByAbreviatura(accionistaRepresentation.getPersonaNatural().getTipoDocumento());
		PersonaNaturalModel personaNaturalModel = personaNaturalProvider.findByTipoNumeroDocumento(tipoDocumentoModel, accionistaRepresentation.getPersonaNatural().getNumeroDocumento());

		AccionistaModel accionistaModel = accionistaProvider.addAccionista(personaJuridicaModel, personaNaturalModel, accionistaRepresentation.getPorcentajeParticipacion());		
		AccionistaRepresentation representation = ModelToRepresentation.toRepresentation(accionistaModel);
		return Response.created(uriInfo.getAbsolutePathBuilder().path(representation.getId()).build()).header("Access-Control-Expose-Headers", "Location").entity(Jsend.getSuccessJSend(representation.getId())).build();
	}

	@RolesAllowed(Roles.administrar_personas)
	@Override
	public void update(String idPersonaJuridica, String idAccionista, AccionistaRepresentation accionistaRepresentation) {
		PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.getPersonaJuridicaById(idPersonaJuridica);
		AccionistaModel accionistaModel = accionistaProvider.getAccionistaById(idAccionista);
		if(!personaJuridicaModel.equals(accionistaModel.getPersonaJuridica())) {
			throw new BadRequestException("Accionista y persona juridica no coindicentes");
		}
		
		accionistaModel.setPorcentajeParticipacion(accionistaRepresentation.getPorcentajeParticipacion());
		accionistaModel.commit();
	}

	@RolesAllowed(Roles.administrar_personas)
	@Override
	public void delete(String idPersonaJuridica, String idAccionista) {
		PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.getPersonaJuridicaById(idPersonaJuridica);
		AccionistaModel accionistaModel = accionistaProvider.getAccionistaById(idAccionista);
		if(!personaJuridicaModel.equals(accionistaModel.getPersonaJuridica())) {
			throw new BadRequestException("Accionista y persona juridica no coindicentes");
		}
		accionistaProvider.removeAccionista(accionistaModel);
	}

}
