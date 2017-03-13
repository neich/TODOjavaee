package org.udg.pds.simpleapp_javaee.rest.exceptionMapper;

import org.udg.pds.simpleapp_javaee.util.ToJSON;

import javax.inject.Inject;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class NotAcceptableExceptionMapper implements ExceptionMapper<NotAcceptableException> {
  @Inject ToJSON toJSON;

  @Override
  public Response toResponse(NotAcceptableException e) {
    return Response.serverError().entity(toJSON.buildError("Unknown error", e.toString())).type(MediaType.APPLICATION_JSON_TYPE).build();
  }
}

