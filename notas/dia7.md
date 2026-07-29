
App Diccionarios

V1 - Cliente                (diccionarios en ficheros de texto)

V2 - Cliente-Servidor       (diccionarios en ficheros de texto)

V3 - Cliente-Servidor       (diccionarios en ficheros de texto -> carga de los diccionarios en BBDD -H2-)


---

# Sobre la funcionalidad

- Nueva función en el servidor para obtener un listado de los idiomas disponibles en el servidor.
- Modificar las búsquedas, de forma que no afecte el escribir la palabra en mayúsculas o minúsculas.
- ???


# Cosas que le faltan.

Cuando se produce un error en el lado del servidor (por ejemplo, si la BBDD no está operativa), nuestra aplicación no lo gestiona adecuadamente. La aplicación debería contestar con un error HTTP Status 500.

Para hacer esto, vamos a explicar antes un patrón de desarrollo de software muy usado hoy en día: PROXY.
Gracias a ese patrón podremos aplicar lo que denominamos Programación Orientada a Aspectos (AOP). 

AOP es una forma de crear programas/escribir código que tiene muchas ventajas:
- Permite separar la lógica de negocio de la lógica de control de errores, generando un código más limpio y fácil de mantener.
- Activar o desactivar funcionalidades de forma sencilla, sin tener que modificar el código de la lógica de negocio.

Spring, que es el framework que nosotros estamos usando, tiene soporte nativo para AOP. En este caso, vamos a usarlo para gestionar los errores que se produzcan en el servidor y que no sean controlados por la lógica de negocio.

---

# Estado del arte del servidor web:

Tenemos 3 endpoints actualmente

/diccionarios/test                  Simplemente devuelve un mensaje de prueba para comprobar que el 
                                    servidor está operativo.

/diccionarios/es
             /en                    Saber si un diccionario está disponible en el servidor. 
                                    Devuelve un 200 si está disponible y un 404 si no lo está.

/diccionarios/es/casa               Obtener los significados de una palabra en un idioma determinado. 
                                    Devuelve un 200 si la palabra existe en el idioma 
                                        y un 404 si la palabra o el idioma no existen.
                                    Adicionalmente, devuelve un archivo JSON en el CUERPO de la respuesta con los significados de la palabra en el idioma solicitado cuando la palabra existe en el idioma solicitado.

- [√] Nueva función en el servidor para obtener un listado de los idiomas disponibles en el servidor.

Esto implica que necesitamos un nuevo endpoint en el servidor, que podría ser algo como:

/diccionarios                       Debe devolver un Listado de textos con los códigos de los idiomas
                                    disponibles en el servidor. Por ejemplo, si tenemos diccionarios para español, inglés y éfico, la respuesta podría ser algo como:

                                        ```
                                        ["es", "en", "elfico"]
                                        ```
- [√] Modificar las búsquedas, de forma que no afecte el escribir la palabra en mayúsculas o minúsculas.

Aquí hay varias opciones acerca de dónde implementar este cambio. Hay que tomar decisiones...

> Un sitio donde podríamos hacerlo es en el REPOSITORIO.

El repositorio es el componente que tenemos que permite interactuar con la BBDD.

 Podríamos hacer que cuando se haga una query por palabra e idioma, se ignorase si la palabra está en mayúsculas o minúsculas. Esto se puede hacer con una query SQL que use LOWER() o UPPER() para normalizar la palabra antes de compararla con la base de datos.

```java
// Esta función existe en nuestro repositorio de palabras, y es la que usamos para buscar una palabra en un idioma determinado.
    Optional<Palabra> findByPalabraAndIdioma_Codigo(String palabra, String codigo);
```
Spring/Hibernate, son los que generan el cuerpo de esta función, y por tanto, la query SQL que se ejecuta en la BBDD. Esa query derá del tipo:

```sql
SELECT * FROM palabras, idiomas WHERE palabra = ? AND codigo = ? AND palabras.idioma_id = idiomas.id
```

