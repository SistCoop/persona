package org.sistcoop.persona.services.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

@Stateful
public class GoogleDriveManager {

	private final String APPLICATION_NAME = "sistcoop";
	
	private final String CLIENT_SECRET = "/client_secret.json";
	
	private JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	
	private HttpTransport httpTransport;
	
	private FileDataStoreFactory dataStoreFactory;
	
	private java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".store/drive_sistcoop");

	private Drive service;

	@PostConstruct
	private void init() throws GeneralSecurityException, IOException {
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
		Credential credential = authorize();
		service = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}
	
	private Credential authorize() throws IOException {
		// load client secrets
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(DriveSample.class.getResourceAsStream(CLIENT_SECRET)));
		
		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, Collections.singleton(DriveScopes.DRIVE_FILE)).setDataStoreFactory(dataStoreFactory).build();
		// authorize
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}
	
	public String upload(java.io.File UPLOAD_FILE, String parent, String title, String mimeType, String description) throws GeneralSecurityException, IOException{			    				
		File body = new File();
		body.setTitle(title);
		body.setDescription(description);
		body.setMimeType(mimeType);
							
		FileContent mediaContent = new FileContent(mimeType, UPLOAD_FILE);		
		File file = service.files().insert(body, mediaContent).execute();
		
		return file.getDownloadUrl();
	}
	
}
