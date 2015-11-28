package org.sistcoop.persona.models.jpa.entities;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.ManyToOne;
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

/**
 * @author <a href="mailto:carlosthe19916@sistcoop.com">Carlos Feria</a>
 */

@Audited
@Cacheable
@Entity
@Table(name = "PERSONA_NATURAL")
@NamedQueries(value = {
        @NamedQuery(name = "PersonaNaturalEntity.findAll", query = "SELECT p FROM PersonaNaturalEntity p"),
        @NamedQuery(name = "PersonaNaturalEntity.findByTipoDocumento", query = "SELECT p FROM PersonaNaturalEntity p INNER JOIN p.tipoDocumento t WHERE t.abreviatura = :tipoDocumento"),
        @NamedQuery(name = "PersonaNaturalEntity.findByTipoNumeroDocumento", query = "SELECT p FROM PersonaNaturalEntity p INNER JOIN p.tipoDocumento t WHERE t.abreviatura = :tipoDocumento AND p.numeroDocumento = :numeroDocumento"),
        @NamedQuery(name = "PersonaNaturalEntity.findByFilterText", query = "SELECT p FROM PersonaNaturalEntity p WHERE LOWER(p.numeroDocumento) LIKE :filterText OR LOWER(p.apellidoPaterno) LIKE :filterText OR LOWER(p.apellidoMaterno) LIKE :filterText OR LOWER(p.nombres) LIKE :filterText") })
public class PersonaNaturalEntity extends PersonaEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @NotNull
    @Size(min = 1, max = 50)
    @NotBlank
    @Column(name = "APELLIDO_PATERNO")
    private String apellidoPaterno;

    @NotNull
    @Size(min = 1, max = 50)
    @NotBlank
    @Column(name = "APELLIDO_MATERNO")
    private String apellidoMaterno;

    @NotNull
    @Size(min = 1, max = 70)
    @NotBlank
    @Column(name = "NOMBRES")
    private String nombres;

    @NotNull
    @Past
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_NACIMIENTO")
    private Date fechaNacimiento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "SEXO")
    private Sexo sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_CIVIL")
    private EstadoCivil estadoCivil;

    @Size(min = 0, max = 70)
    @Column(name = "OCUPACION")
    private String ocupacion;

    @URL
    @Column(name = "URL_FOTO")
    private String urlFoto;

    @URL
    @Column(name = "URL_FIRMA")
    private String urlFirma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_STORE_FOTO_ID", foreignKey = @ForeignKey )
    private FileStoreEntity foto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_STORE_FIRMA_ID", foreignKey = @ForeignKey )
    private FileStoreEntity firma;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getUrlFirma() {
        return urlFirma;
    }

    public void setUrlFirma(String urlFirma) {
        this.urlFirma = urlFirma;
    }

    public FileStoreEntity getFoto() {
        return foto;
    }

    public void setFoto(FileStoreEntity foto) {
        this.foto = foto;
    }

    public FileStoreEntity getFirma() {
        return firma;
    }

    public void setFirma(FileStoreEntity firma) {
        this.firma = firma;
    }

    @Override
    public String toString() {
        return "(PersonaNaturalEntity id=" + this.id + " tipoDocumento=" + this.tipoDocumento.getAbreviatura()
                + " numeroDocumento" + this.numeroDocumento + " apellidoPaterno=" + this.apellidoPaterno
                + " apellidoMaterno=" + this.apellidoMaterno + " nombres=" + this.nombres + ")";
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
