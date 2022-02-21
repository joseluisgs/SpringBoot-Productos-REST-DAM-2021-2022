# Spring-Productos-DAM

Sencillo Microservicio para API Rest en Spring (SpringBoot) realizada conjuntamente con 2 DAM. Curso 2021/2022

[![Spring](https://img.shields.io/badge/Code-Spring%20Java-green)](https://spring.io)
[![LISENCE](https://img.shields.io/badge/Lisence-MIT-green)]()
![GitHub](https://img.shields.io/github/last-commit/joseluisgs/Spring-Productos-DAM)

![imagen](https://www.programaenlinea.net/wp-content/uploads/2019/07/java.spring.png)

- [Spring-Productos-DAM](#spring-productos-dam)
  - [API REST](#api-rest)
    - [Arquitectura](#arquitectura)
  - [Componentes Spring](#componentes-spring)
    - [Controladores](#controladores)
    - [Servicios](#servicios)
    - [Repositorios](#repositorios)
    - [Configuración](#configuración)
    - [Bean](#bean)
  - [IoC y DI en SpringBoot](#ioc-y-di-en-springboot)
    - [Inversión de Control](#inversión-de-control)
    - [Inyección de Dependencias](#inyección-de-dependencias)
    - [IoC y DI en Spring](#ioc-y-di-en-spring)
      - [A nivel de constructor](#a-nivel-de-constructor)
      - [A nivel de setter](#a-nivel-de-setter)
  - [DTO y Mappers](#dto-y-mappers)
  - [Fichero de propiedades](#fichero-de-propiedades)
  - [Spring Data](#spring-data)
    - [Repositorios](#repositorios-1)
    - [Definición de entidades](#definición-de-entidades)
    - [Definiciones de consultas](#definiciones-de-consultas)
  - [Spring Security](#spring-security)
    - [JWT](#jwt)
    - [Autenticación](#autenticación)
    - [Autorización](#autorización)
    - [Implementación en SpringBoot](#implementación-en-springboot)
  - [Documentación](#documentación)
  - [Pruebas](#pruebas)
    - [Junit y Mockito](#junit-y-mockito)
    - [MockMvc](#mockmvc)
    - [Postman](#postman)
  - [Despliegue](#despliegue)
    - [Docker](#docker)
      - [Usando Dockefile](#usando-dockefile)
      - [Usando Docker Compose](#usando-docker-compose)
    - [Heroku](#heroku)
  - [Autor](#autor)
    - [Contacto](#contacto)
  - [Licencia](#licencia)

## API REST

Sencilla Api REST con Spring Boot realizada con el alumnado para el curso de 2020/2021.
Con ella pondremos a prueba todo lo que hemos aprendido hasta el momento en Acceso a Datos y Programación de Servicios y Procesos.

En este proyecto realizaremos una API REST con Spring Boot. Es decir, un servicio web que accede a datos de una base de datos para gestionar un recurso, donde a su vez, podemos realizar operaciones CRUD (Create, Read, Update, Delete) implementando mecanismos autenticación y autorización.

### Arquitectura
![arquitectura](./images/layers.png)

![arquitectura3](./images/expla.png)

![arquitectura2](./images/BfNin.jpg)

## Componentes Spring
Nuestros componentes principales se etiquetarán con @ para que el framework Spring lo reconozca. Cada uno tiene una misión en nuestra arquitectura:

![componentes](./images/components.png)

### Controladores
Se etiquetan como @Controller o en nuestro caso al ser una API REST como @RestController. Estos son los controladores que se encargan de recibir las peticiones de los usuarios y devolver respuestas.

### Servicios
Se etiquetan como @Service. Se encargan de implementar la parte de negocio o infraestructura. En nuestro caso puede ser el sistema de almacenamiento o parte de la seguridad y perfiles de usuario.

### Repositorios
Se etiquetan como @Repository e implementan la interfaz y operaciones de persistencia de la información. En nuestro caso, puede ser una base de datos o una API externa. Podemos extender de repositorios pre establecidos o diseñar el nuestro propio.

### Configuración
Se etiquetan como @Configuration. Se encargan de configurar los componentes de la aplicación. Se se suelen iniciar al comienzo de nuestra aplicación.

### Bean
La anotación @Bean, nos sirve para indicar que este bean será administrado por Spring Boot (Spring Container). La administración de estos beans se realiza mediante a anotaciones como @Configuration.

## IoC y DI en SpringBoot
### Inversión de Control
Inversión de control (Inversion of Control en inglés, IoC) es un principio de diseño de software en el que el flujo de ejecución de un programa se invierte respecto a los métodos de programación tradicionales. En su lugar, en la inversión de control se especifican respuestas deseadas a sucesos o solicitudes de datos concretas, dejando que algún tipo de entidad o arquitectura externa lleve a cabo las acciones de control que se requieran en el orden necesario y para el conjunto de sucesos que tengan que ocurrir.

### Inyección de Dependencias
 La inyección de dependencias (en inglés Dependency Injection, DI) es un patrón de diseño orientado a objetos, en el que se suministran objetos a una clase en lugar de ser la propia clase la que cree dichos objetos. Esos objetos cumplen contratos que necesitan nuestras clases para poder funcionar (de ahí el concepto de dependencia). Nuestras clases no crean los objetos que necesitan, sino que se los suministra otra clase 'contenedora' que inyectará la implementación deseada a nuestro contrato.

### IoC y DI en Spring
El contenedor Spring IoC lee el elemento de configuración durante el tiempo de ejecución y luego ensambla el Bean a través de la configuración. La inyección de dependencia de Spring se puede lograr a través del constructor, el método Setter y el dominio de entidad. Podemos hacer uso de la anotación @Autowired para inyectar la dependencia en el contexto requerido. O si usamos Lombok, podemos hacer uso de la anotación @Setter, @AllArgsConstructor, siempre y cuando declaremos como final las dependencias necesitadas.

#### A nivel de constructor
El contenedor llamará al constructor con parámetros al instanciar el bean, y cada parámetro representa la dependencia que queremos establecer. Spring analizará cada parámetro, primero lo analizará por tipo, pero cuando sea incierto, luego lo analizará de acuerdo con el nombre del parámetro (obtenga el nombre del parámetro a través de ParameterNameDiscoverer, implementado por ASM).
```java
@Service
public class UserService {
 
  private final UserDAO userDAO;
 
  @Autowired
  public UserService(UserDAO userDAO) {
    this.userDAO = userDAO;
  }
}
```

#### A nivel de setter 
Spring primero instancia el Bean y luego llama al método Setter que debe inyectarse para lograr la inyección de dependencia.
```java
@Service
public class UserService {
 
  private UserDAO userDAO;
    
  @Autowired
  public void setUserDAO(UserDAO userDAO) {
    this.userDAO = userDAO;
  }
}
```
## DTO y Mappers
Usaremos los DTO, como objetos de transporte y con ellos facilitar la transición entre Request y objetos del modelo, y objetos de modelo y Responses. De esta manera podemos ensamblar y desensamblar los objetos de modelo y de transporte según nuestra necesidades.

Los mapeadores nos ayudarán en la misión de mapear los objetos de modelo a objetos de transporte y viceversa. Podemos usar una librería específica para ello como [ModelMapper](http://modelmapper.org/), o podemos usar un mapeador propio.

## Fichero de propiedades
A la hora de utilizar el contenedor de Spring es una buena práctica separar la configuración de los beans, parcial o totalmente realizada en ficheros XML o clases de configuración (internamente), y los parámetros de configuración que utilizan esos beans como por ejemplo contraseñas o la ubicación de la base de datos. 

Lo habitual es definir los parámetros de configuración en ficheros de propiedades estándar de Java (.properties). Spring permite utilizar cómodamente este tipo de ficheros tal y como vamos a ver y con ello realizar la configuración de los beans sin pasar por el tedioso proceso de configuración de XML.

Podemos tener distintos ficheros por ejemplo para desarrollo y producción.
- Propiedades globales: src/main/resources/application.properties
- Propiedades de producción: src/main/resources/application-prod.properties
- Propiedades de desarrollo: src/main/resources/application-dev.properties

Y luego desde la línea de comandos podemos cargar un perfil concreto de la siguiente manera:
```bash
java -jar -Dspring.profiles.active=prod demo-0.0.1-SNAPSHOT.jar
```

## Spring Data
Spring Data es una librería de persistencia que nos permite acceder a bases de datos relacionales de forma sencilla. Para ello podemos extender de la clase [JpaRepository](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.repositories), que es una clase de repositorio de Spring Data con más funcionalidades, como pueden ser las operaciones de consulta, inserción, actualización y eliminación, así como las de paginación, ordenación o búsquedas.

### Repositorios
Los principales son:
- CrudRepository: tiene las mayoría de las funcionalidades CRUD.
- PagingAndSortingRepository: ofrece mecanismos de paginación, ordenación y búsqueda.
- JpaRepository: proporciona algunos métodos relacionados con JPA, como vaciar el contexto de persistencia y eliminar registros en un lote.

Un ejemplo de controlador que implementa filtrado, paginación y búsqueda puede ser:
```java
 @GetMapping("/all")
    public ResponseEntity<?> listado(
            // Podemos buscar por los campos que quieramos... nombre, precio... así construir consultas
            @RequestParam(required = false, name = "nombre") Optional<String> nombre,
            @RequestParam(required = false, name = "precio") Optional<Double> precio,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
      ...
      Pageable paging = PageRequest.of(page, size, Sort.Direction.ASC, sort);
      Page<Producto> pagedResult = productosRepository.findAll(paging);
      ...
    }
```


Podemos trabajar con BBDD relacionales de forma sencilla con Spring Data o usar la versión específica para MongoDB.

### Definición de entidades
Usaremos las anotaciones de JPA para definir entidades o colecciones, sus atributos y características de los mismos, así como las relacionales existentes.

### Definiciones de consultas
Podemos definir consultas personalizadas para las entidades de la aplicación. Para ello podemos usar la anotación @Query con JPQL o @NativeQuery y usar el lenguaje del motor de Base de Datos.

Por otro lado, también podemos definir las consultas en base del nombre del método. Si lo definimos con una [signatura determinada con ellos se generará la consulta automáticamente](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation).

Es importante que usando 

## Spring Security
[Spring Security](https://spring.io/projects/spring-security) es una librería de seguridad que nos permite controlar el acceso a nuestra aplicación permitiendo mecanismos de autenticación y autorización en base a roles.

Para ello haremos uso de UserDetailsService, un servicio que nos permitirá cargar datos específicos del usuario.

Además, actuará como middleware, analizando las rutas y con ellas a base de roles saber si se puede permitir el acceso a operar con ellas.

![security](./images/security.png)

### JWT
Spring Security ofrece una librería para generar y verificar [JWT](https://jwt.io/introduction). Gracias a ella podemos realizar el proceso de autenticación y autorización. Json Web Token es un estándar que define una forma auto contenida de transmitir información como JSON. Consta de tres partes separadas por puntos.
- Header: algoritmo (SHA256, HS512 …) y el tipo de token.
- Payload: contiene las propiedades(claims) del token.
- Signature: header (codificado en base64), payload (codificado en base64), una clave, y todo firmado con el algoritmo del header.
- Claim: porción de información en el cuerpo del token.

La arquitectura de Spring Security para JWT es la siguiente

![jwt](./images/jwt01.png)

### Autenticación
![jwt02](./images/jwt03.jpeg)

### Autorización
![jwt03](./images/jwt02.jpeg)

### Implementación en SpringBoot
Podemos implementar la autenticación y autorización de Spring Security en SpringBoot con JWT de la siguiente manera.

- Usuarios que extiendan de UserDetails, con ellos usaremos los mecanismos de Spring Security nos indica. Deberemos tener el campo username y password. Además usaremos una lista de roles para la parte de autenticación. 
- CustomUserDetailsService que implementa UserDetailsService con los métodos: loadUserByUsername() y loadUserById().
- Configuración del PasswordEncoder, por ejemplo con BCryptPasswordEncoder.
- JwtTokenProvider, donde implementamos los datos del JWT y con ello generar los JWT según nuestras necesidades, así como verificar los mismos.
- JwtAuthorizationFilter que extiende de OncePerRequestFilter. Nos permite definir el filtro de la cadena de autenticación/autorización en base al contenido del JWT y de nuestra información en BB.DD.
- JwtAuthenticationEntryPoint que implementa AuthenticationEntryPoint. Nos permite definir el comportamiento de la aplicación cuando no se ha autenticado o el JWT no es el correcto.
- SecurityConfig que extiende de WebSecurityConfigurerAdapter. Es la clase principal del sistema de Spring Security y se define en base a todas las anteriores.
  - Usará userDetailsService y passwordEncoder.
  - Su authenticationEntryPoint será en base a jwtAuthenticationEntryPoint
  - Definiremos las políticas de acceso en base a roles si ha pasado la autenticación. Es decir, las políticas de autorización por rutas, o verbos HTTP.
  - Aplicará todo estos procesos en base a a los filtros definidos en jwtAuthorizationFilter.

## Documentación
Para documentar nuestra API REST podemos hacer uso de [Swagger](https://swagger.io/). Swagger es un conjunto de herramientas de software de código abierto para diseñar, construir, documentar, y utilizar servicios web RESTful. Además podemos usar su UI para testear nuestra API.

## Pruebas
### Junit y Mockito
Podemos probar nuestra API Rest como cualquier aplicación Java usando las librerías JUnit y Mockito, que vienen incluidas en las dependencias de pruebas de Spring. 
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

De esta manera podemos realizar test unitarios y de integración sobre repositorios, servicios y controladores. Podemos hacer uso de las anotaciones @Mock, @MockBean e @InjectMocks según nuestros intereses para facilitar el proceso de configurar nuestros mocks.

### MockMvc
Con MockMvc podemos mockear desde la propia aplicación las llamadas a la propia API y con ello analizar el funcionamiento de los controladores a nivel de integración con el resto del sistema. Esto nos hace que tengamos un cliente rest que interactúa con nuestro servicio, realizando las peticiones (resquest) oportunas para obtener los resultados de dicha petición al servicio (response).

Posteriormente, podemos analizar usado asserciones si los resultados obtenidos (una vez procesado el JSON de respuesta), son los resultados esperados, como siempre lo haríamos.

Alternativamente, podemos usar lso expect de esta librerías para realizar los test analizando en JSON directamente. 

### Postman
Para probar nuestra API podemos usar [Postman](https://www.getpostman.com/). Con ella podemos [probar nuestra API REST](./postman/Spring-Productos-DAM.postman_collection.json) y analizar su comportamiento. Además podemos documentarla y subirla a la nube para que pueda ser probada por otros usuarios.

## Despliegue

### Docker
Para facilitar el despliegue de nuestra API podemos usar [Docker](https://docker.io/). Podemos crear un contenedor de nuestra API y ejecutarla en el puerto por defecto de nuestra API. Además, podemos subir nuestra API a la nube para que pueda ser usada por otros usuarios usando Docker Hub.

#### Usando Dockefile
Podemos usar el [Dockerfile](https://docs.docker.com/engine/reference/builder/) para crear nuestra API. Puedes consultar el [Dockerfile](Dockerfile).
Para generar y ejecutar el contenedor, usamos: 
```bash
docker build --tag=joseluisgs/springboot-productos-dam:latest . 
docker run -p6969:6969 joseluisgs/spring-boot-productos-dam:latest
```
Para subirla a Docker Hub, usamos:
```bash
docker push joseluisgs/springboot-productos-dam:latest
```
#### Usando Docker Compose
Podemos usar [Docker Compose](https://docs.docker.com/compose/) para desplegar nuestra API. Puedes consultar el fichero [docker-compose.yml](docker-compose.yml).
Para levantar lso contenedores y construir las imágenes
```bash
docker-compose up --build
```
Para parar nuestros contenedores
```bash
docker-compose down
```

### Heroku
Para subir y distribuir nuestra API en la nube, podemos usar el servicio de Heroku [Heroku](https://www.heroku.com/). Con ello podemos desplegar a partir de los cambios en nuestro repositorio de manera continua. Para ello, necesitamos una cuenta de Heroku y una cuenta de Github. Lo primero es instalar y configurar el [CLI de Heroku](https://devcenter.heroku.com/articles/heroku-cli#install-the-heroku-cli). Puedes leer toda la información [aquí](https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku). También puedes [automatizar el proceso con Maven](https://www.callicoder.com/deploy-host-spring-boot-apps-on-heroku/).

Debemos tener en cuenta que el entorno de ejecución de Heroku es Java 8. Si nuestro proyecto es Java 11 (<java.version>11</java.version>) debes cambiarlo a 8, o, crear en la raíz de tu proyecto un fichero llamado [system.properties](system.properties), con la cadena java.runtime.version=11.

Por otro lado, Heroku expone el puerto 8080 por defecto. Si quieres usar otro puerto, debes indicarlo en la plataforma como variable de entorno, o usar este. Puedes consultar la documentación de Heroku al respecto.

Antes de publicar en Heroku, debemos tener en cuenta que nuestro repositorio debe estar actualizado en el último commit. Para ello, debemos ejecutar el siguiente comando:
```bash
git add .
git commit -m "heroku init"
```

Ahora creamos nuestra aplicación Heroku o [con su web](https://dashboard.heroku.com/new-app), o usando el CLI instalado con el siguiente comando:
```bash
heroku create springboot-productos-dam
```
Desplegamos la apliacación con el siguiente comando:
```bash
git push heroku master
```
El tiempo de despliegue tarda un poco, una vez terminada puedes ver la consola remota con:
```bash
heroku open
```
Puedes consultar los logs con: 
```bash
heroku logs --tail
```

Podremos consultarla una vez deplegada en: https://springboot-productos-dam.herokuapp.com/rest/productos/

Como es de suponer, el servicio de Heroku al ser gratis, tiene algunas limitaciones, como el almacenamiento efímero de ficheros persistente, o el uso de base de datos, la cual podemos usar una versión limitada y gratis de PostgreSQL que ofrecen. Si no, debemos usar una alternativa de pago, o usar para el almacenamiento de ficheros otras opciones existentes y usar otras bases de datos en la nube, como MongoDB Atlas, o alternativas globales como Amazon Web Services S3 o Google Firebase.

## Autor

Codificado con :sparkling_heart: por [José Luis González Sánchez](https://twitter.com/joseluisgonsan)

[![Twitter](https://img.shields.io/twitter/follow/joseluisgonsan?style=social)](https://twitter.com/joseluisgonsan)
[![GitHub](https://img.shields.io/github/followers/joseluisgs?style=social)](https://github.com/joseluisgs)

### Contacto

<p>
  Cualquier cosa que necesites házmelo saber por si puedo ayudarte 💬.
</p>
<p>
    <a href="https://twitter.com/joseluisgonsan" target="_blank">
        <img src="https://i.imgur.com/U4Uiaef.png" 
    height="30">
    </a> &nbsp;&nbsp;
    <a href="https://github.com/joseluisgs" target="_blank">
        <img src="https://distreau.com/github.svg" 
    height="30">
    </a> &nbsp;&nbsp;
    <a href="https://www.linkedin.com/in/joseluisgonsan" target="_blank">
        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/LinkedIn_logo_initials.png/768px-LinkedIn_logo_initials.png" 
    height="30">
    </a>  &nbsp;&nbsp;
    <a href="https://joseluisgs.github.io/" target="_blank">
        <img src="https://joseluisgs.github.io/favicon.png" 
    height="30">
    </a>
</p>

## Licencia

Este proyecto está licenciado bajo licencia **MIT**, si desea saber más, visite el fichero [LICENSE](./LICENSE) para su
uso docente y educativo.