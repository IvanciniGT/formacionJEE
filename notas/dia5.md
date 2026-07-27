
# Procedimiento para crear una app web (servicio web) con springboot:

1. En el archivo pom.xml, añadimos el starter de springboot para we.
   Eso desacarga todas las librerías que nos hacen falta (unas cuantas decenas)

2. En el archivo pom.xml, añadimos el plugin de springboot para maven. 
   Esto nos permite ejecutar la app con un simple comando.

3. Crear una clase con función main (La que va a arrancar), con una sola linea de código.
   Inversión de control. SIEMPRE ES LA MISMA LINEA: 
    > SpringApplication.run(NombreClase.class, args); 

4. En esa clase añadimos la anotación @SpringBootApplication. 
   Esto hace que springboot haga su magia y cargue AUTOMATICAMENTE todos los 
   componentes que se definan para nuestra aplicación.
   Cada componente se definirá en un fichero java independiente... que tendrá que llevar antes del nombre de la clas una anotación especial de Spring dependiendo del tipo de componente que sea.

5. Hemos creado una clase con anotación @RestController. 
   Esto hace que springboot sepa que esta clase es un controlador de peticiones web, que define RUTAS.
   En esta clase definimos métodos con anotaciones @GetMapping, @PostMapping, etc... dependiendo del tipo de petición que queramos atender. 
   Cada método tendrá un parámetro de entrada (el cuerpo de la petición) y devolverá un objeto (que será convertido a JSON automáticamente por springboot).

6. Ejecutamos con el comando: 
   > mvn spring-boot:run
   Esto arranca la app (dentro de un servidor de aplicaciones embebido: TOMCAT),
   y nos permite probarla con un cliente REST (navegador, curl,...)


```java
@SpringBootApplication
public class ServicioWeb {
    public static void main(String[] args) {
        SpringApplication.run(ServicioWeb.class, args);
    }
}
@RestController 
public class DiccionariosRestController {
    @GetMapping("/diccionarios/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hola desde DiccionariosRestController");
    }
}
```


# Pruebas de servicios WEB REST

Hay una herramienta muy sencilla (en realidad es una extensión de navegador, disponible para firefox y para chrome) llamada Boomerang (soap&rest client) que nos permite probar servicios web REST de manera muy sencilla.


---

# De dónde veníamos:

App que se instalaba en cada cliente, que llevaba embebido:
- Una interfaz gráfica (de consola)
- Diccionarios en ficheros para distintos idiomas
- Una librería (diccionarios-en-ficheros) que se encargaba de cargar los diccionarios y de hacer las búsquedas de palabras para obtener sus significados.

El problema es que si cambiaba la forma de buscar palabras (por ejemplo para hacer que no importe si la pasan en mayúsculas o minúsculas) había que actualizar la aplicación en todos los clientes.
Además, si quería añadir un nuevo idioma, había que actualizar la aplicación en todos los clientes.
O si quería añadir palabras en el diccionario de un idioma, había que actualizar la aplicación en todos los clientes.

# Dónde estamos?

Ahora tenemos una aplicación (servicio web) que corre en un servidor CENTRAL de la empresa.
Esa aplicación contiene:
- Diccionarios en ficheros para distintos idiomas
- Una librería (diccionarios-en-ficheros) que se encargaba de cargar los diccionarios y de hacer las búsquedas de palabras para obtener sus significados.
Además, tenemos un controlador REST para exponer la funcionalidad de la librería vía HTTP.

Si cambia la forma de buscar palabras, o si se añaden nuevos idiomas o nuevas palabras, solo hay que actualizar la aplicación en el servidor central. Los clientes no tienen que hacer nada.

LA MEJORA EN CUANTO A MANTENIMIENTO ES ENORME. Y además en cuanto a estandarización ... tolos clientes usan la mism,a versión de la librería y de los diccionarios, y todos los clientes usan la misma forma de buscar palabras.

Esto es un SALTO DE CALIDAD.

Lo que necesitamos ahora es crear un cliente NUEVO que ya no tenga embebido:
- Diccionarios en ficheros para distintos idiomas
- Una librería (diccionarios-en-ficheros) que se encargaba de cargar los diccionarios y de hacer las búsquedas de palabras para obtener sus significados.

Necesito un cliente que tenga:
- Una interfaz gráfica (de consola)
- Una forma de conectarse al servidor central para pedirle los significados de las palabras (por ejemplo, usando un cliente HTTP)
No lo tenemos, hay que montarlo. El que tenemos ahora tiene integrado todo, y no nos sirve para lo que queremos hacer ahora.

Ahora bien... cómo tenemos un diseño modular, podemos reutilizar la interfaz gráfica de la versión que tenía todo integrado para montar la nueva versión de cliente.
También puedo reusar toda la lógica de esa aplicación.

Mi aplicación usaba un Suministrador de diccionarios en FICHEROS.
De hecho, es lo único que necesito cambiar en mi app.

Mi aplicación ahora debe usar un Suministrador de diccionarios definidos en un SERVICIO WEB.

> ANTES

   AplicaciónCliente
      InterfazGráfica (InterfazGráficaDeConsola)
      SuministradorDeDiccionarios (SuministradorDeDiccionariosEnFichero)
                                    tienesDiccionarioDe(idioma)? 
                                       BUSCABA En los ficheros
      Ficheros de diccionarios


