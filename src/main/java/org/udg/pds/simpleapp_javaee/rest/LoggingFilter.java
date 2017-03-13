package org.udg.pds.simpleapp_javaee.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class LoggingFilter implements ContainerResponseFilter {

  private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

  public void filter(ContainerRequestContext requestContext,
                     ContainerResponseContext responseContext) throws IOException {
    String method = requestContext.getMethod();

    logger.info("Requesting " + method + " for path " + requestContext.getUriInfo().getPath() + " with MIME type " + requestContext.getMediaType());
    // String json = IOUtils.toString(requestContext.getEntityStream());
    Object entity = responseContext.getEntity();
    if (entity != null) {
      logger.info("Response " + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(entity));
    }

  }
}