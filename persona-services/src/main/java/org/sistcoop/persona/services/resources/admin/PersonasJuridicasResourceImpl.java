package org.sistcoop.persona.services.resources.admin;

import java.util.ArrayList;
import java.util.List;

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
import org.sistcoop.persona.models.search.PagingModel;
import org.sistcoop.persona.models.search.SearchCriteriaModel;
import org.sistcoop.persona.models.search.SearchResultsModel;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.models.utils.RepresentationToModel;
import org.sistcoop.persona.representations.idm.PersonaJuridicaRepresentation;
import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;
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
        TipoDocumentoModel tipoDocumentoPersonaJuridica = tipoDocumentoProvider.findByAbreviatura(rep
                .getTipoDocumento());

        // Check duplicated tipo y numero de documento
        if (personaJuridicaProvider.findByTipoNumeroDocumento(tipoDocumentoPersonaJuridica,
                rep.getNumeroDocumento()) != null) {
            return ErrorResponse.exists("PersonaJuridica existe con el mismo tipo y numero de documento");
        }

        PersonaNaturalRepresentation representanteRep = rep.getRepresentanteLegal();
        TipoDocumentoModel tipoDocumentoRepresentante = tipoDocumentoProvider
                .findByAbreviatura(representanteRep.getTipoDocumento());
        PersonaNaturalModel representante = personaNaturalProvider.findByTipoNumeroDocumento(
                tipoDocumentoRepresentante, representanteRep.getNumeroDocumento());
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

    @Override
    public SearchResultsRepresentation<PersonaJuridicaRepresentation> search(String tipoDocumento,
            String numeroDocumento, String filterText, int page, int pageSize) {
        SearchResultsModel<PersonaJuridicaModel> results = null;
        if (tipoDocumento != null && numeroDocumento != null) {
            TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.findByAbreviatura(tipoDocumento);
            PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.findByTipoNumeroDocumento(
                    tipoDocumentoModel, numeroDocumento);

            List<PersonaJuridicaModel> items = new ArrayList<>();
            if (personaJuridicaModel != null) {
                items.add(personaJuridicaModel);
            }

            results = new SearchResultsModel<>();
            results.setModels(items);
            results.setTotalSize(items.size());
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
