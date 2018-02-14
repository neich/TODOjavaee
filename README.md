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

Collaborations are welcome

**WARNING**: the project includes IntelliJ IDEA configuration files. They are not needed but they provide running configurations out of the box if you use this IDE. All the configuration files are in ```.gitignore``` in order to avoid updating these files.
