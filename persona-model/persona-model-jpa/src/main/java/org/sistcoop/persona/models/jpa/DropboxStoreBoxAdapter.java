package org.sistcoop.persona.models.jpa;

import java.io.File;
import java.io.IOException;

import org.sistcoop.persona.models.StoreBoxModel;

/**
 * @author <a href="mailto:carlosthe19916@sistcoop.com">Carlos Feria</a>
 */

public class DropboxStoreBoxAdapter implements StoreBoxModel {

    private static final long serialVersionUID = 1L;

    //private DbxClientV2 dbxClient;

    /*public DropboxStoreBoxAdapter(DbxClientV2 dbxClient) {
        this.dbxClient = dbxClient;
    }

    public DbxClientV2 getDbxClient() {
        return dbxClient;
    }*/

    @Override
    public void commit() {
        // TODO Auto-generated method stub
    }

    @Override
    public String createFolder(String... titles) throws IOException {
        /*String folderName = new String();
        for (int i = 0; i < titles.length; i++) {
            folderName = folderName + "/" + titles[i];
        }

        FolderMetadata folder;
        try {
            folder = dbxClient.files.createFolder(folderName);
            return folder.id;
        } catch (CreateFolderException e) {
            throw new IOException(e);
        } catch (DbxException e) {
            throw new IOException(e);
        }*/
        return null;
    }

    @Override
    public String upload(File file, String title, String mimeType, String... parents) throws IOException {
        // Upload "test.txt" to Dropbox
        /*InputStream in = new FileInputStream(file);
        try {
            FileMetadata metadata = dbxClient.files.uploadBuilder(title).run(in);
            return metadata.id;
        } catch (UploadException e) {
            throw new IOException(e);
        } catch (DbxException e) {
            throw new IOException(e);
        }*/
        return null;
    }

}
