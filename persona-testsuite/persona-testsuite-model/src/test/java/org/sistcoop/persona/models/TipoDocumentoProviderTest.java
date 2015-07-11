package org.sistcoop.persona.models;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sistcoop.persona.models.enums.TipoPersona;

@RunWith(Arquillian.class)
@UsingDataSet("empty.xml")
public class TipoDocumentoProviderTest extends AbstractTest {

    @Inject
    private TipoDocumentoProvider tipoDocumentoProvider;

    @Test
    public void create() {
        TipoDocumentoModel model = tipoDocumentoProvider.create("DNI", "Documento nacional de identidad", 8,
                TipoPersona.NATURAL);

        assertThat("model no debe ser null", model, is(notNullValue()));
        assertThat("estado debe ser true", model.getEstado(), is(true));
    }

    @Test
    public void findByAbreviatura() {
        TipoDocumentoModel model1 = tipoDocumentoProvider.create("DNI", "Documento nacional de identidad", 8,
                TipoPersona.NATURAL);

        String abreviatura = model1.getAbreviatura();
        TipoDocumentoModel model2 = tipoDocumentoProvider.findByAbreviatura(abreviatura);

        assertThat("model1 debe ser igual a model2", model1, is(equalTo(model2)));
    }

    @Test
    public void removeTipoDocumento() {
        TipoDocumentoModel model1 = tipoDocumentoProvider.create("DNI", "Documento nacional de identidad", 8,
                TipoPersona.NATURAL);

        String abreviatura = model1.getAbreviatura();
        boolean result = tipoDocumentoProvider.remove(model1);

        TipoDocumentoModel model2 = tipoDocumentoProvider.findByAbreviatura(abreviatura);

        assertThat(result, is(true));
        assertThat(model2, is(nullValue()));
    }

}
