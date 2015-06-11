package org.sistcoop.persona.services.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

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
	
	public String upload(java.io.File UPLOAD_FILE, String title, String mimeType, String description, String... parents) throws GeneralSecurityException, IOException{			    						
		//Crear folders padres
		List<ParentReference> parentReferences = createFoldersPath(service, parents);
		
		//Folder to upload
		File body = new File();
		body.setTitle(title);
		body.setDescription(description);
		body.setMimeType(mimeType);
		body.setParents(parentReferences);	
		
		//upload file
		FileContent mediaContent = new FileContent(mimeType, UPLOAD_FILE);		
		File file = service.files().insert(body, mediaContent).execute();		
		return file.getEmbedLink();
	}
	
	private Credential authorize() throws IOException {
		// load client secrets
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(DriveSample.class.getResourceAsStream(CLIENT_SECRET)));
		
		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, Collections.singleton(DriveScopes.DRIVE_FILE)).setDataStoreFactory(dataStoreFactory).build();
		// authorize
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}	
	
	/**
     * 
     * @param service google drive instance
     * @param title the title (name) of the folder (the one you search for)
     * @param parentId the parent Id of this folder (use root) if the folder is in the main directory of google drive
     * @return google drive file object 
     * @throws IOException
     */
    private File getExistsFolder(Drive service, String title, String parentId) throws IOException 
    {
        Drive.Files.List request;
        request = service.files().list();
        String query = "mimeType='application/vnd.google-apps.folder' AND trashed=false AND title='" + title + "' AND '" + parentId + "' in parents";        
        request = request.setQ(query);
        FileList files = request.execute();        
        if (files.getItems().size() == 0) //if the size is zero, then the folder doesn't exist
            return null;
        else
            //since google drive allows to have multiple folders with the same title (name)
            //we select the first file in the list to return
            return files.getItems().get(0);
    }
    
    /**
     * 
     * @param service google drive instance
     * @param title the folder's title
     * @param listParentReference the list of parents references where you want the folder to be created, 
     * if you have more than one parent references, then a folder will be created in each one of them  
     * @return google drive file object   
     * @throws IOException
     */
    private File createFolder(Drive service, String title,List<ParentReference> listParentReference) throws IOException
    {
        File body = new File();
        body.setTitle(title);
        body.setParents(listParentReference);
        body.setMimeType("application/vnd.google-apps.folder");
        File file = service.files().insert(body).execute(); 
        return file;

    }
    
    /**
     * 
     * @param service google drive instance
     * @param titles list of folders titles 
     * i.e. if your path like this folder1/folder2/folder3 then pass them in this order createFoldersPath(service, folder1, folder2, folder3)
     * @return parent reference of the last added folder in case you want to use it to create a file inside this folder.
     * @throws IOException
     */
    private List<ParentReference> createFoldersPath(Drive service, String... titles) throws IOException
    {
        List<ParentReference> listParentReference = new ArrayList<ParentReference>();
        File file = null;
        for(int i=0;i<titles.length;i++)
        {
            file = getExistsFolder(service, titles[i], (file==null)?"root":file.getId());
            if (file == null)
            {
                file = createFolder(service, titles[i], listParentReference);
            }
            listParentReference.clear();
            listParentReference.add(new ParentReference().setId(file.getId()));
        }
        return listParentReference;
    }
	
}