Esa query no la vemos... Spring la está construyendo por nosotros. Pero podríamos pedir a Spring que generase esa query de forma que las mayúsculas y minúsculas no afectasen a la búsqueda. Para ello, podríamos modificar la query para que fuese algo como:

```sql
SELECT * FROM palabras, idiomas WHERE LOWER(palabra) = LOWER(?) AND LOWER(codigo) = LOWER(?) AND palabras.idioma_id = idiomas.id
```

Esto se podría hacer. De hecho hay varias formas:
- Una es diciendole a Spring exactamente qué query queremos que genere, usando la anotación @Query en el repositorio.
 ```java
  @Query("SELECT p FROM Palabra p JOIN p.idioma i WHERE LOWER(p.palabra) = LOWER(:palabra) AND LOWER(i.codigo) = LOWER(:codigo)")
  Optional<Palabra> findByPalabraAndIdioma_CodigoIgnoreCase(@Param("palabra") String palabra, @Param("codigo") String codigo);
  ```
- Otra opcion es dejar a Spring que genere él la query, pero insisitiendo en que ignore mayúsculas y minúsculas. Para ello, podríamos cambiar el nombre de la función a algo como:

```java
//    Optional<Palabra> findByPalabraAndIdioma_Codigo(String palabra, String codigo);
      Optional<Palabra> findByPalabraIgnoreCaseAndIdioma_CodigoIgnoreCase(String palabra, String codigo);
```
Al poner "IgnoreCase" detrás del nombre de los campos, le estamos diciendo a Spring que genere la query de forma que no afecte si la palabra o el código del idioma están en mayúsculas o minúsculas.

Pero... No es el único sitio donde poder hacer este cambio. Y SERIA UN MEJOR SITIO

El problema es que esa funcionalidad debe ser independiente de dónde se almacenen los diccionarios. Es decir, si en el futuro cambiamos la BBDD por otra, o incluso si decidimos almacenar los diccionarios en ficheros de texto, la funcionalidad de ignorar mayúsculas y minúsculas debería seguir funcionando.
Si intrucimos ese cambio a nivel del repositorio, si el día de mañana quito el repositorio o cambio de repositorio, tendré que reimplementar esa funcionalidad en el nuevo repositorio. Y eso no es bueno.

Lo que podríamos hacer es otra cosa.
-  Da igual como vengan las palabras. Las vamos a guardar en Mayusculas (al leer las palabras de los ficheros de texto, las convertimos a Mayusculas antes de guardarlas en la BBDD).
- Da igual como escriba la palabra/idioma el usuario... la vamos a convertir a Mayusculas antes de buscarla en la BBDD.


Pistas:
- En java tengo la función .toUpperCase() que me permite convertir un String a Mayúsculas.
- En el repositorio de Idiomas tenemos una función que nos regala Spring que  permite obtener un listado de todos los Objetos de tito Idioma existentes en la BBDD. Esa función se llama findAll() y devuelve un List<Idioma> con todos los idiomas que hay en la BBDD.(No es lo que queremos devolver... Queremos devolver un List<String> con los códigos de los idiomas, pero podemos usar esa función para obtener todos los idiomas y luego extraer sus códigos).
  Hemos hecho cosas muy parecidas al extraer los significados de una palabra en un idioma determinado. En ese caso, obteníamos un objeto de tipo Palabra y luego extraíamos sus significados (List<Significado>) y luego de cada significado extraíamos su texto (String). Aquí haremos algo parecido, pero con los idiomas. Obtenemos un List<Idioma> y luego de cada idioma extraemos su código (String).


---

# Patrón proxy

Es un patrón de desarrollo de software que nos permite crear un "proxy" o "intermediario" entre 2 componentes que establezcan comunicación entre ellos. Este patrón tiene muchas ventajas, pero en nuestro caso, nos va a permitir separar la lógica de negocio de la lógica de control de errores.

