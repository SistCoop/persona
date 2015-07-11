package org.sistcoop.persona.models.search.filters;

import javax.ejb.Local;

import org.sistcoop.persona.provider.Provider;

@Local
public interface PersonaJuridicaFilterProvider extends Provider {

    String getIdFilter();

    String getTipoDocumentoFilter();

    String getNumeroDocumentoFilter();

    String getRazonSocialFilter();

}
