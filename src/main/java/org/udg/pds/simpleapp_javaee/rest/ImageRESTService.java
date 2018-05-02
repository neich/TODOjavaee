package org.udg.pds.simpleapp_javaee.rest;

import com.sun.mail.iap.ByteArray;
import io.minio.MinioClient;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.udg.pds.simpleapp_javaee.util.Global;
import org.udg.pds.simpleapp_javaee.util.ToJSON;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/images")
@RequestScoped
public class ImageRESTService extends RESTService {

    @Inject
    Global global;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@Context HttpServletRequest req,
                           MultipartFormDataInput input) {

        MinioClient minioClient = global.getMinioClient();
        if (minioClient == null)
            throw new WebApplicationException("Minio client not configured");

        Map<String, List<InputPart>> formParts = input.getFormDataMap();


        List<String> images = new ArrayList<>();

        for (List<InputPart> inPart : input.getFormDataMap().values())
            for (InputPart inputPart : inPart) {

                if (!inputPart.getMediaType().getType().equals("image")) continue;

                try {
                    // Handle the body of that part with an InputStream
                    InputStream istream = inputPart.getBody(InputStream.class, null);
                    String contentType = inputPart.getMediaType().toString();
                    UUID imgName = UUID.randomUUID();

                    String objectName = imgName + "." + inputPart.getMediaType().getSubtype();
                    // Upload the file to the bucket with putObject
                    minioClient.putObject(global.getMinioBucket(),
                            objectName,
                            istream,
                            contentType);

                    images.add(global.getBaseURL() + "/rest/images/" + objectName);

                } catch (Exception e) {
                    throw new WebApplicationException("Error saving file: " + e.getMessage());
                }
            }

        return buildResponse(images);
    }

    @GET
    @Path("{filename}")
    public Response download(@PathParam("filename") String filename) {

        MinioClient minioClient = global.getMinioClient();
        if (minioClient == null)
            throw new WebApplicationException("Minio client not configured");

        try {
            InputStream file = minioClient.getObject(global.getMinioBucket(), filename);
            return Response.ok(IOUtils.toByteArray(file)).type("image/png").build();
        } catch (Exception e) {
            throw new WebApplicationException("Error downloading file: " + e.getMessage());
        }
    }

}
