Spring Security RabbitMQ
========================

Provides an example on how to implement security, in a distributed system,
using AMQP.

Getting started
---------------

### Requirements

* Java 8
* Maven
* Docker

To build and run the example you have to start the the RabbitMQ server and the
build and run the Spring Boot application using Maven:

    $ docker-compose up -d
    $ mvn spring-boot:run

Event reference
---------------

* _A new user principal was just added_ - `user.added`

* _A user principal was successfully authenticated_ - `auth.success`

* _A user principal failed to be authenticated_ - `auth.failed`

Happy hacking!
