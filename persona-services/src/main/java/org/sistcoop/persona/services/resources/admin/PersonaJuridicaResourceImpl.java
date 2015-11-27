package org.sistcoop.persona.services.resources.admin;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.sistcoop.persona.admin.client.resource.AccionistasResource;
import org.sistcoop.persona.admin.client.resource.PersonaJuridicaResource;
import org.sistcoop.persona.models.PersonaJuridicaModel;
import org.sistcoop.persona.models.PersonaJuridicaProvider;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.representations.idm.PersonaJuridicaRepresentation;
import org.sistcoop.persona.services.ErrorResponse;
import org.sistcoop.persona.services.managers.PersonaJuridicaManager;

@Stateless
public class PersonaJuridicaResourceImpl implements PersonaJuridicaResource {

	@PathParam("idPersonaJuridica")
	private String idPersonaJuridica;

	@Inject
	private PersonaJuridicaProvider personaJuridicaProvider;

	@Inject
	private PersonaJuridicaManager personaJuridicaManager;

	@Inject
	private AccionistasResource accionistasResource;

	private PersonaJuridicaModel getPersonaJuridicaModel() {
		return personaJuridicaProvider.findById(idPersonaJuridica);
	}

	@Override
	public PersonaJuridicaRepresentation toRepresentation() {
		PersonaJuridicaRepresentation rep = ModelToRepresentation.toRepresentation(getPersonaJuridicaModel());
		if (rep != null) {
			return rep;
		} else {
			throw new NotFoundException("Persona Juridica no encontrada");
		}
	}

	@Override
	public void update(PersonaJuridicaRepresentation rep) {
		personaJuridicaManager.update(getPersonaJuridicaModel(), rep);
	}

	@Override
	public Response remove() {
		PersonaJuridicaModel personaJuridica = getPersonaJuridicaModel();
		if (personaJuridica == null) {
			throw new NotFoundException("PersonaJuridica no encontrado");
		}
		boolean removed = personaJuridicaProvider.remove(personaJuridica);
		if (removed) {
			return Response.noContent().build();
		} else {
			return ErrorResponse.error("Persona Juridica no pudo ser eliminada", Response.Status.BAD_REQUEST);
		}
	}

	@Override
	public AccionistasResource accionistas() {
		return accionistasResource;
	}

}