```java

public interface LibreriaQueHaceAlgoAPI {
    public void haceAlgo();
}

public class LibreriaQueHaceAlgoDeUnaFormaConcreta implements LibreriaQueHaceAlgoAPI {
    public void haceAlgo() {
        //long tin=System.currentTimeMillis();
        // Hace algo...
        //long tout=System.currentTimeMillis();
        //System.out.println("Tiempo que tarda la librería en hacer algo: " + (tout-tin) + " milisegundos");
    }
}

public class ClaseQueNecesitaAlguienQueHagaAlgo {
    
    private LibreriaQueHaceAlgoAPI libreria;

    public ClaseQueNecesitaAlguienQueHagaAlgo(LibreriaQueHaceAlgoAPI libreria) { // Inyección de dependencias
        this.libreria = libreria;
    }

    public void trabajo() {
        // Hace algo...
        //long tin=System.currentTimeMillis();
        libreria.haceAlgo();
        //long tout=System.currentTimeMillis();
        //System.out.println("Tiempo que tarda la librería en hacer algo: " + (tout-tin) + " milisegundos");
        // Hace algo más...
    }
}
// En algún sitio del código, podríamos tener algo como:
//LibreriaQueHaceAlgoAPI libreria = new LibreriaQueHaceAlgoDeUnaFormaConcreta();
//ClaseQueNecesitaAlguienQueHagaAlgo clase = new ClaseQueNecesitaAlguienQueHagaAlgo(libreria);
//clase.trabajo();

// Este es el punto de partida del ejemplo
// Ahora queremos medir el tiempo que tarda la librería en hacer algo.
// Tengo que anotar la hora antes de llamar a la librería y después de llamar a la librería.
// Restando las 2 horas, obtengo el tiempo que tarda la librería en hacer algo.
// Pero hay muchos sitios donde poner ese código potencialmente.
// Sitio 1: Dentro de la librería. Problemas de poinerlo aquñi:
//    - Puede ser que la librería no sea nuestra y no podamos modificarla.
//    - Puede ser que en algunos sitios donde se use la librería quiera medir los tiempos y en otros no. 
//      Si pongo el código dentro de la librería, lo tendré que poner en todos los sitios donde se use la librería
//      aunque no quiera medir los tiempos.
// Sitio 2: Dentro de la clase que necesita a la librería. Problemas de ponerlo aquí:
//    - Puede ser que haya más sitios donde necesite medir los tiempos de esa función...Y al hacerlo así,
//      necesitaría poner esas lineas de código en todos esos sitios, 
//      repitiendo el mismo código una y otra vez, lo que no es bueno.
//    - Puede ser que por momentos quiera desactivar la medición de tiempos. 
//      Si pongo el código aquí, tendré que poner condicionales en todos los sitios donde se use la librería 
//      para decidir si medir o no medir los tiempos. O lo que es peor... 
//      Comentando y descomentando el código en todos esos sitios dependiendo del momento o del entorno. 
//      Esto no es bueno.
// En estos escenarios es donde el patrón proxy nos puede ayudar. La idea es crear un "proxy" o "intermediario" entre la clase que necesita a la librería y la librería.

public class ProxyQueMideTiempos implements LibreriaQueHaceAlgoAPI {
    
    private LibreriaQueHaceAlgoAPI libreria;

    public ProxyQueMideTiempos(LibreriaQueHaceAlgoAPI libreria) { // Inyección de dependencias
        this.libreria = libreria;
    }

    public void haceAlgo() {
        long tin=System.currentTimeMillis();
        libreria.haceAlgo();
        long tout=System.currentTimeMillis();
        System.out.println("Tiempo que tarda la librería en hacer algo: " + (tout-tin) + " milisegundos");
    }
}
LibreriaQueHaceAlgoAPI libreria = new LibreriaQueHaceAlgoDeUnaFormaConcreta();
LibreriaQueHaceAlgoAPI proxy = new ProxyQueMideTiempos(libreria);
ClaseQueNecesitaAlguienQueHagaAlgo clase = new ClaseQueNecesitaAlguienQueHagaAlgo(proxy);
clase.trabajo();

// En todos los sitios donde necesite medir tiempos, puedo pasar un proxy en lugar de la librería directamente. 
// Y si no quiero medir tiempos, puedo pasar la librería directamente. 
// Esto me permite activar o desactivar la medición de tiempos sin tener que modificar el código de:
// - la clase que necesita a la librería
// - ni el código de la librería.

// Esto es un buen patrón de diseño.
// El objetivo, y lo hemos visto MUCHAS VECES desde que empezamos la formación es que cuando quiera hacer un cambio NO TOQUE / TRATE DE NO TOCAR o TOQUE LO MENOS POSIBLE el código que ya está funcionando.
// Lo que quiero SIEMPRE es crear código nuevo que haga lo que quiero hacer, y que se integre con el código que ya está funcionando.
// Si no toco el código que ya está funcionando, las probabilidades de romper algo que ya funciona son mucho menores. Y si rompo algo, será en el código nuevo que he creado, y eso es mucho más fácil de arreglar.


```

