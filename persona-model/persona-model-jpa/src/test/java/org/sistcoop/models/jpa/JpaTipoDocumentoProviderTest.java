package org.sistcoop.models.jpa;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sistcoop.models.TipoDocumentoModel;
import org.sistcoop.models.TipoDocumentoProvider;
import org.sistcoop.models.enums.TipoPersona;
import org.sistcoop.models.jpa.entities.TipoDocumentoEntity;
import org.sistcoop.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public class JpaTipoDocumentoProviderTest {

	Logger log = LoggerFactory.getLogger(JpaTipoDocumentoProviderTest.class);

	@PersistenceContext
	EntityManager em;

	@Inject
	private TipoDocumentoProvider tipoDocumentoProvider;
	
	private static final String dniAbreviatura = "DNI";
	private static final String passAbreviatura = "Pass";
	private static final String rucAbreviatura = "RUC";
	
	private TipoDocumentoModel dniModel;
	private TipoDocumentoModel passModel;
	private TipoDocumentoModel rucModel;
	
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
				.addClass(TipoDocumentoEntity.class)
				
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

		war.addAsLibraries(dependencies);

		return war;
	}		

	@Before
    public void executedBeforeEach() throws Exception {      
		dniModel = tipoDocumentoProvider.addTipoDocumento(dniAbreviatura, "Documento Nacional de identidad", 8, TipoPersona.NATURAL);
		passModel = tipoDocumentoProvider.addTipoDocumento(passAbreviatura, "Pasaporte", 8, TipoPersona.NATURAL);
		rucModel = tipoDocumentoProvider.addTipoDocumento(rucAbreviatura, "Registro unico de contribuyente", 8, TipoPersona.JURIDICA);				
    }
	
	@After
    public void executedAfterEach() throws Exception {      
		List<TipoDocumentoModel> models = tipoDocumentoProvider.getTiposDocumento();
		for (TipoDocumentoModel tipoDocumentoModel : models) {
			tipoDocumentoProvider.removeTipoDocumento(tipoDocumentoModel);
		}
    }
	   
	@Test
	public void addTipoDocumento() throws Exception {
		TipoDocumentoModel model = tipoDocumentoProvider.addTipoDocumento("XXX", "Xxx xxx xxx", 8, TipoPersona.NATURAL);
		if(model != null) {
			if(!model.getEstado())
				throw new Exception("Model creado inactivo");
			else
				log.info("Model creado:" + model.getAbreviatura());
		} else {
			throw new Exception("Model no creado");
		}
	}
	
	@Test
	public void addTipoDocumentoUniqueTest() throws Exception {
		TipoDocumentoModel model1 = tipoDocumentoProvider.addTipoDocumento("Unique", "Xxx xxx xxx", 8, TipoPersona.NATURAL);
		tipoDocumentoProvider.removeTipoDocumento(model1);
		try {
			tipoDocumentoProvider.addTipoDocumento("Unique", "Yyy yyy yyy", 8, TipoPersona.NATURAL);
		} catch (Exception e) {
			if(e instanceof EntityExistsException)
				log.info("Unique success");
			else
				throw new Exception("No debe dejar crear duplicados");
		}				
	}

	@Test
	public void getTipoDocumentoByAbreviatura() throws Exception {
		TipoDocumentoModel model1 = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(dniAbreviatura);
		TipoDocumentoModel model2 = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(passAbreviatura);
		TipoDocumentoModel model3 = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(rucAbreviatura);
		
		if(!model1.equals(dniModel))
			throw new Exception("Model dniModel no es igual al extraido de la base de datos");
		if(!model2.equals(passModel))
			throw new Exception("Model dniModel no es igual al extraido de la base de datos");
		if(!model3.equals(rucModel))
			throw new Exception("Model dniModel no es igual al extraido de la base de datos");
	}
	

	@Test
	public void getTiposDocumento() throws Exception {
		List<TipoDocumentoModel> models = tipoDocumentoProvider.getTiposDocumento();
		for (TipoDocumentoModel tipoDocumentoModel : models) {
			if(!tipoDocumentoModel.getEstado())
				throw new Exception("Model inactivo. No debe de devolver documentos inactivos");
		}
	}
	
	@Test
	public void getTiposDocumentoByTipoPersona() throws Exception {		
		List<TipoDocumentoModel> modelsNatural = tipoDocumentoProvider.getTiposDocumento(TipoPersona.NATURAL);
		for (TipoDocumentoModel tipoDocumentoModel : modelsNatural) {
			if(!tipoDocumentoModel.getEstado())
				throw new Exception("Model inactivo. No debe de devolver documentos inactivos");
			if(!tipoDocumentoModel.getTipoPersona().equals(TipoPersona.NATURAL))
				throw new Exception("Model no pertenece a persona NATURAL");
		}
		
		List<TipoDocumentoModel> modelsJuridico = tipoDocumentoProvider.getTiposDocumento(TipoPersona.JURIDICA);
		for (TipoDocumentoModel tipoDocumentoModel : modelsJuridico) {
			if(!tipoDocumentoModel.getEstado())
				throw new Exception("Model inactivo. No debe de devolver documentos inactivos");
			if(!tipoDocumentoModel.getTipoPersona().equals(TipoPersona.JURIDICA))
				throw new Exception("Model no pertenece a persona JURIDICA");
		}
	}
	
	@Test
	public void getTiposDocumentoByEstado() throws Exception {				
		List<TipoDocumentoModel> modelsActive = tipoDocumentoProvider.getTiposDocumento(true);
		for (TipoDocumentoModel tipoDocumentoModel : modelsActive) {
			if(!tipoDocumentoModel.getEstado())
				throw new Exception("Model inactivo. No debe de devolver documentos inactivos");			
		}
		
		List<TipoDocumentoModel> modelsInactive = tipoDocumentoProvider.getTiposDocumento(false);
		for (TipoDocumentoModel tipoDocumentoModel : modelsInactive) {
			if(tipoDocumentoModel.getEstado())
				throw new Exception("Model active. No debe de devolver documentos activos");			
		}
	}
	
	@Test
	public void getTiposDocumentoByTipoPersonaAndEstado() throws Exception {				
		List<TipoDocumentoModel> modelsNaturalActivo = tipoDocumentoProvider.getTiposDocumento(TipoPersona.NATURAL, true);
		for (TipoDocumentoModel tipoDocumentoModel : modelsNaturalActivo) {
			if(!tipoDocumentoModel.getEstado())
				throw new Exception("Model inactivo. No debe de devolver documentos inactivos");		
			if(!tipoDocumentoModel.getTipoPersona().equals(TipoPersona.NATURAL))
				throw new Exception("Model no pertenece a persona NATURAL");
		}
		
		List<TipoDocumentoModel> modelsNaturalInactivo = tipoDocumentoProvider.getTiposDocumento(TipoPersona.NATURAL, false);
		for (TipoDocumentoModel tipoDocumentoModel : modelsNaturalInactivo) {
			if(tipoDocumentoModel.getEstado())
				throw new Exception("Model activo. No debe de devolver documentos activos");		
			if(!tipoDocumentoModel.getTipoPersona().equals(TipoPersona.NATURAL))
				throw new Exception("Model no pertenece a persona NATURAL");
		}
		
		List<TipoDocumentoModel> modelsJuridicaActivo = tipoDocumentoProvider.getTiposDocumento(TipoPersona.JURIDICA, true);
		for (TipoDocumentoModel tipoDocumentoModel : modelsJuridicaActivo) {
			if(!tipoDocumentoModel.getEstado())
				throw new Exception("Model inactivo. No debe de devolver documentos inactivos");		
			if(!tipoDocumentoModel.getTipoPersona().equals(TipoPersona.JURIDICA))
				throw new Exception("Model no pertenece a persona JURIDICA");
		}
		
		List<TipoDocumentoModel> modelsJuridicaInactivo = tipoDocumentoProvider.getTiposDocumento(TipoPersona.JURIDICA, false);
		for (TipoDocumentoModel tipoDocumentoModel : modelsJuridicaInactivo) {
			if(tipoDocumentoModel.getEstado())
				throw new Exception("Model activo. No debe de devolver documentos activos");		
			if(!tipoDocumentoModel.getTipoPersona().equals(TipoPersona.JURIDICA))
				throw new Exception("Model no pertenece a persona JURIDICA");
		}
	}
	
	@Test
	public void removeTipoDocumento() throws Exception {							
		boolean result = tipoDocumentoProvider.removeTipoDocumento(dniModel);
		if(!result)
			throw new Exception("Remove result false");
		
		dniModel = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(dniAbreviatura);
		if(dniModel != null)
			throw new Exception("No se elimino model");
	}
	
}
