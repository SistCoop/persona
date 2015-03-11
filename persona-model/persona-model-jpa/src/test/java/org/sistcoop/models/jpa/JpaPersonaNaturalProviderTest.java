package org.sistcoop.models.jpa;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
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
		tipoDocumentoModel = tipoDocumentoProvider.addTipoDocumento("DNI", "Documento nacional de identidad", 8, TipoPersona.NATURAL);
    }
	
	@After
    public void executedAfterEach() throws NotSupportedException, 
    SystemException, SecurityException, IllegalStateException, 
    RollbackException, HeuristicMixedException, HeuristicRollbackException {      
		
		utx.begin();
       
		//remove all PersonaNaturalEntity
		List<Object> listPersonaNatural = null;
		CriteriaQuery<Object> cqPersonaNatural = this.em.getCriteriaBuilder().createQuery();
		cqPersonaNatural.select(cqPersonaNatural.from(PersonaNaturalEntity.class));
		listPersonaNatural = this.em.createQuery(cqPersonaNatural).getResultList();
			
		for (Object object : listPersonaNatural) {
			this.em.remove(object);
		}
			
		//remove all TipoDocumentoEntity
		tipoDocumentoModel = null;
		
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
	public void addPersonaNatural() {
		PersonaNaturalModel model = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				Calendar.getInstance().getTime(), Sexo.MASCULINO);
		
		assertThat(model, is(notNullValue()));
	}
	
	@Test
	public void addPersonaNaturalUniqueTest()  {		
		PersonaNaturalModel model1 = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				Calendar.getInstance().getTime(), Sexo.MASCULINO);
		
		PersonaNaturalModel model2 = null;
		try {
			model2 = personaNaturalProvider.addPersonaNatural(
					"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
					Calendar.getInstance().getTime(), Sexo.MASCULINO);
		} catch (Exception e) {		
			assertThat(e, instanceOf(EJBException.class));						
		}	
		
		assertThat(model1, is(notNullValue()));
		assertThat(model2, is(nullValue()));
	}
	
	/*@SuppressWarnings("unchecked")
	@Test
	public void addPersonaNaturalNotnullAttibutesTest()  {		
		Object[] fields = new Object[8];
		fields[0] = "PER";
		fields[1] = tipoDocumentoModel;
		fields[2] = "12345678";
		fields[3] = "Flores";
		fields[4] = "Huertas";
		fields[5] = "Jhon wilber";
		fields[6] = Calendar.getInstance().getTime();
		fields[7] = Sexo.MASCULINO;
				
		Object aux = null;
		
		for (int i = 0; i < fields.length; i++) {
			if(i > 0)
				fields[i - 1] = aux;
			aux = fields[i];
			fields[i] = null;			
			
			PersonaNaturalModel model = null;
			try {
				model = personaNaturalProvider.addPersonaNatural(
						(String) fields[0], (TipoDocumentoModel) fields[1], (String) fields[2], (String) fields[3], 
						(String) fields[4], (String) fields[5], (Date) fields[6], (Sexo) fields[7]);
			} catch (Exception e) {		
				assertThat(e, anyOf(instanceOf(NullPointerException.class), instanceOf(EJBException.class)));					
			}	
			
			assertThat(model, is(nullValue()));
		}			
	}*/	

	@Test
	public void getPersonaNaturalById()  {
		PersonaNaturalModel model1 = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				Calendar.getInstance().getTime(), Sexo.MASCULINO);		
		
		Long id = model1.getId();		
		PersonaNaturalModel model2 = personaNaturalProvider.getPersonaNaturalById(id);
		
		assertThat(model1, is(equalTo(model2)));
	}
	
	@Test
	public void getPersonaNaturalByTipoNumeroDoc()  {							
		PersonaNaturalModel model1 = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				Calendar.getInstance().getTime(), Sexo.MASCULINO);	
		
		TipoDocumentoModel tipoDocumento = model1.getTipoDocumento();
		String numeroDocumento = model1.getNumeroDocumento();		
		PersonaNaturalModel model2 = personaNaturalProvider.getPersonaNaturalByTipoNumeroDoc(tipoDocumento, numeroDocumento);
		
		assertThat(model1, is(equalTo(model2)));
	}
	
	@Test
	public void getPersonasNaturales()  {										
		List<PersonaNaturalModel> models = personaNaturalProvider.getPersonasNaturales();
		for (PersonaNaturalModel personaNaturalModel : models) {
			assertThat(personaNaturalModel, is(notNullValue()));	
		}		
	}
	
	@Test
	public void getPersonasNaturalesFirstResulAndLimit()  {										
		List<PersonaNaturalModel> models = personaNaturalProvider.getPersonasNaturales(0, 10);
		for (PersonaNaturalModel personaNaturalModel : models) {
			assertThat(personaNaturalModel, is(notNullValue()));
		}
	}
	
	@Test
	public void searchForFilterText()  {										
		List<PersonaNaturalModel> models = personaNaturalProvider.searchForFilterText("f");
		for (PersonaNaturalModel personaNaturalModel : models) {
			assertThat(personaNaturalModel, is(notNullValue()));
		}		
	}
	
	@Test
	public void searchForFilterTextFirstResulAndLimit()  {										
		List<PersonaNaturalModel> models = personaNaturalProvider.searchForFilterText("f", 0, 10);
		for (PersonaNaturalModel personaNaturalModel : models) {
			assertThat(personaNaturalModel, is(notNullValue()));
		}		
	}
	
	@Test
	public void getPersonasNaturalesCount()  {	
		Long count1 = personaNaturalProvider.getPersonasNaturalesCount();
		
		CriteriaQuery<Object> cq = this.em.getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<PersonaNaturalEntity> rt = cq.from(PersonaNaturalEntity.class);
        cq.select(this.em.getCriteriaBuilder().count(rt));
        javax.persistence.Query q = this.em.createQuery(cq);
        Long count2 = (Long) q.getSingleResult();
                          
        assertThat(count1, is(equalTo(count2)));        
	}
	
	@Test
	public void removePersonaNatural()  {	
		PersonaNaturalModel model1 = personaNaturalProvider.addPersonaNatural(
				"PER", tipoDocumentoModel, "12345678", "Flores", "Huertas", "Jhon wilber", 
				Calendar.getInstance().getTime(), Sexo.MASCULINO);				
		
		Long id = model1.getId();		
		boolean result = personaNaturalProvider.removePersonaNatural(model1);
		
		PersonaNaturalModel model2 = personaNaturalProvider.getPersonaNaturalById(id);
		
		assertThat(result, is(true));
		assertThat(model2, is(nullValue()));				
	}
	
}
