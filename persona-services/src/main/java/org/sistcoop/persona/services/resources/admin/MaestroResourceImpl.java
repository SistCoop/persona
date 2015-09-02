package org.sistcoop.persona.services.resources.admin;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.sistcoop.persona.admin.client.resource.MaestroResource;
import org.sistcoop.persona.models.enums.EstadoCivil;
import org.sistcoop.persona.models.enums.Sexo;
import org.sistcoop.persona.models.enums.TipoEmpresa;
import org.sistcoop.persona.models.enums.TipoPersona;

@Stateless
public class MaestroResourceImpl implements MaestroResource {

    @Override
    public List<String> getAllTipoPersonas() {
        TipoPersona[] enums = TipoPersona.values();

        List<String> representations = new ArrayList<>();
        for (int i = 0; i < enums.length; i++) {
            representations.add(enums[i].toString());
        }
        return representations;
    }

    @Override
    public List<String> getAllEstadosCiviles() {
        EstadoCivil[] enums = EstadoCivil.values();

        List<String> representations = new ArrayList<>();
        for (int i = 0; i < enums.length; i++) {
            representations.add(enums[i].toString());
        }
        return representations;
    }

    @Override
    public List<String> getAllSexos() {
        Sexo[] enums = Sexo.values();

        List<String> representations = new ArrayList<>();
        for (int i = 0; i < enums.length; i++) {
            representations.add(enums[i].toString());
        }
        return representations;
    }

    @Override
    public List<String> getAllTiposEmpresa() {
        TipoEmpresa[] enums = TipoEmpresa.values();

        List<String> representations = new ArrayList<>();
        for (int i = 0; i < enums.length; i++) {
            representations.add(enums[i].toString());
        }
        return representations;
    }

}
