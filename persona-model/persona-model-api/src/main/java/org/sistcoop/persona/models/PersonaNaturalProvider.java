package org.sistcoop.persona.models;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import org.sistcoop.persona.models.enums.Sexo;
import org.sistcoop.persona.models.search.SearchCriteriaModel;
import org.sistcoop.persona.models.search.SearchResultsModel;
import org.sistcoop.persona.provider.Provider;

@Local
public interface PersonaNaturalProvider extends Provider {

	PersonaNaturalModel findById(String id);

	PersonaNaturalModel findByTipoNumeroDocumento(TipoDocumentoModel tipoDocumento, String numeroDocumento);

	PersonaNaturalModel create(String codigoPais, TipoDocumentoModel tipoDocumentoModel, String numeroDocumento,
			String apellidoPaterno, String apellidoMaterno, String nombres, Date fechaNacimiento, Sexo sexo);

	boolean remove(PersonaNaturalModel personaNaturalModel);

	List<PersonaNaturalModel> getAll();

	List<PersonaNaturalModel> searchByAttributes(Map<String, Object> attributes, Integer firstResult,
			Integer maxResults);

	List<PersonaNaturalModel> search(Integer firstResult, Integer maxResults);

	List<PersonaNaturalModel> search(String trim, Integer firstResult, Integer maxResults);

	SearchResultsModel<PersonaNaturalModel> search(SearchCriteriaModel criteria);

	SearchResultsModel<PersonaNaturalModel> search(SearchCriteriaModel criteria, String filterText);

}
