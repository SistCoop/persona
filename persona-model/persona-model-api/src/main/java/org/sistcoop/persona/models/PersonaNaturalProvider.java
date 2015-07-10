package org.sistcoop.persona.models;

import java.util.Date;

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

    boolean remove(PersonaNaturalModel personaNaturalModel);

    SearchResultsModel<PersonaNaturalModel> search();

    SearchResultsModel<PersonaNaturalModel> search(SearchCriteriaModel searchCriteriaBean);

}
