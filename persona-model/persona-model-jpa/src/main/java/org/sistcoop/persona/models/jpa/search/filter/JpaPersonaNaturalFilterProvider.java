package org.sistcoop.persona.models.jpa.search.filter;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;

import org.sistcoop.persona.models.search.filters.PersonaNaturalFilterProvider;

@Named
@Stateless
@Local(PersonaNaturalFilterProvider.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JpaPersonaNaturalFilterProvider implements PersonaNaturalFilterProvider {

    private final String id = "id";
    private final String tipoDocumento = "tipoDocumento";
    private final String numeroDocumento = "numeroDocumento";
    private final String apellidoPaterno = "apellidoPaterno";
    private final String apellidoMaterno = "apellidoMaterno";
    private final String nombres = "nombres";

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
    public String getApellidoPaternoFilter() {
        return this.apellidoPaterno;
    }

    @Override
    public String getApellidoMaternoFilter() {
        return this.apellidoMaterno;
    }

    @Override
    public String getNombresFilter() {
        return this.nombres;
    }

}
