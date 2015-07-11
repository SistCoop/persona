package org.sistcoop.persona.models.search.filters;

import javax.ejb.Local;

import org.sistcoop.persona.provider.Provider;

@Local
public interface PersonaNaturalFilterProvider extends Provider {

    String getIdFilter();

    String getTipoDocumentoFilter();

    String getNumeroDocumentoFilter();

    String getApellidoPaternoFilter();

    String getApellidoMaternoFilter();

    String getNombresFilter();

}
