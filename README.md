# persona - javaee restful services

Es una aplicaci�n java restful que permite el acceso a un conjunto de web services que simulan el funcionamiento de instituciones como la RENIEC (Per�).

Servicios restful disponibles:
  - TipoDocumento  
  - PersonaNatural
  - PersonaJuridica
  - Accionista

> Revisar las clases Resources para mayor informacion..

este proyecto es mantenido por SistCoop S.A.C.

### Version
1.0.0

### Tecnolog�as

Dependences:

* [Java] - Java 1.7.0
* [Resteasy] - Resteasy 3.0.9.Final
* [Hibernate] - Hibernate ORM
* [Gradle] - Gradle

### Instalaci�n

Necesitas tener gradle instalado y configurarlo. Una vez instalado gradle puedes ejecutar los siguientes comandos:

```sh
$ gradle test
```

```sh
$ gradle assemble
```

con eso se generar� un archivo "persona.war" para que pueda publicarlo en un contenedor de aplicaciones. Este software est� probado para ser publicado en Wildfly 8.1.0.Final o Jboss 6.0.0.Final

### Development

�Te gustar�a contribuir? �Magn�fico!

Tenemos un sistema de control scrum en https://persona.atlassian.net

Tambien puedes hacer descargar el proyecto y contactarse con nosotros.



License
----

MIT