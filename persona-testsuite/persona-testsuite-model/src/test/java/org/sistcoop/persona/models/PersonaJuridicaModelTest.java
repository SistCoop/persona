package org.sistcoop.persona.models;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
public class PersonaJuridicaModelTest {

	Logger log = LoggerFactory.getLogger(PersonaJuridicaModelTest.class);
	
	private Date date;
	
	@Inject
	private TipoDocumentoProvider tipoDocumentoProvider;
	
	@Inject
	private PersonaNaturalProvider personaNaturalProvider;
	
	@Inject
	private PersonaJuridicaProvider personaJuridicaProvider;		
	
	@Deployment
	public static WebArchive createDeployment() {
		File[] dependencies = Maven.resolver()
				.resolve("org.slf4j:slf4j-simple:1.7.10")
				.withoutTransitivity().asFile();

		WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
				/**persona-model-api**/
				.addClass(Provider.class)										
				.addClass(TipoDocumentoProvider.class)
				.addClass(PersonaJuridicaProvider.class)
				.addClass(PersonaNaturalProvider.class)
				
				.addPackage(TipoDocumentoModel.class.getPackage())
				.addPackage(TipoPersona.class.getPackage())								
				
				/**persona-model-jpa**/
				.addClass(JpaTipoDocumentoProvider.class)
				.addClass(TipoDocumentoAdapter.class)		
						
				.addClass(JpaPersonaNaturalProvider.class)
				.addClass(PersonaNaturalAdapter.class)	
				
				.addClass(JpaPersonaJuridicaProvider.class)
				.addClass(PersonaJuridicaAdapter.class)
				
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
	public void commit() {
		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.addTipoDocumento("RUC", "Registro unico de contribuyente", 8, TipoPersona.JURIDICA);		
		
		PersonaNaturalModel representanteLegalModel = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				date, Sexo.MASCULINO);				
		
		PersonaJuridicaModel model1 = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel, "10467793549", 
				"Softgreen S.A.C.", date, TipoEmpresa.PRIVADA, true);
		
		Long id = model1.getId();
		String razonSocialNueva = "SistCoopEE S.A.C";
		
		model1.setRazonSocial(razonSocialNueva);;
		model1.commit();	

		PersonaJuridicaModel model2 = personaJuridicaProvider.getPersonaJuridicaById(id);
				
		assertThat(model2.getRazonSocial(), is(equalTo(razonSocialNueva)));
	}
	
	@Test
	public void addAccionista() {
		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.addTipoDocumento("RUC", "Registro unico de contribuyente", 8, TipoPersona.JURIDICA);		
		
		PersonaNaturalModel representanteLegalModel = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				date, Sexo.MASCULINO);				
		
		PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel, "10467793549", 
				"Softgreen S.A.C.", date, TipoEmpresa.PRIVADA, true);
		
		PersonaNaturalModel personaNaturalAccionistaModel = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "00000000", "Flores", "Huertas", "Jhon wilber", 
				date, Sexo.MASCULINO);
		
		AccionistaModel accionistaModel = personaJuridicaModel.addAccionista(personaNaturalAccionistaModel, BigDecimal.TEN);
						
		assertThat(accionistaModel, is(notNullValue()));
		assertThat(accionistaModel.getPersonaNatural(), is(equalTo(personaNaturalAccionistaModel)));
		assertThat(accionistaModel.getPersonaJuridica(), is(equalTo(personaJuridicaModel)));
	}
	
	@Test
	public void getAccionistas() {
		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.addTipoDocumento("RUC", "Registro unico de contribuyente", 8, TipoPersona.JURIDICA);		
		
		PersonaNaturalModel representanteLegalModel = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				date, Sexo.MASCULINO);				
		
		PersonaNaturalModel personaNaturalAccionistaModel = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "00000000", "Flores", "Huertas", "Jhon wilber", 
				date, Sexo.MASCULINO);
		
		PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel, "10467793549", 
				"Softgreen S.A.C.", date, TipoEmpresa.PRIVADA, true);
						
		AccionistaModel accionistaModel = personaJuridicaModel.addAccionista(personaNaturalAccionistaModel, BigDecimal.TEN);
		
		List<AccionistaModel> accionistaModels = personaJuridicaModel.getAccionistas();						
		
		assertThat(accionistaModels.size(), is(equalTo(1)));
		assertThat(accionistaModels.get(0), is(equalTo(accionistaModel)));
		
	}
	
	@Test
	public void removeAccionista() {
		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.addTipoDocumento("RUC", "Registro unico de contribuyente", 8, TipoPersona.JURIDICA);		
		
		PersonaNaturalModel representanteLegalModel = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				date, Sexo.MASCULINO);				
		
		PersonaJuridicaModel personaJuridicaModel = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel, "10467793549", 
				"Softgreen S.A.C.", date, TipoEmpresa.PRIVADA, true);
		
		PersonaNaturalModel personaNaturalAccionistaModel = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "00000000", "Flores", "Huertas", "Jhon wilber", 
				date, Sexo.MASCULINO);
		
		AccionistaModel accionistaModel = personaJuridicaModel.addAccionista(personaNaturalAccionistaModel, BigDecimal.TEN);
			
		boolean result = personaJuridicaModel.removeAccionista(accionistaModel);
		
		List<AccionistaModel> accionistaModels = personaJuridicaModel.getAccionistas(); 
				
		assertThat(result, is(true));
		assertThat(accionistaModels.size(), is(equalTo(0)));
		
	}
	
	@Test
	public void testAttributes() {
		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.addTipoDocumento("RUC", "Registro unico de contribuyente", 8, TipoPersona.JURIDICA);		
		
		PersonaNaturalModel representanteLegalModel = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				date, Sexo.MASCULINO);				
		
		PersonaJuridicaModel model = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel, "10467793549", 
				"Softgreen S.A.C.", date, TipoEmpresa.PRIVADA, true);
		
		assertThat(model.getRepresentanteLegal(), is(equalTo(representanteLegalModel)));
		assertThat(model.getCodigoPais(), is(notNullValue()));
		assertThat(model.getTipoDocumento(), is(equalTo(tipoDocumentoModel)));
		assertThat(model.getNumeroDocumento(), is(notNullValue()));
		assertThat(model.getRazonSocial(), is(notNullValue()));
		assertThat(model.getFechaConstitucion(), is(notNullValue()));
		assertThat(model.getTipoEmpresa(), is(equalTo(TipoEmpresa.PRIVADA)));
		assertThat(model.isFinLucro(), is(equalTo(true)));	
	}
	
}
