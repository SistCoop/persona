package org.sistcoop.persona.models.jpa.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.sistcoop.persona.models.enums.TipoEmpresa;

@Entity
@Table(name = "PERSONA_JURIDICA", indexes = { @Index(columnList = "id") }, uniqueConstraints = { @UniqueConstraint(columnNames = { "TIPO_DOCUMENTO", "NUMERO_DOCUMENTO" }) })
@NamedQueries({ 
	@NamedQuery(name = PersonaJuridicaEntity.findAll, query = "SELECT p FROM PersonaJuridicaEntity p"), 
	@NamedQuery(name = PersonaJuridicaEntity.findByNumeroDocumento, query = "SELECT p FROM PersonaJuridicaEntity p WHERE p.numeroDocumento like :numeroDocumento "), @NamedQuery(name = PersonaJuridicaEntity.findByTipoAndNumeroDocumento, query = "SELECT p FROM PersonaJuridicaEntity p WHERE p.tipoDocumento.abreviatura = :tipoDocumento AND p.numeroDocumento = :numeroDocumento"),		
	@NamedQuery(name = PersonaJuridicaEntity.findByFilterText, query = "SELECT p FROM PersonaJuridicaEntity p WHERE p.numeroDocumento like :filtertext OR UPPER(p.razonSocial) LIKE :filtertext"), @NamedQuery(name = PersonaJuridicaEntity.count, query = "select count(u) from PersonaJuridicaEntity u") })
public class PersonaJuridicaEntity extends PersonaEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String base = "org.softgreen.persona.entity.PersonaJuridica";
	public final static String findAll = base + "FindAll";
	public final static String findByNumeroDocumento = base + "findByNumeroDocumento";
	public final static String findByTipoAndNumeroDocumento = base + "FindByTipoAndNumeroDocumento";
	public final static String findByFilterText = base + "FindByFilterText";
	public final static String count = base + "count";

	private Long id;
	private String razonSocial;
	private String nombreComercial;
	private Date fechaConstitucion;
	private String actividadPrincipal;
	private TipoEmpresa tipoEmpresa;
	private boolean finLucro;

	private PersonaNaturalEntity representanteLegal;
	private Set<AccionistaEntity> accionistas = new HashSet<AccionistaEntity>();

	public PersonaJuridicaEntity() {
		super();
	}

	public PersonaJuridicaEntity(Long id) {
		super();
		this.id = id;
	}

	public PersonaJuridicaEntity(TipoDocumentoEntity tipoDocumento, String numeroDocumento) {
		super(tipoDocumento, numeroDocumento);
	}

	@Id
	@GeneratedValue(generator = "SgGenericGenerator")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	@Size(min = 1, max = 70)
	@NotEmpty
	@NotBlank
	@Column(name = "RAZON_SOCIAL")
	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	@Size(min = 0, max = 50)
	@Column(name = "NOMBRE_COMERCIAL", nullable = true)
	public String getNombreComercial() {
		return nombreComercial;
	}

	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}

	@NotNull
	@Past
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_CONSTITUCION")
	public Date getFechaConstitucion() {
		return fechaConstitucion;
	}

	public void setFechaConstitucion(Date fechaConstitucion) {
		this.fechaConstitucion = fechaConstitucion;
	}

	@Size(min = 0, max = 70)
	@Column(name = "ACTIVIDAD_PRINCIPAL", nullable = true)
	public String getActividadPrincipal() {
		return actividadPrincipal;
	}

	public void setActividadPrincipal(String actividadPrincipal) {
		this.actividadPrincipal = actividadPrincipal;
	}

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_EMPRESA")
	public TipoEmpresa getTipoEmpresa() {
		return tipoEmpresa;
	}

	public void setTipoEmpresa(TipoEmpresa tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}

	@NotNull
	@Type(type = "org.hibernate.type.TrueFalseType")
	@Column(name = "FIN_LUCRO")
	public boolean isFinLucro() {
		return finLucro;
	}

	public void setFinLucro(boolean finLucro) {
		this.finLucro = finLucro;
	}

	@NotNull
	@OneToOne
	@JoinColumn(name = "REPRESENTANTE_LEGAL", foreignKey = @ForeignKey)
	public PersonaNaturalEntity getRepresentanteLegal() {
		return representanteLegal;
	}

	public void setRepresentanteLegal(PersonaNaturalEntity representanteLegal) {
		this.representanteLegal = representanteLegal;
	}

	@OneToMany(mappedBy = "personaJuridica", fetch = FetchType.LAZY)
	public Set<AccionistaEntity> getAccionistas() {
		return accionistas;
	}

	public void setAccionistas(Set<AccionistaEntity> accionistas) {
		this.accionistas = accionistas;
	}

}
