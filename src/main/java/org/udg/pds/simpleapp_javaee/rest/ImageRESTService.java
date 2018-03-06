package org.udg.pds.simpleapp_javaee.rest;

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
import java.io.*;
import java.util.List;
import java.util.Map;

@Path("/images")
@RequestScoped
public class ImageRESTService extends RESTService {

  @Inject
  Global global;

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public String upload(@Context HttpServletRequest req,
                       MultipartFormDataInput input) {

    Map<String, List<InputPart>> formParts = input.getFormDataMap();

    List<InputPart> inPart = formParts.get("file");

    for (InputPart inputPart : inPart) {

      try {
        // Retrieve headers, read the Content-Disposition header to obtain the original name of the file
        MultivaluedMap<String, String> headers = inputPart.getHeaders();
        String fileName = parseFileName(headers);

        // Handle the body of that part with an InputStream
        InputStream istream = inputPart.getBody(InputStream.class, null);

        fileName = global.getDataDir() + fileName;

        saveFile(istream, fileName);

      } catch (Exception e) {
        throw new WebApplicationException("Error saving file: " + e.getMessage());
      }
    }

    return "{}";
  }

  // Parse Content-Disposition header to get the original file name
  private String parseFileName(MultivaluedMap<String, String> headers) {

    String[] contentDispositionHeader = headers.getFirst("Content-Disposition").split(";");

    for (String name : contentDispositionHeader) {

      if ((name.trim().startsWith("filename"))) {

        String[] tmp = name.split("=");

        String fileName = tmp[1].trim().replaceAll("\"", "");

        return fileName;
      }
    }
    return "randomName";
  }

  // save uploaded file to a defined location on the server
  private void saveFile(InputStream uploadedInputStream,
                        String serverLocation) throws IOException {

    int read;
    byte[] bytes = new byte[1024];

    OutputStream outpuStream = new FileOutputStream(new File(serverLocation));
    while ((read = uploadedInputStream.read(bytes)) != -1) {
      outpuStream.write(bytes, 0, read);
    }
    outpuStream.flush();
    outpuStream.close();
  }
}
