# Proyecto base Spring-Boot + Jquery + JS

Se ha desarrollado un proyecto base que consta de dos partes, un `back-end` desarrollado con `Spring Boot` (este
proyecto) y un `front-end` desarrollado con Jquery y JS (Repositorio [`timer-frontend`](https://github.com/mikiPP/Timer-Frontend)). La documentación que sigue es referente al proyecto de back-end.

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

#### Notas de seguridad
- Por seguridad se guarda un `salted hash` de la contraseña del usuario en base de datos. Se utiliza BCrypt y se pueden
generar hashes por ejemplo aquí: `http://bcrypthashgenerator.apphb.com/`


## Swagger

El proyecto incluye Swagger para la documentación de la API. Se configura en el `application.properties` a través de los
properties con prefijo `swagger`.

Existe una configuración llamada `swagger.enabled` que permite activar / desactivar Swagger. Por ejemplo, puede ser
interesante no tenerlo activado en producción.

Para ver todos los endpoints y sus ejemplos, los puedes ver,una vez puesto el servidor en marcha en la dirección "http://localhost:8080/swagger-ui.html#/"


## JWT

La api esta securizada en todos los campos con jwt, al hacer loggin a la api, devuelve un token que espira en 6 horas. 
Los unicos endpoints que son públicos son los de swagger,el de la creación de usuario y finalmente, el endpoint que mira 
si ese usuario existe.


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
