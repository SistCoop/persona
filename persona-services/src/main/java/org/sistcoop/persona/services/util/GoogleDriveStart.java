package org.sistcoop.persona.services.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

public class GoogleDriveStart {

    private final String CLIENT_SECRET = "/client_secret.json";

    private JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private HttpTransport httpTransport;

    private FileDataStoreFactory dataStoreFactory;

    /** Directory to store user credentials for this application. */
    private java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
            ".store/drive_sistcoop");

    @PostConstruct
    private void init() throws GeneralSecurityException, IOException {
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
        Credential credential = authorize();
    }

    private Credential authorize() throws IOException {
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(DriveSample.class.getResourceAsStream(CLIENT_SECRET)));
        
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
                JSON_FACTORY, clientSecrets, Collections.singleton(DriveScopes.DRIVE_FILE))
                        .setDataStoreFactory(dataStoreFactory).build();
        
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    private boolean existStoreCredential(JsonFactory jsonFactory, GoogleClientSecrets clientSecrets) {
        new AuthorizationCodeInstalledApp(null, new LocalServerReceiver());
        return false;
    }

}
