package org.sistcoop.persona.services.resources.admin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.sistcoop.persona.Jsend;
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
import org.sistcoop.persona.services.util.GoogleDriveManager;

@Stateless
@SecurityDomain("keycloak")
public class PersonaNaturalResourceImpl implements PersonaNaturalResource {

	@Inject
	private GoogleDriveManager googleDriveManager;
	
	@Inject
	private PersonaNaturalProvider personaNaturalProvider;

	@Inject
	private TipoDocumentoProvider tipoDocumentoProvider;

	@Inject
	private RepresentationToModel representationToModel;

	@Context
	private UriInfo uriInfo;
    
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
	public PersonaNaturalRepresentation findById(String id) {
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
		return Response.created(uriInfo.getAbsolutePathBuilder().path(personaNaturalModel.getId().toString()).build()).header("Access-Control-Expose-Headers", "Location").entity(Jsend.getSuccessJSend(personaNaturalModel.getId())).build();
	}

	@RolesAllowed(Roles.administrar_personas)
	@Override
	public void update(String id, PersonaNaturalRepresentation personaNaturalRepresentation) {
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
	public void remove(String id) {
		PersonaNaturalModel personaNaturalModel = personaNaturalProvider.getPersonaNaturalById(id);
		personaNaturalProvider.removePersonaNatural(personaNaturalModel);
	}	
	
	@RolesAllowed(Roles.administrar_personas)
	@Override
	public void setFoto(String id, MultipartFormDataInput input) {	
		PersonaNaturalModel model = personaNaturalProvider.getPersonaNaturalById(id);				
		
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");		
		for (InputPart inputPart : inputParts) {
			try {
				// convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class, null);
				byte[] bytes = IOUtils.toByteArray(inputStream);
				
				//writeFile(bytes, fileName);
				java.io.File UPLOAD_FILE = java.io.File.createTempFile("tpm-sg", ".tmp", null);
				FileOutputStream fos = new FileOutputStream(UPLOAD_FILE);
				fos.write(bytes);
				fos.close();
				
				//google drive											
				String url = googleDriveManager.upload(UPLOAD_FILE, UUID.randomUUID().toString(), "image/*", "Photo siscoop", "sistcoop_app", "ventura", "foto");														  
			    model.setUrlFoto(url);
			    model.commit();				
			    			    
			} catch (IOException e) {				
				throw new InternalServerErrorException();
			} catch (GeneralSecurityException e) {					
				throw new InternalServerErrorException();
			}
		}		
	}

	@Override
	public void setFirma(String id, MultipartFormDataInput input) {
		
	}

}
