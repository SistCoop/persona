package org.sistcoop.persona.models;

import java.util.List;

import javax.ejb.Local;

import org.sistcoop.persona.models.enums.TipoPersona;
import org.sistcoop.persona.models.search.SearchCriteriaModel;
import org.sistcoop.persona.models.search.SearchResultsModel;
import org.sistcoop.persona.provider.Provider;

@Local
public interface TipoDocumentoProvider extends Provider {

	TipoDocumentoModel findById(String id);

	TipoDocumentoModel findByAbreviatura(String abreviatura);

	TipoDocumentoModel create(String abreviatura, String denominacion, int cantidadCaracteres, TipoPersona tipoPersona);

	boolean remove(TipoDocumentoModel tipoDocumentoModel);

	List<TipoDocumentoModel> getAll();

	SearchResultsModel<TipoDocumentoModel> search(SearchCriteriaModel criteria);

	SearchResultsModel<TipoDocumentoModel> search(SearchCriteriaModel criteria, String filterText);

}
