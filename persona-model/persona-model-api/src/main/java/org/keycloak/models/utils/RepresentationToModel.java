package org.keycloak.models.utils;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.keycloak.models.PersonaJuridicaModel;
import org.keycloak.models.PersonaJuridicaProvider;
import org.keycloak.models.PersonaNaturalModel;
import org.keycloak.models.PersonaNaturalProvider;
import org.keycloak.models.TipoDocumentoModel;
import org.keycloak.models.TipoDocumentoProvider;
import org.keycloak.models.enums.EstadoCivil;
import org.keycloak.models.enums.Sexo;
import org.keycloak.models.enums.TipoEmpresa;
import org.keycloak.models.enums.TipoPersona;
import org.keycloak.representations.idm.PersonaJuridicaRepresentation;
import org.keycloak.representations.idm.PersonaNaturalRepresentation;
import org.keycloak.representations.idm.TipoDocumentoRepresentation;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RepresentationToModel {
		
	public TipoDocumentoModel createTipoDocumento(TipoDocumentoRepresentation rep, TipoDocumentoProvider provider) {
		TipoDocumentoModel model = provider.addTipoDocumento(
				rep.getAbreviatura(), 
				rep.getDenominacion(), 
				rep.getCantidadCaracteres(), 
				TipoPersona.valueOf(rep.getTipoPersona()));	
		return model;
	}
		
	public PersonaNaturalModel createPersonaNatural(
			PersonaNaturalRepresentation rep, 
			TipoDocumentoModel tipoDocumentoModel, 
			PersonaNaturalProvider personaNaturalProvider) {		

		PersonaNaturalModel model = personaNaturalProvider.addPersonaNatural(
				rep.getCodigoPais(), 
				tipoDocumentoModel, 
				rep.getNumeroDocumento(), 
				rep.getApellidoPaterno(), 
				rep.getApellidoMaterno(), 
				rep.getNombres(), 
				rep.getFechaNacimiento(), 
				Sexo.valueOf(rep.getSexo().toUpperCase()));		

		model.setEstadoCivil(rep.getEstadoCivil() != null ? EstadoCivil.valueOf(rep.getEstadoCivil().toUpperCase()) : null);		
		model.setOcupacion(rep.getOcupacion());
		model.setUrlFoto(rep.getUrlFoto());
		model.setUrlFirma(rep.getUrlFirma());
		model.setUbigeo(rep.getUbigeo());
		model.setDireccion(rep.getDireccion());
		model.setReferencia(rep.getReferencia());
		model.setTelefono(rep.getTelefono());
		model.setCelular(rep.getCelular());
		model.setEmail(rep.getEmail());

		model.commit();
		return model;
	}
	
	public PersonaJuridicaModel createPersonaJuridica(
			PersonaJuridicaRepresentation rep, 			
			TipoDocumentoModel tipoDocumentoModel, 
			PersonaNaturalModel representanteLegal, 
			PersonaJuridicaProvider personaJuridicaProvider) {

		PersonaJuridicaModel model = personaJuridicaProvider.addPersonaJuridica(
				representanteLegal, 
				rep.getCodigoPais(), 
				tipoDocumentoModel, 
				rep.getNumeroDocumento(), 
				rep.getRazonSocial(), 
				rep.getFechaConstitucion(), 
				TipoEmpresa.valueOf(rep.getTipoEmpresa().toUpperCase()),
				rep.isFinLucro());
		
		model.setActividadPrincipal(rep.getActividadPrincipal());
		model.setNombreComercial(rep.getNombreComercial());
		
		model.setUbigeo(rep.getUbigeo());
		model.setDireccion(rep.getDireccion());
		model.setReferencia(rep.getReferencia());
		model.setTelefono(rep.getTelefono());
		model.setCelular(rep.getCelular());
		model.setEmail(rep.getEmail());

		model.commit();		
		return model;
	}

}
