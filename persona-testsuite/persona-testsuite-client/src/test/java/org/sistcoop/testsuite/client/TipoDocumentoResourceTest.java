package org.sistcoop.testsuite.client;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
@UsingDataSet("empty.xml")
public class TipoDocumentoResourceTest {

	Logger log = LoggerFactory.getLogger(TipoDocumentoResourceTest.class);

}
