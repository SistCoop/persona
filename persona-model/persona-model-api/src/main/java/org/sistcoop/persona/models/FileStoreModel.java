package org.sistcoop.persona.models;

import org.sistcoop.persona.models.enums.FileStoreProviderName;

/**
 * @author <a href="mailto:carlosthe19916@sistcoop.com">Carlos Feria</a>
 */

public interface FileStoreModel extends Model {

	String getId();

	String getFileId();

	void setFileId(String fileId);

	FileStoreProviderName getProvider();

	void setProvider(FileStoreProviderName provider);

	Byte[] getFile();

	void setFile(Byte[] file);

	String getUrl();

}
