package org.sistcoop.persona.models;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import org.sistcoop.persona.models.enums.Sexo;
import org.sistcoop.persona.provider.Provider;

@Local
public interface PersonaNaturalProvider extends Provider {

	PersonaNaturalModel addPersonaNatural(
			String codigoPais,
			TipoDocumentoModel tipoDocumentoModel,
			String numeroDocumento,	
			String apellidoPaterno,
			String apellidoMaterno,
			String nombres,
			Date fechaNacimiento,
			Sexo sexo);

	boolean removePersonaNatural(PersonaNaturalModel personaNaturalModel);

	PersonaNaturalModel getPersonaNaturalById(Long id);

	PersonaNaturalModel getPersonaNaturalByTipoNumeroDoc(TipoDocumentoModel tipoDocumento, String numeroDocumento);

	List<PersonaNaturalModel> getPersonasNaturales();

	long getPersonasNaturalesCount();

	List<PersonaNaturalModel> getPersonasNaturales(int firstResult, int maxResults);

	List<PersonaNaturalModel> searchForNumeroDocumento(String numeroDocumento);

	List<PersonaNaturalModel> searchForNumeroDocumento(String numeroDocumento, int firstResult, int maxResults);

	List<PersonaNaturalModel> searchForFilterText(String filterText);

	List<PersonaNaturalModel> searchForFilterText(String filterText, int firstResult, int maxResults);

}
