package org.sistcoop.persona.models;

import java.util.List;

import javax.ejb.Local;

import org.sistcoop.persona.models.enums.TipoPersona;
import org.sistcoop.persona.provider.Provider;

@Local
public interface TipoDocumentoProvider extends Provider {

	TipoDocumentoModel addTipoDocumento(String abreviatura, String denominacion, int cantidadCaracteres, TipoPersona tipoPersona);

	boolean desactivarTipoDocumento(TipoDocumentoModel tipoDocumentoModel);
	
	boolean removeTipoDocumento(TipoDocumentoModel tipoDocumentoModel);

	TipoDocumentoModel getTipoDocumentoByAbreviatura(String abreviatura);

	List<TipoDocumentoModel> getTiposDocumento();

	List<TipoDocumentoModel> getTiposDocumento(TipoPersona tipoPersona);

	List<TipoDocumentoModel> getTiposDocumento(boolean estado);

	List<TipoDocumentoModel> getTiposDocumento(TipoPersona tipoPersona, boolean estado);

}
