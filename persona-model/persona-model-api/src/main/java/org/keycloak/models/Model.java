package org.keycloak.models;

import java.io.Serializable;

public interface Model extends Serializable {

	void commit();

}
