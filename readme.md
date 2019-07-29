# Proyecto base Spring-Boot + Angular2

Se ha desarrollado un proyecto base que consta de dos partes, un `back-end` desarrollado con `Spring Boot` (este
proyecto) y un `front-end` desarrollado con `Angular2` (proyecto `base-sboot-ng2`). La documentación que sigue es
referente al proyecto de back-end.

Nota: La aplicación angular (`base-sboot-ng2`) todavía no está actualizada para autenticar contra la API (esta app),
por lo que para lanzar peticiones a endpoints anotados con `@Secured` hay las siguientes alternativas. Se puede utilizar
cualquiera de ellas:

* Arrancar con el perfil `MOCK-ENDPOINT-SECURITY`
* Desactivar la seguridad a nivel de property asignando `false` a la propiedad `core.security.enableEndpointSecurity`
* Hacer un login válido, copiar el token e incluirlo en la cabecera de las peticiones a endpoints protegidos. En la
carpeta `postman` hay un proyecto `Postman` con peticiones de ejemplo.

## Estructura

Este proyecto contiene una aplicación API REST y está dividido en los siguientes módulos:

* `base-sboot-rest-api`: Contiene la capa de API que escucha y gestiona las peticiones REST.
* `base-sboot-rest-model`: Contiene los modelos de la aplicación (dominio, API, etc).
* `base-sboot-rest-repository`: Contiene los repositorios que permiten acceder a los datos en BBDD.
* `base-sboot-rest-service`: Contiene la lógica de negocio.

## Base de datos

Por defecto se utiliza MySQL aunque con Spring se pueden usar otros gestores, como por ejemplo Oracle, SQL Server, H2, etc.

* ***Creación de base de datos***

Para poder utilizar `base-sboot-rest` es necesario crear la base de datos `base_project` en el MySQL que tengas
instalado en local. Para ello, hay que conectar al MySQL local con el usuario `root` (o un usuario privilegiado) y
ejecutar el siguiente comando:

```sql
CREATE DATABASE `base_project` /*!40100 COLLATE 'utf8_general_ci' */
```

* ***Crear usuario de la base de datos***

```sql
CREATE USER 'base_project'@'%' IDENTIFIED BY 'base_project';

GRANT USAGE ON *.* TO 'base_project'@'%';
GRANT SELECT, EXECUTE, SHOW VIEW, ALTER, ALTER ROUTINE, CREATE, CREATE ROUTINE, CREATE TEMPORARY TABLES, CREATE VIEW, DELETE, DROP, EVENT, INDEX, INSERT, REFERENCES, TRIGGER, UPDATE, LOCK TABLES  ON `base_project`.* TO 'base_project'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;
```

* ***Crear objetos en base de datos***

Para crear los objetos en base de datos podemos usar el plugin de `Liquibase`. Ver el sub-apartado ***Aplicar los
cambios en la base de datos*** dentro del apartado *Liquibase*.

### Liquibase

El plugin de Liquibase se halla dentro del módulo `base-sboot-rest-repository`. Nos referiremos a esta ubicación
cuando ejecutemos un comando de Liquibase.

Liquibase dispone de un fichero de propiedades (que se puede definir por perfil), donde se indican los valores
necesarios para configurar el plugin:

|   Nombre          |   Descripción                                         |
|   -------------   |   -------------                                       |
|   *changeLogFile* |  Indica la ruta al fichero de cambios de Liquibase    |
|   *url*           |  Indica la url a la base de datos                     |
|   *username*      |  Usuario de la base de datos                          |
|   *password*      |  Contraseña del usuario de la base de datos           |

* ***Generar el fichero de cambios***

Para generar el fichero de cambios a partir de una base de datos existente deberemos ejecutar el comando
`mvn liquibase:generateChangeLog`. Se deberá sustituir el contenido del fichero `changeLogFile` (definido en las
propiedades de Liquibase), por el contenido generado por el comando anterior.

* ***Aplicar los cambios a la base de datos***

