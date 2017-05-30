package org.udg.pds.simpleapp_javaee;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

/**
 * Created by imartin on 26/05/17.
 */
public class Main {

  static String driverModule;

  public static void main(String[] args) throws Exception {

    Swarm swarm = new Swarm();

    swarm.fraction(datasourceWithMysql());
    driverModule = "com.mysql";

    // Start the swarm
    swarm.start();
    JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
    appDeployment.addPackages(true, "org.udg.pds.simpleapp_javaee.model");
    appDeployment.addPackages(true, "org.udg.pds.simpleapp_javaee.rest");
    appDeployment.addPackages(true, "org.udg.pds.simpleapp_javaee.service");
    appDeployment.addPackages(true, "org.udg.pds.simpleapp_javaee.util");

/*
    appDeployment.addResource(MyApplication.class);
    appDeployment.addResource(RESTService.class);
    appDeployment.addResource(UserRESTService.class);
    appDeployment.addResource(TaskRESTService.class);
    appDeployment.addResource(TagRESTService.class);

    appDeployment.addResource(UserService.class);
    appDeployment.addResource(TaskService.class);
    appDeployment.addResource(TagService.class);

    appDeployment.addResource(User.class);
    appDeployment.addResource(Task.class);
    appDeployment.addResource(Tag.class);
*/

    appDeployment.addAllDependencies();

    appDeployment.addModule(driverModule);

    // appDeployment.addAsResource("persistence.xml");
    // appDeployment.addAsManifestResource("META-INF/persistence.xml", "persistence.xml");
    appDeployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", Main.class.getClassLoader()), "classes/META-INF/persistence.xml");
    appDeployment.addAsWebInfResource(new ClassLoaderAsset("import.sql", Main.class.getClassLoader()), "classes/import.sql");

    // Deploy your app
    swarm.deploy(appDeployment);

  }

  private static DatasourcesFraction datasourceWithMysql() {
    return new DatasourcesFraction()
        .jdbcDriver("com.mysql", (d) -> {
          d.driverClassName("com.mysql.jdbc.Driver");
          d.xaDatasourceClass("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
          d.driverModuleName("com.mysql");
        })
        .dataSource("MySQLDS", (ds) -> {
          ds.driverName("com.mysql");
          ds.connectionUrl("jdbc:mysql://localhost:3306/pdsdb?autoReconnect=true&useSSL=false");
          ds.userName("pdsdb");
          ds.password("pdsdb");
        });
  }

}
