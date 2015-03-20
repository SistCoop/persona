package org.sistcoop.persona.models;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sistcoop.persona.models.AccionistaModel;
import org.sistcoop.persona.models.AccionistaProvider;
import org.sistcoop.persona.models.PersonaJuridicaModel;
import org.sistcoop.persona.models.PersonaJuridicaProvider;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.PersonaNaturalProvider;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.enums.Sexo;
import org.sistcoop.persona.models.enums.TipoEmpresa;
import org.sistcoop.persona.models.enums.TipoPersona;
import org.sistcoop.persona.models.jpa.AccionistaAdapter;
import org.sistcoop.persona.models.jpa.JpaAccionistaProvider;
import org.sistcoop.persona.models.jpa.JpaPersonaJuridicaProvider;
import org.sistcoop.persona.models.jpa.JpaPersonaNaturalProvider;
import org.sistcoop.persona.models.jpa.JpaTipoDocumentoProvider;
import org.sistcoop.persona.models.jpa.PersonaJuridicaAdapter;
import org.sistcoop.persona.models.jpa.PersonaNaturalAdapter;
import org.sistcoop.persona.models.jpa.TipoDocumentoAdapter;
import org.sistcoop.persona.models.jpa.entities.PersonaEntity;
import org.sistcoop.persona.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
@UsingDataSet("empty.xml")
public class AccionistaProviderTest {

	Logger log = LoggerFactory.getLogger(AccionistaProviderTest.class);	
	
	private Date date;
	
	@Inject
	private TipoDocumentoProvider tipoDocumentoProvider;
	
	@Inject
	private PersonaNaturalProvider personaNaturalProvider;
	
	@Inject
	private PersonaJuridicaProvider personaJuridicaProvider;		
	
	@Inject
	private AccionistaProvider accionistaProvider;
	
	@Deployment
	public static WebArchive createDeployment() {
		File[] dependencies = Maven.resolver()
				.resolve("org.slf4j:slf4j-simple:1.7.10")
				.withoutTransitivity().asFile();

		WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
				/**persona-model-api**/
				.addClass(Provider.class)										
				.addClass(TipoDocumentoProvider.class)
				.addClass(PersonaNaturalProvider.class)
				.addClass(PersonaJuridicaProvider.class)				
				.addClass(AccionistaProvider.class)
				
				.addPackage(TipoDocumentoModel.class.getPackage())
				.addPackage(TipoPersona.class.getPackage())								
				
				/**persona-model-jpa**/		
				.addClass(JpaTipoDocumentoProvider.class)
				.addClass(TipoDocumentoAdapter.class)		
						
				.addClass(JpaPersonaNaturalProvider.class)
				.addClass(PersonaNaturalAdapter.class)	
				
				.addClass(JpaPersonaJuridicaProvider.class)
				.addClass(PersonaJuridicaAdapter.class)						
				
				.addClass(JpaAccionistaProvider.class)
				.addClass(AccionistaAdapter.class)	
				
				.addPackage(PersonaEntity.class.getPackage())
				
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("test-ds.xml");

		war.addAsLibraries(dependencies);

		return war;
	}		
	
	@Before
    public void executedBeforeEach() throws ParseException {   
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		date = formatter.parse("01/01/1991");				
    }			
	   
	@Test
	public void addAccionista() {
		TipoDocumentoModel tipoDocumentoModel1 = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);
		TipoDocumentoModel tipoDocumentoModel2 = tipoDocumentoProvider.addTipoDocumento("RUC", "Registro unico de contribuyente", 11, TipoPersona.JURIDICA);
				
		PersonaNaturalModel representanteLegalModel = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel1, "12345678", "Flores", "Huertas", "Jhon wilber", 
				date, Sexo.MASCULINO);
		
		PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel2, "10467793549", 
				"Softgreen S.A.C.", date, TipoEmpresa.PRIVADA, true);
		
		AccionistaModel model = accionistaProvider.addAccionista(personaJuridicaModel, representanteLegalModel, BigDecimal.TEN);
		
		assertThat(model, is(notNullValue()));
	}
	
	@Test
	public void getAccionistaById()  {		
		TipoDocumentoModel tipoDocumentoModel1 = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);
		TipoDocumentoModel tipoDocumentoModel2 = tipoDocumentoProvider.addTipoDocumento("RUC", "Registro unico de contribuyente", 11, TipoPersona.JURIDICA);
				
		PersonaNaturalModel representanteLegalModel = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel1, "12345678", "Flores", "Huertas", "Jhon wilber", 
				date, Sexo.MASCULINO);
		
		PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel2, "10467793549", 
				"Softgreen S.A.C.", date, TipoEmpresa.PRIVADA, true);
		
		AccionistaModel model1 =  accionistaProvider.addAccionista(personaJuridicaModel, representanteLegalModel, BigDecimal.TEN);
		
		Long id = model1.getId();		
		AccionistaModel model2 = accionistaProvider.getAccionistaById(id);
		
		assertThat(model1, is(equalTo(model2)));
	}
	
	@Test
	public void removeAccionista()  {	
		TipoDocumentoModel tipoDocumentoModel1 = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);
		TipoDocumentoModel tipoDocumentoModel2 = tipoDocumentoProvider.addTipoDocumento("RUC", "Registro unico de contribuyente", 11, TipoPersona.JURIDICA);
				
		PersonaNaturalModel representanteLegalModel = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel1, "12345678", "Flores", "Huertas", "Jhon wilber", 
				date, Sexo.MASCULINO);
		
		PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel2, "10467793549", 
				"Softgreen S.A.C.", date, TipoEmpresa.PRIVADA, true);
		
		AccionistaModel model1 =  accionistaProvider.addAccionista(personaJuridicaModel, representanteLegalModel, BigDecimal.TEN);		
		
		Long id = model1.getId();		
		boolean result = accionistaProvider.removeAccionista(model1);
		
		AccionistaModel model2 = accionistaProvider.getAccionistaById(id);
		
		assertThat(result, is(true));
		assertThat(model2, is(nullValue()));			
	}
	
}
