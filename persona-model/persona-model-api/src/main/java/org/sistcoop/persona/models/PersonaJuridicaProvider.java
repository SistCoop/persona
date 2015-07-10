package org.sistcoop.persona.models;

import java.util.Date;

import javax.ejb.Local;

import org.sistcoop.persona.models.enums.TipoEmpresa;
import org.sistcoop.persona.models.search.SearchCriteriaModel;
import org.sistcoop.persona.models.search.SearchResultsModel;
import org.sistcoop.persona.provider.Provider;

@Local
public interface PersonaJuridicaProvider extends Provider {

    PersonaJuridicaModel findById(String id);

    PersonaJuridicaModel findByTipoNumeroDocumento(TipoDocumentoModel tipoDocumento, String numeroDocumento);

    PersonaJuridicaModel create(PersonaNaturalModel representanteLegal, String codigoPais,
            TipoDocumentoModel tipoDocumentoModel, String numeroDocumento, String razonSocial,
            Date fechaConstitucion, TipoEmpresa tipoEmpresa, boolean finLucro);

    boolean remove(PersonaJuridicaModel personaJuridicaModel);

    SearchResultsModel<PersonaJuridicaModel> search(SearchCriteriaModel searchCriteriaBean);
}
