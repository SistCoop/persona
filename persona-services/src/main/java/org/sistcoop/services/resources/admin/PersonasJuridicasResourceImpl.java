package org.sistcoop.services.resources.admin;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sistcoop.admin.client.resource.PersonasJuridicasResource;
import org.sistcoop.models.PersonaJuridicaModel;
import org.sistcoop.models.PersonaJuridicaProvider;
import org.sistcoop.models.utils.ModelToRepresentation;
import org.sistcoop.representations.idm.PersonaJuridicaRepresentation;

@Stateless
public class PersonasJuridicasResourceImpl implements PersonasJuridicasResource {

	@Inject
	protected PersonaJuridicaProvider personaJuridicaProvider;

	@Override
	public List<PersonaJuridicaRepresentation> findAll(String filterText, Integer firstResult, Integer maxResults) {
		filterText = (filterText == null ? "" : filterText);
		firstResult = (firstResult == null ? -1 : firstResult);
		maxResults = (maxResults == null ? -1 : maxResults);

		List<PersonaJuridicaModel> list = personaJuridicaProvider.searchForFilterText(filterText, firstResult, maxResults);
		List<PersonaJuridicaRepresentation> result = new ArrayList<PersonaJuridicaRepresentation>();
		for (PersonaJuridicaModel model : list) {
			result.add(ModelToRepresentation.toRepresentation(model));
		}
		return result;
	}

	@Override
	public long countAll() {
		Long count = personaJuridicaProvider.getPersonasJuridicasCount();
		return count;
	}

}
