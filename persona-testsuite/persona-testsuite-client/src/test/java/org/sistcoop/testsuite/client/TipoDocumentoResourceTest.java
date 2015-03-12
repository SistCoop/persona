package org.sistcoop.testsuite.client;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sistcoop.JaxRsActivator;
import org.sistcoop.admin.client.resource.TipoDocumentoResource;
import org.sistcoop.representations.idm.TipoDocumentoRepresentation;
import org.sistcoop.services.resources.admin.TipoDocumentoResourceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
@UsingDataSet("empty.xml")
public class TipoDocumentoResourceTest {

	Logger log = LoggerFactory.getLogger(TipoDocumentoResourceTest.class);	
		
	@ArquillianResource
    private URL deploymentURL;	
	
	@Deployment(testable = false)
    public static WebArchive create()
    {
        return ShrinkWrap.create(WebArchive.class)
            .addPackage(TipoDocumentoRepresentation.class.getPackage())
            .addClasses(TipoDocumentoResource.class, TipoDocumentoResourceImpl.class, JaxRsActivator.class);
    }
			 	
    @Test
    public void getTipoDocumentoById(@ArquillianResteasyResource TipoDocumentoResource tipoDocumentoResource)
    {  
        final String name = "DNI";  
        final TipoDocumentoRepresentation result = tipoDocumentoResource.findById(name);   
        System.out.println(result);
    }
	
}
