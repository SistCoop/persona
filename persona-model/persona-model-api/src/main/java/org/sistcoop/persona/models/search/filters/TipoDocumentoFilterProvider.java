package org.sistcoop.persona.models.search.filters;

import javax.ejb.Local;

import org.sistcoop.persona.provider.Provider;

@Local
public interface TipoDocumentoFilterProvider extends Provider {

    String getAbreviaturaFilter();

    String getTipoPersonaFilter();
    
    String getDenominacionFilter();

    String getEstadoFilter();

}
