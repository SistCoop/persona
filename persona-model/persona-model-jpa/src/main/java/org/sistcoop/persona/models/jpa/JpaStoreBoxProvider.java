package org.sistcoop.persona.models.jpa;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.sistcoop.persona.models.ModelReadOnlyException;
import org.sistcoop.persona.models.StoreBoxModel;
import org.sistcoop.persona.models.StoreBoxProvider;
import org.sistcoop.persona.models.StoreConfigurationModel;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

/**
 * @author <a href="mailto:carlosthe19916@sistcoop.com">Carlos Feria</a>
 */

@Named
@Stateless
@Local(StoreBoxProvider.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JpaStoreBoxProvider implements StoreBoxProvider {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void close() {
        // TODO Auto-generated method stub
    }

    @Override
    public StoreBoxModel getByStoreConfiguration(StoreConfigurationModel storeConfiguration)
            throws IOException {
        /*if (storeConfiguration.getDenominacion().equalsIgnoreCase("GoogleDrive")) {
            try {
                return getGoogleDriveStoreBoxModel(storeConfiguration);
            } catch (GeneralSecurityException e) {
                throw new IOException(e);
            } catch (IOException e) {
                throw new IOException(e);
            }
        } else if (storeConfiguration.getDenominacion().equalsIgnoreCase("Dropbox")) {
            try {
                return getDropboxStoreBoxModel(storeConfiguration);
            } catch (DbxException e) {
                throw new IOException(e);
            }
        } else {
            throw new ModelReadOnlyException("Box configuration is not supported now");
        }*/
        return null;
    }

    private StoreBoxModel getGoogleDriveStoreBoxModel(StoreConfigurationModel storeConfiguration)
            throws GeneralSecurityException, IOException {
        /*HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        // Load Client Secret
        GoogleClientSecrets clientSecrets = jsonFactory.fromString(storeConfiguration.getAppSecret(),
                GoogleClientSecrets.class);

        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory,
                clientSecrets, Collections.singleton(DriveScopes.DRIVE_FILE)).build();

        // Load credential
        Credential credential = flow.loadCredential(storeConfiguration.getAppKey());

        // load Google Servive
        Drive service = new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(storeConfiguration.getAppKey()).build();*/

        //return new GoogleDriveStoreBoxAdapter(service);
        return null;
    }

    /*private StoreBoxModel getDropboxStoreBoxModel(StoreConfigurationModel storeConfiguration)
            throws DbxException {
        DbxRequestConfig config = new DbxRequestConfig(storeConfiguration.getAppKey(), "en_US");
        DbxClientV2 dbxClient = new DbxClientV2(config, storeConfiguration.getToken());
        return new DropboxStoreBoxAdapter(dbxClient);
        return null;
    }*/

}
