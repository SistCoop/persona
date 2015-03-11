package org.sistcoop.models;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sistcoop.models.TipoDocumentoModel;
import org.sistcoop.models.TipoDocumentoProvider;
import org.sistcoop.models.enums.TipoPersona;
import org.sistcoop.models.jpa.JpaTipoDocumentoProvider;
import org.sistcoop.models.jpa.TipoDocumentoAdapter;
import org.sistcoop.models.jpa.entities.PersonaEntity;
import org.sistcoop.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public class TipoDocumentoProviderTest {

	Logger log = LoggerFactory.getLogger(TipoDocumentoProviderTest.class);

	@PersistenceContext
	private EntityManager em;

	@Resource           
	private UserTransaction utx; 
	
	@Inject
	private TipoDocumentoProvider tipoDocumentoProvider;	
	
	@Deployment
	public static WebArchive createDeployment() {
		File[] dependencies = Maven.resolver()
				.resolve("org.slf4j:slf4j-simple:1.7.10")
				.withoutTransitivity().asFile();

		WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
				/**persona-model-api**/
				.addClass(Provider.class)										
				.addClass(TipoDocumentoProvider.class)
				
				.addPackage(TipoDocumentoModel.class.getPackage())
				.addPackage(TipoPersona.class.getPackage())
				
				/**persona-model-jpa**/
				.addClass(JpaTipoDocumentoProvider.class)
				.addClass(TipoDocumentoAdapter.class)

				.addPackage(PersonaEntity.class.getPackage())
				
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("test-ds.xml");

		war.addAsLibraries(dependencies);

		return war;
	}		

	@Before
    public void executedBeforeEach() {      						
    }
	
	@After
    public void executedAfterEach() {      
		// remove all TipoDocumentoModels
		List<TipoDocumentoModel> tipoDocumentoModels = tipoDocumentoProvider.getTiposDocumento();
		for (TipoDocumentoModel tipoDocumentoModel : tipoDocumentoModels) {
			tipoDocumentoProvider.removeTipoDocumento(tipoDocumentoModel);
		}
    }
	   
	@Test
	public void addTipoDocumento() {
		TipoDocumentoModel model = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);		
		assertThat(model, is(notNullValue()));
		assertThat(model.getEstado(), is(true));	
	}
	
	@Test
	public void addTipoDocumentoUniqueTest() {
		TipoDocumentoModel model1 = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);
		
		TipoDocumentoModel model2 = null;
		try {
			model2 = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);
		} catch (Exception e) {		
			assertThat(e, instanceOf(EJBException.class));						
		}		
		
		assertThat(model1, is(notNullValue()));
		assertThat(model2, is(nullValue()));		
	}

	@Test
	public void getTipoDocumentoByAbreviatura() {
		TipoDocumentoModel model1 = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);						
		String abreviatura = model1.getAbreviatura();		
		TipoDocumentoModel model2 = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(abreviatura);
				
		assertThat(model1, is(equalTo(model2)));
	}
	

	@Test
	public void getTiposDocumento() {
		List<TipoDocumentoModel> models = tipoDocumentoProvider.getTiposDocumento();
		for (TipoDocumentoModel tipoDocumentoModel : models) {			
			assertThat(tipoDocumentoModel.getEstado(), is(true));
		}
	}
	
	@Test
	public void getTiposDocumentoByTipoPersona() {		
		List<TipoDocumentoModel> modelsNatural = tipoDocumentoProvider.getTiposDocumento(TipoPersona.NATURAL);
		for (TipoDocumentoModel tipoDocumentoModel : modelsNatural) {			
			assertThat(tipoDocumentoModel.getEstado(), is(true));
			assertThat(tipoDocumentoModel.getTipoPersona(), is(equalTo(TipoPersona.NATURAL)));
		}
		
		List<TipoDocumentoModel> modelsJuridico = tipoDocumentoProvider.getTiposDocumento(TipoPersona.JURIDICA);
		for (TipoDocumentoModel tipoDocumentoModel : modelsJuridico) {
			assertThat(tipoDocumentoModel.getEstado(), is(true));
			assertThat(tipoDocumentoModel.getTipoPersona(), is(equalTo(TipoPersona.JURIDICA)));	
		}
	}
	
	@Test
	public void getTiposDocumentoByEstado() {				
		List<TipoDocumentoModel> modelsActive = tipoDocumentoProvider.getTiposDocumento(true);
		for (TipoDocumentoModel tipoDocumentoModel : modelsActive) {
			assertThat(tipoDocumentoModel.getEstado(), is(true));								
		}
		
		List<TipoDocumentoModel> modelsInactive = tipoDocumentoProvider.getTiposDocumento(false);
		for (TipoDocumentoModel tipoDocumentoModel : modelsInactive) {
			assertThat(tipoDocumentoModel.getEstado(), is(false));				
		}
	}
	
	@Test
	public void getTiposDocumentoByTipoPersonaAndEstado()  {				
		List<TipoDocumentoModel> modelsNaturalActivo = tipoDocumentoProvider.getTiposDocumento(TipoPersona.NATURAL, true);
		for (TipoDocumentoModel tipoDocumentoModel : modelsNaturalActivo) {
			assertThat(tipoDocumentoModel.getEstado(), is(true));
			assertThat(tipoDocumentoModel.getTipoPersona(), is(equalTo(TipoPersona.NATURAL)));
		}
		
		List<TipoDocumentoModel> modelsNaturalInactivo = tipoDocumentoProvider.getTiposDocumento(TipoPersona.NATURAL, false);
		for (TipoDocumentoModel tipoDocumentoModel : modelsNaturalInactivo) {
			assertThat(tipoDocumentoModel.getEstado(), is(false));
			assertThat(tipoDocumentoModel.getTipoPersona(), is(equalTo(TipoPersona.NATURAL)));
		}
		
		List<TipoDocumentoModel> modelsJuridicaActivo = tipoDocumentoProvider.getTiposDocumento(TipoPersona.JURIDICA, true);
		for (TipoDocumentoModel tipoDocumentoModel : modelsJuridicaActivo) {
			assertThat(tipoDocumentoModel.getEstado(), is(true));
			assertThat(tipoDocumentoModel.getTipoPersona(), is(equalTo(TipoPersona.JURIDICA)));
		}
		
		List<TipoDocumentoModel> modelsJuridicaInactivo = tipoDocumentoProvider.getTiposDocumento(TipoPersona.JURIDICA, false);
		for (TipoDocumentoModel tipoDocumentoModel : modelsJuridicaInactivo) {
			assertThat(tipoDocumentoModel.getEstado(), is(false));
			assertThat(tipoDocumentoModel.getTipoPersona(), is(equalTo(TipoPersona.JURIDICA)));
		}
	}
	
	@Test
	public void desactivarTipoDocumento()  {			
		TipoDocumentoModel model1 = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);						
		
		String abreviatura = model1.getAbreviatura();		
		boolean result = tipoDocumentoProvider.desactivarTipoDocumento(model1);
		
		TipoDocumentoModel model2 = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(abreviatura);
		
		assertThat(result, is(true));
		assertThat(model2, is(notNullValue()));
		assertThat(model2.getEstado(), is(false));			
	}
	
	@Test
	public void removeTipoDocumento()  {			
		TipoDocumentoModel model1 = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);				
		
		String abreviatura = model1.getAbreviatura();
		boolean result = tipoDocumentoProvider.removeTipoDocumento(model1);
		
		TipoDocumentoModel model2 = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(abreviatura);			
		
		assertThat(result, is(true));
		assertThat(model2, is(nullValue()));	
	}
	
}
