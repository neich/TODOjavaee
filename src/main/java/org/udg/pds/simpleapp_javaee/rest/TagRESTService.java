package org.udg.pds.simpleapp_javaee.rest;

import org.udg.pds.simpleapp_javaee.service.TagService;
import org.udg.pds.simpleapp_javaee.util.ToJSON;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tags")
@RequestScoped
public class TagRESTService extends RESTService {

  @EJB
  TagService tagService;

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getTask(@Context HttpServletRequest req,
                        @PathParam("id") Long id) {
    Long userId = getLoggedUser(req);

    return buildResponse(tagService.getTag(id));

  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response listAllTags(@Context HttpServletRequest req) {
    Long userId = getLoggedUser(req);

    return buildResponse(tagService.getTags(userId));
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response addTag(@Valid R_Tag tag, @Context HttpServletRequest req) {

    Long userId = getLoggedUser(req);

    if (tag.description == null) {
      tag.description = "";
    }

    return buildResponse(tagService.addTag(tag.name, tag.description));
  }

  @DELETE
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteTag(@Context HttpServletRequest req,
                            @PathParam("id") Long tagId) {

    Long userId = getLoggedUser(req);

    return buildResponse(tagService.remove(tagId));
  }

  static class ID {
    public Long id;

    public ID(Long id) {
      this.id = id;
    }
  }


  static class R_Tag {
    @NotNull public String name;
    public String description;
  }

}
