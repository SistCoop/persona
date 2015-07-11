package org.sistcoop.persona.models;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.inject.Inject;

import org.junit.Test;
import org.sistcoop.persona.models.enums.Sexo;
import org.sistcoop.persona.models.enums.TipoEmpresa;
import org.sistcoop.persona.models.enums.TipoPersona;

public class AccionistaProviderTest extends AbstractTest {

    @Inject
    private TipoDocumentoProvider tipoDocumentoProvider;

    @Inject
    private PersonaNaturalProvider personaNaturalProvider;

    @Inject
    private PersonaJuridicaProvider personaJuridicaProvider;

    @Inject
    private AccionistaProvider accionistaProvider;

    @Test
    public void create() {
        TipoDocumentoModel tipoDocumentoModel1 = tipoDocumentoProvider.create("DNI",
                "Documento nacional de identidad", 8, TipoPersona.NATURAL);
        TipoDocumentoModel tipoDocumentoModel2 = tipoDocumentoProvider.create("RUC",
                "Registro unico de contribuyente", 11, TipoPersona.JURIDICA);

        PersonaNaturalModel representanteLegalModel = personaNaturalProvider.create("PER",
                tipoDocumentoModel1, "12345678", "Flores", "Huertas", "Jhon wilber", Calendar.getInstance()
                        .getTime(), Sexo.MASCULINO);

        PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.create(representanteLegalModel,
                "PER", tipoDocumentoModel2, "10467793549", "Softgreen S.A.C.", Calendar.getInstance()
                        .getTime(), TipoEmpresa.PRIVADA, true);

        AccionistaModel model = accionistaProvider.create(personaJuridicaModel, representanteLegalModel,
                BigDecimal.TEN);

        assertThat(model, is(notNullValue()));
        assertThat(model.getId(), is(notNullValue()));
        assertThat(model.getPersonaJuridica(), is(notNullValue()));
        assertThat(model.getPersonaJuridica(), is(equalTo(personaJuridicaModel)));
        assertThat(model.getPersonaNatural(), is(notNullValue()));
        assertThat(model.getPersonaNatural(), is(equalTo(representanteLegalModel)));
    }

    @Test
    public void findById() {
        TipoDocumentoModel tipoDocumentoModel1 = tipoDocumentoProvider.create("DNI",
                "Documento nacional de identidad", 8, TipoPersona.NATURAL);
        TipoDocumentoModel tipoDocumentoModel2 = tipoDocumentoProvider.create("RUC",
                "Registro unico de contribuyente", 11, TipoPersona.JURIDICA);

        PersonaNaturalModel representanteLegalModel = personaNaturalProvider.create("PER",
                tipoDocumentoModel1, "12345678", "Flores", "Huertas", "Jhon wilber", Calendar.getInstance()
                        .getTime(), Sexo.MASCULINO);

        PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.create(representanteLegalModel,
                "PER", tipoDocumentoModel2, "10467793549", "Softgreen S.A.C.", Calendar.getInstance()
                        .getTime(), TipoEmpresa.PRIVADA, true);

        AccionistaModel model1 = accionistaProvider.create(personaJuridicaModel, representanteLegalModel,
                BigDecimal.TEN);

        String id = model1.getId();
        AccionistaModel model2 = accionistaProvider.findById(id);

        assertThat(model1, is(equalTo(model2)));
    }

    @Test
    public void remove() {
        TipoDocumentoModel tipoDocumentoModel1 = tipoDocumentoProvider.create("DNI",
                "Documento nacional de identidad", 8, TipoPersona.NATURAL);
        TipoDocumentoModel tipoDocumentoModel2 = tipoDocumentoProvider.create("RUC",
                "Registro unico de contribuyente", 11, TipoPersona.JURIDICA);

        PersonaNaturalModel representanteLegalModel = personaNaturalProvider.create("PER",
                tipoDocumentoModel1, "12345678", "Flores", "Huertas", "Jhon wilber", Calendar.getInstance()
                        .getTime(), Sexo.MASCULINO);

        PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.create(representanteLegalModel,
                "PER", tipoDocumentoModel2, "10467793549", "Softgreen S.A.C.", Calendar.getInstance()
                        .getTime(), TipoEmpresa.PRIVADA, true);

        AccionistaModel model1 = accionistaProvider.create(personaJuridicaModel, representanteLegalModel,
                BigDecimal.TEN);

        String id = model1.getId();
        boolean result = accionistaProvider.remove(model1);

        AccionistaModel model2 = accionistaProvider.findById(id);

        assertThat(result, is(true));
        assertThat(model2, is(nullValue()));
    }

}
