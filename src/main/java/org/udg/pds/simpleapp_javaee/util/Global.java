package org.udg.pds.simpleapp_javaee.util;


import io.minio.MinioClient;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.udg.pds.simpleapp_javaee.model.Tag;
import org.udg.pds.simpleapp_javaee.model.Task;
import org.udg.pds.simpleapp_javaee.model.User;
import org.udg.pds.simpleapp_javaee.service.TagService;
import org.udg.pds.simpleapp_javaee.service.TaskService;
import org.udg.pds.simpleapp_javaee.service.UserService;

import java.util.ArrayList;
import java.util.Date;

@Singleton
@Startup
//@DependsOn({"UserService", "TaskService"})
public class Global {
    private MinioClient minioClient;
    private String minioBucket;
    private String BASE_URL;

    @Inject
    private Logger logger;

    @EJB
    UserService userService;

    @EJB
    TaskService taskService;

    @EJB
    TagService tagService;

    @PostConstruct
    void init() {
        String minioURL = null;
        String minioAccessKey = null;
        String minioSecretKey = null;

        try {
            minioURL = System.getProperty("swarm.project.minio.url");
            minioAccessKey = System.getProperty("swarm.project.minio.access-key");
            minioSecretKey = System.getProperty("swarm.project.minio.secret-key");
            minioClient = new MinioClient(minioURL, minioAccessKey, minioSecretKey);
            minioBucket = System.getProperty("swarm.project.minio.bucket");
        } catch (Exception e) {
            logger.warn("Cannot initialize minio service with url:" + minioURL + ", access-key:" + minioAccessKey + ", secret-key:" + minioSecretKey);
        }

        if (minioBucket == null) {
            logger.warn("Cannot initialize minio bucket: " + minioBucket);
            minioClient = null;
        }

        if (System.getProperty("swarm.project.base-url") != null)
            BASE_URL = System.getProperty("swarm.project.base-url");
        else {
            String port = System.getProperty("swarm.http.port") != null ? System.getProperty("swarm.http.port") : "8080";
            BASE_URL = "http://localhost:" + port;
        }

        initData();
    }

  private void initData() {
    User user = userService.register("usuari", "usuari@hotmail.com", "123456");
    Task task = taskService.addTask("Una tasca", user.getId(), new Date(), new Date());
    Tag tag = tagService.addTag("ATag", "Just a tag");
    taskService.addTagsToTask(user.getId(), task.getId(), new ArrayList<Long>() {{ add(tag.getId()); }});
  }

  public MinioClient getMinioClient() {
        return minioClient;
    }

    public String getBaseURL() { return BASE_URL; }
}
