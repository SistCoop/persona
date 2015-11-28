package org.sistcoop.persona.models.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author <a href="mailto:carlosthe19916@sistcoop.com">Carlos Feria</a>
 */

@MappedSuperclass
public abstract class PersonaEntity implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIPO_DOCUMENTO_ID", foreignKey = @ForeignKey)
    protected TipoDocumentoEntity tipoDocumento;

    @NotNull
    @Size(min = 1, max = 20)
    @NotBlank
    @Column(name = "NUMERO_DOCUMENTO")
    protected String numeroDocumento;

    // nacionalidad
    @NotNull
    @Size(min = 3, max = 3)
    @NotBlank
    @Column(name = "CODIGO_PAIS")
    protected String codigoPais;

    @Size(min = 0, max = 6)
    @Column(name = "UBIGEO")
    protected String ubigeo;

    @Size(min = 0, max = 100)
    @Column(name = "DIRECCION")
    protected String direccion;

    @Size(min = 0, max = 70)
    @Column(name = "REFERENCIA")
    protected String referencia;

    @Size(min = 0, max = 20)
    @Column(name = "TELEFONO")
    protected String telefono;

    @Size(min = 0, max = 20)
    @Column(name = "CELULAR")
    protected String celular;

    @Email
    @Column(name = "EMAIL")
    protected String email;

    @Version
    protected Integer optlk;

    public PersonaEntity() {
        // TODO Auto-generated constructor stub
    }

    public PersonaEntity(TipoDocumentoEntity tipoDocumento, String numeroDocumento) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
    }

    public TipoDocumentoEntity getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumentoEntity tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getOptlk() {
        return optlk;
    }

    public void setOptlk(Integer optlk) {
        this.optlk = optlk;
    }

    @Override
    public String toString() {
        return "(PersonaEntity tipoDocumento=" + this.tipoDocumento.getAbreviatura() + " numeroDocumento="
                + this.numeroDocumento + ")";
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
        if (!(obj instanceof PersonaEntity))
            return false;
        PersonaEntity other = (PersonaEntity) obj;
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
