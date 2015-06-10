package org.sistcoop.persona.services.resources.admin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.sistcoop.persona.services.util.DriveSample;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.model.Permission;

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

	JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	HttpTransport httpTransport;
	FileDataStoreFactory dataStoreFactory;
	java.io.File DATA_STORE_DIR =
		      new java.io.File(System.getProperty("user.home"), ".store/drive_sample");
	
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
				try {
					httpTransport = GoogleNetHttpTransport.newTrustedTransport();	
					dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
				    Credential credential = authorize();
					Drive service = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
					          "sistcoop").build();
										
					File body = new File();
					body.setTitle("/carlos/My document");
					body.setDescription("A test document");
					body.setMimeType("image/*");
					
					//java.io.File fileContent = new java.io.File("C:\\Users\\Huertas\\document.txt");
					FileContent mediaContent = new FileContent("text/plain", UPLOAD_FILE);
					
					File file = service.files().insert(body, mediaContent).execute();
					System.out.println("File ID: " + file.getId());
					
					//update model					
				    String url = file.getAlternateLink();
				    model.setUrlFoto(url);
				    model.commit();
				    
				} catch (GeneralSecurityException e) {					
					e.printStackTrace();
				}				
			    			    
			} catch (IOException e) {
				System.out.println("IOException....................................");
				System.out.println(e.getMessage());
				System.out.println(e.getCause().toString());
				throw new InternalServerErrorException();
			}
		}		
	}

	@Override
	public void setFirma(String id, MultipartFormDataInput input) {
		
	}
	
	/**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
	private Credential authorize() {
		try {
			// load client secrets
		    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
		        new InputStreamReader(DriveSample.class.getResourceAsStream("/client_secret.json")));
		    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
		        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
		      System.out.println(
		          "Enter Client ID and Secret from https://code.google.com/apis/console/?api=drive "
		          + "into drive-cmdline-sample/src/main/resources/client_secrets.json");
		      System.exit(1);
		    }
		    // set up authorization code flow
		    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
		        httpTransport, JSON_FACTORY, clientSecrets,
		        Collections.singleton(DriveScopes.DRIVE_FILE)).setDataStoreFactory(dataStoreFactory)
		        .build();
		    // authorize
		    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		  
		} catch (Exception e) {
			System.out.println("errorrrrrrrrrrrrrrr");
			System.out.println(e.getMessage());
		}
		return null;
	}   

}
