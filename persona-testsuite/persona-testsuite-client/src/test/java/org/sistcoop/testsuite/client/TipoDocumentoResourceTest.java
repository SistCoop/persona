package org.sistcoop.testsuite.client;

import java.io.File;
import java.net.URL;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
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
	
	@Inject
	private TipoDocumentoResource tipoDocumentoResource;
	
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
    }
	
}
