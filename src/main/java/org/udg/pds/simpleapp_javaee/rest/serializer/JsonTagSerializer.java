package org.udg.pds.simpleapp_javaee.rest.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.udg.pds.simpleapp_javaee.model.Tag;
import org.udg.pds.simpleapp_javaee.model.Views;
import org.udg.pds.simpleapp_javaee.util.ToJSON;

import javax.ejb.LocalBean;
import javax.inject.Inject;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by imartin on 14/02/17.
 */
public class JsonTagSerializer extends JsonSerializer<Tag> {

  @Override
  public void serialize(Tag tag, JsonGenerator gen, SerializerProvider provider)
          throws IOException, JsonProcessingException {
    if (provider.getActiveView() == Views.UserProfile.class)
      gen.writeString(tag.getName());
    else {
      gen.writeStartObject();
      gen.writeNumberField("id", tag.getId());
      gen.writeStringField("name", tag.getName());
      gen.writeStringField("description", tag.getDescription());
      gen.writeEndObject();
    }
  }
}
