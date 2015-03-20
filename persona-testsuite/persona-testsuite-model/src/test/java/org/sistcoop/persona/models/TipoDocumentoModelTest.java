package org.sistcoop.persona.models;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.enums.TipoPersona;
import org.sistcoop.persona.models.jpa.JpaTipoDocumentoProvider;
import org.sistcoop.persona.models.jpa.TipoDocumentoAdapter;
import org.sistcoop.persona.models.jpa.entities.PersonaEntity;
import org.sistcoop.persona.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
@UsingDataSet("empty.xml")
public class TipoDocumentoModelTest {

	Logger log = LoggerFactory.getLogger(TipoDocumentoModelTest.class);

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
	
	@Test
	public void desactivar() {
		TipoDocumentoModel model = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);				
		model.desactivar();
		
		assertThat(model.getEstado(), is(false));
	}
	
	@Test
	public void commit() {
		TipoDocumentoModel model1 = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);		
		
		String abreviatura = model1.getAbreviatura();
		String denominacionNueva = "Otra denominacion";
		
		model1.setDenominacion(denominacionNueva);
		model1.commit();	
						
		TipoDocumentoModel model2 = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(abreviatura);
		
		assertThat(model2.getDenominacion(), is(equalTo(denominacionNueva)));
	}	
	
	@Test
	public void testAttributes() {
		TipoDocumentoModel model = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);		
		
		assertThat(model.getAbreviatura(), is(notNullValue()));
		assertThat(model.getDenominacion(), is(notNullValue()));
		assertThat(model.getCantidadCaracteres(), is(notNullValue()));
		assertThat(model.getTipoPersona(), is(equalTo(TipoPersona.NATURAL)));
		assertThat(model.getEstado(), is(true));	
	}	
		
}
