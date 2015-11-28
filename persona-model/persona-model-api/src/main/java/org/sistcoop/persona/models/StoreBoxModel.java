package org.sistcoop.persona.models;

import java.io.File;
import java.io.IOException;

public interface StoreBoxModel extends Model {

    /**
     * 
     * @param titles
     *            list of folders titles i.e. if your path like this
     *            folder1/folder2/folder3 then pass them in this order
     *            createFoldersPath(service, folder1, folder2, folder3)
     * @return folderId reference of the last added folder in case you want to
     *         use it to create a file inside this folder.
     * @throws IOException
     */
    String createFolder(String... titles) throws IOException;

    String upload(File file, String title, String mimeType, String... parents) throws IOException;

}
