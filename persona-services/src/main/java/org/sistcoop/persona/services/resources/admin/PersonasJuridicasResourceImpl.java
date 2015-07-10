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
import org.sistcoop.persona.models.search.util.PersonaModelAtribute;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.models.utils.RepresentationToModel;
import org.sistcoop.persona.representations.idm.PersonaJuridicaRepresentation;
import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;
import org.sistcoop.producto.representations.idm.search.SearchResultsRepresentation;

@Stateless
public class PersonasJuridicasResourceImpl implements PersonasJuridicasResource {

    @Inject
    private TipoDocumentoProvider tipoDocumentoProvider;

    @Inject
    private PersonaJuridicaProvider personaJuridicaProvider;

    @Inject
    private PersonaNaturalProvider personaNaturalProvider;

    @Inject
    private RepresentationToModel representationToModel;

    @Context
    private UriInfo uriInfo;

    @Inject
    private PersonaJuridicaResource personaJuridicaResource;

    @Override
    public PersonaJuridicaResource persona(String persona) {
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
    public SearchResultsRepresentation<PersonaJuridicaRepresentation> search(String documento, String numero) {
        SearchCriteriaModel searchCriteriaBean = new SearchCriteriaModel();

        // add filters
        searchCriteriaBean.addFilter(PersonaModelAtribute.documento, documento,
                SearchCriteriaFilterOperator.eq);
        searchCriteriaBean.addFilter(PersonaModelAtribute.numero, numero, SearchCriteriaFilterOperator.eq);

        // search
        SearchResultsModel<PersonaJuridicaModel> results = personaJuridicaProvider.search(searchCriteriaBean);
        SearchResultsRepresentation<PersonaJuridicaRepresentation> rep = new SearchResultsRepresentation<>();
        List<PersonaJuridicaRepresentation> representations = new ArrayList<>();
        for (PersonaJuridicaModel model : results.getModels()) {
            representations.add(ModelToRepresentation.toRepresentation(model));
        }
        rep.setTotalSize(results.getTotalSize());
        rep.setItems(representations);
        return rep;
    }

    @Override
    public SearchResultsRepresentation<PersonaJuridicaRepresentation> search(String filterText, int page,
            int pageSize) {

        PagingModel paging = new PagingModel();
        paging.setPage(page);
        paging.setPageSize(pageSize);

        SearchCriteriaModel searchCriteriaBean = new SearchCriteriaModel();
        searchCriteriaBean.setPaging(paging);

        // add filters
        searchCriteriaBean.addFilter(PersonaModelAtribute.documento, filterText,
                SearchCriteriaFilterOperator.like);
        searchCriteriaBean.addFilter(PersonaModelAtribute.numero, filterText,
                SearchCriteriaFilterOperator.like);
        searchCriteriaBean.addFilter(PersonaModelAtribute.razonSocial, filterText,
                SearchCriteriaFilterOperator.like);

        // search
        SearchResultsModel<PersonaJuridicaModel> results = personaJuridicaProvider.search(searchCriteriaBean);
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
