package org.sistcoop.persona.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.sistcoop.persona.models.enums.TipoEmpresa;

public interface PersonaJuridicaModel extends Model {

	Long getId();

	PersonaNaturalModel getRepresentanteLegal();

	void setRepresentanteLegal(PersonaNaturalModel representanteLegal);

	AccionistaModel addAccionista(PersonaNaturalModel personaNaturalModel, BigDecimal porcentajeParticipacion);

	boolean removeAccionista(AccionistaModel accionistaModel);

	List<AccionistaModel> getAccionistas();

	String getCodigoPais();

	void setCodigoPais(String codigoPais);

	TipoDocumentoModel getTipoDocumento();

	void setTipoDocumento(TipoDocumentoModel tipoDocumento);

	String getNumeroDocumento();

	void setNumeroDocumento(String numeroDocumento);

	String getRazonSocial();

	void setRazonSocial(String razonSocial);

	String getNombreComercial();

	void setNombreComercial(String nombreComercial);

	Date getFechaConstitucion();

	void setFechaConstitucion(Date fechaConstitucion);

	String getActividadPrincipal();

	void setActividadPrincipal(String actividadPrincipal);

	TipoEmpresa getTipoEmpresa();

	void setTipoEmpresa(TipoEmpresa tipoEmpresa);

	boolean isFinLucro();

	void setFinLucro(boolean finLucro);

	String getUbigeo();

	void setUbigeo(String ubigeo);

	String getDireccion();

	void setDireccion(String direccion);

	String getReferencia();

	void setReferencia(String referencia);

	String getTelefono();

	void setTelefono(String telefono);

	String getCelular();

	void setCelular(String celular);

	String getEmail();

	void setEmail(String email);

}