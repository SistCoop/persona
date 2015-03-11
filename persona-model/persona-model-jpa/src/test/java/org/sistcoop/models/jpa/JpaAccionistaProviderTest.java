package org.sistcoop.models.jpa;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
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
import org.sistcoop.models.AccionistaModel;
import org.sistcoop.models.AccionistaProvider;
import org.sistcoop.models.PersonaJuridicaModel;
import org.sistcoop.models.PersonaJuridicaProvider;
import org.sistcoop.models.PersonaNaturalModel;
import org.sistcoop.models.PersonaNaturalProvider;
import org.sistcoop.models.TipoDocumentoModel;
import org.sistcoop.models.TipoDocumentoProvider;
import org.sistcoop.models.enums.Sexo;
import org.sistcoop.models.enums.TipoEmpresa;
import org.sistcoop.models.enums.TipoPersona;
import org.sistcoop.models.jpa.entities.AccionistaEntity;
import org.sistcoop.models.jpa.entities.PersonaEntity;
import org.sistcoop.models.jpa.entities.PersonaJuridicaEntity;
import org.sistcoop.models.jpa.entities.PersonaNaturalEntity;
import org.sistcoop.models.jpa.entities.TipoDocumentoEntity;
import org.sistcoop.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public class JpaAccionistaProviderTest {

	Logger log = LoggerFactory.getLogger(JpaAccionistaProviderTest.class);

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
	
	@Inject
	private AccionistaProvider accionistaProvider;
	
	private PersonaJuridicaModel personaJuridicaModel;	
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
    public void executedBeforeEach()  {    
		TipoDocumentoModel tipoDocumentoModel1 = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);
		TipoDocumentoModel tipoDocumentoModel2 = tipoDocumentoProvider.addTipoDocumento("RUC", "Registro unico de contribuyente", 11, TipoPersona.JURIDICA);
				
		representanteLegalModel = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel1, "12345678", "Flores", "Huertas", "Jhon wilber", 
				Calendar.getInstance().getTime(), Sexo.MASCULINO);
		
		personaJuridicaModel = personaJuridicaProvider.addPersonaJuridica(
				representanteLegalModel, "PER", tipoDocumentoModel2, "10467793549", 
				"Softgreen S.A.C.", Calendar.getInstance().getTime(), TipoEmpresa.PRIVADA, true);								
    }
	
	@After
    public void executedAfterEach() throws NotSupportedException, 
    SystemException, SecurityException, IllegalStateException, 
    RollbackException, HeuristicMixedException, HeuristicRollbackException {      
		
		utx.begin();
	     
		//remove all AccionistaEntity
		List<Object> listAccionista = null;
		CriteriaQuery<Object> cqAccionista = this.em.getCriteriaBuilder().createQuery();
		cqAccionista.select(cqAccionista.from(AccionistaEntity.class));
		listAccionista = this.em.createQuery(cqAccionista).getResultList();
			
		for (Object object : listAccionista) {
			this.em.remove(object);
		}
				
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
	public void addAccionista() {
		AccionistaModel model = accionistaProvider.addAccionista(personaJuridicaModel, representanteLegalModel, BigDecimal.TEN);
		
		assertThat(model, is(notNullValue()));
	}
	
	public void addAccionistaUniqueTest()  {		
		AccionistaModel model1 = accionistaProvider.addAccionista(personaJuridicaModel, representanteLegalModel, BigDecimal.TEN);
		
		AccionistaModel model2 = null;
		try {
			model2 = accionistaProvider.addAccionista(personaJuridicaModel, representanteLegalModel, BigDecimal.TEN);
		} catch (Exception e) {		
			assertThat(e, instanceOf(EJBException.class));						
		}	
		
		assertThat(model1, is(notNullValue()));
		assertThat(model2, is(nullValue()));
	}
	
	@Test
	public void getAccionistaById()  {		
		AccionistaModel model1 =  accionistaProvider.addAccionista(personaJuridicaModel, representanteLegalModel, BigDecimal.TEN);
		
		Long id = model1.getId();		
		AccionistaModel model2 = accionistaProvider.getAccionistaById(id);
		
		assertThat(model1, is(equalTo(model2)));
	}
	
	@Test
	public void removeAccionista()  {	
		AccionistaModel model1 =  accionistaProvider.addAccionista(personaJuridicaModel, representanteLegalModel, BigDecimal.TEN);		
		
		Long id = model1.getId();		
		boolean result = accionistaProvider.removeAccionista(model1);
		
		AccionistaModel model2 = accionistaProvider.getAccionistaById(id);
		
		assertThat(result, is(true));
		assertThat(model2, is(nullValue()));				
	}
	
}
