# TODOjavaee

This is a JavaEE application that implements an pure API REST for a TODO appication. It is used for educational purposes and the goal is to show good practices when building multi-tier applications with JavaEE (and in general). It works with the the [TODOandroid](https://github.com/neich/TODOAndroid) Android application as client.

The main three tiers used are:

* REST tier (JAX-RS)
* Business tier (EJB)
* Persistence tier (JPA)

Other JAva EE APIs used:

* Bean validation
* Exception Mappers
* CDI

It uses [Swarm](http://wildfly-swarm.io/) to produce an jar file than can be executed standalone without an application server:

```
mvn package
java -jar target/TODOjavaee-swarm.jar
```

## Image uploading

In order to store files, the app uses a private object storage server: [minio      ](https://www.minio.io/). The minio configuration has to be passed to the application as properties, either on the ```project-*.yaml``` file, o via command line:

```
mvn package
java -Dswarm.project.minio.ulr=http://your-minio-host.com -Dswarm.project.minio.access-key=your-access-key -Dswarm.project.minio.secret-key=your-secret-key -Dswarm.project.minio.bucket=your-bucket -jar target/TODOjavaee-swarm.jar
```



## Heroku

The app is ready to deploy into [Heroku](http://heroku.com) with the ```web``` profile. There is a file ```Procfile``` with the command line arguments to start the jar, and there is a ```project-heroku.yml``` stage file (at ```src/main/resources```) with the Heroku specific configuration. You **must** change the database information in this file to match yours.

#### Collaborations are welcome!

**WARNING**: the project includes IntelliJ IDEA configuration files. They are not needed but they provide running configurations out of the box if you use this IDE
