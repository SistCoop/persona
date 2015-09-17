package org.sistcoop.persona.services.resources.admin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.sistcoop.persona.admin.client.resource.PersonaNaturalResource;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.PersonaNaturalProvider;
import org.sistcoop.persona.models.utils.ModelToRepresentation;
import org.sistcoop.persona.representations.idm.PersonaNaturalRepresentation;
import org.sistcoop.persona.services.ErrorResponse;
import org.sistcoop.persona.services.managers.PersonaNaturalManager;
import org.sistcoop.persona.services.util.GoogleDriveManager;

@Stateless
public class PersonaNaturalResourceImpl implements PersonaNaturalResource {

    @PathParam("personaNatural")
    private String personaNatural;

    @Inject
    private GoogleDriveManager googleDriveManager;

    @Inject
    private PersonaNaturalProvider personaNaturalProvider;

    @Inject
    private PersonaNaturalManager personaNaturalManager;

    private PersonaNaturalModel getPersonaNaturalModel() {
        return personaNaturalProvider.findById(personaNatural);
    }

    @Override
    public PersonaNaturalRepresentation toRepresentation() {
        PersonaNaturalRepresentation rep = ModelToRepresentation.toRepresentation(getPersonaNaturalModel());
        if (rep != null) {
            return rep;
        } else {
            throw new NotFoundException("PersonaNatural no encontrado");
        }
    }

    @Override
    public void update(PersonaNaturalRepresentation rep) {
        personaNaturalManager.update(getPersonaNaturalModel(), rep);
    }

    @Override
    public void setFoto(MultipartFormDataInput input) {
        PersonaNaturalModel model = getPersonaNaturalModel();

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");
        for (InputPart inputPart : inputParts) {
            try {
                // convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                byte[] bytes = IOUtils.toByteArray(inputStream);

                // writeFile(bytes, fileName);
                java.io.File UPLOAD_FILE = java.io.File.createTempFile("tpm-sg", ".tmp", null);
                FileOutputStream fos = new FileOutputStream(UPLOAD_FILE);
                fos.write(bytes);
                fos.close();

                // google drive
                String url = googleDriveManager.upload(UPLOAD_FILE, UUID.randomUUID().toString(), "image/*",
                        "Photo siscoop", "sistcoop_app", "ventura", "foto");
                model.setUrlFoto(url);
                model.commit();

            } catch (IOException e) {
                throw new InternalServerErrorException();
            } catch (GeneralSecurityException e) {
                throw new InternalServerErrorException();
            }
        }
    }

    @Override
    public void setFirma(MultipartFormDataInput input) {
        PersonaNaturalModel model = getPersonaNaturalModel();

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");
        for (InputPart inputPart : inputParts) {
            try {
                // convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                byte[] bytes = IOUtils.toByteArray(inputStream);

                // writeFile(bytes, fileName);
                java.io.File UPLOAD_FILE = java.io.File.createTempFile("tpm-sg", ".tmp", null);
                FileOutputStream fos = new FileOutputStream(UPLOAD_FILE);
                fos.write(bytes);
                fos.close();

                // google drive
                String url = googleDriveManager.upload(UPLOAD_FILE, UUID.randomUUID().toString(), "image/*",
                        "Photo siscoop", "sistcoop_app", "ventura", "firma");
                model.setUrlFirma(url);
                model.commit();

            } catch (IOException e) {
                throw new InternalServerErrorException();
            } catch (GeneralSecurityException e) {
                throw new InternalServerErrorException();
            }
        }
    }

    @Override
    public void disable() {
        throw new NotFoundException();
    }

    @Override
    public Response remove() {
        PersonaNaturalModel personaNatural = getPersonaNaturalModel();
        if (personaNatural == null) {
            throw new NotFoundException("PersonaNatural no encontrado");
        }
        boolean removed = personaNaturalProvider.remove(personaNatural);
        if (removed) {
            return Response.noContent().build();
        } else {
            return ErrorResponse.error("PersonaNatural no pudo ser eliminado", Response.Status.BAD_REQUEST);
        }
    }

}
