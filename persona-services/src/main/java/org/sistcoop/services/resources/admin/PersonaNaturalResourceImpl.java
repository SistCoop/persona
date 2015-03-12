package org.sistcoop.services.resources.admin;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sistcoop.admin.client.resource.PersonaNaturalResource;
import org.sistcoop.models.PersonaNaturalModel;
import org.sistcoop.models.PersonaNaturalProvider;
import org.sistcoop.models.TipoDocumentoModel;
import org.sistcoop.models.TipoDocumentoProvider;
import org.sistcoop.models.enums.EstadoCivil;
import org.sistcoop.models.enums.Sexo;
import org.sistcoop.models.utils.ModelToRepresentation;
import org.sistcoop.models.utils.RepresentationToModel;
import org.sistcoop.representations.idm.PersonaNaturalRepresentation;

@Stateless
public class PersonaNaturalResourceImpl implements PersonaNaturalResource {

	@Inject
	protected PersonaNaturalProvider personaNaturalProvider;

	@Inject
	protected TipoDocumentoProvider tipoDocumentoProvider;

	@Inject
	protected RepresentationToModel representationToModel;

	@Context
	protected UriInfo uriInfo;

	@Override
	public PersonaNaturalRepresentation findById(Long id) {
		PersonaNaturalModel personaNaturalModel = personaNaturalProvider.getPersonaNaturalById(id);
		PersonaNaturalRepresentation rep = ModelToRepresentation.toRepresentation(personaNaturalModel);
		return rep;
	}

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

	@Override
	public Response create(PersonaNaturalRepresentation personaNaturalRepresentation) {
		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(personaNaturalRepresentation.getTipoDocumento());
		PersonaNaturalModel personaNaturalModel = representationToModel.createPersonaNatural(personaNaturalRepresentation, tipoDocumentoModel, personaNaturalProvider);
		return Response.created(uriInfo.getAbsolutePathBuilder().path(personaNaturalModel.getId().toString()).build()).header("Access-Control-Expose-Headers", "Location").entity(personaNaturalModel.getId()).build();
	}

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
		model.setTelefono(personaNaturalRepresentation.getTelefono());
		model.setCelular(personaNaturalRepresentation.getCelular());
		model.setEmail(personaNaturalRepresentation.getEmail());

		model.commit();
	}

	@Override
	public void remove(Long id) {
		PersonaNaturalModel personaNaturalModel = personaNaturalProvider.getPersonaNaturalById(id);
		personaNaturalProvider.removePersonaNatural(personaNaturalModel);
	}

}
