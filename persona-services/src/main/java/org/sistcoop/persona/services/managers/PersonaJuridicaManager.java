package org.sistcoop.persona.services.managers;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.sistcoop.persona.models.PersonaJuridicaModel;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.PersonaNaturalProvider;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.enums.TipoEmpresa;
import org.sistcoop.persona.representations.idm.PersonaJuridicaRepresentation;
import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PersonaJuridicaManager {

    @Inject
    private TipoDocumentoProvider tipoDocumentoProvider;

    @Inject
    private PersonaNaturalProvider personaNaturalProvider;

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

        PersonaNaturalRepresentation representanteRep = rep.getRepresentanteLegal();
        if (representanteRep != null) {
            PersonaNaturalRepresentation representanteRepresentation = rep.getRepresentanteLegal();
            TipoDocumentoModel tipoDocumentoRepresentanteModel = tipoDocumentoProvider
                    .findByAbreviatura(representanteRepresentation.getTipoDocumento());
            PersonaNaturalModel representanteModel = personaNaturalProvider.findByTipoNumeroDocumento(
                    tipoDocumentoRepresentanteModel, representanteRepresentation.getNumeroDocumento());

            model.setRepresentanteLegal(representanteModel);
        }

        model.commit();
    }

}