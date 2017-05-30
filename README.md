# TODOjavaee

This is a JavaEE application that implements an pure API REST for a TODO appication. It is used for educational purposes and the goal is to show good practices when building multi-tier applications with JavaEE (and in general).

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
