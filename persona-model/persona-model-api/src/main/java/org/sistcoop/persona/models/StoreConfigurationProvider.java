package org.sistcoop.persona.models;

import java.util.List;

import javax.ejb.Local;

import org.sistcoop.persona.provider.Provider;

@Local
public interface StoreConfigurationProvider extends Provider {

    StoreConfigurationModel findById(String id);

    StoreConfigurationModel findByDenominacion(String denominacion);

    StoreConfigurationModel getDefaultStoreConfiguration();

    StoreConfigurationModel create(String appKey, String denominacion);

    boolean remove(StoreConfigurationModel storeConfiguration);

    List<StoreConfigurationModel> getAll();

}
