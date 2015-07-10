package org.sistcoop.persona.services.managers;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.representations.idm.TipoDocumentoRepresentation;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TipoDocumentoManager {

    public void update(TipoDocumentoModel tipoDocumentoModel, TipoDocumentoRepresentation representation) {
        // TODO Auto-generated method stub
        
    }

    public void disable(TipoDocumentoModel tipoDocumentoModel) {
        // TODO Auto-generated method stub
        
    }

}