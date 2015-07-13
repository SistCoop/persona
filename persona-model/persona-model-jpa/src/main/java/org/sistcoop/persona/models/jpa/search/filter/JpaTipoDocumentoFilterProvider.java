package org.sistcoop.persona.models.jpa.search.filter;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;

import org.sistcoop.persona.models.search.filters.TipoDocumentoFilterProvider;

@Named
@Stateless
@Local(TipoDocumentoFilterProvider.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JpaTipoDocumentoFilterProvider implements TipoDocumentoFilterProvider {

    private final String abreviatura = "abreviatura";
    private final String denominacion = "denominacion";
    private final String tipoPersona = "tipoPersona";
    private final String estado = "estado";

    @Override
    public void close() {
        // TODO Auto-generated method stub
    }

    @Override
    public String getAbreviaturaFilter() {
        return this.abreviatura;
    }

    @Override
    public String getTipoPersonaFilter() {
        return this.tipoPersona;
    }

    @Override
    public String getEstadoFilter() {
        return this.estado;
    }

    @Override
    public String getDenominacionFilter() {
        return this.denominacion;
    }

}
