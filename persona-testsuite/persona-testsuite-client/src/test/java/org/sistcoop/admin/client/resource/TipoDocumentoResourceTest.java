package org.sistcoop.admin.client.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sistcoop.JaxRsActivator;
import org.sistcoop.models.TipoDocumentoModel;
import org.sistcoop.models.TipoDocumentoProvider;
import org.sistcoop.models.enums.TipoPersona;
import org.sistcoop.models.jpa.JpaTipoDocumentoProvider;
import org.sistcoop.models.jpa.TipoDocumentoAdapter;
import org.sistcoop.models.utils.ModelToRepresentation;
import org.sistcoop.models.utils.RepresentationToModel;
import org.sistcoop.provider.Provider;
import org.sistcoop.representations.idm.TipoDocumentoRepresentation;
import org.sistcoop.services.resources.admin.TipoDocumentoResourceImpl;
import org.sistcoop.services.resources.admin.TiposDocumentoResourceImpl;

@RunWith(Arquillian.class)
public class TipoDocumentoResourceTest {

	@ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive create()
    {
        return ShrinkWrap.create(WebArchive.class)
        	/**persona-core*/
            .addPackage(TipoDocumentoRepresentation.class.getPackage())
            
            /**persona-core-jaxrs*/
            .addPackage(JaxRsActivator.class.getPackage())
            
            /**persona-client*/ 
            .addClasses(TipoDocumentoResource.class, TiposDocumentoResource.class)            
            
            /**persona-services*/
            .addClasses(TipoDocumentoResourceImpl.class, TiposDocumentoResourceImpl.class)
            
            /**persona-model-api*/
            .addClass(Provider.class)
            .addClass(TipoDocumentoProvider.class)
            .addPackage(TipoDocumentoModel.class.getPackage())
            .addPackage(TipoPersona.class.getPackage())         
            
            .addClass(RepresentationToModel.class)
            .addClass(ModelToRepresentation.class)
            
            /**persona-model-jpa*/
            .addClass(TipoDocumentoAdapter.class)
            .addClass(JpaTipoDocumentoProvider.class)                      
            
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("test-ds.xml");
    }
    
    @Test
    public void findAll(@ArquillianResteasyResource TipoDocumentoResource tipoDocumentoResource)
    {   
    	TipoDocumentoRepresentation tipoDocumentoRepresentation = new TipoDocumentoRepresentation();
    	tipoDocumentoRepresentation.setAbreviatura("DNI");
    	tipoDocumentoRepresentation.setDenominacion("Documento Nacional de identidad");    	
    	tipoDocumentoRepresentation.setCantidadCaracteres(8);
    	tipoDocumentoRepresentation.setTipoPersona(TipoPersona.NATURAL.toString());
    	
    	assertThat(tipoDocumentoResource, is(notNullValue()));    	
    	tipoDocumentoResource.create(tipoDocumentoRepresentation);
    	//assertThat(tipoDocumentoRepresentations, is(notNullValue()));
    }
    
}
