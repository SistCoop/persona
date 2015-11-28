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

    PersonaNaturalModel create(String codigoPais, TipoDocumentoModel tipoDocumentoModel,
            String numeroDocumento, String apellidoPaterno, String apellidoMaterno, String nombres,
            Date fechaNacimiento, Sexo sexo);

    boolean remove(PersonaNaturalModel personaNatural);

    List<PersonaNaturalModel> getAll();

    List<PersonaNaturalModel> getAll(int firstResult, int maxResults);

    List<PersonaNaturalModel> search(String filterText);

    List<PersonaNaturalModel> search(String filterText, int firstResult, int maxResults);

    List<PersonaNaturalModel> searchByAttributes(Map<String, String> attributes);

    List<PersonaNaturalModel> searchByAttributes(Map<String, String> attributes, int firstResult,
            int maxResults);

    SearchResultsModel<PersonaNaturalModel> search(SearchCriteriaModel criteria);

    SearchResultsModel<PersonaNaturalModel> search(SearchCriteriaModel criteria, String filterText);

}