Para aplicar los cambios del fichero de cambios de Liquibase (`changeLogFile`) en la base de datos configurada en las
propiedades de Liquibase, deberemos ejecutar el comando `mvn liquibase:update`. Liquibase comprobará qué cambios se han
aplicado ya en la base de datos (la base de datos contiene tablas propias de Liquibase con dicha información), y
ejecutará aquellos que no se hayan ejecutado aún. Los cambios aplicados serán registrados en las tablas internas de
Liquibase.

También es posible generar un script con los cambios en vez de aplicarlos directamente. Para ello se debe ejecutar el
comando `mvn liquibase:updateSQL`

Si no se especifica perfil por defecto se usa el perfil `LOCAL`. Se puede especificar un perfil con el parámetro `-P`.
Por ejemplo, para aplicar los cambios en la base de datos de `PRO` se ejecutará `mvn liquibase:update -PPRO`

## Auditoría

Se puede hacer auditoría a dos niveles que se pueden combinar:

Por una parte se utiliza `Hibernate Envers` para hacer auditoría de la base de datos. Para auditar una entidad basta
anotarla con `@Audited` y crear su tabla de auditoría asociada editando el script de Liquibase. Se puede tomar como
ejemplo la entidad `Hero`, que se encuentra mapeada en la tabla `HERO` y auditada en la tabla `HERO_AUD`. Todas las 
tablas de auditoría deben tener el sufijo `_AUD`.

Hecho esto, cualquier operación de inserción, modificación o borrado tendrá su registro asociado en la tabla de
auditoría que corasponda. A esto `Hibernate Envers` le llama revisión. En la tabla `USER_REVISION_ENTITY` se puede
ver qué usuario está asociado a cada revisión.

Por otra parte, además, se puede hacer que una entidad tenga por defecto los campos `createdBy, creationDate, 
lastModifiedBy y lastModificationDate` extendiendo la clase `AuditedEntity`. Estos campos se alimentarán de forma
automática gracias a los listeners de JPA `@PrePersist` y `@PreUpdate`.

## Modos de autenticación

Existen diferentes modos de autenticación. Se configuran con la propiedad `core.security.authentication.type`. Los
valores permitidos son:

- `database`: Se autentica contra la base de datos.
- `ldap`: Se autentica contra LDAP. Se deben configurar los properties con prefijo `core.security.ldap`
- `custom`: Se utiliza la implementación de la clase `LoginServiceCustomImpl`. Se puede sobrescribir como se desee.
- `fake`: Usar la implementación de la clase `LoginServiceFakeImpl` (normalmente para los tests de integración).
- `properties`: Utilizar los usuarios definidos en la propiedad `core.security.authentication.users property`

## Modelo de seguridad

El modelo de seguridad de la aplicación contiene los siguientes elementos en la base de datos:

- `User`: Usuario de la aplicación con su información asociada (username, password, etc)
- `Role`: Roles de un usuario. La relación es N-M.
- `Permission`: Es un permiso asignado a uno o varios roles. La relación es N-M.
- `MenuOption`: Opciones de menú para la generación del menú. Siempre habrá una principal para toda la aplicación desde
la que se empezará la generación recursiva de menú de usuario. Tiene relación N-M con `Role`.
- `Company`: Empresa. Se pueden crear N empresas y vincular diferentes roles a un usuario por empresa.

#### Notas de seguridad
- Por seguridad se guarda un `salted hash` de la contraseña del usuario en base de datos. Se utiliza BCrypt y se pueden
generar hashes por ejemplo aquí: `http://bcrypthashgenerator.apphb.com/`

## Servicios Fake y Mock

Se han desarrollado implementaciones FAKE y MOCK de ciertas áreas de la aplicación. Son las siguientes:

* ##### Fake del menú de usuario

Esta implementación hace que siempre se genere el mismo menú de usuario, independientemente del usuario que se utilice o
de si se usa LDAP o no. Para utilizar esta implementación se debe arrancar con el perfil `FAKE-USER-MENU`.

* ##### Mock de la seguridad de los endpoints

Esta implementación hace que la anotación `@Secured` no se tenga en cuenta. Para utilizar esta implementación se debe
arrancar con el perfil `MOCK-ENDPOINT-SECURITY`.

