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
import org.sistcoop.persona.models.search.SearchCriteriaFilterOperator;
import org.sistcoop.persona.models.search.SearchCriteriaModel;
import org.sistcoop.persona.models.search.SearchResultsModel;
import org.sistcoop.persona.models.search.filters.TipoDocumentoFilterProvider;

@RunWith(Arquillian.class)
@UsingDataSet("empty.xml")
public class TipoDocumentoProviderTest extends AbstractTest {

    @Inject
    private TipoDocumentoProvider tipoDocumentoProvider;

    @Inject
    private TipoDocumentoFilterProvider tipoDocumentoFilterProvider;

    @Test
    public void findByAbreviatura() {
        TipoDocumentoModel model1 = tipoDocumentoProvider.create("DNI", "Documento nacional de identidad", 8,
                TipoPersona.NATURAL);

        String abreviatura = model1.getAbreviatura();
        TipoDocumentoModel model2 = tipoDocumentoProvider.findByAbreviatura(abreviatura);

        assertThat("model1 debe ser igual a model2", model1, is(equalTo(model2)));
    }

    @Test
    public void create() {
        TipoDocumentoModel model = tipoDocumentoProvider.create("DNI", "Documento nacional de identidad", 8,
                TipoPersona.NATURAL);

        assertThat("model no debe ser null", model, is(notNullValue()));
        assertThat("estado debe ser true", model.getEstado(), is(true));
    }

    @Test
    public void remove() {
        TipoDocumentoModel model1 = tipoDocumentoProvider.create("DNI", "Documento nacional de identidad", 8,
                TipoPersona.NATURAL);

        String abreviatura = model1.getAbreviatura();
        boolean result = tipoDocumentoProvider.remove(model1);

        TipoDocumentoModel model2 = tipoDocumentoProvider.findByAbreviatura(abreviatura);

        assertThat(result, is(true));
        assertThat(model2, is(nullValue()));
    }

    @Test
    public void search1() {
        @SuppressWarnings("unused")
        TipoDocumentoModel model1 = tipoDocumentoProvider.create("DNI", "Documento nacional de identidad", 8,
                TipoPersona.NATURAL);
        @SuppressWarnings("unused")
        TipoDocumentoModel model2 = tipoDocumentoProvider.create("P.NAC", "Partida de nacimiento", 11,
                TipoPersona.NATURAL);

        SearchResultsModel<TipoDocumentoModel> searched = tipoDocumentoProvider.search();

        assertThat(searched, is(notNullValue()));
        assertThat(searched.getTotalSize(), is(2));
        assertThat(searched.getModels().size(), is(2));
    }

    @Test
    public void search2() {
        TipoDocumentoModel model1 = tipoDocumentoProvider.create("DNI", "Documento nacional de identidad", 8,
                TipoPersona.NATURAL);
        @SuppressWarnings("unused")
        TipoDocumentoModel model2 = tipoDocumentoProvider.create("P.NAC", "Partida de nacimiento", 11,
                TipoPersona.NATURAL);

        model1.setEstado(false);
        model1.commit();

        SearchResultsModel<TipoDocumentoModel> searched = tipoDocumentoProvider.search();

        assertThat(searched, is(notNullValue()));
        assertThat(searched.getTotalSize(), is(1));
        assertThat(searched.getModels().size(), is(1));
    }

    @Test
    public void searchCriteria1() {
        TipoDocumentoModel model1 = tipoDocumentoProvider.create("DNI", "Documento nacional de identidad", 8,
                TipoPersona.NATURAL);
        @SuppressWarnings("unused")
        TipoDocumentoModel model2 = tipoDocumentoProvider.create("P.NAC", "Partida de nacimiento", 11,
                TipoPersona.NATURAL);
        @SuppressWarnings("unused")
        TipoDocumentoModel model3 = tipoDocumentoProvider.create("RUC", "Registro unico de contribuyente",
                11, TipoPersona.JURIDICA);

        model1.setEstado(false);
        model1.commit();

        // add filters
        SearchCriteriaModel criteria = new SearchCriteriaModel();
        criteria.addFilter(tipoDocumentoFilterProvider.getTipoPersonaFilter(), TipoPersona.NATURAL,
                SearchCriteriaFilterOperator.eq);

        SearchResultsModel<TipoDocumentoModel> searched = tipoDocumentoProvider.search(criteria);

        assertThat(searched, is(notNullValue()));
        assertThat(searched.getTotalSize(), is(2));
        assertThat(searched.getModels().size(), is(2));
    }

    @Test
    public void searchCriteria2() {
        TipoDocumentoModel model1 = tipoDocumentoProvider.create("DNI", "Documento nacional de identidad", 8,
                TipoPersona.NATURAL);
        @SuppressWarnings("unused")
        TipoDocumentoModel model2 = tipoDocumentoProvider.create("P.NAC", "Partida de nacimiento", 11,
                TipoPersona.NATURAL);
        @SuppressWarnings("unused")
        TipoDocumentoModel model3 = tipoDocumentoProvider.create("RUC", "Registro unico de contribuyente",
                11, TipoPersona.JURIDICA);

        model1.setEstado(false);
        model1.commit();

        // add filters
        SearchCriteriaModel criteria = new SearchCriteriaModel();
        criteria.addFilter(tipoDocumentoFilterProvider.getEstadoFilter(), true,
                SearchCriteriaFilterOperator.bool_eq);

        SearchResultsModel<TipoDocumentoModel> searched = tipoDocumentoProvider.search(criteria);

        assertThat(searched, is(notNullValue()));
        assertThat(searched.getTotalSize(), is(2));
        assertThat(searched.getModels().size(), is(2));
    }

