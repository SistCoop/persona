package org.sistcoop.persona.models.jpa.search.filter;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;

import org.sistcoop.persona.models.search.filters.PersonaJuridicaFilterProvider;

@Named
@Stateless
@Local(PersonaJuridicaFilterProvider.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JpaPersonaJuridicaFilterProvider implements PersonaJuridicaFilterProvider {

    private final String id = "id";
    private final String tipoDocumento = "tipoDocumento.abreviatura";
    private final String numeroDocumento = "numeroDocumento";
    private final String razonSocial = "razonSocial";

    @Override
    public void close() {
        // TODO Auto-generated method stub
    }

    @Override
    public String getIdFilter() {
        return this.id;
    }

    @Override
    public String getTipoDocumentoFilter() {
        return this.tipoDocumento;
    }

    @Override
    public String getNumeroDocumentoFilter() {
        return this.numeroDocumento;
    }

    @Override
    public String getRazonSocialFilter() {
        return this.razonSocial;
    }

}
