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
  - [Fichero de propiedades](#fichero-de-propiedades)
  - [Spring Data](#spring-data)
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
## Fichero de propiedades
A la hora de utilizar el contenedor de Spring es una buena práctica separar la configuración de los beans, parcial o totalmente realizada en ficheros XML o clases de configuración (internamente), y los parámetros de configuración que utilizan esos beans como por ejemplo contraseñas o la ubicación de la base de datos. 

Lo habitual es definir los parámetros de configuración en ficheros de propiedades estándar de Java (.properties). Spring permite utilizar cómodamente este tipo de ficheros tal y como vamos a ver y con ello realizar la configuración de los beans sin pasar por el tedioso proceso de configuración de XML.

## Spring Data


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