    @Test
    public void searchCriteria3() {
        TipoDocumentoModel model1 = tipoDocumentoProvider.create("DNI", "Documento nacional de identidad", 8,
                TipoPersona.NATURAL);
        @SuppressWarnings("unused")
        TipoDocumentoModel model2 = tipoDocumentoProvider.create("P.NAC", "Partida de nacimiento", 11,
                TipoPersona.NATURAL);
        @SuppressWarnings("unused")
        TipoDocumentoModel model3 = tipoDocumentoProvider.create("RUC", "Registro unico de contribuyente",
                11, TipoPersona.JURIDICA);

        model1.setEstado(false);
        model1.commit();

        // add filters
        SearchCriteriaModel criteria = new SearchCriteriaModel();
        criteria.addFilter(tipoDocumentoFilterProvider.getTipoPersonaFilter(), TipoPersona.NATURAL,
                SearchCriteriaFilterOperator.eq);
        criteria.addFilter(tipoDocumentoFilterProvider.getEstadoFilter(), true,
                SearchCriteriaFilterOperator.bool_eq);

        SearchResultsModel<TipoDocumentoModel> searched = tipoDocumentoProvider.search(criteria);

        assertThat(searched, is(notNullValue()));
        assertThat(searched.getTotalSize(), is(1));
        assertThat(searched.getModels().size(), is(1));
    }

    @Test
    public void searchCriteriaFiltertext1() {
        @SuppressWarnings("unused")
        TipoDocumentoModel model1 = tipoDocumentoProvider.create("DNI", "Documento nacional de identidad", 8,
                TipoPersona.NATURAL);
        @SuppressWarnings("unused")
        TipoDocumentoModel model2 = tipoDocumentoProvider.create("P.NAC", "Partida de nacimiento", 11,
                TipoPersona.NATURAL);
        @SuppressWarnings("unused")
        TipoDocumentoModel model3 = tipoDocumentoProvider.create("Pasaporte", "Pasaporte", 11,
                TipoPersona.NATURAL);
        @SuppressWarnings("unused")
        TipoDocumentoModel model4 = tipoDocumentoProvider.create("RUC", "Registro unico de contribuyente",
                11, TipoPersona.JURIDICA);

        SearchCriteriaModel criteria = new SearchCriteriaModel();
        SearchResultsModel<TipoDocumentoModel> searched = tipoDocumentoProvider.search(criteria, "dni");

        assertThat(searched, is(notNullValue()));
        assertThat(searched.getTotalSize(), is(1));
        assertThat(searched.getModels().size(), is(1));
    }

    @Test
    public void searchCriteriaFiltertext2() {
        TipoDocumentoModel model1 = tipoDocumentoProvider.create("DNI", "Documento nacional de identidad", 8,
                TipoPersona.NATURAL);
        @SuppressWarnings("unused")
        TipoDocumentoModel model2 = tipoDocumentoProvider.create("P.NAC", "Partida de nacimiento", 11,
                TipoPersona.NATURAL);
        @SuppressWarnings("unused")
        TipoDocumentoModel model3 = tipoDocumentoProvider.create("Pasaporte", "Pasaporte", 11,
                TipoPersona.NATURAL);
        @SuppressWarnings("unused")
        TipoDocumentoModel model4 = tipoDocumentoProvider.create("RUC", "Registro unico de contribuyente",
                11, TipoPersona.JURIDICA);

        model1.setEstado(false);
        model1.commit();

        // add filters
        SearchCriteriaModel criteria = new SearchCriteriaModel();
        criteria.addFilter(tipoDocumentoFilterProvider.getEstadoFilter(), true,
                SearchCriteriaFilterOperator.bool_eq);

        SearchResultsModel<TipoDocumentoModel> searched = tipoDocumentoProvider.search(criteria, "dni");

        assertThat(searched, is(notNullValue()));
        assertThat(searched.getTotalSize(), is(0));
        assertThat(searched.getModels().size(), is(0));
    }

    @Test
    public void searchCriteriaFiltertext3() {
        @SuppressWarnings("unused")
        TipoDocumentoModel model1 = tipoDocumentoProvider.create("DNI", "Documento nacional de identidad", 8,
                TipoPersona.NATURAL);
        TipoDocumentoModel model2 = tipoDocumentoProvider.create("P.NAC", "Partida de nacimiento", 11,
                TipoPersona.NATURAL);
        @SuppressWarnings("unused")
        TipoDocumentoModel model3 = tipoDocumentoProvider.create("Pasaporte", "Pasaporte", 11,
                TipoPersona.NATURAL);
        @SuppressWarnings("unused")
        TipoDocumentoModel model4 = tipoDocumentoProvider.create("RUC", "Registro unico de contribuyente",
                11, TipoPersona.JURIDICA);

        model2.setEstado(false);
        model2.commit();

        // add filters
        SearchCriteriaModel criteria = new SearchCriteriaModel();
        criteria.addFilter(tipoDocumentoFilterProvider.getTipoPersonaFilter(), TipoPersona.NATURAL,
                SearchCriteriaFilterOperator.eq);
        criteria.addFilter(tipoDocumentoFilterProvider.getEstadoFilter(), true,
                SearchCriteriaFilterOperator.bool_eq);

        SearchResultsModel<TipoDocumentoModel> searched = tipoDocumentoProvider.search(criteria, "dni");

        assertThat(searched, is(notNullValue()));
        assertThat(searched.getTotalSize(), is(1));
        assertThat(searched.getModels().size(), is(1));
    }

}
