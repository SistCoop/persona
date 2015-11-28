package org.sistcoop.persona.models;

import java.io.IOException;

import javax.ejb.Local;

import org.sistcoop.persona.provider.Provider;

@Local
public interface StoreBoxProvider extends Provider {

    StoreBoxModel getByStoreConfiguration(StoreConfigurationModel storeConfiguration) throws IOException;

}
