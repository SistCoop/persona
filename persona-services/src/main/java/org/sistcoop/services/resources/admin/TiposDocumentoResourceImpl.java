package org.sistcoop.services.resources.admin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.sistcoop.admin.client.resource.TiposDocumentoResource;
import org.sistcoop.models.TipoDocumentoModel;
import org.sistcoop.models.TipoDocumentoProvider;
import org.sistcoop.models.enums.TipoPersona;
import org.sistcoop.models.utils.ModelToRepresentation;
import org.sistcoop.representations.idm.TipoDocumentoRepresentation;

public class TiposDocumentoResourceImpl implements TiposDocumentoResource {

	@Inject
	protected TipoDocumentoProvider tipoDocumentoProvider;

	@Override
	public List<TipoDocumentoRepresentation> findAll(String tipoPersona, Boolean estado) {
		List<TipoDocumentoModel> list = null;
		if (tipoPersona != null) {
			TipoPersona personType = TipoPersona.valueOf(tipoPersona.toUpperCase());
			if (personType != null) {
				list = tipoDocumentoProvider.getTiposDocumento(personType);
			} else {
				list = tipoDocumentoProvider.getTiposDocumento();
			}
		} else {
			list = tipoDocumentoProvider.getTiposDocumento();
		}

		List<TipoDocumentoRepresentation> result = new ArrayList<TipoDocumentoRepresentation>();
		for (TipoDocumentoModel model : list) {
			result.add(ModelToRepresentation.toRepresentation(model));
		}

		return result;
	}

}
