package org.sistcoop.persona.services.resources.admin;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sistcoop.persona.Jsend;
import org.sistcoop.persona.admin.client.resource.PersonaJuridicaResource;
import org.sistcoop.persona.admin.client.resource.PersonasJuridicasResource;
import org.sistcoop.persona.models.PersonaJuridicaModel;
import org.sistcoop.persona.models.PersonaJuridicaProvider;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.PersonaNaturalProvider;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.search.PagingModel;
import org.sistcoop.persona.models.search.SearchCriteriaFilterOperator;
import org.sistcoop.persona.models.search.SearchCriteriaModel;
import org.sistcoop.persona.models.search.SearchResultsModel;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.models.utils.RepresentationToModel;
import org.sistcoop.persona.representations.idm.PersonaJuridicaRepresentation;
import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;
import org.sistcoop.persona.representations.idm.search.SearchResultsRepresentation;

@Stateless
public class PersonasJuridicasResourceImpl implements PersonasJuridicasResource {

    @Inject
    private TipoDocumentoProvider tipoDocumentoProvider;

    @Inject
    private PersonaNaturalProvider personaNaturalProvider;

    @Inject
    private PersonaJuridicaProvider personaJuridicaProvider;

    @Inject
    private RepresentationToModel representationToModel;

    @Inject
    private PersonaJuridicaResource personaJuridicaResource;

    @Context
    private UriInfo uriInfo;

    @Override
    public PersonaJuridicaResource personaJuridica(String personaJuridica) {
        return personaJuridicaResource;
    }

    @Override
    public Response create(PersonaJuridicaRepresentation representation) {
        TipoDocumentoModel tipoDocumentoPersonaJuridicaModel = tipoDocumentoProvider
                .findByAbreviatura(representation.getTipoDocumento());

        PersonaNaturalRepresentation representanteRepresentation = representation.getRepresentanteLegal();
        TipoDocumentoModel tipoDocumentoRepresentanteModel = tipoDocumentoProvider
                .findByAbreviatura(representanteRepresentation.getTipoDocumento());
        PersonaNaturalModel representanteModel = personaNaturalProvider.findByTipoNumeroDocumento(
                tipoDocumentoRepresentanteModel, representanteRepresentation.getNumeroDocumento());

        PersonaJuridicaModel model = representationToModel.createPersonaJuridica(representation,
                tipoDocumentoPersonaJuridicaModel, representanteModel, personaJuridicaProvider);

        return Response.created(uriInfo.getAbsolutePathBuilder().path(model.getId()).build())
                .header("Access-Control-Expose-Headers", "Location")
                .entity(Jsend.getSuccessJSend(model.getId())).build();
    }

    @Override
    public SearchResultsRepresentation<PersonaJuridicaRepresentation> search(String tipoDocumento,
            String numeroDocumento, String filterText, int page, int pageSize) {
        SearchResultsModel<PersonaJuridicaModel> results = null;
        if (tipoDocumento != null && numeroDocumento != null) {
            SearchCriteriaModel searchCriteriaBean = new SearchCriteriaModel();

            searchCriteriaBean.addFilter("tipoDocumento", tipoDocumento, SearchCriteriaFilterOperator.eq);
            searchCriteriaBean.addFilter("numeroDocumento", numeroDocumento, SearchCriteriaFilterOperator.eq);

            results = personaJuridicaProvider.search(searchCriteriaBean);
        } else {
            PagingModel paging = new PagingModel();
            paging.setPage(page);
            paging.setPageSize(pageSize);

            SearchCriteriaModel searchCriteriaBean = new SearchCriteriaModel();
            searchCriteriaBean.setPaging(paging);

            results = personaJuridicaProvider.search(searchCriteriaBean, filterText);
        }

        SearchResultsRepresentation<PersonaJuridicaRepresentation> rep = new SearchResultsRepresentation<>();
        List<PersonaJuridicaRepresentation> representations = new ArrayList<>();
        for (PersonaJuridicaModel model : results.getModels()) {
            representations.add(ModelToRepresentation.toRepresentation(model));
        }
        rep.setTotalSize(results.getTotalSize());
        rep.setItems(representations);
        return rep;
    }

}
