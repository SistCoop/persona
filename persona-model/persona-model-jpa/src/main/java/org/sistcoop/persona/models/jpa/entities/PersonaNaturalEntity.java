package org.sistcoop.persona.models.jpa.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.sistcoop.persona.models.enums.EstadoCivil;
import org.sistcoop.persona.models.enums.Sexo;

@Audited
@Cacheable
@Entity
@Table(name = "PERSONA_NATURAL")
@NamedQueries(value = {
        @NamedQuery(name = "PersonaNaturalEntity.findAll", query = "SELECT p FROM PersonaNaturalEntity p"),
        @NamedQuery(name = "PersonaNaturalEntity.findByTipoNumeroDocumento", query = "SELECT p FROM PersonaNaturalEntity p WHERE p.tipoDocumento.abreviatura = :tipoDocumento AND p.numeroDocumento = :numeroDocumento") })
public class PersonaNaturalEntity extends PersonaEntity implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String id;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombres;
    private Date fechaNacimiento;
    private Sexo sexo;
    private EstadoCivil estadoCivil;
    private String ocupacion;
    private String urlFoto;
    private String urlFirma;

    public PersonaNaturalEntity() {
        super();
    }

    public PersonaNaturalEntity(String id) {
        super();
        this.id = id;
    }

    public PersonaNaturalEntity(TipoDocumentoEntity tipoDocumento, String numeroDocumento) {
        super(tipoDocumento, numeroDocumento);
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
    @Size(min = 1, max = 50)
    @NotBlank
    
    @Column(name = "APELLIDO_PATERNO")
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    @NotNull
    @Size(min = 1, max = 50)
    @NotBlank
    
    @Column(name = "APELLIDO_MATERNO")
    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    @NotNull
    @Size(min = 1, max = 70)
    @NotBlank
    
    @Column(name = "NOMBRES")
    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    @NotNull
    @Past
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_NACIMIENTO")
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "SEXO")
    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_CIVIL")
    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    @Size(min = 0, max = 70)
    @Column(name = "OCUPACION")
    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    @URL
    @Column(name = "URL_FOTO")
    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    @URL
    @Column(name = "URL_FIRMA")
    public String getUrlFirma() {
        return urlFirma;
    }

    public void setUrlFirma(String urlFirma) {
        this.urlFirma = urlFirma;
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
        if (!(obj instanceof PersonaNaturalEntity))
            return false;
        PersonaNaturalEntity other = (PersonaNaturalEntity) obj;
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
