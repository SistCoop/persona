package org.sistcoop.persona.models.utils;

import org.sistcoop.persona.models.AccionistaModel;
import org.sistcoop.persona.models.PersonaJuridicaModel;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.representations.idm.AccionistaRepresentation;
import org.sistcoop.persona.representations.idm.PersonaJuridicaRepresentation;
import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;
import org.sistcoop.persona.representations.idm.TipoDocumentoRepresentation;

public class ModelToRepresentation {

	public static TipoDocumentoRepresentation toRepresentation(TipoDocumentoModel model) {
		if (model == null)
			return null;
		TipoDocumentoRepresentation rep = new TipoDocumentoRepresentation();

		rep.setAbreviatura(model.getAbreviatura());
		rep.setDenominacion(model.getDenominacion());
		rep.setCantidadCaracteres(model.getCantidadCaracteres());
		rep.setTipoPersona(model.getTipoPersona().toString());

		return rep;
	}

	public static PersonaNaturalRepresentation toRepresentation(PersonaNaturalModel model) {
		if (model == null)
			return null;

		PersonaNaturalRepresentation rep = new PersonaNaturalRepresentation();

		rep.setId(model.getId());

		rep.setCodigoPais(model.getCodigoPais());
		rep.setTipoDocumento(model.getTipoDocumento().getAbreviatura());
		rep.setNumeroDocumento(model.getNumeroDocumento());

		// datos personales
		rep.setApellidoPaterno(model.getApellidoPaterno());
		rep.setApellidoMaterno(model.getApellidoMaterno());
		rep.setNombres(model.getNombres());
		rep.setFechaNacimiento(model.getFechaNacimiento());
		rep.setSexo(model.getSexo() != null ? model.getSexo().toString() : null);

		rep.setEstadoCivil(model.getEstadoCivil() != null ? model.getEstadoCivil().toString() : null);
		rep.setOcupacion(model.getOcupacion());

		// direccion de residencia
		rep.setUbigeo(model.getUbigeo());
		rep.setDireccion(model.getDireccion());
		rep.setReferencia(model.getReferencia());
		rep.setTelefono(model.getTelefono());
		rep.setCelular(model.getCelular());
		rep.setEmail(model.getEmail());

		rep.setUrlFoto(model.getUrlFoto());
		rep.setUrlFirma(model.getUrlFirma());

		return rep;
	}

	public static PersonaJuridicaRepresentation toRepresentation(PersonaJuridicaModel model) {
		if (model == null)
			return null;

		PersonaJuridicaRepresentation rep = new PersonaJuridicaRepresentation();

		rep.setId(model.getId());

		rep.setCodigoPais(model.getCodigoPais());
		rep.setTipoDocumento(model.getTipoDocumento().getAbreviatura());
		rep.setNumeroDocumento(model.getNumeroDocumento());

		// datos personales
		rep.setRazonSocial(model.getRazonSocial());
		rep.setActividadPrincipal(model.getActividadPrincipal());
		rep.setFechaConstitucion(model.getFechaConstitucion());
		rep.setFinLucro(model.isFinLucro());
		rep.setNombreComercial(model.getNombreComercial());
		rep.setTipoEmpresa(model.getTipoEmpresa() != null ? model.getTipoEmpresa().toString() : null);

		// representante legal
		PersonaNaturalRepresentation representanteRep = new PersonaNaturalRepresentation();
		representanteRep.setId(model.getRepresentanteLegal().getId());
		representanteRep.setCodigoPais(model.getRepresentanteLegal().getCodigoPais());
		representanteRep.setTipoDocumento(model.getRepresentanteLegal().getTipoDocumento().getAbreviatura());
		representanteRep.setNumeroDocumento(model.getRepresentanteLegal().getNumeroDocumento());
		representanteRep.setApellidoPaterno(model.getRepresentanteLegal().getApellidoPaterno());
		representanteRep.setApellidoMaterno(model.getRepresentanteLegal().getApellidoMaterno());
		representanteRep.setNombres(model.getRepresentanteLegal().getNombres());
		representanteRep.setFechaNacimiento(model.getRepresentanteLegal().getFechaNacimiento());
		representanteRep.setSexo(model.getRepresentanteLegal().getSexo().toString());
		rep.setRepresentanteLegal(representanteRep);

		// direccion de residencia
		rep.setUbigeo(model.getUbigeo());
		rep.setDireccion(model.getDireccion());
		rep.setReferencia(model.getReferencia());
		rep.setTelefono(model.getTelefono());
		rep.setCelular(model.getCelular());
		rep.setEmail(model.getEmail());

		return rep;
	}

	public static AccionistaRepresentation toRepresentation(AccionistaModel model) {
		if (model == null)
			return null;

		AccionistaRepresentation rep = new AccionistaRepresentation();

		rep.setId(model.getId());
		rep.setPorcentajeParticipacion(model.getPorcentajeParticipacion());

		PersonaNaturalRepresentation personaNaturalRepresentation = ModelToRepresentation.toRepresentation(model.getPersonaNatural());		
		rep.setPersonaNatural(personaNaturalRepresentation);

		return rep;
	}

}
