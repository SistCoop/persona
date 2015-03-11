package org.sistcoop.models.jpa;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
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
import org.sistcoop.models.jpa.entities.PersonaEntity;
import org.sistcoop.models.jpa.entities.TipoDocumentoEntity;
import org.sistcoop.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public class JpaTipoDocumentoProviderTest {

	Logger log = LoggerFactory.getLogger(JpaTipoDocumentoProviderTest.class);

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
    public void executedBeforeEach() throws Exception {      						
    }
	
	@After
    public void executedAfterEach() throws Exception {      
		utx.begin();
         
		//remove all TipoDocumentoEntity					
		List<Object> listTipoDocumento = null;
		CriteriaQuery<Object> cqTipoDocumento = this.em.getCriteriaBuilder().createQuery();
		cqTipoDocumento.select(cqTipoDocumento.from(TipoDocumentoEntity.class));
		listTipoDocumento = this.em.createQuery(cqTipoDocumento).getResultList();		
		for (Object object : listTipoDocumento) {
			this.em.remove(object);
		}
		
		utx.commit();
    }
	   
	@Test
	public void addTipoDocumento() throws Exception {
		TipoDocumentoModel model = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);
		if(model != null) {
			log.info("Objeto " + model.toString() + " creado");							
		} else {
			throw new Exception("Objeto no creado");
		}
		
		if(model.getEstado()){
			log.info("Objeto creado con estado true");
		} else {
			log.error("Objeto creado con estado false");
			throw new Exception("Objeto creado con estado false");
		}			
		
		log.info("SUCCESS");
	}
	
	@Test
	public void addTipoDocumentoUniqueTest() throws Exception {
		TipoDocumentoModel model1 = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);
		
		TipoDocumentoModel model2 = null;
		try {
			model2 = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);
		} catch (EJBTransactionRolledbackException e) {
			log.info("Objeto " + model1.toString() + "creado");
			log.info("Segundo Objeto no creado por tener la misma abreviatura");				
		} catch (Exception e) {
			log.error("No se creó el objeto por una excepcion que no es EntityExistsException");
			throw new Exception("No se creó el objeto por una excepcion que no es EJBTransactionRolledbackException");					
		}		
		
		if(model2 != null) {
			log.error("Existen dos TipoDocumento con la misma abreviatura");
			throw new Exception("Existen dos TipoDocumento con la misma abreviatura");	
		}
		
		log.info("SUCCESS");			
	}

	@Test
	public void getTipoDocumentoByAbreviatura() throws Exception {
		TipoDocumentoModel model1 = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);				
		
		String abreviatura = model1.getAbreviatura();
		
		TipoDocumentoModel model2 = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(abreviatura);
		if(model1.equals(model2)) {
			log.info("Objeto:" + model1.toString() + " encontrado");				
		} else {
			log.error("Objeto creado:"+ model1.toString() + " no pudo ser encontrado");
			throw new Exception("Objeto creado:"+ model1.toString() + " no pudo ser encontrado");
		}
		
		log.info("SUCCESS");
	}
	

	@Test
	public void getTiposDocumento() throws Exception {
		List<TipoDocumentoModel> models = tipoDocumentoProvider.getTiposDocumento();
		for (TipoDocumentoModel tipoDocumentoModel : models) {
			if(tipoDocumentoModel.getEstado()) {
				log.info("Objeto:" + tipoDocumentoModel.toString() + " encontrado");
			} else {
				throw new Exception("Model inactivo. No debe de devolver documentos inactivos");
			}				
		}
	}
	
	@Test
	public void getTiposDocumentoByTipoPersona() throws Exception {		
		List<TipoDocumentoModel> modelsNatural = tipoDocumentoProvider.getTiposDocumento(TipoPersona.NATURAL);
		for (TipoDocumentoModel tipoDocumentoModel : modelsNatural) {
			if(tipoDocumentoModel.getEstado()) {
				log.info("Objeto:" + tipoDocumentoModel.toString() + " encontrado");
			} else {
				throw new Exception("Model inactivo. No debe de devolver documentos inactivos");
			}
			
			if(tipoDocumentoModel.getTipoPersona().equals(TipoPersona.NATURAL)) {
				log.info("Objeto:" + tipoDocumentoModel.toString() + " es de tipo NATURAL");
			} else {
				throw new Exception("Model no pertenece a persona NATURAL");
			}						
		}
		
		List<TipoDocumentoModel> modelsJuridico = tipoDocumentoProvider.getTiposDocumento(TipoPersona.JURIDICA);
		for (TipoDocumentoModel tipoDocumentoModel : modelsJuridico) {
			if(tipoDocumentoModel.getEstado()) {
				log.info("Objeto:" + tipoDocumentoModel.toString() + " encontrado");
			} else {
				throw new Exception("Model inactivo. No debe de devolver documentos inactivos");
			}
			
			if(tipoDocumentoModel.getTipoPersona().equals(TipoPersona.JURIDICA)) {
				log.info("Objeto:" + tipoDocumentoModel.toString() + " es de tipo JURIDICA");
			} else {
				throw new Exception("Model no pertenece a persona JURIDICA");
			}	
		}
	}
	
	@Test
	public void getTiposDocumentoByEstado() throws Exception {				
		List<TipoDocumentoModel> modelsActive = tipoDocumentoProvider.getTiposDocumento(true);
		for (TipoDocumentoModel tipoDocumentoModel : modelsActive) {
			if(tipoDocumentoModel.getEstado()) {
				log.info("Objeto:" + tipoDocumentoModel.toString() + " encontrado");
			} else {
				throw new Exception("Model inactivo. No debe de devolver documentos inactivos");
			}							
		}
		
		List<TipoDocumentoModel> modelsInactive = tipoDocumentoProvider.getTiposDocumento(false);
		for (TipoDocumentoModel tipoDocumentoModel : modelsInactive) {
			if(!tipoDocumentoModel.getEstado()) {
				log.info("Objeto:" + tipoDocumentoModel.toString() + " encontrado");
			} else {
				throw new Exception("Model activo. No debe de devolver documentos activos");
			}			
		}
	}
	
	@Test
	public void getTiposDocumentoByTipoPersonaAndEstado() throws Exception {				
		List<TipoDocumentoModel> modelsNaturalActivo = tipoDocumentoProvider.getTiposDocumento(TipoPersona.NATURAL, true);
		for (TipoDocumentoModel tipoDocumentoModel : modelsNaturalActivo) {
			if(tipoDocumentoModel.getEstado()) {
				log.info("Objeto:" + tipoDocumentoModel.toString() + " encontrado");
			} else {
				throw new Exception("Model inactivo. No debe de devolver documentos inactivos");
			}
			
			if(tipoDocumentoModel.getTipoPersona().equals(TipoPersona.NATURAL)) {
				log.info("Objeto:" + tipoDocumentoModel.toString() + " es de tipo NATURAL");
			} else {
				throw new Exception("Model no pertenece a persona NATURAL");
			}	
		}
		
		List<TipoDocumentoModel> modelsNaturalInactivo = tipoDocumentoProvider.getTiposDocumento(TipoPersona.NATURAL, false);
		for (TipoDocumentoModel tipoDocumentoModel : modelsNaturalInactivo) {
			if(!tipoDocumentoModel.getEstado()) {
				log.info("Objeto:" + tipoDocumentoModel.toString() + " encontrado");
			} else {
				throw new Exception("Model activo. No debe de devolver documentos activos");
			}
			
			if(tipoDocumentoModel.getTipoPersona().equals(TipoPersona.NATURAL)) {
				log.info("Objeto:" + tipoDocumentoModel.toString() + " es de tipo NATURAL");
			} else {
				throw new Exception("Model no pertenece a persona NATURAL");
			}	
		}
		
		List<TipoDocumentoModel> modelsJuridicaActivo = tipoDocumentoProvider.getTiposDocumento(TipoPersona.JURIDICA, true);
		for (TipoDocumentoModel tipoDocumentoModel : modelsJuridicaActivo) {
			if(tipoDocumentoModel.getEstado()) {
				log.info("Objeto:" + tipoDocumentoModel.toString() + " encontrado");
			} else {
				throw new Exception("Model inactivo. No debe de devolver documentos inactivos");
			}
			
			if(tipoDocumentoModel.getTipoPersona().equals(TipoPersona.JURIDICA)) {
				log.info("Objeto:" + tipoDocumentoModel.toString() + " es de tipo JURIDICA");
			} else {
				throw new Exception("Model no pertenece a persona JURIDICA");
			}	
		}
		
		List<TipoDocumentoModel> modelsJuridicaInactivo = tipoDocumentoProvider.getTiposDocumento(TipoPersona.JURIDICA, false);
		for (TipoDocumentoModel tipoDocumentoModel : modelsJuridicaInactivo) {
			if(!tipoDocumentoModel.getEstado()) {
				log.info("Objeto:" + tipoDocumentoModel.toString() + " encontrado");
			} else {
				throw new Exception("Model activo. No debe de devolver documentos activos");
			}
			
			if(tipoDocumentoModel.getTipoPersona().equals(TipoPersona.JURIDICA)) {
				log.info("Objeto:" + tipoDocumentoModel.toString() + " es de tipo JURIDICA");
			} else {
				throw new Exception("Model no pertenece a persona JURIDICA");
			}	
		}
	}
	
	@Test
	public void desactivarTipoDocumento() throws Exception {			
		TipoDocumentoModel model1 = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);				
		
		String abreviatura = model1.getAbreviatura();
		
		boolean result = tipoDocumentoProvider.desactivarTipoDocumento(model1);
		if(result) {
			log.info("Resultado de eliminacion:" + result);
		} else {
			log.error("Resultado de eliminacion:" + result);
			throw new Exception("Resultado de eliminacion:" + result);
		}
				
		TipoDocumentoModel model2 = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(abreviatura);		
		if(model2 != null) {
			log.info("Objeto no encontrado");			
		} else {
			log.error("Objeto " + model1.toString() + " encontrado, fue eliminado");
			throw new Exception("Objeto encontrado, fue eliminado");
		}	
		
		if(model2.getEstado()) {
			log.error("Objeto tiene estado TRUE");
			throw new Exception("Objeto tiene estado TRUE");
		}
				
		log.info("SUCCESS");
	}
	
	@Test
	public void removeTipoDocumento() throws Exception {			
		TipoDocumentoModel model = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);				
		
		String abreviatura = model.getAbreviatura();
		
		boolean result = tipoDocumentoProvider.removeTipoDocumento(model);
		if(result) {
			log.info("Resultado de eliminacion:" + result);
		} else {
			log.error("Resultado de eliminacion:" + result);
			throw new Exception("Resultado de eliminacion:" + result);
		}
				
		model = tipoDocumentoProvider.getTipoDocumentoByAbreviatura(abreviatura);
		if(model == null) {
			log.info("Objeto no encontrado");			
		} else {
			log.error("Objeto " + model.toString() + " encontrado, no fue eliminado");
			throw new Exception("Objeto encontrado, no fue eliminado");
		}	
		
		log.info("SUCCESS");
	}
	
}