## Configuraciones

En este apartado se indican a grandes rasgos las configuraciones más importantes que se pueden hacer. 

##### Para la generación de menú

* Se debe indicar la opción de menú padre desde la que se debe iniciar la generación recursiva del menú del usuario.
Para ello se debe dar valor a la propiedad `core.mainMenuOptionId` del `application.properties`.

* En las opciones de menú se puede indicar el orden de cada una para que el menú pueda ser ordenado.

* Se debe indicar el código de empresa a utilizar por defecto. Esto permite que enviar la empresa en el login sea 
opcional, muy útil para los clientes que sólo usan una empresa. Para ello se debe dar valor a la propiedad 
`core.defaultCompanyId` del `application.properties`.

#### JWT

Se pueden configurar ciertos parámetros para la gestión de tokens con JWT. Para ello se utilizan los properties con
prefijo `jwt` del `application.properties`. Se puede configurar, por ejemplo:

* Clave privada para la firma / validación de tokens.
* TTL's para los tokens de acceso y refresco.
* Algoritmo a utilizar.

##### Swagger

El proyecto incluye Swagger para la documentación de la API. Se configura en el `application.properties` a través de los
properties con prefijo `swagger`.

Existe una configuración llamada `swagger.enabled` que permite activar / desactivar Swagger. Por ejemplo, puede ser
interesante no tenerlo activado en producción.

##### Configuración de niveles de log

La configuración de los niveles de log se hace desde el `application.properties` del perfil que se desea configurar.
En el siguiente ejemplo se ve como para las clases cuyo paquete empiece por `es.sm2baleares` se configura el nivel
`DEBUG` y para el resto nivel `INFO`
 ```
logging.level.es.sm2baleares=DEBUG
logging.level.root=INFO
```

##### Logs de rendimiento

Es posible loggear lo que tarda en ejecutarse cualquier método mediante la anotación `@PerformanceLogged`. Esta
anotación tiene un atributo opcional llamado `threshold` que permite indicar un valor en milisegundos a partir del cual
se debe loggear la petición. Si el tiempo de ejecución del método iguala o supera el threshold entonces queda loggeado.

Además, existe una propiedad en el `application.properties` llamada `logging.performance.defaultThreshold` que permite
indicar el valor por defecto. Se utilizará cuando no se ponga el atributo en la anotación.

## Aspectos a implementar / configurar en el proyecto a desarrollar

Hay diversas partes que requirarán configuración a la hora de utilizar este proyecto como base.

* Páginas de error 404, 500... ahora contienen datos de ejemplo que conviene modificar en aplicaciones reales.
	public/error/404.html

* TODO: ir anotando aquí todos los puntos que se deben modificar.

## API securizada con JWT

La API está securizada con tokens JWT. Para securizar un método de la API basta con anotarlo con `@Secured`, indicando
como parámetro un `methodKey`. Un `methodKey` es un identificador que se encuentra para cada registro de la tabla
`PERMISSION`. Se pueden ver ejemplos de `@Secured` en `HeroController`. La seguridad se controla mediante la clase
`SecurityAspect` que es un Aspect de Spring. Se encarga de interceptar cualquier llamada a métodos de controlador
anotados con `@Secured` y se encarga de obtener el token del header y validarlo. Para ello decodifica el token y
recupera el usuario al que se refiere junto con
sus permisos. Luego mira si el `methodKey` del método se encuentra entre los permisos contenidos en el token.

Tal como se explica en la introducción se puede desactivar la validación de acceso a los métodos de las siguientes
formas.

* Arrancar con el perfil `MOCK-ENDPOINT-SECURITY`
* Desactivar la seguridad a nivel de property asignando `false` a la propiedad `core.security.enableEndpointSecurity`

## SM2 Spring Commons

La aplicación necesita `SM2 Spring Commons` que de momento no está en el artifactory. Por eso, para poder compilar,
es necesario tener SM2 Spring Commons en nuestro repositorio local. Para ello basta clonarlo y ejecutar un 
`mvn clean install` sobre la rama develop. El proyecto se puede clonar de aquí:

