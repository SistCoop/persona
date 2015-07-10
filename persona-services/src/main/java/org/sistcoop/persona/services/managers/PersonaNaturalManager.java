package org.sistcoop.persona.services.managers;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.enums.EstadoCivil;
import org.sistcoop.persona.models.enums.Sexo;
import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PersonaNaturalManager {

    public void update(PersonaNaturalModel model, PersonaNaturalRepresentation representation) {
        model.setCodigoPais(representation.getCodigoPais());
        model.setApellidoPaterno(representation.getApellidoPaterno());
        model.setApellidoMaterno(representation.getApellidoMaterno());
        model.setNombres(representation.getNombres());
        model.setFechaNacimiento(representation.getFechaNacimiento());
        model.setSexo(Sexo.valueOf(representation.getSexo().toUpperCase()));
        model.setEstadoCivil(representation.getEstadoCivil() != null ? EstadoCivil.valueOf(representation
                .getEstadoCivil().toUpperCase()) : null);

        model.setUbigeo(representation.getUbigeo());
        model.setDireccion(representation.getDireccion());
        model.setReferencia(representation.getReferencia());
        model.setOcupacion(representation.getOcupacion());
        model.setTelefono(representation.getTelefono());
        model.setCelular(representation.getCelular());
        model.setEmail(representation.getEmail());

        model.commit();
    }

}