package org.sistcoop.persona.services.resources.admin;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import org.sistcoop.persona.admin.client.resource.AccionistaResource;
import org.sistcoop.persona.models.AccionistaModel;
import org.sistcoop.persona.models.AccionistaProvider;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.representations.idm.AccionistaRepresentation;

@Stateless
public class AccionistaResourceImpl implements AccionistaResource {
	@Inject
	protected AccionistaProvider accionistaProvider;

	@Override
	public AccionistaRepresentation findById(Long id) {
		AccionistaModel model = accionistaProvider.getAccionistaById(id);
		AccionistaRepresentation rep = ModelToRepresentation.toRepresentation(model);
		return rep;
	}

	@Override
	public void updateAccionista(Long id, AccionistaRepresentation accionistaRepresentation) {
		AccionistaModel accionistaModel = accionistaProvider.getAccionistaById(id);
		if (accionistaModel == null) {
			throw new NotFoundException("Accionista not found.");
		}
		accionistaModel.setPorcentajeParticipacion(accionistaRepresentation.getPorcentajeParticipacion());
		accionistaModel.commit();
	}

	@Override
	public void remove(Long id) {
		AccionistaModel accionistaModel = accionistaProvider.getAccionistaById(id);
		boolean result = accionistaProvider.removeAccionista(accionistaModel);
		if (!result) {
			throw new InternalServerErrorException("No se pudo eliminar el elemento");
		}
	}

}
