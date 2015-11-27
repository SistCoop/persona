package org.sistcoop.persona.services.resources.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
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
	public TipoDocumentoRepresentation findByAbreviatura(TipoDocumentoRepresentation rep) {
		TipoDocumentoModel tipoDocumento = tipoDocumentoProvider.findByAbreviatura(rep.getAbreviatura());
		TipoDocumentoRepresentation representation = ModelToRepresentation.toRepresentation(tipoDocumento);
		if (representation != null) {
			return representation;
		} else {
			throw new NotFoundException("TipoDocumento no encontrado");
		}
	}

	@Override
	public List<TipoDocumentoRepresentation> getAll(String tipoPersona, Boolean estado) {
		List<TipoDocumentoModel> results = tipoDocumentoProvider.getAll();
		for (Iterator<TipoDocumentoModel> iterator = results.iterator(); iterator.hasNext();) {
			TipoDocumentoModel model = iterator.next();
			if (tipoPersona != null && !tipoPersona.equalsIgnoreCase(model.getTipoPersona().toString())) {
				iterator.remove();
				break;
			}
			if (estado != null && estado != model.getEstado()) {
				iterator.remove();
				break;
			}
		}

		List<TipoDocumentoRepresentation> representations = new ArrayList<>();
		for (TipoDocumentoModel model : results) {
			representations.add(ModelToRepresentation.toRepresentation(model));
		}
		return representations;
	}

	@Override
	public List<TipoDocumentoRepresentation> getAll(String abreviatura, String tipoPersona, Boolean estado,
			Integer firstResult, Integer maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TipoDocumentoRepresentation> search(String filterText, Integer firstResult, Integer maxResults) {
		return null;
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
