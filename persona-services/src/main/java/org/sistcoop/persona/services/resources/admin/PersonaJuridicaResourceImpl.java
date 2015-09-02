package org.sistcoop.persona.services.resources.admin;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PathParam;

import org.sistcoop.persona.admin.client.resource.AccionistasResource;
import org.sistcoop.persona.admin.client.resource.PersonaJuridicaResource;
import org.sistcoop.persona.models.PersonaJuridicaModel;
import org.sistcoop.persona.models.PersonaJuridicaProvider;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.representations.idm.PersonaJuridicaRepresentation;
import org.sistcoop.persona.services.managers.PersonaJuridicaManager;

@Stateless
public class PersonaJuridicaResourceImpl implements PersonaJuridicaResource {

    @PathParam("personaJuridica")
    private String personaJuridica;

    @Inject
    private PersonaJuridicaProvider personaJuridicaProvider;

    @Inject
    private PersonaJuridicaManager personaJuridicaManager;

    @Inject
    private AccionistasResource accionistasResource;

    private PersonaJuridicaModel getPersonaJuridicaModel() {
        return personaJuridicaProvider.findById(personaJuridica);
    }

    @Override
    public PersonaJuridicaRepresentation personaJuridica() {
        PersonaJuridicaRepresentation rep = ModelToRepresentation.toRepresentation(getPersonaJuridicaModel());
        if (rep != null) {
            return rep;
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public void update(PersonaJuridicaRepresentation representation) {
        personaJuridicaManager.update(getPersonaJuridicaModel(), representation);
    }

    @Override
    public void disable() {
        throw new NotFoundException();
    }

    @Override
    public void remove() {
        boolean result = personaJuridicaProvider.remove(getPersonaJuridicaModel());
        if (!result) {
            throw new InternalServerErrorException();
        }
    }

    @Override
    public AccionistasResource accionistas() {
        return accionistasResource;
    }

}
