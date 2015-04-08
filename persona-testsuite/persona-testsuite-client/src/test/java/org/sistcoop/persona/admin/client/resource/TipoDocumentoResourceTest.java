package org.sistcoop.persona.admin.client.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URL;

import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sistcoop.persona.JaxRsActivator;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.enums.TipoPersona;
import org.sistcoop.persona.models.jpa.JpaTipoDocumentoProvider;
import org.sistcoop.persona.models.jpa.TipoDocumentoAdapter;
import org.sistcoop.persona.models.jpa.entities.TipoDocumentoEntity;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.models.utils.RepresentationToModel;
import org.sistcoop.persona.provider.Provider;
import org.sistcoop.persona.representations.idm.TipoDocumentoRepresentation;
import org.sistcoop.persona.services.resources.admin.TipoDocumentoResourceImpl;
import org.sistcoop.persona.services.resources.admin.TiposDocumentoResourceImpl;

@RunWith(Arquillian.class)
@UsingDataSet("empty.xml")
public class TipoDocumentoResourceTest {

	@ArquillianResource
	private URL deploymentURL;

	@Deployment(testable = false)
	public static WebArchive create() {
		return ShrinkWrap.create(WebArchive.class)

		/**client**/
		.addClass(TipoDocumentoResource.class)
		.addClass(TipoDocumentoResourceImpl.class)
		
		.addClass(TiposDocumentoResource.class)
		.addClass(TiposDocumentoResourceImpl.class)
		
		.addClass(JaxRsActivator.class)
		
		/**core*/
		.addPackage(TipoDocumentoRepresentation.class.getPackage())
		
		/**model-api**/
		.addClass(Provider.class)										
		.addClass(TipoDocumentoProvider.class)
				
		.addPackage(TipoDocumentoModel.class.getPackage())
		.addPackage(TipoPersona.class.getPackage())
				
		.addClass(ModelToRepresentation.class)										
		.addClass(RepresentationToModel.class)
		
		/**model-jpa**/
		.addClass(JpaTipoDocumentoProvider.class)
		.addClass(TipoDocumentoAdapter.class)

		.addPackage(TipoDocumentoEntity.class.getPackage())
				
		.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
		.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
		.addAsManifestResource(EmptyAsset.INSTANCE, "web.xml")
		.addAsWebInfResource("test-ds.xml");
		
	}

	@Test
	public void create(@ArquillianResteasyResource TipoDocumentoResource tipoDocumentoResource) {
		//create
		final TipoDocumentoRepresentation tipoDocumentoRepresentation = new TipoDocumentoRepresentation();
		tipoDocumentoRepresentation.setAbreviatura("DNI");
		tipoDocumentoRepresentation.setDenominacion("Documento nacional de identidad");
		tipoDocumentoRepresentation.setTipoPersona("NATURAL");
		tipoDocumentoRepresentation.setCantidadCaracteres(8);
				
		final Response response = tipoDocumentoResource.create(tipoDocumentoRepresentation);
		
		assertThat(response.getStatus(), is(201));
	}
	
	@Test
	public void findById(@ArquillianResteasyResource TipoDocumentoResource tipoDocumentoResource) {		
		TipoDocumentoRepresentation tipoDocumentoRepresentation1 = new TipoDocumentoRepresentation();
		tipoDocumentoRepresentation1.setAbreviatura("DNI");
		tipoDocumentoRepresentation1.setDenominacion("Documento nacional de identidad");
		tipoDocumentoRepresentation1.setTipoPersona("NATURAL");
		tipoDocumentoRepresentation1.setCantidadCaracteres(8);
		tipoDocumentoResource.create(tipoDocumentoRepresentation1);
		
		TipoDocumentoRepresentation tipoDocumentoRepresentation2 = tipoDocumentoResource.findById("DNI");
		
		assertThat(tipoDocumentoRepresentation2, is(notNullValue()));
	}
	
	@Test
	public void update(@ArquillianResteasyResource TipoDocumentoResource tipoDocumentoResource) {		
		TipoDocumentoRepresentation tipoDocumentoRepresentation = new TipoDocumentoRepresentation();
		tipoDocumentoRepresentation.setAbreviatura("DNI");
		tipoDocumentoRepresentation.setDenominacion("Documento nacional de identidad");
		tipoDocumentoRepresentation.setTipoPersona("NATURAL");
		tipoDocumentoRepresentation.setCantidadCaracteres(8);
		tipoDocumentoResource.create(tipoDocumentoRepresentation);
		
		tipoDocumentoRepresentation.setDenominacion("Prueba");
		tipoDocumentoResource.update("DNI", tipoDocumentoRepresentation);
	}
	
	@Test
	public void delete(@ArquillianResteasyResource TipoDocumentoResource tipoDocumentoResource) {		
		TipoDocumentoRepresentation tipoDocumentoRepresentation = new TipoDocumentoRepresentation();
		tipoDocumentoRepresentation.setAbreviatura("DNI");
		tipoDocumentoRepresentation.setDenominacion("Documento nacional de identidad");
		tipoDocumentoRepresentation.setTipoPersona("NATURAL");
		tipoDocumentoRepresentation.setCantidadCaracteres(8);
		tipoDocumentoResource.create(tipoDocumentoRepresentation);
		
		tipoDocumentoResource.delete("DNI");
	}

}
