
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