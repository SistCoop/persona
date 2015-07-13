package org.sistcoop.persona.models;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.runner.RunWith;
import org.sistcoop.persona.models.enums.TipoPersona;
import org.sistcoop.persona.models.jpa.JpaTipoDocumentoProvider;
import org.sistcoop.persona.models.jpa.entities.TipoDocumentoEntity;
import org.sistcoop.persona.models.jpa.search.filter.JpaTipoDocumentoFilterProvider;
import org.sistcoop.persona.models.search.SearchCriteriaFilterModel;
import org.sistcoop.persona.models.search.filters.TipoDocumentoFilterProvider;
import org.sistcoop.persona.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
@UsingDataSet("empty.xml")
public abstract class AbstractTest {

    protected Logger log = LoggerFactory.getLogger(TipoDocumentoProviderTest.class);

    @Deployment
    public static WebArchive createDeployment() {
        File[] dependencies = Maven.resolver().resolve("org.slf4j:slf4j-simple:1.7.10").withoutTransitivity()
                .asFile();

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "test.war")
                /** model-api **/
                .addPackage(Provider.class.getPackage())
                .addPackage(TipoDocumentoModel.class.getPackage())

                .addPackage(TipoPersona.class.getPackage())

                .addPackage(SearchCriteriaFilterModel.class.getPackage())
                .addPackage(TipoDocumentoFilterProvider.class.getPackage())

                /** model-jpa **/
                .addPackage(JpaTipoDocumentoProvider.class.getPackage())
                .addPackage(JpaTipoDocumentoFilterProvider.class.getPackage())
                .addPackage(TipoDocumentoEntity.class.getPackage())

                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsManifestResource("META-INF/test-jboss-deployment-structure.xml",
                        "jboss-deployment-structure.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml").addAsWebInfResource("test-ds.xml");

        war.addAsLibraries(dependencies);

        return war;
    }
}
