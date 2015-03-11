package org.sistcoop.models.jpa;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
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
import org.sistcoop.models.PersonaNaturalModel;
import org.sistcoop.models.PersonaNaturalProvider;
import org.sistcoop.models.TipoDocumentoModel;
import org.sistcoop.models.TipoDocumentoProvider;
import org.sistcoop.models.enums.Sexo;
import org.sistcoop.models.enums.TipoPersona;
import org.sistcoop.models.jpa.entities.PersonaEntity;
import org.sistcoop.models.jpa.entities.PersonaNaturalEntity;
import org.sistcoop.models.jpa.entities.TipoDocumentoEntity;
import org.sistcoop.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public class JpaPersonaNaturalProviderTest {

	Logger log = LoggerFactory.getLogger(JpaPersonaNaturalProviderTest.class);

	@PersistenceContext
	private EntityManager em;

	@Resource           
	private UserTransaction utx; 
	
	@Inject
	private PersonaNaturalProvider personaNaturalProvider;		
	
	@Inject
	private TipoDocumentoProvider tipoDocumentoProvider;	
	
	private TipoDocumentoModel tipoDocumentoModel;
	
	@Deployment
	public static WebArchive createDeployment() {
		File[] dependencies = Maven.resolver()
				.resolve("org.slf4j:slf4j-simple:1.7.10")
				.withoutTransitivity().asFile();

		WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
				/**persona-model-api**/
				.addClass(Provider.class)										
				.addClass(PersonaNaturalProvider.class)
				.addClass(TipoDocumentoProvider.class)
				.addPackage(PersonaNaturalModel.class.getPackage())
				.addPackage(TipoPersona.class.getPackage())
												
				/**persona-model-jpa**/
				.addClass(JpaPersonaNaturalProvider.class)
				.addClass(PersonaNaturalAdapter.class)
				.addClass(PersonaNaturalEntity.class)
				.addPackage(PersonaEntity.class.getPackage())
				
				.addClass(JpaTipoDocumentoProvider.class)
				.addClass(TipoDocumentoAdapter.class)					
				
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("test-ds.xml");

		war.addAsLibraries(dependencies);

		return war;
	}		

	@Before
    public void executedBeforeEach() throws Exception {    
		tipoDocumentoModel = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);
    }
	
	@After
    public void executedAfterEach() throws Exception {      
		 utx.begin();
         
		//remove all TipoDocumentoEntity
		tipoDocumentoModel = null;
		
		List<Object> listTipoDocumento = null;
		CriteriaQuery<Object> cqTipoDocumento = this.em.getCriteriaBuilder().createQuery();
		cqTipoDocumento.select(cqTipoDocumento.from(TipoDocumentoEntity.class));
		listTipoDocumento = this.em.createQuery(cqTipoDocumento).getResultList();		
		for (Object object : listTipoDocumento) {
			this.em.remove(object);
		}
		
		//remove all PersonaNaturalEntity
		List<Object> listPersonaNatural = null;
		CriteriaQuery<Object> cqPersonaNatural = this.em.getCriteriaBuilder().createQuery();
		cqPersonaNatural.select(cqPersonaNatural.from(PersonaNaturalEntity.class));
		listPersonaNatural = this.em.createQuery(cqPersonaNatural).getResultList();
		
		for (Object object : listPersonaNatural) {
			this.em.remove(object);
		}
		
		utx.commit();
    }
	   
	@Test
	public void addPersonaNatural() throws Exception {
		PersonaNaturalModel model = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				Calendar.getInstance().getTime(), Sexo.MASCULINO);
		
		log.info("Objeto " + model.toString() + " creado");
		log.info("SUCCESS");
	}
	
	/*@Test
	public void addPersonaNaturalUniqueTest() throws Exception {		
		PersonaNaturalModel model1 = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				Calendar.getInstance().getTime(), Sexo.MASCULINO);
		
		PersonaNaturalModel model2 = null;
		try {
			model2 = personaNaturalProvider.addPersonaNatural(
					"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
					Calendar.getInstance().getTime(), Sexo.MASCULINO);
		} catch (EJBTransactionRolledbackException e) {
			log.info("Objeto " + model1.toString() + "creado");
			log.info("Segundo Objeto no creado por tener el mismo TipoDocumento y NumeroDocumento");				
		} catch (Exception e) {
			log.error("No se creó el objeto por una excepcion que no es EntityExistsException");
			throw new Exception("No se creó el objeto por una excepcion que no es EJBTransactionRolledbackException");					
		}		
		
		if(model2 != null) {
			log.error("Existen dos personas con el mismo TipoDocumento y NumeroDocumento");
			throw new Exception("Existen dos personas con el mismo TipoDocumento y NumeroDocumento");	
		}
		
		log.info("SUCCESS");
	}
	
	@Test
	public void addPersonaNaturalNotnullAttibutesTest() throws Exception {		
		Object[] fields = new Object[8];
		fields[0] = "PER";
		fields[1] = tipoDocumentoModel;
		fields[2] = "12345678";
		fields[3] = "Flores";
		fields[4] = "Huertas";
		fields[5] = "Jhon wilber";
		fields[6] = Calendar.getInstance().getTime();
		fields[7] = Sexo.MASCULINO;
				
		for (int i = 0; i < fields.length; i++) {
			fields[i] = null;
			PersonaNaturalModel model = null;
			try {
				model = personaNaturalProvider.addPersonaNatural(
						(String) fields[0], (TipoDocumentoModel) fields[1], (String) fields[2], (String) fields[3], 
						(String) fields[4], (String) fields[5], (Date) fields[6], (Sexo) fields[7]);
			} catch (Exception e) {
				log.info(e.getMessage());				
			}		
			
			if(model != null) {
				log.error("No debe permitir un valor nulo para fields[" + i + "]");
				throw new Exception("No debe permitir un valor nulo para fields[" + i + "]");	
			}
		}
		
		log.info("SUCCESS");
	}
	

	@Test
	public void getPersonaNaturalById() throws Exception {
		PersonaNaturalModel model1 = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				Calendar.getInstance().getTime(), Sexo.MASCULINO);		
		
		Long id = model1.getId();
		
		PersonaNaturalModel model2 = personaNaturalProvider.getPersonaNaturalById(id);
		if(model1.equals(model2)) {
			log.info("Objeto:" + model1.toString() + " encontrado");				
		} else {
			log.error("Objeto creado:"+ model1.toString() + " no pudo ser encontrado");
			throw new Exception("Objeto creado:"+ model1.toString() + " no pudo ser encontrado");
		}
		
		log.info("SUCCESS");
	}
	
	@Test
	public void getPersonaNaturalByTipoNumeroDoc() throws Exception {		
		PersonaNaturalModel model1 = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				Calendar.getInstance().getTime(), Sexo.MASCULINO);				
		
		TipoDocumentoModel tipoDocumento = model1.getTipoDocumento();
		String numeroDocumento = model1.getNumeroDocumento();
		
		PersonaNaturalModel model2 = personaNaturalProvider.getPersonaNaturalByTipoNumeroDoc(tipoDocumento, numeroDocumento);
		if(model1.equals(model2)) {
			log.info("Objeto:" + model1.toString() + " encontrado");				
		} else {
			log.error("Objeto creado:"+ model1.toString() + " no pudo ser encontrado");
			throw new Exception("Objeto creado:"+ model1.toString() + " no pudo ser encontrado");
		}
		
		log.info("SUCCESS");
	}
	
	@Test
	public void getPersonasNaturales() throws Exception {										
		List<PersonaNaturalModel> models = personaNaturalProvider.getPersonasNaturales();
		for (PersonaNaturalModel personaNaturalModel : models) {
			log.info("Objeto:" + personaNaturalModel.toString() + " encontrado");	
		}
		
		log.info("SUCCESS");
	}
	
	@Test
	public void getPersonasNaturalesFirstResulAndLimit() throws Exception {										
		List<PersonaNaturalModel> models = personaNaturalProvider.getPersonasNaturales(0, 10);
		for (PersonaNaturalModel personaNaturalModel : models) {
			log.info("Objeto:" + personaNaturalModel.toString() + " encontrado");	
		}
		
		log.info("SUCCESS");
	}
	
	@Test
	public void searchForFilterText() throws Exception {										
		List<PersonaNaturalModel> models = personaNaturalProvider.searchForFilterText("f");
		for (PersonaNaturalModel personaNaturalModel : models) {
			log.info("Objeto:" + personaNaturalModel.toString() + " encontrado");	
		}
		
		log.info("SUCCESS");
	}
	
	@Test
	public void searchForFilterTextFirstResulAndLimit() throws Exception {										
		List<PersonaNaturalModel> models = personaNaturalProvider.searchForFilterText("f", 0, 10);
		for (PersonaNaturalModel personaNaturalModel : models) {
			log.info("Objeto:" + personaNaturalModel.toString() + " encontrado");	
		}
		
		log.info("SUCCESS");
	}
	
	@Test
	public void getPersonasNaturalesCount() throws Exception {	
		Long count1 = personaNaturalProvider.getPersonasNaturalesCount();
		
		CriteriaQuery<Object> cq = this.em.getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<PersonaNaturalEntity> rt = cq.from(PersonaNaturalEntity.class);
        cq.select(this.em.getCriteriaBuilder().count(rt));
        javax.persistence.Query q = this.em.createQuery(cq);
        Long count2 = (Long) q.getSingleResult();
        
        if(count1.compareTo(count2) != 0) {
        	log.error("Count " + count1 + " no es igual a " + count2);
			throw new Exception("Count " + count1 + " no es igual a " + count2);
        }
        
        log.info("SUCCESS");
	}
	
	@Test
	public void removePersonaNatural() throws Exception {	
		PersonaNaturalModel model = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				Calendar.getInstance().getTime(), Sexo.MASCULINO);				
		
		Long id = model.getId();
		
		boolean result = personaNaturalProvider.removePersonaNatural(model);
		if(result) {
			log.info("Resultado de eliminacion:" + result);
		} else {
			log.error("Resultado de eliminacion:" + result);
			throw new Exception("Resultado de eliminacion:" + result);
		}
				
		model = personaNaturalProvider.getPersonaNaturalById(id);
		if(model == null) {
			log.info("Objeto no encontrado");			
		} else {
			log.error("Objeto " + model.toString() + " encontrado, no fue eliminado");
			throw new Exception("Objeto encontrado, no fue eliminado");
		}	
		
		log.info("SUCCESS");
	}*/
	
}
