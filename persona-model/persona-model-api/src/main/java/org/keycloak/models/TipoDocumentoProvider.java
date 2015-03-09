package org.keycloak.models;

import java.util.List;

import javax.ejb.Local;

import org.keycloak.models.enums.TipoPersona;
import org.keycloak.provider.Provider;

@Local
public interface TipoDocumentoProvider extends Provider {

	TipoDocumentoModel addTipoDocumento(String abreviatura, String denominacion, int cantidadCaracteres, TipoPersona tipoPersona);

	boolean removeTipoDocumento(TipoDocumentoModel tipoDocumentoModel);

	TipoDocumentoModel getTipoDocumentoByAbreviatura(String abreviatura);

	List<TipoDocumentoModel> getTiposDocumento();

	List<TipoDocumentoModel> getTiposDocumento(TipoPersona tipoPersona);

}
