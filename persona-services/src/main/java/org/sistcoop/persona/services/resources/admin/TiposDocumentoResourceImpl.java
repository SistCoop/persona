package org.sistcoop.persona.services.resources.admin;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sistcoop.persona.Jsend;
import org.sistcoop.persona.admin.client.resource.TipoDocumentoResource;
import org.sistcoop.persona.admin.client.resource.TiposDocumentoResource;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.search.SearchCriteriaFilterOperator;
import org.sistcoop.persona.models.search.SearchCriteriaModel;
import org.sistcoop.persona.models.search.SearchResultsModel;
import org.sistcoop.persona.models.search.filters.TipoDocumentoFilterProvider;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.models.utils.RepresentationToModel;
import org.sistcoop.persona.representations.idm.TipoDocumentoRepresentation;
import org.sistcoop.producto.representations.idm.search.SearchResultsRepresentation;

@Stateless
public class TiposDocumentoResourceImpl implements TiposDocumentoResource {

    @Inject
    private TipoDocumentoProvider tipoDocumentoProvider;

    @Inject
    private TipoDocumentoFilterProvider tipoDocumentoFilterProvider;

    @Inject
    private RepresentationToModel representationToModel;

    @Context
    private UriInfo uriInfo;

    @Inject
    private TipoDocumentoResource tipoDocumentoResource;

    @Override
    public TipoDocumentoResource documento(String documento) {
        return tipoDocumentoResource;
    }

    @Override
    public Response create(TipoDocumentoRepresentation representation) {
        TipoDocumentoModel model = representationToModel.createTipoDocumento(representation,
                tipoDocumentoProvider);

        return Response.created(uriInfo.getAbsolutePathBuilder().path(model.getAbreviatura()).build())
                .header("Access-Control-Expose-Headers", "Location")
                .entity(Jsend.getSuccessJSend(model.getAbreviatura())).build();
    }

    @Override
    public SearchResultsRepresentation<TipoDocumentoRepresentation> search(String tipoPersona, boolean estado) {
        SearchCriteriaModel searchCriteriaBean = new SearchCriteriaModel();

        // add filters
        if (tipoPersona != null) {
            searchCriteriaBean.addFilter(tipoDocumentoFilterProvider.getTipoPersonaFilter(), tipoPersona,
                    SearchCriteriaFilterOperator.eq);
        }
        searchCriteriaBean.addFilter(tipoDocumentoFilterProvider.getEstadoFilter(),
                estado ? "true" : "false", SearchCriteriaFilterOperator.bool_eq);

        // search
        SearchResultsModel<TipoDocumentoModel> results = tipoDocumentoProvider.search(searchCriteriaBean);
        SearchResultsRepresentation<TipoDocumentoRepresentation> rep = new SearchResultsRepresentation<>();
        List<TipoDocumentoRepresentation> representations = new ArrayList<>();
        for (TipoDocumentoModel model : results.getModels()) {
            representations.add(ModelToRepresentation.toRepresentation(model));
        }
        rep.setTotalSize(results.getTotalSize());
        rep.setItems(representations);
        return rep;
    }

}
