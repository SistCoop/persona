package org.sistcoop.persona.services.resources.admin;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.sistcoop.persona.admin.client.Roles;
import org.sistcoop.persona.admin.client.resource.AccionistaResource;
import org.sistcoop.persona.models.AccionistaModel;
import org.sistcoop.persona.models.AccionistaProvider;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.representations.idm.AccionistaRepresentation;

@Stateless
@SecurityDomain("keycloak")
public class AccionistaResourceImpl implements AccionistaResource {
	
	@Inject
	protected AccionistaProvider accionistaProvider;

	@RolesAllowed(Roles.ver_personas)
	@Override
	public AccionistaRepresentation findById(Long id) {
		AccionistaModel model = accionistaProvider.getAccionistaById(id);
		AccionistaRepresentation rep = ModelToRepresentation.toRepresentation(model);
		return rep;
	}

	@RolesAllowed(Roles.administrar_personas)
	@Override
	public void updateAccionista(Long id, AccionistaRepresentation accionistaRepresentation) {
		AccionistaModel accionistaModel = accionistaProvider.getAccionistaById(id);
		if (accionistaModel == null) {
			throw new NotFoundException("Accionista not found.");
		}
		accionistaModel.setPorcentajeParticipacion(accionistaRepresentation.getPorcentajeParticipacion());
		accionistaModel.commit();
	}

	@RolesAllowed(Roles.administrar_personas)
	@Override
	public void remove(Long id) {
		AccionistaModel accionistaModel = accionistaProvider.getAccionistaById(id);
		boolean result = accionistaProvider.removeAccionista(accionistaModel);
		if (!result) {
			throw new InternalServerErrorException("No se pudo eliminar el elemento");
		}
	}

}
