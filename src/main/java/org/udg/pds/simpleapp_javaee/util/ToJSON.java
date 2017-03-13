/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.udg.pds.simpleapp_javaee.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.udg.pds.simpleapp_javaee.model.Error;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @author imartin
 */
@Singleton
public class ToJSON {

  ObjectMapper mapper;

  @PostConstruct
  void init() {
      mapper = new ObjectMapper();
  }

  public String Object(Object o) throws IOException {
    StringWriter sw = new StringWriter();
    mapper.writeValue(sw, o);
    return sw.toString();
  }

  public String Object(Class<?> view, Object o) throws IOException {
    StringWriter sw = new StringWriter();
    mapper.writerWithView(view).writeValue(sw, o);
    return sw.toString();
  }

  public String buildError(String type, String message) {
    try {
      return this.Object(new Error(type, message));
    } catch (Exception e) {
      return "{\"type\": \"Serialize error\", \"message\": \"" + e.getMessage() + "\", \"originalMsg\": " + message + "\"}";
    }
  }
}
