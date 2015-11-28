package org.sistcoop.persona.models.jpa;

import java.io.File;
import java.io.IOException;

import org.sistcoop.persona.models.StoreBoxModel;

/*import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;*/

/**
 * @author <a href="mailto:carlosthe19916@sistcoop.com">Carlos Feria</a>
 */

public class GoogleDriveStoreBoxAdapter implements StoreBoxModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*private Drive service;

    public GoogleDriveStoreBoxAdapter(Drive service) {
        this.service = service;
    }

    public Drive getDrive() {
        return service;
    }*/

    @Override
    public void commit() {
        // TODO Auto-generated method stub
    }

    @Override
    public String createFolder(String... titles) throws IOException {
        /*List<ParentReference> listParentReference = new ArrayList<>();
        com.google.api.services.drive.model.File file = null;
        for (int i = 0; i < titles.length; i++) {
            file = getExistsFolder(titles[i], (file == null) ? "root" : file.getId());
            if (file == null) {
                file = createFolder(titles[i], listParentReference);
            }
            listParentReference.clear();
            listParentReference.add(new ParentReference().setId(file.getId()));
        }
        return listParentReference.get(0).getId();*/
        return null;
    }

    @Override
    public String upload(File uploadFile, String title, String mimeType, String... parents)
            throws IOException {
        // Create parent folders
        /*String parentReferenceId = createFolder(parents);
        ParentReference parentReference = new ParentReference().setId(parentReferenceId);
        List<ParentReference> parentReferences = new ArrayList<>();
        parentReferences.add(parentReference);

        // Folder to upload
        com.google.api.services.drive.model.File body = new com.google.api.services.drive.model.File();
        body.setTitle(title);
        body.setMimeType(mimeType);
        body.setParents(parentReferences);

        // upload file
        FileContent mediaContent = new FileContent(mimeType, uploadFile);
        com.google.api.services.drive.model.File file = service.files().insert(body, mediaContent).execute();
        return file.getWebContentLink();*/
        return null;
    }

    /*private com.google.api.services.drive.model.File createFolder(String title,
            List<ParentReference> listParentReference) throws IOException {
        com.google.api.services.drive.model.File body = new com.google.api.services.drive.model.File();
        body.setTitle(title);
        body.setParents(listParentReference);
        body.setMimeType("application/vnd.google-apps.folder");
        com.google.api.services.drive.model.File file = service.files().insert(body).execute();
        return file;
    }

    private com.google.api.services.drive.model.File getExistsFolder(String title, String parentId)
            throws IOException {
        Drive.Files.List request;
        request = service.files().list();
        String query = "mimeType='application/vnd.google-apps.folder' AND trashed=false AND title='" + title
                + "' AND '" + parentId + "' in parents";
        request = request.setQ(query);
        FileList files = request.execute();
        if (files.getItems().size() == 0) // if the size is zero, then the
                                          // folder doesn't exist
            return null;
        else
            // since google drive allows to have multiple folders with the same
            // title (name)
            // we select the first file in the list to return
            return files.getItems().get(0);
    }*/

}