https://gitbucket.sm2baleares.es/sm2/sm2-spring-commons

## Lombok

Se utiliza Lombok para la generación de getters, setters, constructores sin argumentos o con todos los argumentos y, en
algunos casos, `equals()`, `hashCode()` y `toString()`. A grandes rasgos tiene las siguientes anotaciones:

* `@Getter`: Genera los getters 
* `@Setter`: Genera los setters
* `@NoArgsConstructor`: Genera un constructor sin parámetros
* `@AllArgsConstructor`: Genera un constructor con todos los atributos de la clase como parámetros
* `@EqualsAndHashCode`: Genera los métodos equals() y hashCode(). No se debe utilizar con las clases @Entity
* `@ToString`: Genera el método toString()
* `@Data`: Aglutina `@Getter`, `@Setter`, `@EqualsAndHashCode` y `@ToString`
* `@Slf4j`: Incluye un logger al que podemos referenciar con `log`

Se puede ver la documentación aquí: [Documentación Lombok](https://projectlombok.org/features/all)

Para poder utilizar Lombok es necesario instalar el plug-in [Lombok Plugin](https://plugins.jetbrains.com/plugin/6317-lombok-plugin) de IntelliJ

## Docker

El proyecto está configurado para crear una imagen de docker que contenga y arranque la aplicación. Puede ser muy útil en proyectos grandes
donde intervengan varios microservicios desarrollados mediante este proyecto base. De modo que sea más ágil levantar microservicios con los que 
tengamos dependencia para desarrollar.

Para construir la imagen de docker del proyecto hay que ejecutar este comando de maven en la raíz del proyecto:

    mvn dockerfile:build
    
El archivo que configura cómo se construye la imagen es el `Dockerfile` que hay en la raíz del proyecto.
Por otro lado en el tag `<build>` del pom principal se encuentra la configuración del plugin `dockerfile-maven-plugin` 
para interpretar este archivo. En los módulos del proyecto hay otros build de este plugin pero con el flag skip por
temas de configuración, pero el que realmente hace algo es el del pom parent.

### Ejecutar en Docker
Hay que tener en cuenta que la aplicación necesita conectarse a una base de datos y posiblemente a otros servicios. Esta puede estar en otro contenedor 
de Docker, en nuestra máquina local, en un servidor de desarrollo, etc. Tal y como está configurado el datasource con `localhost` no funcionará. 
Se pueden configurar entradas del host en la máquina virtual, o definir bridges, etc. Pero lo más sencillo es definir una variable de entorno para la 
JVM durante el arranque, pues Spring Boot sobrescribe configuraciones en application.properties con las definidas en variables de entorno. Esto nos
permite configurar externamente (y durante el arranque) cualquier URI. 

Para ejecutar en Docker la imagen creada para este proyecto se haría lo siguiente (cambiando <ip> por la IP de la máquina local o la máquina donde
está la base de datos):

    docker run -p 8080:8080 -e SPRING_DATASOURCE_URL='jdbc:mysql://<ip>/base_project?autoReconnect=true&useSSL=false' sm2baleares/base-sboot-rest

La máquina quedará escuchando en el puerto 8080. Se puede mapear el puerto de la máquina virtualizada con un puerto de la máquina host cambiando
los parámetros que siguen a `-p`. El primer número es el puerto de la máquina host y el segundo el de la máquina virtual.

También se puede ejecutar la máquina mediante la utilidad Kitematic de Docker en Windows, que es más visual. En ese caso las variables de entorno se 
pueden configurar en el apartado Settings - General - Environment Variables.

### Siguientes pasos: Docker Compose
Para disponer de una configuración más completa para Docker, en el proyecto base, sería interesante crear una configuración básica con Docker Compose.
Consistiría en una configuración de Compose que levante una imagen con una base de datos, otra imagen con base-sboot-rest y otra con base-sboot-ng2.
De este modo para levantar todos los componentes necesarios para ejecutar la aplicación bastaría con ejecutar un comando.