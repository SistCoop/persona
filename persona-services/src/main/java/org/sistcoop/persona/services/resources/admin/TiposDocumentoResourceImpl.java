package org.sistcoop.persona.services.resources.admin;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sistcoop.persona.admin.client.resource.TipoDocumentoResource;
import org.sistcoop.persona.admin.client.resource.TiposDocumentoResource;
import org.sistcoop.persona.models.ModelDuplicateException;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.search.SearchCriteriaFilterOperator;
import org.sistcoop.persona.models.search.SearchCriteriaModel;
import org.sistcoop.persona.models.search.SearchResultsModel;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.models.utils.RepresentationToModel;
import org.sistcoop.persona.representations.idm.TipoDocumentoRepresentation;
import org.sistcoop.persona.representations.idm.search.OrderByRepresentation;
import org.sistcoop.persona.representations.idm.search.PagingRepresentation;
import org.sistcoop.persona.representations.idm.search.SearchCriteriaFilterRepresentation;
import org.sistcoop.persona.representations.idm.search.SearchCriteriaRepresentation;
import org.sistcoop.persona.representations.idm.search.SearchResultsRepresentation;
import org.sistcoop.persona.services.ErrorResponse;

@Stateless
public class TiposDocumentoResourceImpl implements TiposDocumentoResource {

    @Inject
    private TipoDocumentoProvider tipoDocumentoProvider;

    @Inject
    private RepresentationToModel representationToModel;

    @Context
    private UriInfo uriInfo;

    @Inject
    private TipoDocumentoResource tipoDocumentoResource;

    @Override
    public TipoDocumentoResource tipoDocumento(String documento) {
        return tipoDocumentoResource;
    }

    @Override
    public Response create(TipoDocumentoRepresentation rep) {
        // Check duplicated abreviatura
        if (tipoDocumentoProvider.findByAbreviatura(rep.getAbreviatura()) != null) {
            return ErrorResponse.exists("TipoDocumento existe con la misma abreviatura");
        }
        try {
            TipoDocumentoModel model = representationToModel.createTipoDocumento(rep, tipoDocumentoProvider);
            return Response.created(uriInfo.getAbsolutePathBuilder().path(model.getAbreviatura()).build())
                    .header("Access-Control-Expose-Headers", "Location")
                    .entity(ModelToRepresentation.toRepresentation(model)).build();
        } catch (ModelDuplicateException e) {
            return ErrorResponse.exists("TipoDocumento existe con la misma abreviatura");
        }
    }

    @Override
    public List<TipoDocumentoRepresentation> getAll() {
        List<TipoDocumentoModel> results = tipoDocumentoProvider.getAll();
        List<TipoDocumentoRepresentation> representations = new ArrayList<>();
        for (TipoDocumentoModel model : results) {
            representations.add(ModelToRepresentation.toRepresentation(model));
        }
        return representations;
    }

    /*
     * @Override public SearchResultsRepresentation<TipoDocumentoRepresentation>
     * search(String abreviatura, String tipoPersona, boolean estado, String
     * filterText, int page, int pageSize) {
     * 
     * PagingModel paging = new PagingModel(); paging.setPage(page);
     * paging.setPageSize(pageSize);
     * 
     * SearchCriteriaModel searchCriteriaBean = new SearchCriteriaModel();
     * searchCriteriaBean.setPaging(paging);
     * 
     * // add filters if (abreviatura != null) {
     * searchCriteriaBean.addFilter("abreviatura", abreviatura,
     * SearchCriteriaFilterOperator.eq); }
     * 
     * // add filters if (tipoPersona != null) {
     * searchCriteriaBean.addFilter("tipoPersona",
     * TipoPersona.valueOf(tipoPersona.toUpperCase()),
     * SearchCriteriaFilterOperator.eq); }
     * searchCriteriaBean.addFilter("estado", estado,
     * SearchCriteriaFilterOperator.bool_eq);
     * 
     * // search SearchResultsModel<TipoDocumentoModel> results =
     * tipoDocumentoProvider.search(searchCriteriaBean, filterText);
     * SearchResultsRepresentation<TipoDocumentoRepresentation> rep = new
     * SearchResultsRepresentation<>(); List<TipoDocumentoRepresentation>
     * representations = new ArrayList<>(); for (TipoDocumentoModel model :
     * results.getModels()) {
     * representations.add(ModelToRepresentation.toRepresentation(model)); }
     * rep.setTotalSize(results.getTotalSize()); rep.setItems(representations);
     * 
     * return rep; }
     */

    @Override
    public SearchResultsRepresentation<TipoDocumentoRepresentation> search(
            SearchCriteriaRepresentation criteria) {
        SearchCriteriaModel criteriaModel = new SearchCriteriaModel();

        // set filter and order
        for (SearchCriteriaFilterRepresentation filter : criteria.getFilters()) {
            criteriaModel.addFilter(filter.getName(), filter.getValue(),
                    SearchCriteriaFilterOperator.valueOf(filter.getOperator().toString()));
        }
        for (OrderByRepresentation order : criteria.getOrders()) {
            criteriaModel.addOrder(order.getName(), order.isAscending());
        }

        // set paging
        PagingRepresentation paging = criteria.getPaging();
        if (paging == null) {
            paging = new PagingRepresentation();
            paging.setPage(1);
            paging.setPageSize(20);
        }
        criteriaModel.setPageSize(paging.getPageSize());
        criteriaModel.setPage(paging.getPage());

        // extract filterText
        String filterText = criteria.getFilterText();

        // search
        SearchResultsModel<TipoDocumentoModel> results = null;
        if (filterText == null) {
            results = tipoDocumentoProvider.search(criteriaModel);
        } else {
            results = tipoDocumentoProvider.search(criteriaModel, filterText);
        }

        SearchResultsRepresentation<TipoDocumentoRepresentation> rep = new SearchResultsRepresentation<>();
        List<TipoDocumentoRepresentation> items = new ArrayList<>();
        for (TipoDocumentoModel model : results.getModels()) {
            items.add(ModelToRepresentation.toRepresentation(model));
        }
        rep.setItems(items);
        rep.setTotalSize(results.getTotalSize());
        return rep;
    }

}