En su momento os hablé de los principios SOLID de desarrollo de software.
Uno de ellos: O -> Open/Closed Principle (OCP) - Principio de Abierto/Cerrado

Un sistema de software debería estar abierto a la extensión, pero cerrado a la modificación.
Esto básicamente significa que debería ser posible añadir nueva funcionalidad a un sistema de software sin tener que modificar el código existente. Esto es lo que hemos hecho con el patrón proxy. Hemos añadido nueva funcionalidad (medición de tiempos) sin tener que modificar el código existente (la clase que necesita a la librería y la librería).

Spring hace uso interno de este patrón en muchos sitios. 
Y me pone muy fácil el crear proxies para mis clases. 

Vamos a aplicar esto a nuestro servidor de diccionarios para que cuando se produzca un error en el servidor, podamos devolver un error HTTP Status 500 al cliente, con el mensaje de error que se haya producido en el servidor. 

Esto lo haremos SIN NECESIDAD DE MODIFICAR EL CODIGO DEL CONTROLADOR REST QUE YA TENEMOS CREADO.
Lo que quiero es NO TENER QUE MODIFICAR EL CONTROLADOR REST QUE YA TENEMOS CREADO. Si no lo modifico:
- Hay menos probabilidades de romper algo que ya funciona.
- Si rompo algo, será en el código nuevo que he creado, y eso es mucho más fácil de arreglar.
- Además, el controlador no quiero inundarlo de código repetitivo que gestione los errores.
- En el controlador quiero definir solo EL HAPPY PATH de la API REST que estoy creando, y que se encargue de la lógica de negocio.

HAPPY PATH = CAMINO FELIZ. Es el camino que sigue la ejecución de un programa cuando todo va bien, sin errores ni excepciones. En nuestro caso, el happy path es cuando el cliente hace una petición al servidor y el servidor responde correctamente con los datos solicitados.

Esto puedo resolverlo con un patrón proxy, que me permita separar la lógica de negocio de la lógica de control de errores.

Necesitaría para ello, una clase con las mismas funciones que el controlador REST, y que internamente llame a las funciones del controlador REST que tengo ahora mismo, pero que esa llamada la haga entre un bloque try/catch que me permita capturar cualquier excepción que se produzca en el controlador REST y devolver un error HTTP Status 500 al cliente, con el mensaje de error que se haya producido en el servidor.

