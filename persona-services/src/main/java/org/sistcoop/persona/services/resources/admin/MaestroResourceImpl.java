package org.sistcoop.persona.services.resources.admin;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.sistcoop.persona.admin.client.resource.MaestroResource;
import org.sistcoop.persona.models.enums.EstadoCivil;
import org.sistcoop.persona.models.enums.Sexo;
import org.sistcoop.persona.models.enums.TipoEmpresa;
import org.sistcoop.persona.models.enums.TipoPersona;
import org.sistcoop.producto.representations.idm.search.SearchResultsRepresentation;

@Stateless
public class MaestroResourceImpl implements MaestroResource {

    @Override
    public SearchResultsRepresentation<String> getTipoPersonas() {
        TipoPersona[] enums = TipoPersona.values();

        SearchResultsRepresentation<String> rep = new SearchResultsRepresentation<>();
        List<String> representations = new ArrayList<>();
        for (int i = 0; i < enums.length; i++) {
            representations.add(enums[i].toString());
        }
        rep.setTotalSize(representations.size());
        rep.setItems(representations);
        return rep;
    }

    @Override
    public SearchResultsRepresentation<String> getEstadosCiviles() {
        EstadoCivil[] enums = EstadoCivil.values();

        SearchResultsRepresentation<String> rep = new SearchResultsRepresentation<>();
        List<String> representations = new ArrayList<>();
        for (int i = 0; i < enums.length; i++) {
            representations.add(enums[i].toString());
        }
        rep.setTotalSize(representations.size());
        rep.setItems(representations);
        return rep;
    }

    @Override
    public SearchResultsRepresentation<String> getSexos() {
        Sexo[] enums = Sexo.values();

        SearchResultsRepresentation<String> rep = new SearchResultsRepresentation<>();
        List<String> representations = new ArrayList<>();
        for (int i = 0; i < enums.length; i++) {
            representations.add(enums[i].toString());
        }
        rep.setTotalSize(representations.size());
        rep.setItems(representations);
        return rep;
    }

    @Override
    public SearchResultsRepresentation<String> getTiposEmpresa() {
        TipoEmpresa[] enums = TipoEmpresa.values();

        SearchResultsRepresentation<String> rep = new SearchResultsRepresentation<>();
        List<String> representations = new ArrayList<>();
        for (int i = 0; i < enums.length; i++) {
            representations.add(enums[i].toString());
        }
        rep.setTotalSize(representations.size());
        rep.setItems(representations);
        return rep;
    }

}
