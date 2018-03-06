package org.udg.pds.simpleapp_javaee.util;


import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class Global {

    String dataDir;

    @PostConstruct
    void init() {
        dataDir = System.getProperty("local.data-dir");
    }

    public String getDataDir() { return dataDir; }
}