```java
public class ProxyQueGestionaErrores implements DiccionariosRestControllerAPI {
    
    private DiccionariosRestControllerAPI controlador;

    public ProxyQueGestionaErrores(DiccionariosRestControllerAPI controlador) { // Inyección de dependencias
        this.controlador = controlador;
    }

    public ResponseEntity<RespuestaPalabra> dameSignificados(String idioma, String palabra) {
        try {
            return controlador.dameSignificados(idioma, palabra);
        } catch (Exception e) {
            return procesarExcepcion(e);
        }
    }

    public ResponseEntity<List<String>> idiomas() {
        try {
            return controlador.dameIdiomas();
        } catch (Exception e) {
            return procesarExcepcion(e);
        }
    }
    public ResponseEntity<Void> existeDiccionarioDe(String idioma) {
        try {
            return controlador.existeDiccionarioDe(idioma);
        } catch (Exception e) {
            return procesarExcepcion(e);
        }
    }
    public ResponseEntity<String> test() {
        try {
            return controlador.test();
        } catch (Exception e) {
            return procesarExcepcion(e);
        }
    }
    private ResponseEntity<Void> procesarExcepcion(Exception e) {
        // Aquí podríamos hacer cosas como:
        // - Loguear el error en un fichero de log
        // - Enviar un email al administrador del sistema
        // - Enviar un mensaje a un sistema de monitorización
        // - etc...
        return ResponseEntity.status(500).body(e.getMessage());
    }
}

// Esto sería el proxy que podríamos montar para ayudarnos a gestionar los errores en el servidor y devolver un error HTTP Status 500 al cliente, con el mensaje de error que se haya producido en el servidor.
// PERO, Y AQUI VIENE LO BUENO Y LO BONITO Y LA MAGIA!
// Spring va a montar este proxy por nosotros, sin que tengamos que escribir ni una sola línea de código para ello.
// Lo único que tengo que definir es una clase con la(s) funcién(es) de tramite de error que me interesan (procesarExcepcion) y a esa clase, ponerle una anotación: @RestControllerAdvice

@RestControllerAdvice
public class ProxyQueGestionaErrores{
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String procesarExcepcion(Exception e) {
        // Aquí podríamos hacer cosas como:
        // - Loguear el error en un fichero de log
        // - Enviar un email al administrador del sistema
        // - Enviar un mensaje a un sistema de monitorización
        // - etc...
        return e.getMessage();
    }
}

// Esto es la forma CORRECTA y GENIAL de tramitar los errores en el servidor cuando usamos SPRING. Esta forma de trabajar entra en el concepto de Programación Orientada a Aspectos (AOP) que os comenté antes.
// Programación Orientada a Aspectos (AOP) lo podemos definir como una forma de crear programas/escribir código según la cual podemos separar la lógica de negocio de otras funcionalidades transversales, como por ejemplo la gestión de errores, la seguridad, el logging, auditoría, rendimiento,  etc... Esto nos permite tener un código más limpio y fácil de mantener, y además nos permite activar o desactivar funcionalidades de forma sencilla, sin tener que modificar el código de la lógica de negocio.

```

Antes:
Servidor de Aplicaciones/Web(TOMCAT) -> Controlador REST

Ahora:
Servidor de Aplicaciones/Web(TOMCAT) -> PROXY del Controlador REST -> Controlador REST 


# Ultima fuincionalidad de nuestro sistema de diccionarios: 

Ya tenemos un sistema guay!
Para buscar los significados de palabras en un idioma determinado.
Además, no me importa/afecta a nuestro sistema si la palabra que busco está en mayúsculas o minúsculas.

Pero... vamos a completar la funcionalidad de nuestro sistema de diccionarios con lo siguiente:
- Si una palabra no existe en un idioma determinado, el servidor debe devolver un listado de las 10 palabras más parecidas a la palabra que se ha buscado en ese idioma (ordenadas de más parecida a menos parecida).

PALABRA BUSCADA: manana. IDIOMA: ESPAÑOL
Y la palabra manana no existe en el diccionario de español. 

El servidor debe devolver, en lugar de los significados, es un listado de las 10 palabras más parecidas a la palabra "manana" en el idioma español, ordenadas de más parecida a menos parecida:

- mañana
- manzana
- manada
- manzano
- ...

Cómo determinamos si una palabra es parecida a otra?
Una mejor pregunta es cómo puedo medir como de diferentes son 2 palabras.

