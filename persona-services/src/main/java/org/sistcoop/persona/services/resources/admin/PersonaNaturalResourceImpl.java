package org.sistcoop.persona.services.resources.admin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
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
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

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
    
    /**
     * Be sure to specify the name of your application. If the application name is {@code null} or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "sistcoop";
    
    /** Directory to store user credentials. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".store/drive_sample");

    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
     * globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global Drive API client. */
    private static Drive drive;
    
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
		
		Credential credential = null;
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			JSON_FACTORY = JacksonFactory.getDefaultInstance();
			credential = authorize();
		} catch (GeneralSecurityException e1) {
			throw new InternalServerErrorException();
		} catch (IOException e1) {
			throw new InternalServerErrorException();
		} catch (Exception e1) {
			throw new InternalServerErrorException();
		}				
		drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
		
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
				File fileMetadata = new File();
			    fileMetadata.setTitle(UUID.randomUUID().toString());

			    FileContent mediaContent = new FileContent("image/jpeg", UPLOAD_FILE);

			    Drive.Files.Insert insert = drive.files().insert(fileMetadata, mediaContent);
			    MediaHttpUploader uploader = insert.getMediaHttpUploader();
			    uploader.setDirectUploadEnabled(true);
			    uploader.setProgressListener(null);			    
			    File fileGoogleDrive = insert.execute();
			    
			    //update model
			    String url = fileGoogleDrive.getWebViewLink();
			    model.setUrlFoto(url);
			    model.commit();
			} catch (IOException e) {
				throw new InternalServerErrorException();
			}
		}		
	}

	@Override
	public void setFirma(String id, MultipartFormDataInput input) {
		
	}
	
	/** Authorizes the installed application to access user's protected data. */
	private static Credential authorize() throws Exception {

		// load client secrets
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(PersonaNaturalResourceImpl.class.getResourceAsStream("/client_secrets.json")));

		if (clientSecrets.getDetails().getClientId().startsWith("Enter") || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
			System.out.println("Enter Client ID and Secret from https://code.google.com/apis/console/?api=drive " 
					+ "into drive-cmdline-sample/src/main/resources/client_secrets.json");
			System.exit(1);
		}
		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, JSON_FACTORY, clientSecrets,
				Collections.singleton(DriveScopes.DRIVE_FILE))
				.setDataStoreFactory(dataStoreFactory).build();
		// authorize
		return new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver()).authorize("user");		
	}

}
