package org.sistcoop.models.jpa;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
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
public class PersonaJuridicaAdapterTest {

	Logger log = LoggerFactory.getLogger(PersonaJuridicaAdapterTest.class);

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
	
	private PersonaJuridicaModel personaJuridicaModel;
	
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
		TipoDocumentoModel tipoDocumentoModel = tipoDocumentoProvider.addTipoDocumento("RUC", "Registro unico de contribuyente", 8, TipoPersona.JURIDICA);
		
		PersonaNaturalModel personaNaturalModel = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				Calendar.getInstance().getTime(), Sexo.MASCULINO);
		
		personaJuridicaModel = personaJuridicaProvider.addPersonaJuridica(
				personaNaturalModel, "PER", tipoDocumentoModel, "10467793549", 
				"Softgreen S.A.C.", Calendar.getInstance().getTime(), TipoEmpresa.PRIVADA, true);
		
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
	public void addTipoDocumento() throws Exception {
		PersonaJuridicaEntity personaJuridicaEntity = PersonaJuridicaAdapter.toPersonaJuridicaEntity(personaJuridicaModel, em);		
		if(personaJuridicaEntity != null) {
			log.info("Entity:" + personaJuridicaEntity.toString());
		} else {
			log.error("Entity es NULL");
			throw new Exception("Entity es NULL");
		}
		
		log.info("SUCCESS");
	}
	
	
	
}
