package org.sistcoop.persona.admin.client.resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
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
import org.sistcoop.persona.models.AccionistaProvider;
import org.sistcoop.persona.models.PersonaJuridicaModel;
import org.sistcoop.persona.models.PersonaJuridicaProvider;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.PersonaNaturalProvider;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.enums.Sexo;
import org.sistcoop.persona.models.enums.TipoEmpresa;
import org.sistcoop.persona.models.enums.TipoPersona;
import org.sistcoop.persona.models.jpa.AccionistaAdapter;
import org.sistcoop.persona.models.jpa.JpaAccionistaProvider;
import org.sistcoop.persona.models.jpa.JpaPersonaJuridicaProvider;
import org.sistcoop.persona.models.jpa.JpaPersonaNaturalProvider;
import org.sistcoop.persona.models.jpa.JpaTipoDocumentoProvider;
import org.sistcoop.persona.models.jpa.PersonaJuridicaAdapter;
import org.sistcoop.persona.models.jpa.PersonaNaturalAdapter;
import org.sistcoop.persona.models.jpa.TipoDocumentoAdapter;
import org.sistcoop.persona.models.jpa.entities.PersonaJuridicaEntity;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.models.utils.RepresentationToModel;
import org.sistcoop.persona.provider.Provider;
import org.sistcoop.persona.representations.idm.AccionistaRepresentation;
import org.sistcoop.persona.representations.idm.PersonaJuridicaRepresentation;
import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;
import org.sistcoop.persona.representations.idm.TipoDocumentoRepresentation;
import org.sistcoop.persona.services.resources.admin.AccionistaResourceImpl;
import org.sistcoop.persona.services.resources.admin.PersonaJuridicaResourceImpl;
import org.sistcoop.persona.services.resources.admin.PersonaNaturalResourceImpl;
import org.sistcoop.persona.services.resources.admin.PersonasJuridicasResourceImpl;
import org.sistcoop.persona.services.resources.admin.TipoDocumentoResourceImpl;

@RunWith(Arquillian.class)
@UsingDataSet("empty.xml")
public class PersonaJuridicaResourceTest {

	@ArquillianResource
	private URL deploymentURL;
	
	@Deployment(testable = false)
	public static WebArchive create() {
		return ShrinkWrap.create(WebArchive.class)

		/**client**/
		.addClass(PersonaJuridicaResource.class)
		.addClass(PersonaJuridicaResourceImpl.class)
		
		.addClass(PersonasJuridicasResource.class)
		.addClass(PersonasJuridicasResourceImpl.class)
		
		.addClass(TipoDocumentoResource.class)
		.addClass(TipoDocumentoResourceImpl.class)
		
		.addClass(PersonaNaturalResource.class)
		.addClass(PersonaNaturalResourceImpl.class)
		
		.addClass(AccionistaResource.class)
		.addClass(AccionistaResourceImpl.class)
		
		.addClass(JaxRsActivator.class)
		
		/**core*/
		.addPackage(PersonaJuridicaRepresentation.class.getPackage())
		
		/**model-api**/
		.addClass(Provider.class)										
		.addClass(TipoDocumentoProvider.class)
		.addClass(PersonaNaturalProvider.class)
		.addClass(PersonaJuridicaProvider.class)
		.addClass(AccionistaProvider.class)
				
		.addPackage(PersonaJuridicaModel.class.getPackage())
		.addPackage(TipoPersona.class.getPackage())
				
		.addClass(ModelToRepresentation.class)										
		.addClass(RepresentationToModel.class)
		
		/**model-jpa**/
		.addClass(JpaTipoDocumentoProvider.class)
		.addClass(TipoDocumentoAdapter.class)

		.addClass(JpaPersonaNaturalProvider.class)
		.addClass(PersonaNaturalAdapter.class)
		
		.addClass(JpaPersonaJuridicaProvider.class)
		.addClass(PersonaJuridicaAdapter.class)
		
		.addClass(JpaAccionistaProvider.class)
		.addClass(AccionistaAdapter.class)
		
		.addPackage(PersonaJuridicaEntity.class.getPackage())
				
		.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
		.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
		.addAsManifestResource(EmptyAsset.INSTANCE, "web.xml")
		.addAsWebInfResource("test-ds.xml");
		
	}

