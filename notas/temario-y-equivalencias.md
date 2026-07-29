# Temario del curso: tecnologías actuales y equivalencias

Material de referencia del curso de **Java Enterprise Edition (JEE)**.

Este documento recorre los contenidos formativos del curso y, para cada uno, indica:

- **Qué se ha impartido** y en qué sesión.
- **Qué ejemplo concreto** del proyecto de diccionarios lo ilustra, con la ruta del archivo para que podáis abrirlo y releerlo.
- En el caso de las tecnologías que han quedado obsoletas: **qué eran, por qué dejaron de usarse y qué las ha sustituido** en la industria.

El proyecto que hemos construido durante el curso está en la carpeta `diccionarios_v2/` y las notas de cada sesión en `notas/dia1.md` … `notas/dia6.md`.

---

## Índice

1. [Tecnologías del temario que hoy están obsoletas](#1-tecnologías-del-temario-que-hoy-están-obsoletas)
2. [Tabla de equivalencias: ayer y hoy](#2-tabla-de-equivalencias-ayer-y-hoy)
3. [Recorrido por los contenidos formativos](#3-recorrido-por-los-contenidos-formativos)
4. [Mapa del proyecto de prácticas](#4-mapa-del-proyecto-de-prácticas)
5. [Índice de sesiones](#5-índice-de-sesiones)

---

## 1. Tecnologías del temario que hoy están obsoletas

Los primeros bloques del temario original describen las tecnologías con las que se comunicaban sistemas Java distribuidos entre 1997 y 2006 aproximadamente. Todas ellas han sido desplazadas por REST sobre HTTP, que es lo que hemos usado en el curso.

Es importante que las conozcáis: puede que os las encontréis en sistemas heredados (*legacy*) y conviene saber qué son y por qué ya no se construye nada nuevo con ellas.

### RMI (Remote Method Invocation)

**Qué era.** Un mecanismo de Java que permitía invocar métodos de un objeto que en realidad se estaba ejecutando en otra máquina. Desde el código, la llamada parecía local:

```java
Diccionario diccionario = (Diccionario) Naming.lookup("rmi://servidor/diccionario-es");
diccionario.dameSignificados("melón");   // Esta llamada viaja por la red
```

Requería definir interfaces que extendieran `java.rmi.Remote`, generar *stubs* y *skeletons* con la herramienta `rmic`, y arrancar un registro de nombres (`rmiregistry`).

**Por qué se dejó de usar.**

- Solo funciona entre programas Java. Un cliente Android nativo, una web en JavaScript o un sistema en Python no pueden hablar RMI.
- Usa puertos propios y serialización binaria de Java, lo que lo hace difícil de atravesar cortafuegos y *proxies*.
- La serialización nativa de Java ha sido durante años una fuente importante de vulnerabilidades de seguridad.
- Oculta que la llamada es remota. Esto suena bien, pero es precisamente el problema: hace creer al programador que una llamada por red se comporta como una llamada local, cuando no es así.

**Estado actual.** RMI sigue existiendo en la JVM, pero no se usa para sistemas nuevos. El subsistema de activación de RMI se eliminó en Java 17.

**Qué lo ha sustituido.** REST sobre HTTP (lo que hemos hecho en el curso) y, en comunicaciones internas de alto rendimiento, gRPC.

### CORBA, Java IDL, IIOP y RMI-IIOP

**Qué eran.** CORBA (*Common Object Request Broker Architecture*) era un estándar del OMG para hacer lo mismo que RMI pero **entre lenguajes distintos**: un cliente Java podía invocar un objeto escrito en C++ o en COBOL.

Las piezas del temario original encajan así:

| Pieza | Función |
|---|---|
| **IDL** (*Interface Definition Language*) | Lenguaje neutro para describir las interfaces remotas. Se escribía un `.idl` y un compilador generaba el código Java, C++, etc. |
| **Java IDL** / paquete `org.omg` | La implementación de CORBA que venía dentro del JDK. |
| **ORB** (*Object Request Broker*) | El intermediario que localizaba el objeto remoto y transportaba la llamada. |
| **COSNaming** (`org.omg.CosNaming`) | El servicio de nombres de CORBA: permitía registrar y localizar objetos por nombre. |
| **IIOP** (*Internet Inter-ORB Protocol*) | El protocolo binario por el que viajaban las llamadas entre ORBs. |
| **RMI-IIOP** | Un puente: escribir el código con la API de RMI pero transportarlo por IIOP, para poder interoperar con CORBA. Usaba `PortableRemoteObject.narrow()` en lugar del *cast* directo. |

**Por qué se dejó de usar.**

- Complejidad muy alta: definir IDL, compilarlo, generar código, configurar el ORB, gestionar el servicio de nombres… para cada operación.
- Protocolo binario propio, muy difícil de depurar y de atravesar cortafuegos.
- La interoperabilidad entre implementaciones de ORB de distintos fabricantes nunca llegó a funcionar del todo bien en la práctica.
- La llegada de HTTP como transporte universal dejó sin sentido mantener un protocolo específico.

**Estado actual.** **CORBA fue eliminado del JDK en Java 11** (propuesta JEP 320), junto con el compilador `idlj` y el resto del módulo. En el Java que usamos en el curso (Java 21) sencillamente ya no existe.

**Qué lo ha sustituido.** REST + OpenAPI cubre hoy el mismo objetivo —describir un contrato de forma neutra al lenguaje para que sistemas heterogéneos se comuniquen— con muchísimo menos esfuerzo. Lo vimos en `notas/dia4.md`.

### JNDI y los servicios de nombres

**Qué era.** JNDI (*Java Naming and Directory Interface*) es una API para localizar recursos por nombre. En las aplicaciones J2EE clásicas, el código no creaba sus dependencias: las pedía a un directorio central del servidor de aplicaciones.

```java
Context contexto = new InitialContext();
DataSource origenDeDatos = (DataSource) contexto.lookup("java:comp/env/jdbc/MiBaseDeDatos");
```

El servidor de aplicaciones (WebSphere, WebLogic, JBoss) tenía configurada la conexión a la base de datos, y la aplicación la localizaba por su nombre lógico. Así, cambiar de base de datos no obligaba a recompilar la aplicación.

**Estado actual.** JNDI sigue estando en el JDK y los servidores de aplicaciones clásicos lo siguen soportando. Lo que ha cambiado es que ya casi nunca se escribe código de `lookup` a mano.

**Qué lo ha sustituido: la inyección de dependencias.** Y esto sí lo hemos hecho en el curso, en profundidad.

La idea de JNDI y la de Spring es exactamente la misma —*que el componente no construya sus dependencias, sino que las reciba de un directorio central*— pero han cambiado de dirección:

- **JNDI (modelo antiguo):** el componente **va a buscar** lo que necesita. Se llama *Service Locator*.
- **Spring (modelo actual):** el contenedor **le entrega** al componente lo que necesita. Se llama *Inyección de Dependencias*, y es una forma de **Inversión de Control**.

Comparad el ejemplo de JNDI de arriba con lo que hemos escrito nosotros:

```java
// diccionarios_v2/servicio-web/src/main/java/com/curso/diccionarios/servicioweb/DiccionariosRestController.java
public DiccionariosRestController(SuministradorDeDiccionarios suministradorDeDiccionarios) {
    this.suministradorDeDiccionarios = suministradorDeDiccionarios;
}
```

El controlador no busca nada, no conoce ninguna cadena de texto, no depende de ninguna API de directorio. Solo declara qué necesita, y Spring se lo entrega. Y en el caso de la conexión a la base de datos, ni siquiera aparece en nuestro código: la configuración va en `application.properties` (lo vimos en `notas/dia6.md`), que es el equivalente moderno de aquella configuración del servidor de aplicaciones.

En sistemas distribuidos actuales, la parte de "localizar dónde está el servicio" la resuelve el *service discovery* de la plataforma (DNS de Kubernetes, Consul, Eureka).

### SOAP

**Qué era.** El paso intermedio entre CORBA/RMI y REST. Mensajes XML enviados dentro de peticiones HTTP, con el contrato descrito en un archivo WSDL.

**Por qué perdió terreno.** Lo analizamos en `notas/dia4.md`: para enviar la palabra `melón` y el idioma `es` hacían falta más de 300 caracteres de sobre XML. Muy verboso, muy pesado y complejo de programar.

**Estado actual.** JAX-WS, la API de Java para SOAP, también se eliminó del JDK en Java 11 (hay que añadirla como dependencia externa). No se arrancan proyectos nuevos con SOAP salvo por imposición de un tercero, pero **sigue muy presente en producción** en banca, seguros y administración pública. Es bastante probable que os lo encontréis.

---

## 2. Tabla de equivalencias: ayer y hoy

| Concepto del temario original | Equivalente actual | Dónde lo hemos visto |
|---|---|---|
| RMI, CORBA/IIOP | REST sobre HTTP | `notas/dia4.md`, `notas/dia5.md` |
| IDL (definición de interfaces) | OpenAPI (antes Swagger) | `notas/dia4.md` |
| Servicio de nombres JNDI / COSNaming | Inyección de dependencias (contenedor IoC de Spring) | `notas/dia4.md`, `notas/dia5.md` |
| Localizar un servidor remoto | *Service discovery* (DNS, Consul, Eureka) | `notas/dia4.md` |
| XML como formato de intercambio | JSON | `notas/dia1.md`, `notas/dia4.md` |
| Servidor de aplicaciones instalado (WebSphere, WebLogic, JBoss) | Tomcat embebido dentro del propio `.jar` | `notas/dia4.md`, `notas/dia5.md` |
| Descriptores XML (`web.xml`, `ejb-jar.xml`) | Anotaciones (`@RestController`, `@Entity`) + `application.properties` | `notas/dia5.md`, `notas/dia6.md` |
| EJB Session Bean | Componente de Spring (`@Component`, `@Service`) | `notas/dia5.md` |
| EJB Entity Bean | Entidad JPA (`@Entity`) | `notas/dia6.md` |
| DAO escrito a mano con JDBC y SQL | Repositorio de Spring Data JPA | `notas/dia6.md` |
| J2EE (Java 2 Enterprise Edition) | Jakarta EE (paquetes `jakarta.*` en lugar de `javax.*`) | `notas/dia6.md` |

---

## 3. Recorrido por los contenidos formativos

### Punto 2 — El modelo de factorías

Una **factoría** es un componente cuya única responsabilidad es decidir qué implementación concreta se va a construir, de modo que el resto de la aplicación no tenga que saberlo.

Lo vimos en la sesión 3 y lo aplicamos en la aplicación cliente:

```
diccionarios_v2/aplicacion-completa/src/main/java/com/curso/diccionarios/app/cliente/factorias/
    SuministradorDeDiccionariosFactory.java
    InterfazDeUsuarioFactory.java
```

La clase `Aplicacion` no sabe si los diccionarios vienen de ficheros, de una base de datos o de un servicio web. Le pide un `SuministradorDeDiccionarios` a la factoría y trabaja siempre contra la interfaz. Gracias a eso, cambiar de un origen de datos a otro fue **modificar una línea** en la factoría.

Después vimos la evolución natural de este patrón: en el servidor ya no usamos una factoría escrita a mano, sino el contenedor de inyección de dependencias de Spring. La comparación está documentada dentro del propio código, en:

```
diccionarios_v2/servicio-web/src/main/java/com/curso/diccionarios/servicioweb/SuministradorDeDiccionariosConfiguration.java
```

Ese archivo conserva, comentada, la versión con `@Configuration` y `@Bean` (el equivalente Spring de una factoría explícita) junto a la explicación de por qué acabamos sustituyéndola por una simple anotación `@Component` sobre `SuministradorDeDiccionariosEnBBDD`.

---

### Punto 4.4 — Principios de diseño de la orientación a objetos y patrones de diseño

Impartido en la **sesión 3** (`notas/dia3.md`).

**Principios generales:**

- **SoC** (*Separation of Concerns*): cuando construyo un componente, me centro en ese componente y me olvido del resto.
- **DRY** (*Don't Repeat Yourself*): si el código ya existe, lo reutilizo en lugar de reescribirlo.

**SOLID**, los cinco principios:

| Principio | Significado |
|---|---|
| **S** — SRP | *Single Responsibility Principle*: una clase, una responsabilidad. |
| **O** — OCP | *Open/Closed Principle*: abierto a extensión, cerrado a modificación. |
| **L** — LSP | *Liskov Substitution Principle*: cualquier implementación debe poder sustituir a su interfaz sin romper nada. |
| **I** — ISP | *Interface Segregation Principle*: mejor varias interfaces pequeñas que una grande. |
| **D** — DIP | *Dependency Inversion Principle*: los componentes de alto nivel no dependen de implementaciones concretas, sino de abstracciones. |

El **DIP** es el que estructura todo nuestro proyecto. El módulo `diccionarios-api` no contiene ninguna implementación: solo las dos abstracciones de las que depende todo lo demás.

```
diccionarios_v2/diccionarios-api/src/main/java/com/curso/diccionarios/api/
    Diccionario.java
    SuministradorDeDiccionarios.java
```

Y sobre ellas hemos construido **tres implementaciones intercambiables**, sin tocar ni una línea del código que las consume:

| Implementación | Módulo | Origen de los datos |
|---|---|---|
| `SuministradorDeDiccionariosEnFicheros` | `diccionarios-en-ficheros` | Ficheros `.txt` en el *classpath* |
| `SuministradorDeDiccionariosEnServicioWeb` | `diccionarios-en-servicio-web` | Llamadas HTTP a un servidor |
| `SuministradorDeDiccionariosEnBBDD` | `diccionarios-en-bbdd` | Base de datos relacional vía JPA |

Lo mismo con la interfaz de usuario: `ui-api` define el contrato, `ui-consola` lo implementa.

Esa es la razón de que, cuando en la sesión 6 pasamos de ficheros a base de datos, el cambio consistiera en sustituir una dependencia en un `pom.xml`.

---

### Punto 4.5 — Patrones de creación, estructurales y de comportamiento

Los patrones no son código que se copia: son **soluciones con nombre a problemas que se repiten**. Estos son los que hemos aplicado a lo largo del proyecto, clasificados por su categoría.

#### Patrones de creación

**Factory Method / Simple Factory** — centraliza la decisión de qué implementación construir.

```
aplicacion-completa/.../factorias/SuministradorDeDiccionariosFactory.java
aplicacion-completa/.../factorias/InterfazDeUsuarioFactory.java
```

**Singleton** — una única instancia compartida en toda la aplicación.

No lo hemos escrito a mano, pero lo hemos usado constantemente: **todos los componentes de Spring son singletons por defecto**. Cuando anotamos una clase con `@Component`, Spring crea *una sola* instancia y la entrega a todo el que la pida.

```
diccionarios-en-bbdd/.../SuministradorDeDiccionariosEnBBDD.java   →  @Component
diccionarios-en-bbdd/.../CargadorDeDatos.java                     →  @Component
servicio-web/.../DiccionariosRestController.java                  →  @RestController
```

Éste es un buen ejemplo de cómo el framework nos regala patrones ya implementados: obtenemos la garantía de instancia única sin escribir el constructor privado, la variable estática y el control de concurrencia que exigía el Singleton clásico.

#### Patrones estructurales

**Adapter / Gateway** — adapta un sistema externo a la interfaz que nuestra aplicación espera.

```
diccionarios-en-servicio-web/.../DiccionarioEnServicioWeb.java
diccionarios-en-servicio-web/.../SuministradorDeDiccionariosEnServicioWeb.java
```

Estas clases implementan `Diccionario` y `SuministradorDeDiccionarios` —las mismas interfaces de siempre— pero por dentro hacen peticiones HTTP y traducen el JSON de respuesta a objetos Java. Para la aplicación cliente son indistinguibles de la versión que leía ficheros.

**Proxy remoto** — un objeto local que representa a otro que vive en otra máquina.

Es exactamente lo que hace `DiccionarioEnServicioWeb`: desde `InterfazDeUsuarioConsola` se le llama como a cualquier objeto, y por debajo la llamada cruza la red. Este es, conceptualmente, el mismo servicio que prestaban RMI y CORBA, pero construido sobre HTTP y JSON, y con la diferencia importante de que aquí **el programador es consciente de que la llamada es remota**.

#### Patrones de comportamiento

**Strategy** — familia de algoritmos intercambiables detrás de una misma interfaz.

Es la columna vertebral del proyecto. `SuministradorDeDiccionarios` es la interfaz de la estrategia, y sus tres implementaciones (ficheros, servicio web, base de datos) son las estrategias concretas. La aplicación elige una en tiempo de arranque y el resto del código es indiferente a cuál sea.

**Template Method** — el esqueleto del algoritmo lo pone el framework, nosotros rellenamos los huecos.

```
diccionarios-en-bbdd/.../CargadorDeDatos.java   →  implements CommandLineRunner
```

Spring define *cuándo* se ejecuta el arranque de la aplicación y en qué orden; nosotros solo escribimos el método `run()`. No controlamos el flujo: lo controla el framework. Esto enlaza directamente con la Inversión de Control.

#### Patrón de acceso a datos

**Repository** — encapsula el acceso a la persistencia detrás de una interfaz orientada al dominio.

```
diccionarios-en-bbdd/src/main/java/com/curso/diccionarios/bbdd/repositorios/
    IdiomaRepository.java
    PalabraRepository.java
    SignificadoRepository.java
```

Aquí ocurre algo llamativo que vimos en la sesión 6: **nosotros solo declaramos la interfaz**, y Spring Data JPA genera la implementación en tiempo de arranque, incluida la consulta SQL, deduciéndola del nombre del método:

```java
boolean existsByCodigo(String codigo);
Optional<Palabra> findByPalabraAndIdioma_Codigo(String palabra, String codigo);
```

---

### Punto 4.6 — Bloques de constitución arquitectónicos

Impartido en la **sesión 4** (`notas/dia4.md`).

Partimos de una aplicación con diez archivos sueltos en una carpeta y la reorganizamos en un **proyecto Maven multimódulo**: un `pom.xml` padre sin código que agrupa once módulos, cada uno con una responsabilidad única y con sus dependencias declaradas explícitamente.

```
diccionarios_v2/pom.xml          ←  proyecto agrupador (packaging: pom)
```

El principio de fondo: **un módulo es la unidad de reutilización, de versionado y de despliegue**. Al separar `diccionarios-api` de sus implementaciones, cualquiera puede escribir una implementación nueva sin tocar —ni siquiera recompilar— lo existente.

Esto se demostró en la práctica dos veces:

1. Al pasar de aplicación monolítica a cliente-servidor (sesión 5): dos módulos nuevos, una línea modificada en el resto del sistema.
2. Al pasar de ficheros a base de datos (sesión 6): un módulo nuevo y un cambio de dependencia en el `pom.xml` del servicio web.

Complementariamente, en la sesión 1 vimos el **versionado semántico** (`MAYOR.MENOR.PARCHE`), que es lo que permite que esos módulos evolucionen de forma independiente sin romper a quien los consume.

---

### Punto 5.1 — Patrones para la capa de integración

La **capa de integración** es la que habla con los sistemas externos: bases de datos, servicios de terceros, ficheros.

**DAO (Data Access Object)** — aislar el acceso a datos del resto de la aplicación, de forma que cambiar el almacenamiento no afecte a la lógica de negocio.

Es el patrón J2EE clásico, y su forma actual es el **Repository de Spring Data JPA** que hemos usado:

```
diccionarios-en-bbdd/.../repositorios/IdiomaRepository.java
diccionarios-en-bbdd/.../repositorios/PalabraRepository.java
```

**Domain Store / mapeo objeto-relacional** — que el programador trabaje con objetos y no con filas y columnas.

Es la función de **JPA** (el estándar, parte de Jakarta EE) e **Hibernate** (la implementación). Nuestras entidades:

```
diccionarios-en-bbdd/src/main/java/com/curso/diccionarios/bbdd/entidades/
    Idioma.java        →  @Entity @Table(name = "idiomas")
    Palabra.java       →  @Entity + @ManyToOne + @OneToMany
    Significado.java   →  @Entity + @ManyToOne
```

En la sesión 6 partimos del diseño entidad-relación y del script SQL que **antiguamente** habría que escribir a mano (`CREATE TABLE`, `INSERT`…) y comprobamos que Hibernate lo genera solo a partir de las anotaciones. Ese contraste está desarrollado en `notas/dia6.md`.

**Data Loader** — inicialización del almacén de datos en el arranque.

```
diccionarios-en-bbdd/.../CargadorDeDatos.java
```

Lee los ficheros de diccionario del *classpath* y vuelca su contenido en la base de datos, comprobando primero que no haya datos ya cargados. En la sesión se mencionó que en producción este trabajo lo hacen herramientas especializadas en versionado de esquemas de base de datos: **Liquibase** y **Flyway**.

---

### Punto 5.2 — Patrones para la capa presentation-to-business

Son los patrones que gobiernan **cómo la capa de presentación habla con la capa de negocio**, especialmente cuando hay una frontera de red entre ambas.

**Transfer Object / DTO (Data Transfer Object)** — un objeto sin lógica, cuyo único cometido es transportar datos a través de una frontera.

```
servicio-web/.../modelos/RespuestaPalabra.java              (lado servidor)
diccionarios-en-servicio-web/.../RespuestaPalabra.java      (lado cliente)
```

En la sesión 4 lo llamamos **POJO** (*Plain Old Java Object*). La razón de existir de este patrón es que atravesar la red es caro: en lugar de hacer muchas llamadas remotas para pedir dato a dato, se hace una sola y se devuelve un objeto con todo lo necesario.

Spring lo convierte a JSON automáticamente (con la librería Jackson) al devolverlo desde el controlador; en el cliente lo reconstruimos desde el JSON con Gson.

**Business Delegate** — un objeto local que oculta a la capa de presentación toda la mecánica de la invocación remota.

```
diccionarios-en-servicio-web/.../SuministradorDeDiccionariosEnServicioWeb.java
diccionarios-en-servicio-web/.../DiccionarioEnServicioWeb.java
```

`InterfazDeUsuarioConsola` no sabe nada de HTTP, ni de URLs, ni de JSON, ni de códigos de estado. Todo eso vive dentro del delegado.

**Service Locator** — localizar el servicio remoto sin que el cliente tenga la dirección incrustada.

En su versión clásica esto era JNDI. En nuestro proyecto, la ruta del servidor se le pasa al constructor del delegado y la decide la factoría:

```java
public DiccionarioEnServicioWeb(String rutaServidor /*http://localhost:8080*/, String idioma)
```

---

### Punto 5.3 — Patrones para la capa intra-business

Son los patrones que organizan la propia **capa de negocio**.

**Service Façade (Fachada de servicio)** — exponer una interfaz única y sencilla que oculte la complejidad interna de un subsistema.

```
diccionarios-api/.../SuministradorDeDiccionarios.java
```

Toda la funcionalidad del sistema pasa por dos métodos: `tienesDiccionarioDe(idioma)` y `dameDiccionarioDe(idioma)`. Detrás puede haber lectura de ficheros, consultas SQL con JOINs a tres tablas, o peticiones HTTP. El consumidor no lo sabe ni le importa.

Ésta es la versión moderna del **Session Façade** de J2EE, que cumplía exactamente el mismo papel sobre EJBs.

**Inversión de Control e Inyección de Dependencias** — el componente declara qué necesita; el contenedor se lo proporciona.

Es el patrón central de Spring y lo hemos usado en todas las clases del servidor:

```java
// servicio-web/.../DiccionariosRestController.java
public DiccionariosRestController(SuministradorDeDiccionarios suministradorDeDiccionarios) { … }

// diccionarios-en-bbdd/.../SuministradorDeDiccionariosEnBBDD.java
public SuministradorDeDiccionariosEnBBDD(IdiomaRepository idiomasRepository,
                                         PalabraRepository palabraRepository) { … }

// diccionarios-en-bbdd/.../CargadorDeDatos.java
public CargadorDeDatos(IdiomaRepository idiomaRepository,
                       PalabraRepository palabraRepository) { … }
```

Ninguna de estas clases ejecuta `new` sobre sus dependencias. Y ésa es la razón de que sean fáciles de probar: en una prueba automatizada se les puede pasar una implementación falsa.

También vimos la inyección de configuración con `@Value`:

```java
@Value("${diccionarios.carpeta:diccionarios}")
private String carpetaDeDiccionarios;
```

Que lee la propiedad `diccionarios.carpeta` de `application.properties` y, si no existe, usa `diccionarios` como valor por defecto.

---

### Puntos 5.4 y 5.5 — Patrones para las capas de micro y macro presentación

**Macro presentación** es la estructura general que recibe y encamina las peticiones. **Micro presentación** es cómo se construye cada respuesta concreta.

**Front Controller** — un único punto de entrada que recibe *todas* las peticiones y las encamina hacia el código que corresponde.

Este patrón lo aporta Spring: internamente hay un componente llamado `DispatcherServlet` que recibe absolutamente todas las peticiones HTTP y consulta la tabla de rutas. Nuestra parte es declarar esas rutas:

```java
// servicio-web/.../DiccionariosRestController.java
@RestController
public class DiccionariosRestController {

    @GetMapping("/diccionarios/{idioma}")
    public ResponseEntity<Void> existeDiccionarioDe(@PathVariable("idioma") String idioma) { … }

    @GetMapping("/diccionarios/{idioma}/{palabra}")
    public ResponseEntity<RespuestaPalabra> existePalabra(@PathVariable("idioma") String idioma,
                                                          @PathVariable("palabra") String palabra) { … }
}
```

**Application Controller** — traducir una petición entrante en una invocación al negocio y una respuesta.

Es lo que hace cada método del controlador: extraer los parámetros de la ruta, llamar a la fachada de negocio y construir un `ResponseEntity` con su código de estado y su cuerpo.

**Separación estricta presentación / negocio** — la presentación no contiene lógica.

En el cliente, la misma idea con otro medio:

```
ui-api/.../InterfazDeUsuario.java              ←  el contrato
ui-consola/.../InterfazDeUsuarioConsola.java   ←  la implementación para terminal
```

Ninguna de las dos sabe de dónde salen los diccionarios. Por eso la interfaz de consola se reutilizó **sin modificarla** cuando el origen de datos pasó de ficheros a un servicio web remoto.

**Diseño del contrato de la API (equivalente al IDL de CORBA).** En la sesión 4 definimos, antes de escribir código, qué rutas expondría el servidor y qué respondería en cada caso:

| Petición | Situación | Estado HTTP | Cuerpo |
|---|---|---|---|
| `GET /diccionarios/{idioma}` | El diccionario existe | `200` | *(vacío)* |
| `GET /diccionarios/{idioma}` | El diccionario no existe | `404` | *(vacío)* |
| `GET /diccionarios/{idioma}/{palabra}` | Diccionario y palabra existen | `200` | `{"idioma": "es", "palabra": "casa", "significados": [ … ]}` |
| `GET /diccionarios/{idioma}/{palabra}` | El diccionario existe, la palabra no | `404` | `{"idioma": "es"}` |
| `GET /diccionarios/{idioma}/{palabra}` | El diccionario no existe | `404` | `{}` |

Ese contrato es lo que se entrega al equipo que va a construir el cliente. El estándar actual para escribirlo de forma formal es **OpenAPI**, sucesor de Swagger, y es el equivalente moderno del IDL de CORBA.

---

### Punto 5.6 — Antipatrones

Un **antipatrón** es una solución que parece razonable, se repite mucho, y produce sistemáticamente malos resultados. El curso entero está construido sobre la corrección de tres de ellos.

**Big Ball of Mud (gran bola de barro)** — sistema sin estructura reconocible.

Nuestro punto de partida: diez archivos `.java` sueltos en una carpeta, sin paquetes ni módulos (carpeta `diccionarios/`, la primera versión). Como se analizó en la sesión 4: con diez archivos parece manejable, con cuatrocientos es imposible saber qué hace cada cosa y qué se rompe al tocarla.

**Monolito** — un único sistema que lo hace todo, donde cualquier cambio puede impactar en cualquier otra parte.

Analizado en la sesión 1 con el ejemplo de "Animalitos Fermín" (venta, citas veterinarias, peluquería y nóminas en el mismo sistema). La alternativa que aplicamos: **arquitecturas de componentes desacoplados**.

**Cliente pesado con datos embebidos** — meter en cada instalación cliente los datos y la lógica que deberían estar centralizados.

Es el problema que motivó todo el rediseño, analizado en la sesión 4 con sus consecuencias por cada implicado:

| Implicado | Consecuencia |
|---|---|
| Desarrollo | Reempaquetar y redistribuir por cada corrección de un diccionario |
| Usuario | Reinstalar la aplicación cada vez |
| Operaciones | Sin visibilidad de qué versión tiene cada usuario |
| Soporte (CAU) | Imposible reproducir una incidencia sin saber la versión del cliente |
| Negocio | Dos usuarios pueden obtener respuestas distintas a la misma consulta |

**Acoplamiento a implementaciones concretas** — depender de la clase concreta en lugar de la abstracción.

Es la violación del principio DIP (punto 4.4). Su corrección es lo que hace que nuestro sistema tenga tres orígenes de datos intercambiables.

---

### Punto 6.1 — Guías y heurísticas del desarrollo de arquitecturas de sistemas

Impartido en la **sesión 4** (`notas/dia4.md`).

La heurística de fondo del curso, enunciada literalmente en la primera sesión:

> No basta con que una aplicación funcione. La clave es que la aplicación envejezca bien: que sea mantenible, fácil de entender, de modificar y de evolucionar.

De ahí se derivan las dos herramientas de análisis que usamos repetidamente:

**1. Análisis por escenarios de cambio.** No se evalúa una arquitectura en abstracto, sino frente a los cambios que sabemos que van a ocurrir. Los que planteamos:

- Cambiar la interfaz de usuario.
- Añadir funcionalidad (por ejemplo, sugerir palabras similares cuando no hay coincidencia exacta).
- Añadir un idioma nuevo.
- Añadir o corregir palabras de un diccionario existente.
- Cambiar el algoritmo de búsqueda (por ejemplo, ignorar mayúsculas y acentos).

**2. Análisis de impacto por implicado.** Cada cambio no cuesta lo mismo a todo el mundo. Hay que evaluarlo desde la perspectiva de desarrollo, del usuario final, de operaciones y de soporte. Un cambio barato para desarrollo puede ser carísimo para operaciones.

**El criterio económico.** La conclusión de la sesión 5, que es la justificación de fondo de toda la arquitectura del curso:

> El coste de desarrollo inicial de una arquitectura desacoplada es mayor. El coste de mantenimiento a lo largo del ciclo de vida es mucho menor. Lo que importa no es el coste inicial, sino el **coste total del ciclo de vida** del producto.

**Heurística de separación de responsabilidades por rol.** Un caso concreto analizado en la sesión 4: los ficheros de diccionario los mantiene un lingüista, no un programador. Tenerlos mezclados con el código fuente, en la estructura `src/main/resources`, obliga a alguien que no sabe programar a moverse por un árbol de carpetas que no entiende, con riesgo de romper el código. La arquitectura debe reflejar también quién mantiene cada cosa.

---

### Punto 6.2 — Descripción del proceso de desarrollo del software

Contenidos repartidos entre las sesiones 2 y 3.

**Gestión del proyecto y del ciclo de construcción: Maven** (`notas/dia2.md`).

Estructura estándar que impone Maven, y que es idéntica en cualquier proyecto Java del mundo:

```
MI-PROYECTO/
    src/main/java/         ←  código de la aplicación
    src/main/resources/    ←  recursos (nuestros diccionarios .txt)
    src/test/java/         ←  código de las pruebas automatizadas
    src/test/resources/    ←  recursos de las pruebas
    target/                ←  todo lo generado (lo crea Maven)
    pom.xml                ←  configuración del proyecto
```

Fases del ciclo de vida, que se ejecutan en orden:

```
resources  →  compile  →  test  →  package  →  install
clean      ←  borra target/
```

**Gestión de dependencias.** El problema que resuelve: un proyecto mediano acaba con más de cien librerías, cada una con sus propias dependencias (**dependencias transitivas**), y todas cambiando de versión. Buscarlas y descargarlas a mano era trabajo de días.

**Versionado semántico** (`notas/dia1.md`): `MAYOR.MENOR.PARCHE`. Es el contrato que permite a un equipo actualizar una dependencia sabiendo si va a romperle algo.

**Control de versiones.** Git como repositorio del código *y* de los diccionarios, con la observación de la sesión 4: los diccionarios deben ir versionados igual que el código, y el lingüista debe poder aportarlos por una vía trazable, no por correo electrónico.

**Pruebas automatizadas** (`notas/dia3.md`). El razonamiento completo:

- Probar a mano es lento, no es fiable y no es repetible.
- Cada cambio futuro obliga a **volver a probarlo todo**, no solo lo que se ha tocado.
- Por eso se escribe un programa que pruebe el programa. Y no una prueba grande, sino muchas pequeñas: si falla una prueba que ejercita todo el sistema, no sabes qué componente ha fallado.
- La librería estándar en Java es **JUnit**, versión 5 (cuidado con los ejemplos de JUnit 4 que circulan por Internet: la sintaxis es distinta).

Ejemplos en el proyecto:

```
diccionarios-en-ficheros/src/test/java/com/curso/diccionarios/ficheros/
    DiccionarioEnFicheroTest.java
    SuministradorDeDiccionariosEnFicheroTest.java
```

**Control de calidad automatizado.** En la industria se mide la **cobertura de pruebas** (porcentaje de líneas de código ejercitadas por las pruebas) y herramientas como **SonarQube** bloquean el paso a producción si no se alcanza el umbral fijado, típicamente entre el 80% y el 90%. Es una comprobación automática: no hay negociación posible con ella.

---

### Punto 6.3 — Diferencias entre el desarrollo local de objetos y el desarrollo distribuido de objetos

Este punto lo hemos recorrido **construyéndolo**: las tres versiones del sistema son exactamente esa transición.

```
Versión 1  ─  Todo en un proceso, en el ordenador del usuario
              Aplicación → SuministradorDeDiccionariosEnFicheros → ficheros .txt

Versión 2  ─  Dos procesos, dos máquinas, una red en medio
              Aplicación → SuministradorDeDiccionariosEnServicioWeb
                              ↓ HTTP
                           DiccionariosRestController → SuministradorDeDiccionariosEnFicheros

Versión 3  ─  Igual, pero con la persistencia en base de datos
              … → DiccionariosRestController → SuministradorDeDiccionariosEnBBDD → BBDD
```

**Lo que no cambia:** la interfaz. `dameSignificados(palabra)` se llama igual en los tres casos. Ésa es la ventaja del diseño basado en abstracciones.

**Lo que sí cambia**, y que es la esencia de este punto del temario:

| | Llamada local | Llamada distribuida |
|---|---|---|
| **Coste** | Nanosegundos | Milisegundos: entre 10.000 y 1.000.000 de veces más cara |
| **Datos** | Se pasa una referencia en memoria | Hay que **serializar** a JSON, enviar y **deserializar** |
| **Fallos** | La llamada se ejecuta o lanza una excepción | La llamada puede además no llegar, llegar y no volver, o tardar indefinidamente |
| **Acoplamiento** | El compilador verifica los tipos | El contrato es un acuerdo externo: si el servidor cambia el JSON, el cliente no compila mal, **falla en ejecución** |
| **Versionado** | Se despliega todo junto | Cliente y servidor evolucionan por separado y deben mantener la compatibilidad |

De ahí se derivan las decisiones de diseño que tomamos:

- **Enviar el resultado completo en una sola respuesta** (el DTO `RespuestaPalabra` con la lista de significados) en lugar de una llamada por significado. Minimizar el número de viajes por la red es la regla número uno del diseño distribuido.
- **Usar los códigos de estado HTTP** (200, 404, 500) para distinguir situaciones, en lugar de excepciones Java, porque las excepciones no cruzan la red.
- **Reservar el 500** en el contrato precisamente para el caso de que el servidor no pueda atender la petición: en un sistema distribuido, "no encontrado" y "no disponible" son cosas distintas y el cliente tiene que poder diferenciarlas.

Y también explica por qué RMI y CORBA acabaron descartados: su promesa era hacer que una llamada remota *pareciera* local, y esa promesa no se puede cumplir, porque la red no es fiable, no es instantánea y no tiene ancho de banda infinito.

**Fundamentos del protocolo HTTP** (sesión 4). Todo lo anterior descansa sobre entender HTTP:

- Es **unidireccional**: siempre inicia el cliente, el servidor responde.
- Es **síncrono**: el cliente espera la respuesta.
- Toda petición y toda respuesta tienen tres partes: **URL**, **cabeceras** (*headers*, metadatos) y **cuerpo** (*body*, opcional).
- **Verbos:** `GET` (pedir datos), `POST` (enviar datos), `PUT` (modificar), `DELETE` (borrar).
- **Códigos de estado:** `2xx` correcto, `3xx` redirección, `4xx` error del cliente, `5xx` error del servidor.

---

### Puntos 7.3, 7.4 y 7.5 — Servidores y tecnología Jakarta EE

**Qué es JEE** (`notas/dia6.md`). Antes se llamaba **J2EE** (*Java 2 Enterprise Edition*); hoy se llama **Jakarta EE**, tras la cesión de la plataforma por parte de Oracle a la Fundación Eclipse. Ése es el motivo de que los paquetes se llamen ahora `jakarta.persistence.*` y no `javax.persistence.*`, como veréis en nuestras entidades.

JEE **no es una librería, sino una colección de estándares** que definen cómo debe construirse una aplicación empresarial en Java. El que hemos usado a fondo es **JPA** (*Java Persistence API*), el estándar de persistencia en bases de datos relacionales.

Esta distinción es importante:

| | Qué es |
|---|---|
| **JPA** | El **estándar**: define las anotaciones (`@Entity`, `@Id`, `@ManyToOne`) y el comportamiento esperado |
| **Hibernate** | Una **implementación** concreta de ese estándar |
| **Spring Data JPA** | Una capa por encima que elimina el código repetitivo (los repositorios) |

**Servidores de aplicaciones.** Un servidor de aplicaciones es el programa que aloja nuestra aplicación, abre un puerto de red, recibe las peticiones HTTP y se las encamina. Los clásicos de J2EE, que se instalaban aparte y en los que se desplegaba un `.war` o un `.ear`: **WebSphere** (IBM), **WebLogic** (Oracle), **JBoss/WildFly** (Red Hat) y **Apache Tomcat**.

Lo que hemos hecho nosotros es el modelo actual: **Tomcat embebido**. Spring Boot mete el servidor de aplicaciones *dentro* del `.jar` de nuestra aplicación. No hay nada que instalar ni que configurar en la máquina destino: basta con Java y ejecutar el `.jar`.

**Framework frente a librería** (`notas/dia4.md`), la distinción conceptual clave:

> Una **librería** la integro yo en mi programa: yo llamo a su código cuando lo necesito.
> Un **framework** no lo integro: **construyo mi programa alrededor de él**, y es él quien llama a mi código.

**Spring** es un framework: más de doscientas librerías para construir los tipos de aplicación Java más habituales, imponiendo una forma común de hacerlo. **Spring Boot** es la pieza que hace que configurar Spring deje de ser una tarea ingente.

**Inversión de Control**, que es lo que convierte a Spring en un framework y no en una librería: nosotros ya no escribimos el flujo de la aplicación. Toda aplicación Spring del mundo tiene la misma función `main`, de una sola línea:

```java
// servicio-web/.../ServicioWeb.java
@SpringBootApplication(scanBasePackages = {"com.curso.diccionarios"})
@EnableJpaRepositories(basePackages = {"com.curso.diccionarios.bbdd.repositorios"})
@EntityScan(basePackages = {"com.curso.diccionarios.bbdd.entidades"})
public class ServicioWeb {
    public static void main(String[] args) {
        SpringApplication.run(ServicioWeb.class, args);
    }
}
```

**Evaluación de la elección tecnológica.** Spring Boot escribe por nosotros la mayor parte del sistema, y eso tiene dos caras que conviene tener claras:

- **A favor:** desarrollo mucho más rápido, muchísimo menos código propio, y un estándar de facto que hace que cualquier desarrollador reconozca la estructura de un proyecto Spring en cualquier empresa.
- **En contra:** hace mucha "magia" por debajo. Si no se entiende qué está haciendo el framework, se llega a un punto en el que se lee el código fuente y no se entiende por qué el programa funciona, porque una gran parte del comportamiento no está escrita en ningún archivo del proyecto.

De ahí que en el curso hayamos ido enseñando primero cómo se hacía a mano (la factoría, el script SQL) y después qué parte de ese trabajo asume el framework.

**Configuración del acceso a datos** (`notas/dia6.md`). Spring busca al arrancar un archivo `application.properties` con la configuración de la base de datos:

```properties
# Ejemplo para Oracle
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=usuario
spring.datasource.password=contraseña
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.jpa.database-platform=org.hibernate.dialect.Oracle10gDialect
```

En desarrollo hemos usado **H2**, una base de datos en memoria que se crea al arrancar y desaparece al parar. Basta con declarar su dependencia en el `pom.xml`: al no encontrar configuración de ninguna otra base de datos, Spring la arranca y la configura solo. Cada ejecución parte de un entorno limpio, que es justo lo que interesa para probar.

---

### Puntos 7.7 y 7.8 — Creación de un diseño software y de una arquitectura en capas

El resultado de todo el curso es una arquitectura distribuida en capas, construida de forma incremental en tres versiones.

**Diseño de datos** (sesión 6). Antes de escribir código, el modelo entidad-relación:

```
    Idiomas                   Palabras                          Significados
    | ID | codigo |    -<     | ID | palabra | idioma_id |  -<  | ID | palabra_id | significado |
```

Con sus relaciones y sus restricciones de integridad:

- Un idioma tiene muchas palabras · una palabra pertenece a un idioma → `@OneToMany` / `@ManyToOne`
- Una palabra tiene muchos significados · un significado pertenece a una palabra → `@OneToMany` / `@ManyToOne`
- `codigo` es único en `idiomas`
- La combinación (`palabra`, `idioma_id`) es única: la misma palabra puede existir en varios idiomas, pero no dos veces en el mismo
- La combinación (`significado`, `palabra_id`) es única: un significado no puede repetirse en la misma palabra

**Arquitectura final del sistema:**

```
  MÁQUINA DEL USUARIO                                    SERVIDOR CENTRAL
  ─────────────────────────────────────────────    ───────────────────────────────────────────

  Aplicacion                                        DiccionariosRestController      ← Presentación
      │                                                        │
  InterfazDeUsuario (ui-api)                                   │
      └── InterfazDeUsuarioConsola                             │
      │                                                        │
  SuministradorDeDiccionarios (diccionarios-api)    SuministradorDeDiccionarios     ← Negocio
      └── …EnServicioWeb  ──────── HTTP/JSON ───────→  └── …EnBBDD
                                                               │
                                                     PalabraRepository              ← Integración
                                                     IdiomaRepository
                                                               │
                                                          Base de datos             ← Persistencia
```

Nótese que `diccionarios-api` —el contrato— aparece **a los dos lados**. Es el mismo módulo, reutilizado en cliente y servidor. Ésa es la ventaja de haber separado la abstracción de sus implementaciones.

**Balance de la evolución.** Al pasar de la versión 1 a la versión 3 se reutilizaron sin modificarlos: `diccionarios-api`, `ui-api`, `ui-consola`, los tres módulos de diccionarios (`diccionario-es`, `diccionario-en`, `diccionario-elfico`) y la lógica de la aplicación cliente. Se añadieron tres módulos nuevos (`servicio-web`, `diccionarios-en-servicio-web`, `diccionarios-en-bbdd`) y se modificó una línea de la factoría más una dependencia en un `pom.xml`.

Ése es, en una frase, el objetivo de todo lo que hemos visto en el curso: **que un cambio grande en el sistema se traduzca en un impacto pequeño en el código existente**.

---

## 4. Mapa del proyecto de prácticas

```
diccionarios/            ←  Versión 1: aplicación monolítica, sin estructura
                            (se conserva como punto de partida y contraste)

diccionarios_v2/         ←  Versiones 2 y 3: proyecto Maven multimódulo
    pom.xml                        proyecto agrupador (sin código)

    diccionarios-api               CONTRATO: Diccionario, SuministradorDeDiccionarios
    ui-api                         CONTRATO: InterfazDeUsuario

    diccionario-es                 DATOS: diccionario de español
    diccionario-en                 DATOS: diccionario de inglés
    diccionario-elfico             DATOS: diccionario élfico

    diccionarios-en-ficheros       IMPLEMENTACIÓN: lectura de ficheros del classpath
    diccionarios-en-bbdd           IMPLEMENTACIÓN: JPA + Hibernate + repositorios
    diccionarios-en-servicio-web   IMPLEMENTACIÓN: cliente HTTP contra el servicio REST
    ui-consola                     IMPLEMENTACIÓN: interfaz de usuario en terminal

    servicio-web                   SERVIDOR: Spring Boot + controlador REST
    aplicacion-completa            CLIENTE: main, factorías, orquestación
```

**Comandos habituales:**

```bash
# Compilar, probar e instalar todos los módulos (desde diccionarios_v2/)
mvn install

# Arrancar el servicio web (desde diccionarios_v2/servicio-web/)
mvn spring-boot:run

# Probar los endpoints
curl http://localhost:8080/diccionarios/es
curl http://localhost:8080/diccionarios/es/casa
```

Para probar servicios REST de forma cómoda desde el navegador: la extensión **Boomerang (SOAP & REST client)**, disponible para Firefox y Chrome.

---

## 5. Índice de sesiones

| Sesión | Archivo | Contenidos |
|---|---|---|
| 1 | `notas/dia1.md` | Java como lenguaje: tipado, gestión de memoria, compilación. Historia y versiones de Java. Evolución de las arquitecturas empresariales: escritorio → cliente-servidor → web → componentes desacoplados. Internet frente a Web. HTML, XML, JSON. Primera aplicación. Versionado semántico. |
| 2 | `notas/dia2.md` | Instalación del entorno: JDK 21, Maven, VS Code, variables de entorno. Maven: estructura de proyecto, gestión de dependencias, dependencias transitivas, ciclo de vida y fases. |
| 3 | `notas/dia3.md` | Usos de la memoria RAM. Caché y `WeakHashMap`. Principios SoC y DRY. SOLID. Patrón Factoría. El *classpath*. Pruebas automatizadas con JUnit 5, cobertura y SonarQube. |
| 4 | `notas/dia4.md` | Análisis de los problemas de la arquitectura inicial. Arquitectura cliente-servidor. Historia de los protocolos: CORBA, RMI, SOAP, REST. Fundamentos de HTTP. Diseño del API REST. Refactorización a proyecto multimódulo. Framework frente a librería. Spring, Spring Boot e Inversión de Control. |
| 5 | `notas/dia5.md` | Construcción del servicio web con Spring Boot: `@SpringBootApplication`, `@RestController`, `@GetMapping`, `ResponseEntity`. Pruebas de servicios REST. Construcción del nuevo cliente contra el servicio web. Análisis de la reutilización lograda. |
| 6 | `notas/dia6.md` | Las tres versiones del sistema. Diseño entidad-relación. JPA e Hibernate como ORM. Entidades, anotaciones y relaciones. Repositorios de Spring Data y consultas derivadas del nombre del método. Configuración del acceso a datos y H2 en memoria. Carga inicial con `CommandLineRunner`. Liquibase y Flyway. J2EE, JEE y Jakarta EE. |
