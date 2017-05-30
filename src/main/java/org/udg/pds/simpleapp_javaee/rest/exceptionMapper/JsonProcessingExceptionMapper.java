package org.udg.pds.simpleapp_javaee.rest.exceptionMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.udg.pds.simpleapp_javaee.util.ToJSON;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {
  @Inject ToJSON toJSON;

  @Override
  public Response toResponse(JsonProcessingException e) {
      return Response.serverError().entity(toJSON.buildError("JSON processing error", e.getOriginalMessage())).type(MediaType.APPLICATION_JSON_TYPE).build();
  }
}