	@Test
	public void getAccionistas(
			@ArquillianResteasyResource TipoDocumentoResource tipoDocumentoResource,
			@ArquillianResteasyResource PersonaNaturalResource personaNaturalResource,
			@ArquillianResteasyResource PersonaJuridicaResource personaJuridicaResource) {
		
		TipoDocumentoRepresentation tipoDocumentoRepresentation1 = new TipoDocumentoRepresentation();	
		tipoDocumentoRepresentation1.setAbreviatura("DNI");
		tipoDocumentoRepresentation1.setDenominacion("Documento nacional de identidad");
		tipoDocumentoRepresentation1.setTipoPersona("NATURAL");
		tipoDocumentoRepresentation1.setCantidadCaracteres(8);					
		tipoDocumentoResource.create(tipoDocumentoRepresentation1);
		
		TipoDocumentoRepresentation tipoDocumentoRepresentation2 = new TipoDocumentoRepresentation();	
		tipoDocumentoRepresentation2.setAbreviatura("RUC");
		tipoDocumentoRepresentation2.setDenominacion("Documento nacional de identidad");
		tipoDocumentoRepresentation2.setTipoPersona("NATURAL");
		tipoDocumentoRepresentation2.setCantidadCaracteres(8);					
		tipoDocumentoResource.create(tipoDocumentoRepresentation2);
		
		PersonaNaturalRepresentation personaNaturalRepresentation = new PersonaNaturalRepresentation();
		personaNaturalRepresentation.setCodigoPais("PEN");
		personaNaturalRepresentation.setTipoDocumento("DNI");
		personaNaturalRepresentation.setNumeroDocumento("46779354");
		personaNaturalRepresentation.setApellidoPaterno("Feria");
		personaNaturalRepresentation.setApellidoMaterno("Vila");
		personaNaturalRepresentation.setNombres("Carlos");
		personaNaturalRepresentation.setSexo("MASCULINO");
		personaNaturalRepresentation.setEstadoCivil("SOLTERO");
		personaNaturalRepresentation.setFechaNacimiento(Calendar.getInstance().getTime());
		personaNaturalResource.create(personaNaturalRepresentation);		
		
		PersonaJuridicaRepresentation personaJuridicaRepresentation = new PersonaJuridicaRepresentation();
		personaJuridicaRepresentation.setCodigoPais("PEN");
		personaJuridicaRepresentation.setTipoDocumento("RUC");
		personaJuridicaRepresentation.setNumeroDocumento("10467793549");
		personaJuridicaRepresentation.setRazonSocial("Softgreen");
		personaJuridicaRepresentation.setFechaConstitucion(Calendar.getInstance().getTime());
		personaJuridicaRepresentation.setTipoEmpresa("PRIVADA");
		personaJuridicaRepresentation.setFinLucro(true);
		personaJuridicaRepresentation.setRepresentanteLegal(personaNaturalRepresentation);
		personaJuridicaResource.create(personaJuridicaRepresentation);
		
		AccionistaRepresentation accionistaRepresentation = new AccionistaRepresentation();
		accionistaRepresentation.setPersonaNatural(personaNaturalRepresentation);
		accionistaRepresentation.setPorcentajeParticipacion(BigDecimal.TEN);		
		personaJuridicaResource.addAccionista(1L, accionistaRepresentation);
		
		List<AccionistaRepresentation> accionistas = personaJuridicaResource.findAllAccionistas(1L);
		
		assertThat(accionistas, is(notNullValue()));
		assertThat(accionistas.size(), is(equalTo(1)));
	}

}
