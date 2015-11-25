package org.sistcoop.persona.services.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;

@Startup
@Singleton
public class GoogleDriveStarter {

	/** Clien secret json file name. */
	private final String CLIENT_SECRET = "/client_secret.json";

	/** Directory to store user credentials for this application. */
	private java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".store/drive_persona");

	/** Client user id. */
	private String USER_ID = "user";

	private JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private HttpTransport httpTransport;
	private FileDataStoreFactory dataStoreFactory;

	private final String APPLICATION_NAME = "persona";
	private Credential credential;

	@PostConstruct
	private void init() throws GeneralSecurityException, IOException {
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
		loadCredential();
	}

	public String getApplicationName() {
		return APPLICATION_NAME;
	}

	public Credential getCredential() {
		return credential;
	}

	private GoogleClientSecrets getGoogleClientSecrets() throws IOException {
		InputStream inputStream = GoogleDriveStarter.class.getResourceAsStream(CLIENT_SECRET);
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

		// load client secrets
		return GoogleClientSecrets.load(JSON_FACTORY, inputStreamReader);
	}

	private GoogleAuthorizationCodeFlow getGoogleAuthorizationCodeFlow() throws IOException {
		// Load google client secret
		GoogleClientSecrets clientSecrets = getGoogleClientSecrets();

		// set up authorization code flow
		return new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
				Collections.singleton(DriveScopes.DRIVE_FILE)).setDataStoreFactory(dataStoreFactory).build();
	}

	private void loadCredential() throws IOException {
		// Load flow
		GoogleAuthorizationCodeFlow flow = getGoogleAuthorizationCodeFlow();
		credential = flow.loadCredential(USER_ID);
	}

	/*
	 * private String geturl() throws IOException { // Load flow
	 * GoogleAuthorizationCodeFlow flow = getGoogleAuthorizationCodeFlow();
	 * GoogleAuthorizationCodeRequestUrl requestUrl =
	 * flow.newAuthorizationUrl(); return requestUrl.build(); }
	 * 
	 * private String toekn() throws IOException { // Load flow
	 * GoogleAuthorizationCodeFlow flow = getGoogleAuthorizationCodeFlow();
	 * GoogleAuthorizationCodeTokenRequest tokenRequest =
	 * flow.newTokenRequest(null); return null; }
	 * 
	 * private void sto() throws IOException { // Load flow
	 * GoogleAuthorizationCodeFlow flow = getGoogleAuthorizationCodeFlow();
	 * credential = flow.createAndStoreCredential(null, USER_ID); }
	 */

	/**
	 * Authorizes the installed application to access user's protected data.
	 *
	 * @param userId
	 *            user ID or {@code null} if not using a persisted credential
	 *            store
	 * @return credential
	 */
	/*
	 * public void authorize() throws IOException { GoogleAuthorizationCodeFlow
	 * flow = getGoogleAuthorizationCodeFlow(); try { Credential credential =
	 * flow.loadCredential(USER_ID); if (credential != null &&
	 * (credential.getRefreshToken() != null || credential.getExpiresInSeconds()
	 * > 60)) { this.credential = credential; } else { // open in browser String
	 * redirectUri = receiver.getRedirectUri(); AuthorizationCodeRequestUrl
	 * authorizationUrl =
	 * flow.newAuthorizationUrl().setRedirectUri(redirectUri);
	 * onAuthorization(authorizationUrl); // receive authorization code and
	 * exchange it for an access // token String code = receiver.waitForCode();
	 * TokenResponse response =
	 * flow.newTokenRequest(code).setRedirectUri(redirectUri).execute(); //
	 * store credential and return it return
	 * flow.createAndStoreCredential(response, userId); }
	 * 
	 * } finally { receiver.stop(); } }
	 */

	/*
	 * private Credential authorize(GoogleClientSecrets clientSecrets,
	 * HttpTransport httpTransport, JsonFactory jsonFactory) throws IOException
	 * { // Load client secrets. InputStream in =
	 * GoogleDriveStarter.class.getResourceAsStream("/client_secret.json");
	 * GoogleClientSecrets clientSecrets1 =
	 * GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
	 * 
	 * // Build flow and trigger user authorization request.
	 * GoogleAuthorizationCodeFlow flow = new
	 * GoogleAuthorizationCodeFlow.Builder(null, JSON_FACTORY, clientSecrets1,
	 * null).setDataStoreFactory(null).setAccessType("offline").build();
	 * Credential credential = new AuthorizationCodeInstalledApp(flow, new
	 * LocalServerReceiver()).authorize("user"); System.out.println(
	 * "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath()); return
	 * credential; }
	 */

}
