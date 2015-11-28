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

import org.sistcoop.persona.admin.client.resource.PersonaJuridicaResource;
import org.sistcoop.persona.admin.client.resource.PersonasJuridicasResource;
import org.sistcoop.persona.models.ModelDuplicateException;
import org.sistcoop.persona.models.PersonaJuridicaModel;
import org.sistcoop.persona.models.PersonaJuridicaProvider;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.PersonaNaturalProvider;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.search.SearchCriteriaFilterOperator;
import org.sistcoop.persona.models.search.SearchCriteriaModel;
import org.sistcoop.persona.models.search.SearchResultsModel;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.models.utils.RepresentationToModel;
import org.sistcoop.persona.representations.idm.PersonaJuridicaRepresentation;
import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;
import org.sistcoop.persona.representations.idm.search.OrderByRepresentation;
import org.sistcoop.persona.representations.idm.search.PagingRepresentation;
import org.sistcoop.persona.representations.idm.search.SearchCriteriaFilterRepresentation;
import org.sistcoop.persona.representations.idm.search.SearchCriteriaRepresentation;
import org.sistcoop.persona.representations.idm.search.SearchResultsRepresentation;
import org.sistcoop.persona.services.ErrorResponse;

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
    public Response create(PersonaJuridicaRepresentation rep) {
        TipoDocumentoModel tipoDocumentoPersonaJuridica = tipoDocumentoProvider
                .findByAbreviatura(rep.getTipoDocumento());

        // Check duplicated tipo y numero de documento
        if (personaJuridicaProvider.findByTipoNumeroDocumento(tipoDocumentoPersonaJuridica,
                rep.getNumeroDocumento()) != null) {
            return ErrorResponse.exists("Persona Juridica existe con el mismo tipo y numero de documento");
        }

        PersonaNaturalRepresentation representanteRep = rep.getRepresentanteLegal();
        TipoDocumentoModel tipoDocumentoRepresentante = tipoDocumentoProvider
                .findByAbreviatura(representanteRep.getTipoDocumento());
        PersonaNaturalModel representante = personaNaturalProvider
                .findByTipoNumeroDocumento(tipoDocumentoRepresentante, representanteRep.getNumeroDocumento());
        try {
            PersonaJuridicaModel model = representationToModel.createPersonaJuridica(rep,
                    tipoDocumentoPersonaJuridica, representante, personaJuridicaProvider);
            return Response.created(uriInfo.getAbsolutePathBuilder().path(model.getId()).build())
                    .header("Access-Control-Expose-Headers", "Location")
                    .entity(ModelToRepresentation.toRepresentation(model)).build();
        } catch (ModelDuplicateException e) {
            return ErrorResponse.exists("PersonaJuridica existe con el mismo tipo y numero de documento");
        }
    }

    /*
     * @Override public PersonaJuridicaRepresentation
     * findByTipoNumeroDocumento(PersonaJuridicaRepresentation rep) {
     * 
     * PersonaJuridicaRepresentation representation = ModelToRepresentation
     * .toRepresentation(personaJuridica); if (representation != null) { return
     * representation; } else { throw new NotFoundException(
     * "Persona juridica no encontrado"); } }
     */
    @Override
    public List<PersonaJuridicaRepresentation> search(String tipoDocumento, String numeroDocumento,
            String razonSocial, String nombreComercial, String filterText, Integer firstResult,
            Integer maxResults) {
        firstResult = firstResult != null ? firstResult : -1;
        maxResults = maxResults != null ? maxResults : -1;

        List<PersonaJuridicaRepresentation> results = new ArrayList<PersonaJuridicaRepresentation>();
        List<PersonaJuridicaModel> personaJuridicaModels;
        if (filterText != null) {
            personaJuridicaModels = personaJuridicaProvider.search(filterText.trim(), firstResult,
                    maxResults);
        } else if (tipoDocumento != null || numeroDocumento != null) {
            TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.findByAbreviatura(tipoDocumento);
            PersonaJuridicaModel personaJuridica = personaJuridicaProvider
                    .findByTipoNumeroDocumento(tipoDocumentoModel, numeroDocumento);
            personaJuridicaModels = new ArrayList<>();
            personaJuridicaModels.add(personaJuridica);
        } else if (razonSocial != null || nombreComercial != null || numeroDocumento != null) {
            Map<String, String> attributes = new HashMap<String, String>();
            if (razonSocial != null) {
                attributes.put(PersonaJuridicaModel.RAZON_SOCIAL, razonSocial);
            }
            if (nombreComercial != null) {
                attributes.put(PersonaJuridicaModel.NOMBRE_COMERCIAL, nombreComercial);
            }
            if (numeroDocumento != null) {
                attributes.put(PersonaJuridicaModel.NUMERO_DOCUMENTO, numeroDocumento);
            }
            personaJuridicaModels = personaJuridicaProvider.searchByAttributes(attributes, firstResult,
                    maxResults);
        } else {
            personaJuridicaModels = personaJuridicaProvider.getAll(firstResult, maxResults);
        }

        for (PersonaJuridicaModel model : personaJuridicaModels) {
            results.add(ModelToRepresentation.toRepresentation(model));
        }
        return results;
    }

    @Override
    public SearchResultsRepresentation<PersonaJuridicaRepresentation> search(
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
        SearchResultsModel<PersonaJuridicaModel> results = null;
        if (filterText == null) {
            results = personaJuridicaProvider.search(criteriaModel);
        } else {
            results = personaJuridicaProvider.search(criteriaModel, filterText);
        }

        SearchResultsRepresentation<PersonaJuridicaRepresentation> rep = new SearchResultsRepresentation<>();
        List<PersonaJuridicaRepresentation> items = new ArrayList<>();
        for (PersonaJuridicaModel model : results.getModels()) {
            items.add(ModelToRepresentation.toRepresentation(model));
        }
        rep.setItems(items);
        rep.setTotalSize(results.getTotalSize());
        return rep;
    }

}