Y la respuesta a esto la dió nuestro amigo Levenshtein hace ya muchos años.
Levenshtein definió una forma de medir cómo de similares o de diferentes son 2 palabras. A esa forma de medir la diferencia entre 2 palabras la llamó distancia de Levenshtein. 

Cuanto más pequeña sea la distancia de Levenshtein entre 2 palabras, más parecidas son esas 2 palabras.

Una distancia de 0 significa que las 2 palabras son exactamente iguales. Es la misma palabra.

Una distancia de 1 significa que para convertir una palabra en otra, hay que hacer 1 operación. Esa operación puede ser:
- Insertar un caracter en la palabra
- Borrar un caracter de la palabra
- Sustituir un caracter de la palabra por otro caracter

Una distancia de 2 significa que para convertir una palabra en otra, hay que hacer 2 operaciones. Y así sucesivamente.

- mañana y manana tienen una distancia de Levenshtein de 1. Para convertir "manana" en "mañana", hay que sustituir la "n" por una "ñ".
- manzana y manana también tienen una distancia de Levenshtein de 1. Para convertir "manana" en "manzana", hay que añadir una "z" entre la "n" y la "a".
- manzano y manana tienen una distancia de Levenshtein de 2. Para convertir "manana" en "manzano", hay que añadir una "z" entre la "n" y la "a", y sustituir la "a" por una "o".

Si queremos obtener un listado de las 10 palabras más parecidas a una palabra determinada, lo que tenemos que hacer es:
1. Calcular la distancia de Levenshtein entre la palabra que se ha buscado y todas las palabras del diccionario en el idioma determinado
2. Ordenar las palabras por distancia de Levenshtein de menor a mayor
3. Y quedarnos con las 10 palabras que tengan la distancia de Levenshtein más pequeña.

Esto convertiría a nuestro sistema en una herramienta ideal para montar un corrector ortográfico, ya que cuando el usuario escriba una palabra mal, el sistema le devolverá un listado de las palabras más parecidas a la que ha escrito, y el usuario podrá elegir la palabra correcta del listado.

---

Una mejora opcional en el programa sería:
- Hemos dicho que hay que calcular la distancia de Levenshtein entre la palabra que se ha buscado y todas las palabras del diccionario en el idioma determinado. Esto puede ser muy costoso en tiempo de ejecución si el diccionario tiene muchas palabras. 

Aquí podemos hacer una simplificación.
Si 2 palabras son de longitud muy diferente (por ejemplo más de 3 caracteres de diferencia), es muy probable que no sean parecidas. Por tanto, podemos descartar esas palabras y no calcular la distancia de Levenshtein entre ellas y la palabra que se ha buscado. Esto nos permitirá reducir el tiempo de ejecución del programa.
El calcular la distancia de Levenshtein entre 2 palabras es una operación costosa en tiempo de ejecución.

La distancia va a ser al menos la diferencia de longitud entre las 2 palabras.
    Mar vs Margarita 
    La distancia sería al menos 6
    MarGARITA
        Para pasar de Mar a Margarita, hay que añadir 6 caracteres: G, A, R, I, T, A

Si la longitud de 2 palabras es mayor de 3, directamente descartamos esa palabra y no calculamos la distancia de Levenshtein entre ellas y la palabra que se ha buscado. 

Además, no es ya solo que no tenga que calcular la distancia de Levenshtein entre esas 2 palabras, sino que además no necesitaré ordenar esa palabra en el listado de palabras más parecidas, ya que no va a ser una de las 10 palabras más parecidas a la palabra que se ha buscado. Y las ordenaciones también son muy pesadas en términos computacionales. Por tanto, cuanto menos palabras tenga que ordenar, mejor.
Es más, si al calcular una distancia de levensthein obtengo un valor superior a 3, puedo descartar esa palabra y no meto en la ordenación.

    manzana
    melón           2 caracteres de longitud... todavía aceptable

    Distancia de Levenshtein entre manzana y melón:
    manzana
    √el-ó√-    Distancia de 5 (DESCARTADA)