package org.sistcoop.persona.services.managers;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.sistcoop.persona.models.PersonaJuridicaModel;
import org.sistcoop.persona.models.enums.TipoEmpresa;
import org.sistcoop.persona.representations.idm.PersonaJuridicaRepresentation;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PersonaJuridicaManager {

    public void update(PersonaJuridicaModel model, PersonaJuridicaRepresentation rep) {
        model.setCodigoPais(rep.getCodigoPais());
        model.setRazonSocial(rep.getRazonSocial());
        model.setFechaConstitucion(rep.getFechaConstitucion());
        model.setActividadPrincipal(rep.getActividadPrincipal());
        model.setNombreComercial(rep.getNombreComercial());
        model.setFinLucro(rep.isFinLucro());
        model.setTipoEmpresa(TipoEmpresa.valueOf(rep.getTipoEmpresa()));

        model.setUbigeo(rep.getUbigeo());
        model.setDireccion(rep.getDireccion());
        model.setReferencia(rep.getReferencia());
        model.setTelefono(rep.getTelefono());
        model.setCelular(rep.getCelular());
        model.setEmail(rep.getEmail());

        model.commit();
    }

}