> AHORA

   AplicaciónCliente
      InterfazGráfica (InterfazGráficaDeConsola)
      SuministradorDeDiccionarios (SuministradorDeDiccionariosEnServicioWeb)
                                    tienesDiccionarioDe(idioma)? 
                                       BUSCA EN EL SERVIDOR CENTRAL (vía HTTP)

  Servidor Central
      Controlador HTTP (Que define rutas a las que se puede llamar por HTTP)
      Diccionarios en ficheros para distintos idiomas
      SuministradorDeDiccionarios (SuministradorDeDiccionariosEnFichero)
                                    tienesDiccionarioDe(idioma)? 
                                       BUSCA En los ficheros


   Esquema de comunicaciones:
      ANTES:
               Ordenador del usuario                                                         
      -------------------------------------------------------------------------------------------------------------------------------------------
      AplicaciónCliente <-JAVA->                                                                             SuministradorDeDiccionariosEnFichero 

      AHORA:

         Ordenador del usuario                                                         Servidor central
      -------------------------------------------------------------------         ----------------------------------------------------------------
      AplicaciónCliente <-JAVA-> SuministradorDeDiccionariosEnServicioWeb <-HTTP-> Controlador HTTP <-JAVA-> SuministradorDeDiccionariosEnFichero 

                                 SuministradorDeDiccionariosEnServicioWeb <-HTTP-> Controlador HTTP <-JAVA->
                                 ESTA PARTE ES LO NUEVO QUE HE DESARROLLADO. EL RESTO YA EXISTÍA Y LO HE REUTILIZADO.
                                 Algunas las reuso a nivel de la aplicación cliente y otras las reuso a nivel del servidor central.
      
      Qué consigo? 
      - Consigo que la aplicación cliente no tenga que llevar embebido los diccionarios ni la librería de búsqueda de palabras/gestión de ficheros. Todo eso lo lleva el servidor central. Y ESO FACILITA ENORMEMENTE EL MANTENIMIENTO DE LA APLICACIÓN. 
      SI HAY QUE CAMBIAR ALGO DE ESA PARTE (diccionarios, gestión de diccionarios), SOLO HAY QUE CAMBIARLO EN EL SERVIDOR CENTRAL. LOS CLIENTES NO TIENEN QUE HACER NADA.

      El desarrollo es más complejo.
      Voy a tardar más.
      Tampoco mucho más, al final son 4 ficheros... con muy poco código. Una persona suelta con JAVA, escribe eso en unas horas.

      A lo largo del ciclo de VIDA de mi producto (según pasen meses, años, etc...) y vaya aplicando cambios (nuevos diccionarios, palabras, cambios en los algoritmos de búsqueda, etc...) voy a tardar mucho menos en hacer los cambios y en desplegarlos. Y eso me hará:
      - Ahorrar mucho dinero
      - Tener mucha mayor estandarización en todos los clientes (todos usan la misma versión de la librería y de los diccionarios)
      - Hará que todo el mundo (usuarios) puedan acceder a las nuevas funcionalidades de manera inmediata (no hay que esperar a que se actualice la aplicación en cada cliente)

      En resumen, el coste de desarrollo inicial es mayor, pero el coste de mantenimiento a lo largo del ciclo de vida del producto es mucho menor. Y eso hace que el coste total de ciclo de vida del producto sea mucho menor. Y eso es lo que importa a la hora de desarrollar software. No el coste inicial, sino el coste total de ciclo de vida del producto. 


      Acabamos de pasar de una aplicación monolítica que corría entera en un ordenador del usuario, a una aplicación con arquitectura cliente-servidor.
      Y si os fijáis, HEMOS REUSADO CASI TODO LO QUE TENIAMOS.

      Para nuestra aplicación antigua teníamos los siguientes subproyectos:
      - Diccionario-es                                      √
      - Diccionario-en                                      √
      - Diccionario-elfico                                  √
      - Diccionarios-api                                    √
      - Diccionarios-en-ficheros                            √
      - Interfaz-grafica-api
      - Interfaz-grafica-de-consola
      - Aplicación con su lógica
        - Factoria para el SuministradorDeDiccionarios      ~
        - Factoria para la InterfazGráfica

      Para cambiar a la nueva arquitectura:
      - Crear una aplicación servidor (servicio web) (NUEVO PROYECTO COMPLETO) - No hemos tocado nada de lo que había (SOLO AÑADIDO COSAS)
         - Pero aquí no hemos hecho más que crear 4 archivos cutres.
         -Y reusar:
            - Diccionario-es
            - Diccionario-en
            - Diccionario-elfico
            - Diccionarios-api
            - Diccionarios-en-ficheros 
            - Factoria para el SuministradorDeDiccionarios -> SuministradorDeDiccionariosConfiguration
      - Crear la nueva versión de la aplicación cliente:
        - NUEVO PROYECTO: SuministradorDeDiccionariosEnServicioWeb (3 archivos simples)
        - Reusamos:
            - Interfaz-grafica-api                          
            - Interfaz-grafica-de-consola
            - Aplicación con su lógica
              - Factoria para el SuministradorDeDiccionarios      ~
              - Factoria para la InterfazGráfica               
           
Al final, el cambio es:
   2 proyectos NUEVOS: Servicio Web / SuministradorDeDiccionariosEnServicioWeb   
   Modificaciones en el archivo SuministradorDeDiccionariosFactory

Qué posibilidades hay de que haya roto algo de lo que había hecho? NINGUNA!
NO HE TOCADO NADA DE LO QUE HABIA HECHO(bueno si.. 1 línea en SuministradorDeDiccionariosFactory)

El resto ha sido crear proyectos nuevos.

La app completa antigua SEGUIRIA FUNCIONANDO si quisiera... reusando los mismos componentes.
Pero ahora además tengo:
   - Servicio web que corre en un servidor central
   - Nueva versión de la aplicación cliente que se conecta al servicio web para obtener los significados de las palabras.

EXITO ABSOLUTO