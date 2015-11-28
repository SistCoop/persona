package org.sistcoop.persona.services.resources.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public List<TipoDocumentoRepresentation> search(String denominacion, String abreviatura, String tipoPersona,
			Boolean estado, String filterText, Integer firstResult, Integer maxResults) {
		firstResult = firstResult != null ? firstResult : -1;
		maxResults = maxResults != null ? maxResults : -1;

		List<TipoDocumentoRepresentation> results = new ArrayList<TipoDocumentoRepresentation>();
		List<TipoDocumentoModel> tipoDocumentoModels;
		if (filterText != null) {
			tipoDocumentoModels = tipoDocumentoProvider.search(filterText.trim(), firstResult, maxResults);
		} else if (denominacion != null || abreviatura != null || tipoPersona != null || estado != null) {
			Map<String, Object> attributes = new HashMap<String, Object>();
			if (denominacion != null) {
				attributes.put(TipoDocumentoModel.DENOMINACION, denominacion);
			}
			if (abreviatura != null) {
				attributes.put(TipoDocumentoModel.ABREVIATURA, abreviatura);
			}
			if (tipoPersona != null) {
				attributes.put(TipoDocumentoModel.TIPO_PERSONA, tipoPersona);
			}
			if (estado != null) {
				attributes.put(TipoDocumentoModel.ESTADO, estado);
			}
			tipoDocumentoModels = tipoDocumentoProvider.searchByAttributes(attributes, firstResult, maxResults);
		} else {
			tipoDocumentoModels = tipoDocumentoProvider.getAll(firstResult, maxResults);
		}

		for (TipoDocumentoModel model : tipoDocumentoModels) {
			results.add(ModelToRepresentation.toRepresentation(model));
		}
		return results;
	}

	@Override
	public SearchResultsRepresentation<TipoDocumentoRepresentation> search(SearchCriteriaRepresentation criteria) {
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
