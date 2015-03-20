package org.sistcoop.persona.models.jpa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.sistcoop.persona.models.AccionistaModel;
import org.sistcoop.persona.models.PersonaJuridicaModel;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.enums.TipoEmpresa;
import org.sistcoop.persona.models.jpa.entities.AccionistaEntity;
import org.sistcoop.persona.models.jpa.entities.PersonaJuridicaEntity;
import org.sistcoop.persona.models.jpa.entities.PersonaNaturalEntity;
import org.sistcoop.persona.models.jpa.entities.TipoDocumentoEntity;

public class PersonaJuridicaAdapter implements PersonaJuridicaModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected PersonaJuridicaEntity personaJuridicaEntity;
	protected EntityManager em;

	public PersonaJuridicaAdapter(EntityManager em, PersonaJuridicaEntity personaJuridicaEntity) {
		this.em = em;
		this.personaJuridicaEntity = personaJuridicaEntity;
	}

	public PersonaJuridicaEntity getPersonaJuridicaEntity() {
		return personaJuridicaEntity;
	}

	@Override
	public Long getId() {
		return personaJuridicaEntity.getId();
	}

	@Override
	public PersonaNaturalModel getRepresentanteLegal() {
		return new PersonaNaturalAdapter(em, personaJuridicaEntity.getRepresentanteLegal());
	}

	@Override
	public void setRepresentanteLegal(PersonaNaturalModel representanteLegal) {
		PersonaNaturalEntity personaNaturalEntity = PersonaNaturalAdapter.toPersonaNaturalEntity(representanteLegal, em);
		personaJuridicaEntity.setRepresentanteLegal(personaNaturalEntity);
	}

	@Override
	public AccionistaModel addAccionista(PersonaNaturalModel personaNaturalModel, BigDecimal porcentajeParticipacion) {
		PersonaNaturalEntity personaNaturalEntity = PersonaNaturalAdapter.toPersonaNaturalEntity(personaNaturalModel, em);

		AccionistaEntity accionistaEntity = new AccionistaEntity();
		accionistaEntity.setPersonaNatural(personaNaturalEntity);
		accionistaEntity.setPersonaJuridica(personaJuridicaEntity);
		accionistaEntity.setPorcentajeParticipacion(porcentajeParticipacion);

		em.persist(accionistaEntity);
		return new AccionistaAdapter(em, accionistaEntity);
	}

	@Override
	public boolean removeAccionista(AccionistaModel accionistaModel) {
		AccionistaEntity accionistaEntity = AccionistaAdapter.toAccionistaEntity(accionistaModel, em);
		accionistaEntity.setPersonaJuridica(personaJuridicaEntity);
		em.remove(accionistaEntity);
		return true;
	}

	@Override
	public List<AccionistaModel> getAccionistas() {
		Set<AccionistaEntity> list = personaJuridicaEntity.getAccionistas();
		List<AccionistaModel> result = new ArrayList<AccionistaModel>();
		for (AccionistaEntity entity : list) {
			result.add(new AccionistaAdapter(em, entity));
		}
		return result;
	}

	@Override
	public String getCodigoPais() {
		return personaJuridicaEntity.getCodigoPais();
	}

	@Override
	public void setCodigoPais(String codigoPais) {
		personaJuridicaEntity.setCodigoPais(codigoPais);
	}

	@Override
	public TipoDocumentoModel getTipoDocumento() {
		return new TipoDocumentoAdapter(em, personaJuridicaEntity.getTipoDocumento());
	}

	@Override
	public void setTipoDocumento(TipoDocumentoModel tipoDocumento) {
		TipoDocumentoEntity tipoDocumentoEntity = TipoDocumentoAdapter.toTipoDocumentoEntity(tipoDocumento, em);
		personaJuridicaEntity.setTipoDocumento(tipoDocumentoEntity);
	}

	@Override
	public String getNumeroDocumento() {
		return personaJuridicaEntity.getNumeroDocumento();
	}

	@Override
	public void setNumeroDocumento(String numeroDocumento) {
		personaJuridicaEntity.setNumeroDocumento(numeroDocumento);
	}

	@Override
	public String getRazonSocial() {
		return personaJuridicaEntity.getRazonSocial();
	}

	@Override
	public void setRazonSocial(String razonSocial) {
		personaJuridicaEntity.setRazonSocial(razonSocial);
	}

	@Override
	public String getNombreComercial() {
		return personaJuridicaEntity.getNombreComercial();
	}

	@Override
	public void setNombreComercial(String nombreComercial) {
		personaJuridicaEntity.setNombreComercial(nombreComercial);
	}

	@Override
	public Date getFechaConstitucion() {
		return personaJuridicaEntity.getFechaConstitucion();
	}

	@Override
	public void setFechaConstitucion(Date fechaConstitucion) {
		personaJuridicaEntity.setFechaConstitucion(fechaConstitucion);
	}

	@Override
	public String getActividadPrincipal() {
		return personaJuridicaEntity.getActividadPrincipal();
	}

	@Override
	public void setActividadPrincipal(String actividadPrincipal) {
		personaJuridicaEntity.setActividadPrincipal(actividadPrincipal);
	}

	@Override
	public TipoEmpresa getTipoEmpresa() {
		return personaJuridicaEntity.getTipoEmpresa();
	}

	@Override
	public void setTipoEmpresa(TipoEmpresa tipoEmpresa) {
		personaJuridicaEntity.setTipoEmpresa(tipoEmpresa);
	}

	@Override
	public boolean isFinLucro() {
		return personaJuridicaEntity.isFinLucro();
	}

	@Override
	public void setFinLucro(boolean finLucro) {
		personaJuridicaEntity.setFinLucro(finLucro);
	}

	@Override
	public String getUbigeo() {
		return personaJuridicaEntity.getUbigeo();
	}

	@Override
	public void setUbigeo(String ubigeo) {
		personaJuridicaEntity.setUbigeo(ubigeo);
	}

	@Override
	public String getDireccion() {
		return personaJuridicaEntity.getDireccion();
	}

	@Override
	public void setDireccion(String direccion) {
		personaJuridicaEntity.setDireccion(direccion);
	}

	@Override
	public String getReferencia() {
		return personaJuridicaEntity.getReferencia();
	}

	@Override
	public void setReferencia(String referencia) {
		personaJuridicaEntity.setReferencia(referencia);
	}

	@Override
	public String getTelefono() {
		return personaJuridicaEntity.getTelefono();
	}

	@Override
	public void setTelefono(String telefono) {
		personaJuridicaEntity.setTelefono(telefono);
	}

	@Override
	public String getCelular() {
		return personaJuridicaEntity.getCelular();
	}

	@Override
	public void setCelular(String celular) {
		personaJuridicaEntity.setCelular(celular);
	}

	@Override
	public String getEmail() {
		return personaJuridicaEntity.getEmail();
	}

	@Override
	public void setEmail(String email) {
		personaJuridicaEntity.setEmail(email);
	}

	public static PersonaJuridicaEntity toPersonaJuridicaEntity(PersonaJuridicaModel model, EntityManager em) {
		if (model instanceof PersonaJuridicaAdapter) {
			return ((PersonaJuridicaAdapter) model).getPersonaJuridicaEntity();
		}
		return em.getReference(PersonaJuridicaEntity.class, model.getId());
	}

	@Override
	public void commit() {
		em.merge(personaJuridicaEntity);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof PersonaJuridicaModel))
			return false;

		PersonaJuridicaModel that = (PersonaJuridicaModel) o;
		return that.getId().equals(getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}
}
