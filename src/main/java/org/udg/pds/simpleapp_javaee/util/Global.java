package org.udg.pds.simpleapp_javaee.util;


import io.minio.MinioClient;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class Global {
    private MinioClient minioClient;

    private static String BASE_URL;

    @PostConstruct
    void init() {
        try {
            String minioURL = System.getProperty("swarm.project.minio.url");
            String minioAccessKey = System.getProperty("swarm.project.minio.access-key");
            String minioSecretKey = System.getProperty("swarm.project.minio.secret-key");
            minioClient = new MinioClient(minioURL, minioAccessKey, minioSecretKey);
        } catch (Exception e) {
        }

        if (System.getProperty("swarm.project.base-url") != null)
            BASE_URL = System.getProperty("swarm.project.base-url");
        else {
            String port = System.getProperty("swarm.http.port") != null ? System.getProperty("swarm.http.port") : "8080";
            BASE_URL = "http://localhost:" + port;
        }
    }

    public MinioClient getMinioClient() {
        return minioClient;
    }

    public String getBaseURL() { return BASE_URL; }
}
