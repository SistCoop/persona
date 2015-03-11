package org.sistcoop.models.jpa;

import java.io.File;
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
import org.sistcoop.models.TipoDocumentoModel;
import org.sistcoop.models.TipoDocumentoProvider;
import org.sistcoop.models.enums.TipoPersona;
import org.sistcoop.models.jpa.entities.TipoDocumentoEntity;
import org.sistcoop.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public class TipoDocumentoAdapterTest {

	Logger log = LoggerFactory.getLogger(TipoDocumentoAdapterTest.class);

	@PersistenceContext
	private EntityManager em;

	@Resource           
	private UserTransaction utx; 
	
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
				.addClass(TipoDocumentoProvider.class)
				.addPackage(TipoDocumentoModel.class.getPackage())
				.addPackage(TipoPersona.class.getPackage())
				
				/**persona-model-jpa**/
				.addClass(JpaTipoDocumentoProvider.class)
				.addClass(TipoDocumentoAdapter.class)
				.addClass(TipoDocumentoEntity.class)
				
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
		
		utx.commit();
    }
	   
	@Test
	public void addTipoDocumento() throws Exception {
		TipoDocumentoEntity tipoDocumentoEntity = TipoDocumentoAdapter.toTipoDocumentoEntity(tipoDocumentoModel, em);
		if(tipoDocumentoEntity != null) {
			log.info("Entity:" + tipoDocumentoEntity.toString());
		} else {
			log.error("Entity es NULL");
			throw new Exception("Entity es NULL");
		}
		
		log.info("SUCCESS");
	}
	
	
	
}
