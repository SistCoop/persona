package org.sistcoop.persona.models.jpa;

import javax.persistence.EntityManager;

import org.sistcoop.persona.models.FileStoreModel;
import org.sistcoop.persona.models.enums.FileStoreProviderName;
import org.sistcoop.persona.models.jpa.entities.FileStoreEntity;

/**
 * @author <a href="mailto:carlosthe19916@sistcoop.com">Carlos Feria</a>
 */

public class FileStoreAdapter implements FileStoreModel {

	private static final long serialVersionUID = 1L;

	private FileStoreEntity fileStoreEntity;
	private EntityManager em;

	public FileStoreAdapter(EntityManager em, FileStoreEntity fileStoreEntity) {
		this.em = em;
		this.fileStoreEntity = fileStoreEntity;
	}

	public static FileStoreEntity toFileStoreEntity(FileStoreModel model, EntityManager em) {
		if (model instanceof FileStoreAdapter) {
			return ((FileStoreAdapter) model).getFileStoreEntity();
		}
		return em.getReference(FileStoreEntity.class, model.getId());
	}

	public FileStoreEntity getFileStoreEntity() {
		return fileStoreEntity;
	}

	@Override
	public void commit() {
		em.merge(fileStoreEntity);
	}

	@Override
	public String getId() {
		return fileStoreEntity.getId();
	}

	@Override
	public String getFileId() {
		return fileStoreEntity.getFileId();
	}

	@Override
	public void setFileId(String fileId) {
		fileStoreEntity.setFileId(fileId);
	}

	@Override
	public FileStoreProviderName getProvider() {
		String provider = fileStoreEntity.getProvider();
		if (provider != null) {
			return FileStoreProviderName.valueOf(provider);
		} else {
			return null;
		}
	}

	@Override
	public void setProvider(FileStoreProviderName provider) {
		if (provider != null) {
			fileStoreEntity.setProvider(provider.toString());
		} else {
			fileStoreEntity.setProvider(null);
		}
	}

	@Override
	public Byte[] getFile() {
		return fileStoreEntity.getFile();
	}

	@Override
	public void setFile(Byte[] file) {
		fileStoreEntity.setFile(file);
	}

	@Override
	public String getUrl() {
		return fileStoreEntity.getUrl();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FileStoreModel))
			return false;
		FileStoreModel other = (FileStoreModel) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

}
