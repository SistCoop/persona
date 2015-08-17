package org.sistcoop.persona.models.jpa.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;
import org.sistcoop.persona.models.enums.TipoEmpresa;

@Audited
@Cacheable
@Entity
@Table(name = "PERSONA_JURIDICA")
@NamedQueries(value = {
        @NamedQuery(name = "PersonaJuridicaEntity.findAll", query = "SELECT p FROM PersonaJuridicaEntity p"),
        @NamedQuery(name = "PersonaJuridicaEntity.findByTipoNumeroDocumento", query = "SELECT p FROM PersonaJuridicaEntity p WHERE p.tipoDocumento.abreviatura = :tipoDocumento AND p.numeroDocumento = :numeroDocumento") })
public class PersonaJuridicaEntity extends PersonaEntity implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String id;
    private String razonSocial;
    private String nombreComercial;
    private Date fechaConstitucion;
    private String actividadPrincipal;
    private TipoEmpresa tipoEmpresa;
    private boolean finLucro;

    private PersonaNaturalEntity representanteLegal;
    private Set<AccionistaEntity> accionistas = new HashSet<AccionistaEntity>(0);

    public PersonaJuridicaEntity() {
        super();
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull
    @Size(min = 1, max = 70)
    @NotBlank
    @Column(name = "RAZON_SOCIAL")
    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    @Size(min = 0, max = 50)
    @Column(name = "NOMBRE_COMERCIAL")
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
    @Column(name = "ACTIVIDAD_PRINCIPAL")
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((numeroDocumento == null) ? 0 : numeroDocumento.hashCode());
        result = prime * result + ((tipoDocumento == null) ? 0 : tipoDocumento.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof PersonaJuridicaEntity))
            return false;
        PersonaJuridicaEntity other = (PersonaJuridicaEntity) obj;
        if (numeroDocumento == null) {
            if (other.numeroDocumento != null)
                return false;
        } else if (!numeroDocumento.equals(other.numeroDocumento))
            return false;
        if (tipoDocumento == null) {
            if (other.tipoDocumento != null)
                return false;
        } else if (!tipoDocumento.equals(other.tipoDocumento))
            return false;
        return true;
    }

}
