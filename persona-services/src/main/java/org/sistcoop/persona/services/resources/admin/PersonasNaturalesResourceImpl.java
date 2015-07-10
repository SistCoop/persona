package org.sistcoop.persona.services.resources.admin;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sistcoop.persona.Jsend;
import org.sistcoop.persona.admin.client.resource.PersonaNaturalResource;
import org.sistcoop.persona.admin.client.resource.PersonasNaturalesResource;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.PersonaNaturalProvider;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.search.PagingModel;
import org.sistcoop.persona.models.search.SearchCriteriaFilterOperator;
import org.sistcoop.persona.models.search.SearchCriteriaModel;
import org.sistcoop.persona.models.search.SearchResultsModel;
import org.sistcoop.persona.models.search.util.PersonaModelAtribute;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.models.utils.RepresentationToModel;
import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;
import org.sistcoop.producto.representations.idm.search.SearchResultsRepresentation;

@Stateless
public class PersonasNaturalesResourceImpl implements PersonasNaturalesResource {

    @Inject
    private PersonaNaturalProvider personaNaturalProvider;

    @Inject
    private TipoDocumentoProvider tipoDocumentoProvider;

    @Inject
    private RepresentationToModel representationToModel;

    @Context
    private UriInfo uriInfo;

    @Inject
    private PersonaNaturalResource personaNaturalResource;

    @Override
    public PersonaNaturalResource persona(String persona) {
        return personaNaturalResource;
    }

    @Override
    public Response create(PersonaNaturalRepresentation representation) {
        TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.findByAbreviatura(representation
                .getTipoDocumento());
        PersonaNaturalModel model = representationToModel.createPersonaNatural(representation,
                tipoDocumentoModel, personaNaturalProvider);

        return Response.created(uriInfo.getAbsolutePathBuilder().path(model.getId()).build())
                .header("Access-Control-Expose-Headers", "Location")
                .entity(Jsend.getSuccessJSend(model.getId())).build();
    }

    @Override
    public SearchResultsRepresentation<PersonaNaturalRepresentation> search(String documento, String numero) {

        SearchCriteriaModel searchCriteriaBean = new SearchCriteriaModel();

        // add filters
        searchCriteriaBean.addFilter(PersonaModelAtribute.documento, documento,
                SearchCriteriaFilterOperator.eq);
        searchCriteriaBean.addFilter(PersonaModelAtribute.numero, numero, SearchCriteriaFilterOperator.eq);

        // search
        SearchResultsModel<PersonaNaturalModel> results = personaNaturalProvider.search(searchCriteriaBean);
        SearchResultsRepresentation<PersonaNaturalRepresentation> rep = new SearchResultsRepresentation<>();
        List<PersonaNaturalRepresentation> representations = new ArrayList<>();
        for (PersonaNaturalModel model : results.getModels()) {
            representations.add(ModelToRepresentation.toRepresentation(model));
        }
        rep.setTotalSize(results.getTotalSize());
        rep.setItems(representations);
        return rep;
    }

    @Override
    public SearchResultsRepresentation<PersonaNaturalRepresentation> search(String filterText, int page,
            int pageSize) {

        PagingModel paging = new PagingModel();
        paging.setPage(page);
        paging.setPageSize(pageSize);

        SearchCriteriaModel searchCriteriaBean = new SearchCriteriaModel();
        searchCriteriaBean.setPaging(paging);

        // add filters
        // add filters
        searchCriteriaBean.addFilter(PersonaModelAtribute.documento, filterText,
                SearchCriteriaFilterOperator.like);
        searchCriteriaBean.addFilter(PersonaModelAtribute.numero, filterText,
                SearchCriteriaFilterOperator.like);
        searchCriteriaBean.addFilter(PersonaModelAtribute.apellidoPaterno, filterText,
                SearchCriteriaFilterOperator.like);
        searchCriteriaBean.addFilter(PersonaModelAtribute.apellidoMaterno, filterText,
                SearchCriteriaFilterOperator.like);
        searchCriteriaBean.addFilter(PersonaModelAtribute.nombres, filterText,
                SearchCriteriaFilterOperator.like);

        // search
        SearchResultsModel<PersonaNaturalModel> results = personaNaturalProvider.search(searchCriteriaBean);
        SearchResultsRepresentation<PersonaNaturalRepresentation> rep = new SearchResultsRepresentation<>();
        List<PersonaNaturalRepresentation> representations = new ArrayList<>();
        for (PersonaNaturalModel model : results.getModels()) {
            representations.add(ModelToRepresentation.toRepresentation(model));
        }
        rep.setTotalSize(results.getTotalSize());
        rep.setItems(representations);
        return rep;
    }

}
