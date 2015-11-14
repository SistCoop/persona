package org.sistcoop.persona.admin.client.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;

/**
 * @author carlosthe19916@gmail.com
 */

public interface PersonaNaturalResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PersonaNaturalRepresentation toRepresentation();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(PersonaNaturalRepresentation rep);

    @POST
    @Path("foto")
    @Consumes("multipart/form-data")
    public void setFoto(MultipartFormDataInput input);

    @POST
    @Path("firma")
    @Consumes("multipart/form-data")
    public void setFirma(MultipartFormDataInput input);

    @POST
    @Path("disable")
    public void disable();

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove();

}