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
import org.sistcoop.persona.Jsend;
import org.sistcoop.persona.admin.client.Roles;
import org.sistcoop.persona.admin.client.resource.PersonaJuridicaResource;
import org.sistcoop.persona.models.AccionistaProvider;
import org.sistcoop.persona.models.PersonaJuridicaModel;
import org.sistcoop.persona.models.PersonaJuridicaProvider;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.PersonaNaturalProvider;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.enums.TipoEmpresa;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.models.utils.RepresentationToModel;
import org.sistcoop.persona.representations.idm.PersonaJuridicaRepresentation;

@Stateless
@SecurityDomain("keycloak")
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

	@RolesAllowed(Roles.ver_personas)
	@Override
	public List<PersonaJuridicaRepresentation> findAll(String filterText, Integer firstResult, Integer maxResults) {
		filterText = (filterText == null ? "" : filterText);
		firstResult = (firstResult == null ? -1 : firstResult);
		maxResults = (maxResults == null ? -1 : maxResults);

		List<PersonaJuridicaModel> list = personaJuridicaProvider.searchForFilterText(filterText, firstResult, maxResults);
		List<PersonaJuridicaRepresentation> result = new ArrayList<PersonaJuridicaRepresentation>();
		for (PersonaJuridicaModel model : list) {
			result.add(ModelToRepresentation.toRepresentation(model));
		}
		return result;
	}

	@RolesAllowed(Roles.ver_personas)
	@Override
	public long countAll() {
		Long count = personaJuridicaProvider.getPersonasJuridicasCount();
		return count;
	}
	
	@RolesAllowed(Roles.ver_personas)
	@Override
	public PersonaJuridicaRepresentation findById(String id) {
		PersonaJuridicaModel model = personaJuridicaProvider.getPersonaJuridicaById(id);
		PersonaJuridicaRepresentation rep = ModelToRepresentation.toRepresentation(model);
		return rep;
	}

	@RolesAllowed(Roles.ver_personas)
	@Override
	public PersonaJuridicaRepresentation findByTipoNumeroDocumento(String tipoDocumento, String numeroDocumento) {
		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(tipoDocumento);
		PersonaJuridicaModel model = personaJuridicaProvider.getPersonaJuridicaByTipoNumeroDoc(tipoDocumentoModel, numeroDocumento);
		PersonaJuridicaRepresentation rep = ModelToRepresentation.toRepresentation(model);
		return rep;
	}

	@RolesAllowed(Roles.administrar_personas)
	@Override
	public Response create(PersonaJuridicaRepresentation personaJuridicaRepresentation) {
		TipoDocumentoModel representanteTipoDocumentoModel = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(personaJuridicaRepresentation.getRepresentanteLegal().getTipoDocumento());
		PersonaNaturalModel representanteModel = personaNaturalProvider.getPersonaNaturalByTipoNumeroDoc(representanteTipoDocumentoModel, personaJuridicaRepresentation.getRepresentanteLegal().getNumeroDocumento());

		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(personaJuridicaRepresentation.getTipoDocumento());
		PersonaJuridicaModel personaJuridicaModel = representationToModel.createPersonaJuridica(personaJuridicaRepresentation, tipoDocumentoModel, representanteModel, personaJuridicaProvider);
		PersonaJuridicaRepresentation result = ModelToRepresentation.toRepresentation(personaJuridicaModel);
		return Response.created(uriInfo.getAbsolutePathBuilder().path(result.getId()).build()).header("Access-Control-Expose-Headers", "Location").entity(Jsend.getSuccessJSend(result.getId())).build();
	}

	@RolesAllowed(Roles.administrar_personas)
	@Override
	public void update(String id, PersonaJuridicaRepresentation rep) {
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

	@RolesAllowed(Roles.eliminar_personas)
	@Override
	public void remove(String id) {
		PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.getPersonaJuridicaById(id);
		boolean removed = personaJuridicaProvider.removePersonaJuridica(personaJuridicaModel);
		if (!removed) {
			throw new InternalServerErrorException("No se pudo eliminar el elemento");
		}
	}

}
