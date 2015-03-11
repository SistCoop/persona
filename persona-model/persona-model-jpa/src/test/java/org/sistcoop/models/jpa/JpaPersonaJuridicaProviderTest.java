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
import org.sistcoop.models.PersonaJuridicaModel;
import org.sistcoop.models.PersonaJuridicaProvider;
import org.sistcoop.models.PersonaNaturalModel;
import org.sistcoop.models.PersonaNaturalProvider;
import org.sistcoop.models.TipoDocumentoModel;
import org.sistcoop.models.TipoDocumentoProvider;
import org.sistcoop.models.enums.Sexo;
import org.sistcoop.models.enums.TipoEmpresa;
import org.sistcoop.models.enums.TipoPersona;
import org.sistcoop.models.jpa.entities.PersonaEntity;
import org.sistcoop.models.jpa.entities.PersonaJuridicaEntity;
import org.sistcoop.models.jpa.entities.PersonaNaturalEntity;
import org.sistcoop.models.jpa.entities.TipoDocumentoEntity;
import org.sistcoop.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public class JpaPersonaJuridicaProviderTest {

	Logger log = LoggerFactory.getLogger(JpaPersonaJuridicaProviderTest.class);

	@PersistenceContext
	private EntityManager em;

	@Resource           
	private UserTransaction utx; 
	
	@Inject
	private TipoDocumentoProvider tipoDocumentoProvider;
	
	@Inject
	private PersonaNaturalProvider personaNaturalProvider;
	
	@Inject
	private PersonaJuridicaProvider personaJuridicaProvider;		
	
	private TipoDocumentoModel tipoDocumentoModel;
	private PersonaNaturalModel representanteLegalModel;
	
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
				
				.addPackage(PersonaEntity.class.getPackage())
				
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("test-ds.xml");

		war.addAsLibraries(dependencies);

		return war;
	}		

	@Before
    public void executedBeforeEach() throws Exception {    
		tipoDocumentoModel = tipoDocumentoProvider.addTipoDocumento("RUC", "Registro unico de contribuyente", 8, TipoPersona.JURIDICA);
		
		representanteLegalModel = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				Calendar.getInstance().getTime(), Sexo.MASCULINO);					
    }
	
	@After
    public void executedAfterEach() throws Exception {      
		utx.begin();
	       
		//remove all PersonaJuridicaEntity
		List<Object> listPersonaJuridica = null;
		CriteriaQuery<Object> cqPersonaJuridica = this.em.getCriteriaBuilder().createQuery();
		cqPersonaJuridica.select(cqPersonaJuridica.from(PersonaJuridicaEntity.class));
		listPersonaJuridica = this.em.createQuery(cqPersonaJuridica).getResultList();
			
		for (Object object : listPersonaJuridica) {
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
	public void addPersonaJuridica() throws Exception {				
		PersonaJuridicaModel model = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel, "10467793549", 
				"Softgreen S.A.C.", Calendar.getInstance().getTime(), TipoEmpresa.PRIVADA, true);
		
		log.info("Objeto " + model.toString() + " creado");
		log.info("SUCCESS");
	}
	
	@Test
	public void addPersonaJuridicaUniqueTest() throws Exception {		
		PersonaJuridicaModel model1 = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel, "10467793549", 
				"Softgreen S.A.C.", Calendar.getInstance().getTime(), TipoEmpresa.PRIVADA, true);
				
		PersonaJuridicaModel model2 = null;
		try {
			model2 = personaJuridicaProvider.addPersonaJuridica(
					representanteLegalModel, "PER", tipoDocumentoModel, "10467793549", 
					"Softgreen S.A.C.", Calendar.getInstance().getTime(), TipoEmpresa.PRIVADA, true);
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
	public void addPersonaJuridicaNotnullAttibutesTest() throws Exception {		
		Object[] fields = new Object[8];
		fields[0] = representanteLegalModel;
		fields[1] = "PER";
		fields[2] = tipoDocumentoModel;
		fields[3] = "10467793549";
		fields[4] = "Softgreen S.A.C.";
		fields[5] = Calendar.getInstance().getTime();
		fields[6] = TipoEmpresa.PRIVADA;
		fields[7] = true;		
		
		Object aux = null;
		
		for (int i = 0; i < fields.length; i++) {
			aux = fields[i];
			fields[i] = null;
			if(i > 0)
				fields[i - 1] = aux;
			
			PersonaJuridicaModel model = null;
			try {
				model = personaJuridicaProvider.addPersonaJuridica(
						(PersonaNaturalModel) fields[0], (String) fields[1], (TipoDocumentoModel) fields[2], (String) fields[3], 
						(String) fields[4], (Date) fields[5], (TipoEmpresa) fields[6], (Boolean) fields[7]);
			} catch (EJBTransactionRolledbackException e) {
				log.info(e.getMessage());				
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
	public void getPersonaJuridicaById() throws Exception {
		PersonaJuridicaModel model1 = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel, "10467793549", 
				"Softgreen S.A.C.", Calendar.getInstance().getTime(), TipoEmpresa.PRIVADA, true);
			
		Long id = model1.getId();
		
		PersonaJuridicaModel model2 = personaJuridicaProvider.getPersonaJuridicaById(id);
		if(model1.equals(model2)) {
			log.info("Objeto:" + model1.toString() + " encontrado");				
		} else {
			log.error("Objeto creado:"+ model1.toString() + " no pudo ser encontrado");
			throw new Exception("Objeto creado:"+ model1.toString() + " no pudo ser encontrado");
		}
		
		log.info("SUCCESS");
	}
	
	@Test
	public void getPersonaJuridicaByTipoNumeroDoc() throws Exception {							
		PersonaJuridicaModel model1 = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel, "10467793549", 
				"Softgreen S.A.C.", Calendar.getInstance().getTime(), TipoEmpresa.PRIVADA, true);			
		
		TipoDocumentoModel tipoDocumento = model1.getTipoDocumento();
		String numeroDocumento = model1.getNumeroDocumento();
		
		PersonaJuridicaModel model2 = personaJuridicaProvider.getPersonaJuridicaByTipoNumeroDoc(tipoDocumento, numeroDocumento);
		if(model1.equals(model2)) {
			log.info("Objeto:" + model1.toString() + " encontrado");				
		} else {
			log.error("Objeto creado:"+ model1.toString() + " no pudo ser encontrado");
			throw new Exception("Objeto creado:"+ model1.toString() + " no pudo ser encontrado");
		}
		
		log.info("SUCCESS");
	}
	
	@Test
	public void getPersonasJuridicasCount() throws Exception {	
		Long count1 = personaJuridicaProvider.getPersonasJuridicasCount();
		
		CriteriaQuery<Object> cq = this.em.getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<PersonaJuridicaEntity> rt = cq.from(PersonaJuridicaEntity.class);
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
	public void removePersonaJuridica() throws Exception {	
		PersonaJuridicaModel model = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel, "10467793549", 
				"Softgreen S.A.C.", Calendar.getInstance().getTime(), TipoEmpresa.PRIVADA, true);								
		
		Long id = model.getId();
		
		boolean result = personaJuridicaProvider.removePersonaJuridica(model);
		if(result) {
			log.info("Resultado de eliminacion:" + result);
		} else {
			log.error("Resultado de eliminacion:" + result);
			throw new Exception("Resultado de eliminacion:" + result);
		}
				
		model = personaJuridicaProvider.getPersonaJuridicaById(id);
		if(model == null) {
			log.info("Objeto no encontrado");			
		} else {
			log.error("Objeto " + model.toString() + " encontrado, no fue eliminado");
			throw new Exception("Objeto encontrado, no fue eliminado");
		}	
		
		log.info("SUCCESS");
	}
	
}
