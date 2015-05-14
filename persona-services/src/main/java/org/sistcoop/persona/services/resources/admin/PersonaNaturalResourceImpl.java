package org.sistcoop.persona.services.resources.admin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.sistcoop.persona.admin.client.Roles;
import org.sistcoop.persona.admin.client.resource.PersonaNaturalResource;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.PersonaNaturalProvider;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.enums.EstadoCivil;
import org.sistcoop.persona.models.enums.Sexo;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.models.utils.RepresentationToModel;
import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;

@Stateless
@SecurityDomain("keycloak")
public class PersonaNaturalResourceImpl implements PersonaNaturalResource {

	@Inject
	protected PersonaNaturalProvider personaNaturalProvider;

	@Inject
	protected TipoDocumentoProvider tipoDocumentoProvider;

	@Inject
	protected RepresentationToModel representationToModel;

	@Context
	protected UriInfo uriInfo;

	@RolesAllowed(Roles.ver_personas)
	@Override
	public List<PersonaNaturalRepresentation> listAll(String filterText, Integer firstResult, Integer maxResults) {
		List<PersonaNaturalRepresentation> results = new ArrayList<PersonaNaturalRepresentation>();
		List<PersonaNaturalModel> userModels;
		if (filterText == null) {
			if (firstResult == null || maxResults == null) {
				userModels = personaNaturalProvider.getPersonasNaturales();
			} else {
				userModels = personaNaturalProvider.getPersonasNaturales(firstResult, maxResults);
			}
		} else {
			if (firstResult == null || maxResults == null) {
				userModels = personaNaturalProvider.searchForFilterText(filterText);
			} else {
				userModels = personaNaturalProvider.searchForFilterText(filterText, firstResult, maxResults);
			}
		}
		for (PersonaNaturalModel personaNaturalModel : userModels) {
			results.add(ModelToRepresentation.toRepresentation(personaNaturalModel));
		}
		return results;
	}

	@RolesAllowed(Roles.ver_personas)
	@Override
	public long countAll() {
		return personaNaturalProvider.getPersonasNaturalesCount();
	}
	
	@RolesAllowed(Roles.ver_personas)
	@Override
	public PersonaNaturalRepresentation findById(Long id) {
		PersonaNaturalModel personaNaturalModel = personaNaturalProvider.getPersonaNaturalById(id);
		PersonaNaturalRepresentation rep = ModelToRepresentation.toRepresentation(personaNaturalModel);
		return rep;
	}

	@RolesAllowed(Roles.ver_personas)
	@Override
	public PersonaNaturalRepresentation findByTipoNumeroDocumento(String tipoDocumento, String numeroDocumento) {
		if (tipoDocumento == null)
			return null;
		if (numeroDocumento == null)
			return null;

		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(tipoDocumento);
		PersonaNaturalModel personaNaturalModel = personaNaturalProvider.getPersonaNaturalByTipoNumeroDoc(tipoDocumentoModel, numeroDocumento);
		PersonaNaturalRepresentation rep = ModelToRepresentation.toRepresentation(personaNaturalModel);
		return rep;
	}

	@RolesAllowed(Roles.administrar_personas)
	@Override
	public Response create(PersonaNaturalRepresentation personaNaturalRepresentation) {
		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(personaNaturalRepresentation.getTipoDocumento());
		PersonaNaturalModel personaNaturalModel = representationToModel.createPersonaNatural(personaNaturalRepresentation, tipoDocumentoModel, personaNaturalProvider);
		return Response.created(uriInfo.getAbsolutePathBuilder().path(personaNaturalModel.getId().toString()).build()).header("Access-Control-Expose-Headers", "Location").entity(personaNaturalModel.getId()).build();
	}

	@RolesAllowed(Roles.administrar_personas)
	@Override
	public void update(Long id, PersonaNaturalRepresentation personaNaturalRepresentation) {
		PersonaNaturalModel model = personaNaturalProvider.getPersonaNaturalById(id);
		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(personaNaturalRepresentation.getTipoDocumento());

		model.setCodigoPais(personaNaturalRepresentation.getCodigoPais());
		model.setTipoDocumento(tipoDocumentoModel);
		model.setNumeroDocumento(personaNaturalRepresentation.getNumeroDocumento());
		model.setApellidoPaterno(personaNaturalRepresentation.getApellidoPaterno());
		model.setApellidoMaterno(personaNaturalRepresentation.getApellidoMaterno());
		model.setNombres(personaNaturalRepresentation.getNombres());
		model.setFechaNacimiento(personaNaturalRepresentation.getFechaNacimiento());
		model.setSexo(Sexo.valueOf(personaNaturalRepresentation.getSexo().toUpperCase()));
		model.setEstadoCivil(personaNaturalRepresentation.getEstadoCivil() != null ? EstadoCivil.valueOf(personaNaturalRepresentation.getEstadoCivil().toUpperCase()) : null);

		model.setUbigeo(personaNaturalRepresentation.getUbigeo());
		model.setDireccion(personaNaturalRepresentation.getDireccion());
		model.setReferencia(personaNaturalRepresentation.getReferencia());
		model.setOcupacion(personaNaturalRepresentation.getOcupacion());
		model.setTelefono(personaNaturalRepresentation.getTelefono());
		model.setCelular(personaNaturalRepresentation.getCelular());
		model.setEmail(personaNaturalRepresentation.getEmail());

		model.commit();
	}

	@RolesAllowed(Roles.eliminar_personas)
	@Override
	public void remove(Long id) {
		PersonaNaturalModel personaNaturalModel = personaNaturalProvider.getPersonaNaturalById(id);
		personaNaturalProvider.removePersonaNatural(personaNaturalModel);
	}

}
