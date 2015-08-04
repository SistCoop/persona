package org.sistcoop.persona.services.resources.admin;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sistcoop.persona.Jsend;
import org.sistcoop.persona.admin.client.resource.AccionistaResource;
import org.sistcoop.persona.admin.client.resource.AccionistasResource;
import org.sistcoop.persona.models.AccionistaModel;
import org.sistcoop.persona.models.AccionistaProvider;
import org.sistcoop.persona.models.PersonaJuridicaModel;
import org.sistcoop.persona.models.PersonaJuridicaProvider;
import org.sistcoop.persona.models.PersonaNaturalProvider;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.search.SearchResultsModel;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.models.utils.RepresentationToModel;
import org.sistcoop.persona.representations.idm.AccionistaRepresentation;
import org.sistcoop.persona.representations.idm.search.SearchResultsRepresentation;

@Stateless
public class AccionistasResourceImpl implements AccionistasResource {

    @PathParam("persona")
    private String persona;

    @Inject
    private TipoDocumentoProvider tipoDocumentoProvider;

    @Inject
    private PersonaJuridicaProvider personaJuridicaProvider;

    @Inject
    private PersonaNaturalProvider personaNaturalProvider;

    @Inject
    private AccionistaProvider accionistaProvider;

    @Inject
    private AccionistaResource accionistaResource;

    @Inject
    private RepresentationToModel representationToModel;

    @Context
    private UriInfo uriInfo;

    private PersonaJuridicaModel getPersonaJuridicaModel() {
        return personaJuridicaProvider.findById(persona);
    }

    @Override
    public AccionistaResource accionista(String accionista) {
        return accionistaResource;
    }

    @Override
    public Response create(AccionistaRepresentation representation) {
        AccionistaModel model = representationToModel.createAccionista(representation,
                getPersonaJuridicaModel(), tipoDocumentoProvider, personaNaturalProvider, accionistaProvider);

        return Response.created(uriInfo.getAbsolutePathBuilder().path(model.getId()).build())
                .header("Access-Control-Expose-Headers", "Location")
                .entity(Jsend.getSuccessJSend(model.getId())).build();
    }

    @Override
    public SearchResultsRepresentation<AccionistaRepresentation> search() {
        SearchResultsModel<AccionistaModel> results = accionistaProvider.search(getPersonaJuridicaModel());
        SearchResultsRepresentation<AccionistaRepresentation> rep = new SearchResultsRepresentation<>();
        List<AccionistaRepresentation> representations = new ArrayList<>();
        for (AccionistaModel model : results.getModels()) {
            representations.add(ModelToRepresentation.toRepresentation(model));
        }
        rep.setTotalSize(results.getTotalSize());
        rep.setItems(representations);
        return rep;
    }

}
